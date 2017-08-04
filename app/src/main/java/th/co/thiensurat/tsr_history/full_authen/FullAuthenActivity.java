package th.co.thiensurat.tsr_history.full_authen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.ApiURL;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.network.ConnectionDetector;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class FullAuthenActivity extends BaseMvpActivity<FullAuthenInterface.presenter> implements FullAuthenInterface.view {

    private SweetAlertDialog sweetAlertDialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private List<FullAuthenItem> fullAuthenItemList = new ArrayList<FullAuthenItem>();
    @Override
    public FullAuthenInterface.presenter createPresenter() {
        return FullAuthenPresenter.create();
    }

    public static FullAuthenActivity getInstance() {
        return new FullAuthenActivity();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_full_authen;
    }

    @Bind(R.id.user_name) EditText username;
    @Bind(R.id.user_pwd) EditText userpassword;
    @Bind(R.id.login) Button logIn;
    @Bind(R.id.login_tsr) Button logInTSR;
    @Bind(R.id.textVersion) TextView textViewVersion;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    public void setupView() {
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    userpassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        userpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(userpassword.getWindowToken(), 0);
                    logIn.performClick();
                    return true;
                }
                return false;
            }
        });

        logIn.setOnClickListener( onLogin() );
        logInTSR.setOnClickListener( onLogInTSR() );
    }

    @Override
    public void initialize() {
        textViewVersion.setText("App v." + appVersion());
        getDeviceID();
        loginSession();
        OnNetworkChecking();
    }

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(FullAuthenActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    @Override
    public void onFail(String failed) {
        AlertDialog.dialogSearchFail(FullAuthenActivity.this, failed);
        AlertDialog.dialogDimiss();
    }

    @Override
    public void onAuthen(List<AuthenItem> authenItems) {
        if (authenItems.isEmpty()) {
            if (checkPackageInstalled("th.co.thiensurat", getPackageManager())
                    && !MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                dialogLoginConfirm();
                Log.e("Authen", "Empty");
            } else {
                AlertDialog.dialogSearchFail(FullAuthenActivity.this, getResources().getString(R.string.dialog_full_login_msg));
            }
        } else {
            if (authenItems.get(0).getLoggedin() != 1) {
                if (checkPackageInstalled("th.co.thiensurat", getPackageManager())
                        || !MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                    dialogLoginConfirm();
                    Log.e("Authen", "Not empty");
                } else {
                    AlertDialog.dialogSearchFail(FullAuthenActivity.this, getResources().getString(R.string.dialog_full_login_msg));
                }
            } else if (authenItems.get(0).getLoggedin() == 1) {
                MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, authenItems.get(0).getUsername());
                MyApplication.getInstance().getPrefManager().setPreferrenceTimeStamp(Config.KEY_SESSION, new Date().getTime());
                getPresenter().goToSearchActivity();
            }
        }
    }

    @Override
    public void onFullAuthen(List<AuthenItem> authenItems) {
        if (authenItems.isEmpty()) {
            if (checkPackageInstalled("th.co.thiensurat", getPackageManager())
                    || !MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                dialogLoginConfirm();
                Log.e("Full authen", "Empty");
            } else {
                AlertDialog.dialogSearchFail(FullAuthenActivity.this, getResources().getString(R.string.dialog_full_login_msg));
            }
        } else {
            for (AuthenItem item : authenItems) {
                MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, item.getUsername());
                MyApplication.getInstance().getPrefManager().setPreferrenceTimeStamp(Config.KEY_SESSION, new Date().getTime());
                getPresenter().goToSearchActivity();
            }
        }
    }

    @Override
    public void goToSearchActivity() {
        onDismiss();
        startActivityForResult(new Intent(FullAuthenActivity.this, SearchActivity.class), Config.REQUEST_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUEST_SETTINGS :
                if (resultCode == RESULT_OK) {
                    OnNetworkChecking();
                    Log.e("onActivityResult", "Load (onActivityResult)");
                }
                break;
            case Config.REQUEST_LOGIN :
                getPresenter().onLoginValidation(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_DEVICE_ID));
                break;
            case Config.REQUEST_SEARCH :
                break;
            default: break;
        }
    }

    private void getDeviceID() {
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_DEVICE_ID, deviceID);
    }

    private boolean checkPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void loginSession() {
        long loginTime = new Date().getTime() - MyApplication.getInstance().getPrefManager().getPreferrenceTimeStamp(Config.KEY_SESSION);
        int minutes = (int) ((loginTime / (1000*60)) % 60);
        if (minutes > 360) {
            MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, "");
            MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, false);
        }
    }

    private void dialogLoginConfirm() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_warning));
        sweetAlertDialog.setContentText(getResources().getString(R.string.dialog_login_msg));
        sweetAlertDialog.setCancelText(getResources().getString(R.string.dialog_button_cancel));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.dialog_button_login));
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
            }
        });
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("th.co.thiensurat");
                startActivityForResult(launchIntent, Config.REQUEST_LOGIN);
            }
        });
        sweetAlertDialog.show();
    }

    private void dialogUpdate() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_warning));
        sweetAlertDialog.setContentText(getResources().getString(R.string.dialog_text_msg_update));
        sweetAlertDialog.setCancelText(getResources().getString(R.string.dialog_button_cancel));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.dialog_button_update));
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
                finish();
            }
        });
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
                try {
                    PackageManager pm = getPackageManager();
                    PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=" + packageInfo.packageName));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.i("CheckAppVersion", e.getMessage());
                }
            }
        });
        sweetAlertDialog.show();
    }

    private void OnNetworkChecking() {
        boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(this);
        if (!isNetworkAvailable) {
            AlertDialog.dialogNetworkError(FullAuthenActivity.this);
        } else {
            checkAppVersion();
        }
    }

    public void detectWifiConnected(final String state) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (state.equals("connect")){
                    Log.e("Internet connection", state);
                } else {
                    Log.e("Internet connection", state);
                }
            }
        });
    }

    private View.OnClickListener onLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullAuthenItemList.clear();
                if (username.getText().toString().isEmpty() && userpassword.getText().toString().isEmpty()) {
                    AlertDialog.dialogSearchEmpty(getApplicationContext());
                } else {
                    FullAuthenItem item = new FullAuthenItem();
                    item.setUsername(username.getText().toString());
                    item.setPassword(userpassword.getText().toString());
                    fullAuthenItemList.add(item);
                    getPresenter().requestLogin(fullAuthenItemList);
                }
            }
        };
    }

    private View.OnClickListener onLogInTSR() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onLoginValidation(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_DEVICE_ID));
            }
        };
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            return false;
        }
        return true;
    }

    private String appVersion() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkAppVersion() {
        AlertDialog.dialogLoading(this);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            String curVersion = packageInfo.versionName;
            String newVersion = curVersion;
            newVersion = Jsoup.connect(ApiURL.URL_APP_VERSION + "?id=" + packageInfo.packageName)
                    .timeout(3000)
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
            if (!curVersion.equals(newVersion)) {
                AlertDialog.dialogDimiss();
                dialogUpdate();
            } else {
                AlertDialog.dialogDimiss();
                if (!MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                    getPresenter().onLoginValidation(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_DEVICE_ID));
                } else {
                    getPresenter().goToSearchActivity();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("CheckAppVersion", e.getMessage());
            AlertDialog.dialogDimiss();
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.dialogDimiss();
        }
    }
}

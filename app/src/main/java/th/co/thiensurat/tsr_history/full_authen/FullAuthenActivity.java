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

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.ApiURL;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.choice_authen.ChoiceActivity;
import th.co.thiensurat.tsr_history.network.ConnectionDetector;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.CustomDialog;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class FullAuthenActivity extends BaseMvpActivity<FullAuthenInterface.presenter> implements FullAuthenInterface.view {

    private CustomDialog customDialog;
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

    @BindView(R.id.user_name) EditText username;
    @BindView(R.id.user_pwd) EditText userpassword;
    @BindView(R.id.login) Button logIn;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(FullAuthenActivity.this);
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
    }

    @Override
    public void initialize() {
        getDeviceID();
        OnNetworkChecking();
    }

    private void OnNetworkChecking() {
        boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(this);
        if (!isNetworkAvailable) {
            AlertDialog.dialogNetworkError(FullAuthenActivity.this);
        } else {
            if (loginSession()) {
                getPresenter().goToSearchActivity();
            }
        }
    }

    @Override
    public void onLoad() {
        customDialog.dialogLoading();
    }

    @Override
    public void onDismiss() {
        customDialog.dialogDimiss();
    }

    @Override
    public void onFail(String failed) {
        customDialog.dialogFail(failed);
    }

    @Override
    public void onSuccess() {
        getPresenter().goToSearchActivity();
    }

    /*@Override
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
                        && !MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                    dialogLoginConfirm();
                    Log.e("Authen", "Not empty");
                } else {
                    AlertDialog.dialogSearchFail(FullAuthenActivity.this, getResources().getString(R.string.dialog_full_login_msg));
                }
            } else if (authenItems.get(0).getLoggedin() == 1) {
                MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, authenItems.get(0).getUsername());
                //MyApplication.getInstance().getPrefManager().setPreferrenceTimeStamp(Config.KEY_SESSION, new Date().getTime());
                getPresenter().goToSearchActivity();
            }
        }
    }*/

    /*@Override
    public void onFullAuthen(List<AuthenItem> authenItems) {
        if (authenItems.isEmpty()) {
            if (checkPackageInstalled("th.co.thiensurat", getPackageManager())
                    && !MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                dialogLoginConfirm();
                Log.e("Full authen", "Empty");
            } else {
                AlertDialog.dialogSearchFail(FullAuthenActivity.this, getResources().getString(R.string.dialog_full_login_msg));
            }
        } else {
            for (AuthenItem item : authenItems) {
                MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, item.getUsername());
                getPresenter().goToSearchActivity();
            }
        }
    }*/

    @Override
    public void goToSearchActivity() {
        startActivityForResult(new Intent(FullAuthenActivity.this, SearchActivity.class), Config.REQUEST_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUEST_SETTINGS :
                if (resultCode == RESULT_OK) {
                    OnNetworkChecking();
                }
                break;
            case Config.REQUEST_SEARCH :
                break;
            default: break;
        }
    }

    private void getDeviceID() {
        MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_DEVICE_ID, Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    private boolean loginSession() {
        try {
            if (MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
                return true;
            } else {
                MyApplication.getInstance().getPrefManager().clear();
                return false;
            }
        } catch (Exception e) {
            return false;
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
                    customDialog.dialogFail("กรุณาใส่ชื่อผู้ใช้และรหัสผ่านให้ถูกต้อง");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            startActivity(new Intent(getApplicationContext(), ChoiceActivity.class));
            finish();
            return true;
        }
        return false;
    }
}

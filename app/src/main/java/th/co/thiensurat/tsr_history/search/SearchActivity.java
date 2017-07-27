package th.co.thiensurat.tsr_history.search;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.result.CustomerResultActivity;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class SearchActivity extends BaseMvpActivity<SearchInterface.presenter> implements SearchInterface.view{

    private SweetAlertDialog sweetAlertDialog;

    public SearchActivity() {
        super();
    }

    @Override
    public SearchInterface.presenter createPresenter() {
        return SearchPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_search;
    }

    @Bind(R.id.inputSearch) EditText inputSearch;
    @Bind(R.id.display_name) TextView displayName;
    @Override
    public void bindView() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
    }

    @Override
    public void setupView() {
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                    if (!inputSearch.getText().toString().isEmpty()) {
                        getPresenter().goToResultCustomer(inputSearch.getText().toString());
                        return true;
                    } else {
                        alert();
                        return false;
                    }
                }
                return false;
            }
        });
    }

    private void alert() {
        AlertDialog.dialogSearchEmpty(SearchActivity.this);
    }

    @Override
    public void initialize() {
        try {
            displayName.setText(getResources().getString(R.string.authen_username) + ": "
                    + MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME));
        } catch(Exception e) {
            displayName.setText(getResources().getString(R.string.authen_username) + ": -");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUEST_RESULT :
                if (resultCode == RESULT_OK) {
                    Log.e("TSR History", "Success");
                    inputSearch.setText("");
                } else if (resultCode == RESULT_CANCELED) {
                    Log.e("TSR History", "Failed");
                    inputSearch.setText("");
                }
                break;
            default: break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            return false;
        }
        return true;
    }

    @Override
    public void goToResultCustomer(String data) {
        Intent intent = new Intent(getApplicationContext(), CustomerResultActivity.class);
        intent.putExtra(Config.KEY_DATA, data);
        startActivityForResult(intent, Config.REQUEST_RESULT);
    }

    @Override
    public void onFail(String failed) {
        AlertDialog.dialogSearchFail(SearchActivity.this, failed);
        AlertDialog.dialogDimiss();
    }

    /*@Override
    public void onAuthen(List<AuthenItem> authenItems) {
        if (!authenItems.isEmpty()) {
            for (AuthenItem item : authenItems) {
                Log.e("Authen", item.getUsername());
                if (item.getLoggedin() != 1) {
                    if (checkPackageInstalled("th.co.thiensurat", getPackageManager())) {
                        dialogLoginConfirm();
                    } else {
                        startActivityForResult(new Intent(this, FullAuthenActivity.class), Config.REQUEST_FULL_LOGIN);
                    }
                } else if (item.getLoggedin() == 1) {
                    MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                    MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, item.getUsername());
                    MyApplication.getInstance().getPrefManager().setPreferrenceTimeStamp(Config.KEY_SESSION, new Date().getTime());
                    displayName.setText(getResources().getString(R.string.authen_username) + ": " + item.getUsername());
                }
            }
        } else {
            if (checkPackageInstalled("th.co.thiensurat", getPackageManager())) {
                dialogLoginConfirm();
            } else {
                dialogLoginConfirmWithTSR();
            }
        }
    }*/

    /*private void getDeviceID() {
        String deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //Log.e("Device id", deviceID);
        MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_DEVICE_ID, deviceID);
    }*/

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(SearchActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    /*private void dialogLoginConfirm() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_warning));
        sweetAlertDialog.setContentText(getResources().getString(R.string.dialog_login_msg));
        sweetAlertDialog.setCancelText(getResources().getString(R.string.dialog_button_cancel));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.dialog_button_login));
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
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("th.co.thiensurat");
                startActivityForResult(launchIntent, Config.REQUEST_LOGIN);
            }
        });
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
    }

    private void dialogLoginConfirmWithTSR() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_warning));
        sweetAlertDialog.setContentText(getResources().getString(R.string.dialog_full_login_msg));
        sweetAlertDialog.setCancelText(getResources().getString(R.string.dialog_button_cancel));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.dialog_button_login));
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
                startActivityForResult(new Intent(getApplicationContext(), FullAuthenActivity.class), Config.REQUEST_FULL_LOGIN);
            }
        });
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
    }*/

    /*private boolean checkPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }*/

    /*private void loginSession() {
        long loginTime = new Date().getTime() - MyApplication.getInstance().getPrefManager().getPreferrenceTimeStamp(Config.KEY_SESSION);
        int minutes = (int) ((loginTime / (1000*60)) % 60);
        Log.e("Session", minutes + ", " + MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN));
        if (minutes > 30) {
            MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, "");
            MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, false);
            displayName.setText(getResources().getString(R.string.authen_username) + ": -");
            getPresenter().onLoginValidation(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_DEVICE_ID));
            Log.e("true", MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)+"");
        } else {
            Log.e("false", MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)+"");
            displayName.setText(getResources().getString(R.string.authen_username) + ": "
                    + MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME));
        }
    }*/
}

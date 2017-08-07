package th.co.thiensurat.tsr_history.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.choice_authen.ChoiceActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.result.CustomerResultActivity;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;
import th.co.thiensurat.tsr_history.result_customer.CustomerByNameActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.AnimateButton;
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
    @Bind(R.id.button_sign_out) Button signOut;
    @Bind(R.id.button_to_tsr) Button gotoTSR;
    @Bind(R.id.button_search) ImageButton search;
    @Override
    public void bindView() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        ButterKnife.bind(this);
        signOut.setOnClickListener( onSignOut() );
        gotoTSR.setOnClickListener( onGoToTSR() );
        search.setOnClickListener( onSearch() );
    }

    @Override
    public void setupInstance() {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Config.KEY_USERNAME, MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, savedInstanceState.getString(Config.KEY_USERNAME));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        displayName.setText(getResources().getString(R.string.authen_username) + ": "
                + savedInstanceState.getString(Config.KEY_USERNAME));
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
                        //getPresenter().goToResultCustomer(inputSearch.getText().toString());
                        search.performClick();
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

            if (checkPackageInstalled("th.co.thiensurat", getPackageManager())) {
                gotoTSR.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                gotoTSR.setTextColor(getResources().getColor(R.color.White));
                gotoTSR.setEnabled(true);
            } else {
                gotoTSR.setBackgroundColor(getResources().getColor(R.color.DarkGray));
                gotoTSR.setTextColor(getResources().getColor(R.color.Black));
                gotoTSR.setEnabled(false);
            }
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
                    //inputSearch.setText("");
                } else if (resultCode == RESULT_CANCELED) {
                    Log.e("TSR History", "Failed");
                    //inputSearch.setText("");
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
        intent.putExtra(Config.KEY_CLASS, "SearchActivity");
        startActivityForResult(intent, Config.REQUEST_RESULT);
    }

    @Override
    public void goToCustomerByName(String data) {
        Intent intent = new Intent(getApplicationContext(), CustomerByNameActivity.class);
        intent.putExtra(Config.KEY_DATA, data);
        startActivityForResult(intent, Config.REQUEST_RESULT_BY_NAME);
    }

    @Override
    public void onFail(String failed) {
        AlertDialog.dialogSearchFail(SearchActivity.this, failed);
        AlertDialog.dialogDimiss();
    }

    private View.OnClickListener onSignOut() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut.startAnimation(new AnimateButton().animbutton());
                setSignOut();
            }
        };
    }

    private View.OnClickListener onGoToTSR() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTSR.startAnimation(new AnimateButton().animbutton());
                if (checkPackageInstalled("th.co.thiensurat", getPackageManager())) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("th.co.thiensurat");
                    startActivity(launchIntent);
                } else {
                    dialogLog();
                }
            }
        };
    }

    private View.OnClickListener onSearch() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.startAnimation(new AnimateButton().animbutton());
                if (!inputSearch.getText().toString().isEmpty()) {
                    getPresenter().goToResultCustomer(inputSearch.getText().toString());
                } else {
                    alert();
                }
            }
        };
    }

    private boolean checkPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void setSignOut() {
        MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, false);
        MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, "");
        MyApplication.getInstance().getPrefManager().setPreferrenceTimeStamp(Config.KEY_SESSION, 0);
        startActivity(new Intent(getApplicationContext(), ChoiceActivity.class));
        finish();
    }

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(SearchActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    private void dialogLog() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_warning));
        sweetAlertDialog.setContentText(getResources().getString(R.string.dialog_msg_not_installed));
        sweetAlertDialog.showCancelButton(false);
        sweetAlertDialog.setConfirmText("OK");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
            }
        });
        sweetAlertDialog.show();
    }
}

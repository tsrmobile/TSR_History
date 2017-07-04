package th.co.thiensurat.tsr_history.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.network.ConnectionDetector;
import th.co.thiensurat.tsr_history.result.CustomerResultActivity;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;
import th.co.thiensurat.tsr_history.utils.Config;

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_search;
    }

    @Bind(R.id.inputSearch) EditText inputSearch;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        OnNetworkChecking();
    }

    @Override
    public void setupView() {
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                    getPresenter().requestCustomer(inputSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initialize() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.REQUEST_SETTINGS :
                break;
            default: break;
        }
    }

    private void OnNetworkChecking() {
        boolean isNetworkAvailable = ConnectionDetector.isConnectingToInternet(this);
        if (!isNetworkAvailable) {
            dialogConfirm();
        }
    }

    public static SearchActivity getInstance() {
        return new SearchActivity();
    }

    public void detectWifiConnected(final String state) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (state.equals("connect")){
                }
            }
        });
    }

    private void dialogConfirm() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_warning));
        sweetAlertDialog.setContentText(getResources().getString(R.string.dialog_text_msg));
        sweetAlertDialog.setCancelText(getResources().getString(R.string.dialog_button_cancel));
        sweetAlertDialog.setConfirmText(getResources().getString(R.string.dialog_button_confirm));
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
                        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                        startActivityForResult(intent, Config.REQUEST_SETTINGS);
                    }
                });
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            return false;
        }
        return true;
    }

    @Override
    public void goToResultCustomer() {
        Intent intent = new Intent(getApplicationContext(), CustomerResultActivity.class);

        /*Bundle bundle = new Bundle();
        bundle.putParcelable(Config.KEY_ITEM, getPresenter().getCustomer());
        intent.putExtra(Config.KEY_ITEM, bundle);*/
        startActivity(intent);
    }

    @Override
    public void onFail(String failed) {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getResources().getString(R.string.dialog_title_error));
        sweetAlertDialog.setContentText(failed);
        sweetAlertDialog.show();
    }
}

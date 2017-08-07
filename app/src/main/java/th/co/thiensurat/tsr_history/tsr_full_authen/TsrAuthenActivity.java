package th.co.thiensurat.tsr_history.tsr_full_authen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.choice_authen.ChoiceActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.AnimateButton;

public class TsrAuthenActivity extends BaseMvpActivity<TsrAuthenInterface.Presenter> implements TsrAuthenInterface.View  {

    private List<FullAuthenItem> fullAuthenItemList = new ArrayList<FullAuthenItem>();
    @Override
    public TsrAuthenInterface.Presenter createPresenter() {
        return TsrAuthenPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_tsr_authen;
    }

    @Bind(R.id.user_name) EditText username;
    @Bind(R.id.user_pwd) EditText userpassword;
    @Bind(R.id.login) Button logIn;
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
        logIn.setOnClickListener( onLogin() );
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
    }

    @Override
    public void initialize() {

    }

    private View.OnClickListener onLogin() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn.startAnimation(new AnimateButton().animbutton());
                getAuthenInfo();
            }
        };
    }

    private void getAuthenInfo() {
        if (!username.getText().toString().isEmpty() & !userpassword.getText().toString().isEmpty()) {
            getPresenter().requestTSR(username.getText().toString(), userpassword.getText().toString());
        } else {
            AlertDialog.dialogSearchEmpty(this);
        }
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

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(TsrAuthenActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    @Override
    public void goToSearchActivity() {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
    }

    @Override
    public void onFail(String fail) {
        AlertDialog.dialogSearchFail(TsrAuthenActivity.this, fail);
        AlertDialog.dialogDimiss();
    }
}

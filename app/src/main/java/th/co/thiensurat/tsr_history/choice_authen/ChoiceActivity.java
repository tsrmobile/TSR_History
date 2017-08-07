package th.co.thiensurat.tsr_history.choice_authen;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.tsr_full_authen.TsrAuthenActivity;
import th.co.thiensurat.tsr_history.utils.AnimateButton;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class ChoiceActivity extends BaseMvpActivity<ChoiceInterface.Presenter> implements ChoiceInterface.View {

    @Override
    public ChoiceInterface.Presenter createPresenter() {
        return ChoicePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_choice;
    }

    @Bind(R.id.textVersion) TextView textViewVersion;
    @Bind(R.id.tsr_button) LinearLayout tsrButton;
    @Bind(R.id.bighead_button) LinearLayout bigheadButton;
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
        tsrButton.setOnClickListener( onTSR() );
        bigheadButton.setOnClickListener( onBighead() );
    }

    @Override
    public void initialize() {
        textViewVersion.setText("App v." + appVersion());
        loginSession();
    }

    private View.OnClickListener onBighead() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bigheadButton.startAnimation(new AnimateButton().animbutton());
                getPresenter().requestBigHead();
            }
        };
    }

    private View.OnClickListener onTSR() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tsrButton.startAnimation(new AnimateButton().animbutton());
                getPresenter().requestTSR();
            }
        };
    }

    @Override
    public void goToBigHead() {
        startActivity(new Intent(getApplicationContext(), FullAuthenActivity.class));
        finish();
    }

    @Override
    public void goToTSR() {
        startActivity(new Intent(getApplicationContext(), TsrAuthenActivity.class));
        finish();
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

    private void loginSession() {
        long loginTime = new Date().getTime() - MyApplication.getInstance().getPrefManager().getPreferrenceTimeStamp(Config.KEY_SESSION);
        int minutes = (int) ((loginTime / (1000*60)) % 60);
        if (minutes > 360 || !MyApplication.getInstance().getPrefManager().getPreferrenceBoolean(Config.KEY_BOOLEAN)) {
            MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, "");
            MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, false);
        } else {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            finish();
        }
    }
}

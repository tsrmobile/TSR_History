package th.co.thiensurat.tsr_history.choice_authen;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

import org.w3c.dom.Text;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.tsr_full_authen.TsrAuthenActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.AnimateButton;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.CustomDialog;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class ChoiceActivity extends BaseMvpActivity<ChoiceInterface.Presenter> implements ChoiceInterface.View {

    private CustomDialog customDialog;

    @Override
    public ChoiceInterface.Presenter createPresenter() {
        return ChoicePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_choice;
    }

    @BindView(R.id.textVersion) TextView textViewVersion;
    @BindView(R.id.tsr_button) LinearLayout tsrButton;
    @BindView(R.id.bighead_button) LinearLayout bigheadButton;
    @Override
    public void bindView() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        customDialog = new CustomDialog(ChoiceActivity.this);
    }

    @Override
    public void setupView() {
        tsrButton.setOnClickListener( onTSR() );
        bigheadButton.setOnClickListener( onBighead() );
    }

    @Override
    public void initialize() {
        textViewVersion.setText("App v." + appVersion());
        updateApp();
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

    private void updateApp() {
        //AlertDialog.dialogLoading(ChoiceActivity.this);
        customDialog.dialogLoading();
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.e("Latest Version", update.getLatestVersion());
                        //Log.d("Latest Version Code", update.getLatestVersionCode());
                        Log.e("Release notes", update.getReleaseNotes());
                        //Log.d("URL", update.getUrlToDownload());
                        Log.e("Is update available?", Boolean.toString(isUpdateAvailable));
                        //AlertDialog.dialogDimiss();
                        customDialog.dialogDimiss();
                        if (isUpdateAvailable) {
                            MyApplication.getInstance().getPrefManager().clear();
                            customDialog.dialogAppUpdate(getApplicationContext().getPackageName());
                            //AlertDialog.dialogAlertUpdate(ChoiceActivity.this, getApplicationContext().getPackageName());
                        } else {
                            if (loginSession()) {
                                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        //AlertDialog.dialogDimiss();
                        customDialog.dialogDimiss();
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }
}

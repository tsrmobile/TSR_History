package th.co.thiensurat.tsr_history.tsr_full_authen;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.Date;
import java.util.List;

import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.api.result.TsrAuthenResult;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

/**
 * Created by teerayut.k on 8/7/2017.
 */

public class TsrAuthenPresenter extends BaseMvpPresenter<TsrAuthenInterface.View> implements TsrAuthenInterface.Presenter {

    private ServiceManager serviceManager;
    public static TsrAuthenInterface.Presenter create() {
        return new TsrAuthenPresenter();
    }

    public TsrAuthenPresenter(){
        serviceManager = ServiceManager.getInstance();
    }

    public void setManager( ServiceManager manager ){
        serviceManager = manager;
    }

    @Override
    public void onViewCreate() {
        RxBus.get().register( this );
    }

    @Override
    public void onViewDestroy() {
        RxBus.get().unregister( this );
    }

    @Override
    public void requestTSR(String username, String password) {
        getView().onLoad();
        serviceManager.request(username, password, new ServiceManager.ServiceManagerCallback<TsrAuthenResult>() {
            @Override
            public void onSuccess(TsrAuthenResult result) {
                Log.e("TSR Authen result", result.getDisplayname());
                MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, result.getAd_name());
                MyApplication.getInstance().getPrefManager().setPreferrenceTimeStamp(Config.KEY_SESSION, new Date().getTime());
                getView().onDismiss();
                getView().goToSearchActivity();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("TSR Authen fail", t.getMessage());
                getView().onDismiss();
                getView().onFail(t.getMessage());
            }
        });
    }
}

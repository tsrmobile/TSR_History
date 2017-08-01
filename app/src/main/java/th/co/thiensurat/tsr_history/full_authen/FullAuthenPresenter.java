package th.co.thiensurat.tsr_history.full_authen;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.ConvertItem;
import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;

/**
 * Created by teerayut.k on 7/25/2017.
 */

public class FullAuthenPresenter extends BaseMvpPresenter<FullAuthenInterface.view> implements FullAuthenInterface.presenter {

    private ServiceManager serviceManager;
    private List<AuthenItem> authenItems = new ArrayList<AuthenItem>();
    public static FullAuthenPresenter create() {
        return new FullAuthenPresenter();
    }

    public FullAuthenPresenter(){
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
    public void requestLogin(final List<FullAuthenItem> fullAuthenItems) {
        getView().onLoad();
        serviceManager.requestFullAuthentication(fullAuthenItems, new ServiceManager.ServiceManagerCallback<AuthenItemResultGroup>() {
            @Override
            public void onSuccess(AuthenItemResultGroup result) {
                authenItems = ConvertItem.createListAuthenGroupFromResult( result ).getData();
                if (!authenItems.isEmpty()) {
                    getView().onDismiss();
                    getView().onFullAuthen(authenItems);
                    Log.e("onSuccess full authen", authenItems.get(0).getUsername() + "");
                } else {
                    Log.e("onSuccess full authen", authenItems.size() + "");
                    getView().onDismiss();
                    getView().onFullAuthen(authenItems);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e("Full authen onFail", t.getMessage());
                getView().onDismiss();
                getView().onFail(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onLoginValidation(String deviceId) {
        getView().onLoad();
        serviceManager.requestAuthentication(deviceId, new ServiceManager.ServiceManagerCallback<AuthenItemResultGroup>() {
            @Override
            public void onSuccess(AuthenItemResultGroup result) {
                authenItems = ConvertItem.createListAuthenGroupFromResult( result ).getData();
                if (!authenItems.isEmpty()) {
                    getView().onDismiss();
                    getView().onAuthen(authenItems);
                    //Log.e("onSuccess", authenItems.get(0).getLoggedin() + "");
                } else {
                    //Log.e("onSuccess", authenItems.size() + "");
                    getView().onDismiss();
                    getView().onAuthen(authenItems);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Search onFail", t.getMessage());
                getView().onDismiss();
                getView().onFail(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void goToSearchActivity() {
        getView().onLoad();
        getView().goToSearchActivity();
    }
}

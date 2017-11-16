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
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

/**
 * Created by teerayut.k on 7/25/2017.
 */

public class FullAuthenPresenter extends BaseMvpPresenter<FullAuthenInterface.view> implements FullAuthenInterface.presenter {

    private String exception = null;
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
                /*authenItems = ConvertItem.createListAuthenGroupFromResult( result ).getData();
                if (!authenItems.isEmpty()) {
                    getView().onDismiss();
                    getView().onFullAuthen(authenItems);
                    Log.e("onSuccess full authen", authenItems.get(0).getUsername() + "");
                } else {
                    Log.e("onSuccess full authen", authenItems.size() + "");
                    getView().onDismiss();
                    getView().onFullAuthen(authenItems);
                }*/
                if (result.getStatus().equals("SUCCESS")) {
                    getView().onDismiss();
                    authenItems = ConvertItem.createListAuthenGroupFromResult( result ).getData();
                    MyApplication.getInstance().getPrefManager().setPreferrenceBoolean(Config.KEY_BOOLEAN, true);
                    MyApplication.getInstance().getPrefManager().setPreferrence(Config.KEY_USERNAME, authenItems.get(0).getUsername());
                    getView().onSuccess();
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                }
            }
            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
            }
        });
    }

    /*@Override
    public void onLoginValidation(String deviceId) {
        getView().onLoad();
        serviceManager.requestAuthentication(deviceId, new ServiceManager.ServiceManagerCallback<AuthenItemResultGroup>() {
            @Override
            public void onSuccess(AuthenItemResultGroup result) {
                if (result.getStatus().equals("SUCCESS")) {
                    getView().onDismiss();
                    authenItems = ConvertItem.createListAuthenGroupFromResult( result ).getData();
                    getView().onAuthen(authenItems);
                } else if (result.getStatus().equals("FAIL")) {
                    getView().onDismiss();
                    //getView().onFail(result.getMessage());
                } else if (result.getStatus().equals("ERROR")) {
                    getView().onDismiss();
                    //getView().onFail(result.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
            }
        });
    }*/

    @Override
    public void goToSearchActivity() {
        //getView().onLoad();
        getView().goToSearchActivity();
    }

    private void setError(String exception) {
        this.exception = exception;
    }

    @Override
    public String getError() {
        return exception;
    }
}

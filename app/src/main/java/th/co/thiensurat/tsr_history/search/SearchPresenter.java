package th.co.thiensurat.tsr_history.search;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.ConvertItem;
import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.result.AuthenItemResultGroup;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.search.item.AuthenItem;
import th.co.thiensurat.tsr_history.utils.Config;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchPresenter extends BaseMvpPresenter<SearchInterface.view> implements SearchInterface.presenter{

    private ServiceManager serviceManager;
    private List<AuthenItem> authenItems = new ArrayList<AuthenItem>();
    public static SearchInterface.presenter create() {
        return new SearchPresenter();
    }

    public SearchPresenter(){
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
    public void goToResultCustomer(String data) {
        getView().goToResultCustomer(data);
    }

    @Override
    public void onLoginValidation(String deviceId) {
        getView().onLoad();
        serviceManager.requestAuthentication(deviceId, new ServiceManager.ServiceManagerCallback<AuthenItemResultGroup>() {
            @Override
            public void onSuccess(AuthenItemResultGroup result) {
                authenItems = ConvertItem.createListAuthenGroupFromResult( result ).getData();
                Log.e("onSuccess", authenItems.get(0).getUsername());
                /*if (result.getMessage().equals(Config.FAILED)) {
                    getView().onDismiss();
                    getView().onFail("ไม่พบรหัสผู้ใช้");
                } else if (result.getMessage().equals(Config.SUCCESS)) {
                    getView().onDismiss();
                    getView().onAuthen(authenItems);
                }*/
                getView().onDismiss();
                getView().onAuthen(authenItems);
            }

            @Override
            public void onFailure(Throwable t) {
                getView().onDismiss();
                getView().onFail(t.getLocalizedMessage());
            }
        });
    }
}

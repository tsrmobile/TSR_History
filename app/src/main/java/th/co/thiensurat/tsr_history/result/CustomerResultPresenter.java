package th.co.thiensurat.tsr_history.result;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import th.co.thiensurat.tsr_history.api.ConvertItem;
import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;
import th.co.thiensurat.tsr_history.utils.Config;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class CustomerResultPresenter extends BaseMvpPresenter<CustomerResultInterface.view> implements CustomerResultInterface.presenter {

    private ServiceManager serviceManager;
    private ListItemGroup listItemGroup;
    private List<ListItem> listItemList = new ArrayList<ListItem>();

    public static CustomerResultInterface.presenter create() {
        return new CustomerResultPresenter();
    }

    public CustomerResultPresenter(){
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
    public void onCancel() {
        getView().onBackToSearch();
    }

    @Override
    public void requestItem() {
        getView().onLoad();
        String data = getView().receiveItem();
        getView().showServiceAvailableView();
        serviceManager.requestHistory(data, new ServiceManager.ServiceManagerCallback<ListItemResultGroup>() {
            @Override
            public void onSuccess(ListItemResultGroup result) {
                if (result.getMessage().equals(Config.FAILED)) {
                    listItemList.clear();
                    getView().showServiceUnavailableView("ไม่พบข้อมูล\nหรือ\nประวัติลูกค้าอาจถูกตรวจสอบแล้ว");
                } else {
                    listItemList = ConvertItem.createListItemGroupFromResult( result ).getData();
                    getView().setItemAdapter(listItemList);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listItemList.clear();
                getView().onDismiss();
                getView().showServiceUnavailableView("ไม่พบข้อมูล\nหรือ\nประวัติลูกค้าอาจถูกตรวจสอบแล้ว");
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public void addHistory(final List<AddHistoryItem> items) {
        getView().onLoad();
        serviceManager.AddHistoryRequest(items, new ServiceManager.ServiceManagerCallback<AddHistoryResult>() {
            @Override
            public void onSuccess(AddHistoryResult result) {
                Log.e("Message", result.getMessage());
                if (result.getStatus().equals(Config.SUCCESS)) {
                    getView().onDismiss();
                    getView().onSuccess();
                } else if (result.getStatus().equals(Config.FAILED)) {
                    getView().onDismiss();
                    getView().onFail(result.getMessage());
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.e("Error", t.getMessage());
                getView().onDismiss();
            }
        });
    }

    private boolean addHistoryResultChecker( Response<AddHistoryResult> response ){
        if( response.isSuccessful() ){
            AddHistoryResult result = response.body();
            return Config.SUCCESS.equals( result.getStatus() );
        }
        return false;
    }
}

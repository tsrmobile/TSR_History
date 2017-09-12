package th.co.thiensurat.tsr_history.result;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import th.co.thiensurat.tsr_history.api.ConvertItem;
import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.event.AddEvent;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.request.LogBody;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.api.result.AddHistoryResult;
import th.co.thiensurat.tsr_history.api.result.DataItemResultGroup;
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
    private static ListItemGroup listItemGroup;
    private static List<ListItem> listItemList = new ArrayList<ListItem>();

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
    public void onGoToHome() {
        getView().onGoToHome();
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
                    ListItemGroup itemGroup = ConvertItem.createListItemGroupFromResult( result );
                    listItemGroup = itemGroup;
                    Log.e("request history by name", listItemList.get(0).getName());
                    getView().setItemAdapter(listItemList);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listItemList.clear();
                listItemGroup = null;
                getView().onDismiss();
                getView().showServiceUnavailableView("ไม่พบข้อมูล\nหรือ\nประวัติลูกค้าอาจถูกตรวจสอบแล้ว");
                //Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public void addHistory(final List<AddHistoryBody.HistoryBody> items) {
        getView().onLoad();
        serviceManager.AddHistoryRequest(items, new ServiceManager.ServiceManagerCallback<AddHistoryResult>() {
            @Override
            public void onSuccess(AddHistoryResult result) {
                //Log.e("Message", result.getMessage());
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
                //Log.e("Error", t.getMessage());
                getView().onFail(t.getMessage());
                getView().onDismiss();
            }
        });
    }

    @Override
    public void onSetItemGroup(ListItemGroup itemResultGroup) {
        this.listItemGroup = itemResultGroup;
    }

    @Override
    public ListItemGroup onGetItemGroup() {
        return listItemGroup;
    }

    @Override
    public void onRestoreItemToAdapter(ListItemGroup itemGroup) {
        //Log.e("Group size", itemGroup.getData().size() + "");
        this.listItemList = itemGroup.getData();
        getView().setItemAdapter(listItemList);
    }

    @Override
    public void saveLogToServer(List<LogBody.logBody> logBodyList) {
        serviceManager.saveLogRequest(logBodyList, new ServiceManager.ServiceManagerCallback<DataItemResultGroup>() {
            @Override
            public void onSuccess(DataItemResultGroup result) {
                if (result.getStatus().equals(Config.SUCCESS)) {
                    Log.e("Save Log success", result.getMessage());
                } else if (result.getStatus().equals(Config.FAILED)) {
                    Log.e("Save Log failed", result.getMessage());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //Log.e("Save Log failure", t.getMessage());
            }
        });
    }
}

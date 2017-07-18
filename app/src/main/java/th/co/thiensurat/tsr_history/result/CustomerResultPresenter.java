package th.co.thiensurat.tsr_history.result;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.ConvertItem;
import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.api.result.ListItemResultGroup;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;

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
        String data = getView().receiveItem();
        getView().showServiceAvailableView();
        serviceManager.requestHistory(data, new ServiceManager.ServiceManagerCallback<ListItemResultGroup>() {
            @Override
            public void onSuccess(ListItemResultGroup result) {
                listItemList = ConvertItem.createListItemGroupFromResult( result ).getData();
                getView().setItemAdapter(listItemList);
            }

            @Override
            public void onFailure(Throwable t) {
                listItemList.clear();
                String error[] = t.getMessage().split(":");
                getView().showServiceUnavailableView(error[1]);
                Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public void setItemAdapter(List<ListItem> listItems) {
        getView().setItemAdapter(listItems);
    }
}

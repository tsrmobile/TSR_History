package th.co.thiensurat.tsr_history.result_customer;

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
import th.co.thiensurat.tsr_history.utils.Config;

/**
 * Created by teerayut.k on 8/2/2017.
 */

public class CustomerByNamePresenter extends BaseMvpPresenter<CustomerByNameInterface.View>
        implements CustomerByNameInterface.Presenter {

    private ServiceManager serviceManager;
    private static ListItemGroup listItemGroup;
    private static List<ListItem> listItemList = new ArrayList<ListItem>();

    public static CustomerByNameInterface.Presenter create() {
        return new CustomerByNamePresenter();
    }

    public CustomerByNamePresenter(){
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
    public void requestItem() {
        getView().onLoad();
        String data = getView().receiveItem();
        getView().showServiceAvailableView();
        serviceManager.requestHistory(data, new ServiceManager.ServiceManagerCallback<ListItemResultGroup>() {
            @Override
            public void onSuccess(ListItemResultGroup result) {
                if (result.getMessage().equals(Config.FAILED)) {
                    listItemList.clear();
                    getView().onDismiss();
                    getView().showServiceUnavailableView();
                } else {
                    listItemList.clear();
                    listItemList = ConvertItem.createListItemGroupFromResult( result ).getData();

                    ListItemGroup itemGroup = ConvertItem.createListItemGroupFromResult( result );
                    listItemGroup = itemGroup;

                    getView().setItemAdapter(listItemList);
                    getView().onDismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listItemList.clear();
                getView().onDismiss();
                getView().showServiceUnavailableView();
                //Log.e("Error", t.getMessage());
            }
        });
    }

    @Override
    public void onSetItemGroup(ListItemGroup itemGroup) {
        this.listItemGroup = itemGroup;
    }

    @Override
    public ListItemGroup onGetItemGroup() {
        return listItemGroup;
    }

    @Override
    public void onRestoreItemToAdapter(ListItemGroup itemGroup) {
        this.listItemList = itemGroup.getData();
        getView().setItemAdapter(listItemList);
    }
}

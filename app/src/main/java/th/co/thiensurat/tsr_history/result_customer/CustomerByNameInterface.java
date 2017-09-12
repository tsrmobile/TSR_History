package th.co.thiensurat.tsr_history.result_customer;

import java.util.List;

import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;

/**
 * Created by teerayut.k on 8/2/2017.
 */

public class CustomerByNameInterface {

    public interface View extends BaseMvpInterface.View {
        void showServiceAvailableView();
        void showServiceUnavailableView();
        String receiveItem();
        void onLoad();
        void onDismiss();
        void onSuccess();
        void onFail(String fail);
        void setItemAdapter(List<ListItem> listItems);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<CustomerByNameInterface.View> {
        void requestItem();
        void onSetItemGroup(ListItemGroup itemGroup);
        ListItemGroup onGetItemGroup();
        void onRestoreItemToAdapter(ListItemGroup itemGroup);
        void checkHistory(List<AddHistoryBody.HistoryBody> items);
    }
}

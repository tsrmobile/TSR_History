package th.co.thiensurat.tsr_history.result;

import java.util.List;

import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.result.item.ListItem;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class CustomerResultInterface {

    public interface view extends BaseMvpInterface.View {
        String receiveItem();
        void onBackToSearch();
        void setItemAdapter(List<ListItem> listItems);
        void showServiceAvailableView();
        void showServiceUnavailableView(String fail);
        void onLoad();
        void onDismiss();
        void onSuccess();
        void onFail(String fail);
    }

    public interface presenter extends BaseMvpInterface.Presenter<CustomerResultInterface.view> {
        void onCancel();
        void requestItem();
        void addHistory(List<AddHistoryItem> items);
        //void setItemAdapter(List<ListItem> listItems);
    }
}

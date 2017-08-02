package th.co.thiensurat.tsr_history.result_customer;

import java.util.List;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.result.item.ListItem;

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
        void setItemAdapter(List<ListItem> listItems);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<CustomerByNameInterface.View> {
        void requestItem();
    }
}

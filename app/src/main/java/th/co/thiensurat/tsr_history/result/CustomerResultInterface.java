package th.co.thiensurat.tsr_history.result;

import java.util.List;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class CustomerResultInterface {

    public interface view extends BaseMvpInterface.View {
        String receiveItem();

        void setItemToView(List<CustomerItem> customerItems);

        void onBackToSearch();
    }

    public interface presenter extends BaseMvpInterface.Presenter<CustomerResultInterface.view> {
        void onCancel();
        void requestItem();
    }
}

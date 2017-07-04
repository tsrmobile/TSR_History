package th.co.thiensurat.tsr_history.result;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class CustomerResultInterface {

    public interface view extends BaseMvpInterface.View {
        void receiveItem();
    }

    public interface presenter extends BaseMvpInterface.Presenter<CustomerResultInterface.view> {

    }
}

package th.co.thiensurat.tsr_history.result;

import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class CustomerResultPresenter extends BaseMvpPresenter<CustomerResultInterface.view>
    implements CustomerResultInterface.presenter {

    public static CustomerResultInterface.presenter create() {
        return new CustomerResultPresenter();
    }
}

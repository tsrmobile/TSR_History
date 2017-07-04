package th.co.thiensurat.tsr_history.search;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchInterface {

    public interface view extends BaseMvpInterface.View {
        void goToResultCustomer();
    }

    public interface presenter extends BaseMvpInterface.Presenter<SearchInterface.view> {

        void goToResultCustomer();
    }
}

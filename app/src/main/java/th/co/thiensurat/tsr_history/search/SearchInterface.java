package th.co.thiensurat.tsr_history.search;

import java.util.List;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchInterface {

    public interface view extends BaseMvpInterface.View {
        void goToResultCustomer();

        void onFail(String failed);
    }

    public interface presenter extends BaseMvpInterface.Presenter<SearchInterface.view> {

        void requestCustomer(String data);
    }
}

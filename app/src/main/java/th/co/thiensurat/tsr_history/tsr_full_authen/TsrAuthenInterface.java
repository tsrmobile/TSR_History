package th.co.thiensurat.tsr_history.tsr_full_authen;

import java.util.List;

import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 8/7/2017.
 */

public class TsrAuthenInterface {

    public interface View extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void goToSearchActivity();
        void onFail(String fail);
    }

    public interface Presenter extends BaseMvpInterface.Presenter<TsrAuthenInterface.View> {
        void requestTSR(String username, String password);
    }
}

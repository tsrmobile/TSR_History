package th.co.thiensurat.tsr_history.choice_authen;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;

/**
 * Created by teerayut.k on 8/7/2017.
 */

public class ChoiceInterface {

    public interface View extends BaseMvpInterface.View {
        void goToBigHead();
        void goToTSR();
    }

    public interface Presenter extends BaseMvpInterface.Presenter<ChoiceInterface.View> {
        void requestBigHead();
        void requestTSR();
    }
}

package th.co.thiensurat.tsr_history.choice_authen;

import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 8/7/2017.
 */

public class ChoicePresenter extends BaseMvpPresenter<ChoiceInterface.View> implements ChoiceInterface.Presenter {

    public static ChoiceInterface.Presenter create() {
        return new ChoicePresenter();
    }

    @Override
    public void requestBigHead() {
        getView().goToBigHead();
    }

    @Override
    public void requestTSR() {
        getView().goToTSR();
    }
}

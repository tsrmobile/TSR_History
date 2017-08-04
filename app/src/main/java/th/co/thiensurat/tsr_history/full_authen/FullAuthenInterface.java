package th.co.thiensurat.tsr_history.full_authen;

import java.util.List;

import th.co.thiensurat.tsr_history.api.result.FullAuthenItem;
import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;

/**
 * Created by teerayut.k on 7/25/2017.
 */

public class FullAuthenInterface {

    public interface view extends BaseMvpInterface.View {
        void onLoad();
        void onDismiss();
        void onFail(String failed);
        void onAuthen(List<AuthenItem> authenItems);
        void onFullAuthen(List<AuthenItem> authenItems);
        void goToSearchActivity();
    }

    public interface presenter extends BaseMvpInterface.Presenter<FullAuthenInterface.view> {
        void requestLogin(final List<FullAuthenItem> fullAuthenItems);
        void onLoginValidation(String deviceId);
        void goToSearchActivity();
        String getError();
    }
}

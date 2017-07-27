package th.co.thiensurat.tsr_history.search;

import java.util.List;

import th.co.thiensurat.tsr_history.base.BaseMvpInterface;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchInterface {

    public interface view extends BaseMvpInterface.View {
        void goToResultCustomer(String data);
        void onLoad();
        void onDismiss();
        void onFail(String failed);
        //void onAuthen(List<AuthenItem> authenItems);
    }

    public interface presenter extends BaseMvpInterface.Presenter<SearchInterface.view> {
        void goToResultCustomer(String data);
        //void onLoginValidation(String deviceId);
    }
}

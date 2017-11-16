package th.co.thiensurat.tsr_history.search;

import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.api.ServiceManager;
import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.full_authen.item.AuthenItem;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchPresenter extends BaseMvpPresenter<SearchInterface.view> implements SearchInterface.presenter{

    public static SearchInterface.presenter create() {
        return new SearchPresenter();
    }

    public SearchPresenter(){
    }

    @Override
    public void goToResultCustomer(String data) {
        if (data.matches("\\d{13}")) {
            getView().goToResultCustomer(data);
        } else {
            getView().goToCustomerByName(data);
        }
    }
}

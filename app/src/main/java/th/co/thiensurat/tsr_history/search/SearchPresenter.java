package th.co.thiensurat.tsr_history.search;

import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchPresenter extends BaseMvpPresenter<SearchInterface.view>
    implements SearchInterface.presenter{

    public static SearchInterface.presenter create() {
        return new SearchPresenter();
    }
}

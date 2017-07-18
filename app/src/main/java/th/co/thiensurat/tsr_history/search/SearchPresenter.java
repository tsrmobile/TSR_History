package th.co.thiensurat.tsr_history.search;

import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class SearchPresenter extends BaseMvpPresenter<SearchInterface.view> implements SearchInterface.presenter{

    public static SearchInterface.presenter create() {
        return new SearchPresenter();
    }

    @Override
    public void goToResultCustomer(String data) {
        getView().goToResultCustomer(data);
    }

    @Override
    public boolean onLoginValidation(String deviceId) {
        return false;
    }

    /*@Override
    public void requestCustomer(String data) {
        customerItem = new CustomerItem();
        customerItem.setDate("Jul, 01 2017");
        customerItem.setProduct("เครื่องกรองน้ำ Alkaline Deluxe");
        customerItem.setPrice("7,490");
        customerItem.setSale("000001");
        customerItem.setStatus("N");
        customerItems.add(customerItem);

        customerItem = new CustomerItem();
        customerItem.setDate("Jul, 04 2017");
        customerItem.setProduct("เครื่องกรองน้ำ Alkaline Plus");
        customerItem.setPrice("9,500");
        customerItem.setSale("000001");
        customerItem.setStatus("T");
        customerItems.add(customerItem);

        getView().goToResultCustomer(customerItems);
    }*/
}

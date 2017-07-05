package th.co.thiensurat.tsr_history.result;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.base.BaseMvpPresenter;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class CustomerResultPresenter extends BaseMvpPresenter<CustomerResultInterface.view> implements CustomerResultInterface.presenter {

    public static CustomerResultInterface.presenter create() {
        return new CustomerResultPresenter();
    }

    private CustomerItem customerItem;
    private List<CustomerItem> customerItems = new ArrayList<CustomerItem>();

    @Override
    public void onCancel() {
        getView().onBackToSearch();
    }

    @Override
    public void requestItem() {
        String data = getView().receiveItem();
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

        getView().setItemToView(customerItems);
    }
}

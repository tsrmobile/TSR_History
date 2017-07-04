package th.co.thiensurat.tsr_history.result;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;
import th.co.thiensurat.tsr_history.utils.Config;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter> implements CustomerResultInterface.view{

    private CustomerResultAdapter adapter;
    private List<CustomerItem> customerItems = new ArrayList<CustomerItem>();
    @Override
    public CustomerResultInterface.presenter createPresenter() {
        return CustomerResultPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_result;
    }

    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        adapter = new CustomerResultAdapter(this,customerItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void setupView() {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void receiveItem() {
        Bundle bundle = getIntent().getBundleExtra(Config.KEY_ITEM);
    }
}

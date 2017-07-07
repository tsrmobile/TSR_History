package th.co.thiensurat.tsr_history.result;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.utils.Config;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter> implements CustomerResultInterface.view{

    private LinearLayoutManager layoutManager;
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
    @Bind(R.id.totalSummary) TextView textViewTotal;
    @Bind(R.id.btn_save) Button save;
    @Bind(R.id.btn_cancel) Button cancel;
    @Bind(R.id.name) TextView customerName;
    @Bind(R.id.address) TextView customerAddress;
    @Bind(R.id.phone) TextView customerPhone;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        save.setOnClickListener(onSaveData());
        cancel.setOnClickListener(onCancel());
    }

    @Override
    public void setupInstance() {
        getPresenter().requestItem();
        adapter = new CustomerResultAdapter(this, customerItems);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setupView() {
        layoutManager.setReverseLayout(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initialize() {

    }

    @Override
    public String receiveItem() {
        String data = getIntent().getStringExtra(Config.KEY_DATA);
        return data;
    }

    @Override
    public void setItemToView(List<CustomerItem> customerItems) {
        this.customerItems = customerItems;
        textViewTotal.setText(String.valueOf(this.customerItems.size()));
    }

    @Override
    public void onBackToSearch() {
        this.customerItems.clear();
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
    }

    private View.OnClickListener onSaveData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onCancel();
            }
        };
    }
}

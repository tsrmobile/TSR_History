package th.co.thiensurat.tsr_history.result_customer;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.result.CustomerResultActivity;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result_customer.adapter.CustomerByNameAdapter;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class CustomerByNameActivity extends BaseMvpActivity<CustomerByNameInterface.Presenter>
        implements CustomerByNameInterface.View, CustomerByNameAdapter.OnCustomerItemClickListener {

    private CustomerByNameAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ListItem> listItemList = new ArrayList<ListItem>();
    @Override
    public CustomerByNameInterface.Presenter createPresenter() {
        return CustomerByNamePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_same_name;
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.container_service_unavailable) FrameLayout containerServiceUnvailable;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        getPresenter().requestItem();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setupView() {
        setToolbar();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void showServiceAvailableView() {
        recyclerView.setVisibility( View.VISIBLE );
        containerServiceUnvailable.setVisibility( View.GONE );
    }

    @Override
    public void showServiceUnavailableView() {
        recyclerView.setVisibility( View.GONE );
        containerServiceUnvailable.setVisibility( View.VISIBLE );
    }

    @Override
    public String receiveItem() {
        String data = getIntent().getStringExtra(Config.KEY_DATA);
        return data;
    }

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(CustomerByNameActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    @Override
    public void setItemAdapter(List<ListItem> listItems) {
        this.listItemList = listItems;
        adapter = new CustomerByNameAdapter(CustomerByNameActivity.this, listItems);
        recyclerView.setAdapter(adapter);
        adapter.setCustomerClickListener(this);
    }

    @Override
    public void onCustomerClick(View view, int position) {
        ListItem item = listItemList.get(position);
        Intent intent = new Intent(getApplicationContext(), CustomerResultActivity.class);
        intent.putExtra(Config.KEY_DATA, item.getIdcard());
        intent.putExtra(Config.KEY_CLASS, "CustomerByNameActivity");
        startActivityForResult(intent, Config.REQUEST_RESULT);
    }

    private void setToolbar() {
        toolbar.setTitle(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

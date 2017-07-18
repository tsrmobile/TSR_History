package th.co.thiensurat.tsr_history.result.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.result.item.ListItem;

/**
 * Created by teerayut.k on 7/4/2017.
 */

public class CustomerResultAdapter extends RecyclerView.Adapter<CustomerResultAdapter.ViewHolder> {

    private Context context;
    private List<ListItem> listItems = new ArrayList<ListItem>();
    public CustomerResultAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public CustomerResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerResultAdapter.ViewHolder holder, int position) {
        ListItem item = listItems.get(position);
        holder.dateTime.setText(item.getDate());
        holder.productName.setText(item.getProductName() + " (" + item.getProductModel() + ")");
        holder.productPrice.setText(context.getResources().getString(R.string.price_amount) + ": " + String.format("%,d", item.getTotalPrice()));
        holder.saleID.setText(context.getResources().getString(R.string.sale) + ": " + item.getSaleCode());
        holder.statusCode.setText(item.getCustomerStatus().toUpperCase() + ", " +item.getAccountStatus().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.datetime) TextView dateTime;
        @Bind(R.id.product) TextView productName;
        @Bind(R.id.price) TextView productPrice;
        @Bind(R.id.sale) TextView saleID;
        @Bind(R.id.status) TextView statusCode;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

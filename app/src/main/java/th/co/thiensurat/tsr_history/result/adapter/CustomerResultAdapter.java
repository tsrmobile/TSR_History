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
import th.co.thiensurat.tsr_history.result.item.CustomerItem;

/**
 * Created by teerayut.k on 7/4/2017.
 */

public class CustomerResultAdapter extends RecyclerView.Adapter<CustomerResultAdapter.ViewHolder> {

    private Context context;
    private List<CustomerItem> customerItems = new ArrayList<CustomerItem>();
    public CustomerResultAdapter(Context context, List<CustomerItem> customerItems) {
        this.context = context;
        this.customerItems = customerItems;
    }

    @Override
    public CustomerResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerResultAdapter.ViewHolder holder, int position) {
        //CustomerItem item = customerItems.get(position);
        int no = position + 1;
        holder.dateTime.setText("Jul, 04 2017");
        holder.productName.setText(no + ". " + "เครื่องกรองน้ำ Alkaline Deluxe");
        holder.productPrice.setText(context.getResources().getString(R.string.price_amount) + ": " + " 7,490");
        holder.saleID.setText(context.getResources().getString(R.string.sale) + ": " + "000001");
        holder.statusCode.setText("N");
    }

    @Override
    public int getItemCount() {
        //return customerItems.size();
        return 1;
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

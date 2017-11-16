package th.co.thiensurat.tsr_history.result_customer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result_customer.CustomerByNameActivity;

/**
 * Created by teerayut.k on 8/2/2017.
 */

public class CustomerByNameAdapter extends RecyclerView.Adapter<CustomerByNameAdapter.ViewHolder> {

    private Context context;
    private OnCustomerItemClickListener customerClickListener;
    private List<ListItem> listItemList = new ArrayList<ListItem>();
    public CustomerByNameAdapter(Context context, List<ListItem> listItems) {
        this.context = context;
        this.listItemList = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_customer_by_name, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem item = listItemList.get(position);
        holder.textViewName.setText(context.getResources().getString(R.string.text_name_title) + ": " + item.getName());
        holder.idCard.setText(context.getResources().getString(R.string.text_idcard_title) + ": " + item.getIdcard());
    }

    @Override
    public int getItemCount() {
        return listItemList.size();
    }

    public void setCustomerClickListener(OnCustomerItemClickListener customerClickListener) {
        this.customerClickListener = customerClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name) TextView textViewName;
        @BindView(R.id.idcard) TextView idCard;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(onClickListener());
        }

        private View.OnClickListener onClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customerClickListener != null) {
                        customerClickListener.onCustomerClick(view, getPosition());
                    }
                }
            };
        }
    }

    public interface OnCustomerItemClickListener {
        void onCustomerClick(View view , int position);
    }
}

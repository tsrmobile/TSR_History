package th.co.thiensurat.tsr_history.result.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.result.item.ListItem;

/**
 * Created by teerayut.k on 7/4/2017.
 */

public class CustomerResultAdapter extends RecyclerView.Adapter<CustomerResultAdapter.ViewHolder> {

    private Context context;
    private ClickListener clickListener;
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
        holder.contractNumber.setText(context.getResources().getString(R.string.text_contract_number) + ": " + item.getCountno());
        holder.dateTime.setText(context.getResources().getString(R.string.text_contract_date) + ": " + ConvertDateFormat(item.getDate()));
        holder.customerName.setText(context.getResources().getString(R.string.text_name_title) + ": " + item.getName());
        /*holder.customerID.setText(context.getResources().getString(R.string.text_idcard_title) + ": " + item.getIdcard());
        holder.productName.setText(context.getResources().getString(R.string.text_product) + ": " + item.getProductName() + " (" + item.getProductModel() + ")");
        holder.productPrice.setText(context.getResources().getString(R.string.price_amount) + ": " + String.format("%,d", item.getTotalPrice()));
        holder.type.setText(context.getResources().getString(R.string.text_pay_type) + ": " + item.getPaytype());
        holder.totalMonth.setText(context.getResources().getString(R.string.text_pay_total_month) + ": " + item.getPeriods());
        holder.lastMonth.setText(context.getResources().getString(R.string.text_pay_last_month) + ": " + item.getPaylastmonth());
        holder.laststatus.setText(context.getResources().getString(R.string.text_last_status) + ": " + item.getLastpaystatus());
        holder.saleID.setText(context.getResources().getString(R.string.sale) + ": " + item.getSaleCode());*/
        holder.statusCode.setText(((item.getCustomerStatus() == null)? "" : item.getCustomerStatus().toUpperCase()) + " ("
                + ((item.getCustomerStatus().isEmpty())? "ไม่สามารถแสดงสถานะได้" : item.getAgingDetail()) + ") \n"
                + context.getResources().getString(R.string.text_date_status) + " " + ConvertDateFormat(item.getStDate()));


        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.btnSend);
        holder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.send));
            }
        });

        /*if ((position + 1) == listItems.size()) {
            Log.e("Last item", ConvertDateFormat(item.getDate()));
        } else {

        }*/
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /*@Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {


        /*@Bind(R.id.product) TextView productName;
        @Bind(R.id.price) TextView productPrice;
        @Bind(R.id.sale) TextView saleID;*/

        /*@Bind(R.id.payTotalMonth) TextView totalMonth;
        @Bind(R.id.payType) TextView type;
        @Bind(R.id.payLastMonth) TextView lastMonth;
        @Bind(R.id.lastStatus) TextView laststatus;
        @Bind(R.id.idcard) TextView customerID;*/
        @BindView(R.id.number) TextView contractNumber;
        @BindView(R.id.datetime) TextView dateTime;
        @BindView(R.id.name) TextView customerName;
        @BindView(R.id.status) TextView statusCode;
        @BindView(R.id.btn_send) LinearLayout btnSend;
        @BindView(R.id.swipe) SwipeLayout swipeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnSend.setOnClickListener( onSend() );
        }

        private View.OnClickListener onSend() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.sendClickListener(view, getPosition());
                }
            };
        }
    }

    private String ConvertDateFormat(String strdate) {
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
        int year = 0, month = 0, day = 0;
        try {
            Date date = dbFormat.parse(strdate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DATE);
            return String.format("%s/%s/%s", day,month,year+543);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface ClickListener{
        public void sendClickListener(View view, int position);

    }
}

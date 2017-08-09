package th.co.thiensurat.tsr_history.result.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        /*@Bind(R.id.product) TextView productName;
        @Bind(R.id.price) TextView productPrice;
        @Bind(R.id.sale) TextView saleID;*/

        /*@Bind(R.id.payTotalMonth) TextView totalMonth;
        @Bind(R.id.payType) TextView type;
        @Bind(R.id.payLastMonth) TextView lastMonth;
        @Bind(R.id.lastStatus) TextView laststatus;
        @Bind(R.id.idcard) TextView customerID;*/
        @Bind(R.id.number) TextView contractNumber;
        @Bind(R.id.datetime) TextView dateTime;
        @Bind(R.id.name) TextView customerName;
        @Bind(R.id.status) TextView statusCode;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
}

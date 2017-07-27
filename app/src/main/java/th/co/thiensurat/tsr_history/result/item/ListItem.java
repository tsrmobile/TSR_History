package th.co.thiensurat.tsr_history.result.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.tsr_history.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 7/14/2017.
 */

public class ListItem extends BaseItem implements Parcelable {

    private String ref;
    private String countno;
    private String idcard;
    private String name;
    private String lastpaystatus;
    private String customerStatus;
    private String accountStatus;
    private String paytype;
    private int periods;
    private int paylastmonth;
    private int totalPrice;
    private String productName;
    private String productModel;
    private String saleCode;
    private String date;
    private int agingCumulative;
    private int agingContinuous;
    private String agingDetail;

    public ListItem() {

    }

    public String getRef() {
        return ref;
    }

    public ListItem setRef(String ref) {
        this.ref = ref;
        return this;
    }

    public String getCountno() {
        return countno;
    }

    public ListItem setCountno(String countno) {
        this.countno = countno;
        return this;
    }

    public String getIdcard() {
        return idcard;
    }

    public ListItem setIdcard(String idcard) {
        this.idcard = idcard;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastpaystatus() {
        return lastpaystatus;
    }

    public ListItem setLastpaystatus(String lastpaystatus) {
        this.lastpaystatus = lastpaystatus;
        return this;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public ListItem setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
        return this;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public ListItem setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
        return this;
    }

    public String getPaytype() {
        return paytype;
    }

    public ListItem setPaytype(String paytype) {
        this.paytype = paytype;
        return this;
    }

    public int getPeriods() {
        return periods;
    }

    public ListItem setPeriods(int periods) {
        this.periods = periods;
        return this;
    }

    public int getPaylastmonth() {
        return paylastmonth;
    }

    public ListItem setPaylastmonth(int paylastmonth) {
        this.paylastmonth = paylastmonth;
        return this;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public ListItem setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public ListItem setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getProductModel() {
        return productModel;
    }

    public ListItem setProductModel(String productModel) {
        this.productModel = productModel;
        return this;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public ListItem setSaleCode(String saleCode) {
        this.saleCode = saleCode;
        return this;
    }

    public String getDate() {
        return date;
    }

    public ListItem setDate(String date) {
        this.date = date;
        return this;
    }

    public int getAgingCumulative() {
        return agingCumulative;
    }

    public ListItem setAgingCumulative(int agingCumulative) {
        this.agingCumulative = agingCumulative;
        return this;
    }

    public int getAgingContinuous() {
        return agingContinuous;
    }

    public ListItem setAgingContinuous(int agingContinuous) {
        this.agingContinuous = agingContinuous;
        return this;
    }

    public String getAgingDetail() {
        return agingDetail;
    }

    public ListItem setAgingDetail(String agingDetail) {
        this.agingDetail = agingDetail;
        return this;
    }

    protected ListItem(Parcel in) {
        super(in);
        ref = in.readString();
        countno = in.readString();
        idcard = in.readString();
        name = in.readString();
        lastpaystatus = in.readString();
        customerStatus = in.readString();
        accountStatus = in.readString();
        paytype = in.readString();
        periods = in.readInt();
        paylastmonth = in.readInt();
        totalPrice = in.readInt();
        productName = in.readString();
        productModel = in.readString();
        saleCode = in.readString();
        date = in.readString();
        agingCumulative = in.readInt();
        agingContinuous = in.readInt();
        agingDetail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(ref);
        dest.writeString(countno);
        dest.writeString(idcard);
        dest.writeString(name);
        dest.writeString(lastpaystatus);
        dest.writeString(customerStatus);
        dest.writeString(accountStatus);
        dest.writeString(paytype);
        dest.writeInt(periods);
        dest.writeInt(paylastmonth);
        dest.writeInt(totalPrice);
        dest.writeString(productName);
        dest.writeString(productModel);
        dest.writeString(saleCode);
        dest.writeString(date);
        dest.writeInt(agingCumulative);
        dest.writeInt(agingContinuous);
        dest.writeString(agingDetail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException{
        ListItem listItem = new ListItem()
                .setRef(ref)
                .setCountno(countno)
                .setIdcard(idcard)
                .setName(name)
                .setLastpaystatus(lastpaystatus)
                .setCustomerStatus(customerStatus)
                .setAccountStatus(accountStatus)
                .setPaytype(paytype)
                .setPeriods(periods)
                .setPaylastmonth(paylastmonth)
                .setTotalPrice(totalPrice)
                .setProductName(productName)
                .setProductModel(productModel)
                .setSaleCode(saleCode)
                .setDate(date)
                .setAgingCumulative(agingCumulative)
                .setAgingContinuous(agingContinuous)
                .setAgingDetail(agingDetail);
        return listItem;
    }

    @Override
    public int hashCode(){
        int result = ref != null ? ref.hashCode() : 0;
        result = 31 * result + ( countno != null ? countno.hashCode() : 0 );
        result = 31 * result + ( idcard != null ? idcard.hashCode() : 0 );
        result = 31 * result + ( name != null ? name.hashCode() : 0 );
        result = 31 * result + ( lastpaystatus != null ? lastpaystatus.hashCode() : 0 );
        result = 31 * result + ( customerStatus != null ? customerStatus.hashCode() : 0 );
        result = 31 * result + ( accountStatus != null ? accountStatus.hashCode() : 0 );
        result = 31 * result + ( paytype != null ? paytype.hashCode() : 0 );
        result = 31 * result + periods;
        result = 31 * result + paylastmonth;
        result = 31 * result + totalPrice;
        result = 31 * result + ( productName != null ? productName.hashCode() : 0 );
        result = 31 * result + ( productModel != null ? productModel.hashCode() : 0 );
        result = 31 * result + ( saleCode != null ? saleCode.hashCode() : 0 );
        result = 31 * result + (date != null ? date.hashCode() : 0 );
        result = 31 * result + agingCumulative;
        result = 31 * result + agingContinuous;
        result = 31 * result + ( agingDetail != null ? agingDetail.hashCode() : 0 );
        return result;
    }
}

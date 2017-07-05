package th.co.thiensurat.tsr_history.result.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by teerayut.k on 7/4/2017.
 */
public class CustomerItem implements Parcelable {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String date;
    private String product;
    private String price;
    private String sale;
    private String status;

    /*public CustomerItem(String date, String product, String price, String sale, String status){
        this.date = date;
        this.product = product;
        this.price = price;
        this.sale = sale;
        this.status = status;
    }*/

    public CustomerItem() {

    }

    protected CustomerItem(Parcel in) {
        date = in.readString();
        product = in.readString();
        price = in.readString();
        sale = in.readString();
        status = in.readString();
    }

    public static final Creator<CustomerItem> CREATOR = new Creator<CustomerItem>() {
        @Override
        public CustomerItem createFromParcel(Parcel in) {
            return new CustomerItem(in);
        }

        @Override
        public CustomerItem[] newArray(int size) {
            return new CustomerItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(product);
        parcel.writeString(price);
        parcel.writeString(sale);
        parcel.writeString(status);
    }
}

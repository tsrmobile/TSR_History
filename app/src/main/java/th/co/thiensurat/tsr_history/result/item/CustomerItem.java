package th.co.thiensurat.tsr_history.result.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by teerayut.k on 7/4/2017.
 */

public class CustomerItem implements Parcelable {

    private String date;
    private String product;
    private String price;
    private String sale;
    private String status;

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

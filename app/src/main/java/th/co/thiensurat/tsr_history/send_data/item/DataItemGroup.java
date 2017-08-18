package th.co.thiensurat.tsr_history.send_data.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 8/17/2017.
 */

public class DataItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<DataItem> data;

    public DataItemGroup() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public List<BaseItem> getBaseItems(){
        List<BaseItem> baseItems = new ArrayList<>(  );
        for( DataItem item : data ){
            baseItems.add(item);
        }
        return baseItems;
    }

    public DataItemGroup(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(DataItem.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(message);
        parcel.writeTypedList(data);
    }

    public static final Creator<DataItemGroup> CREATOR = new Creator<DataItemGroup>() {
        @Override
        public DataItemGroup createFromParcel(Parcel in) {
            return new DataItemGroup(in);
        }

        @Override
        public DataItemGroup[] newArray(int size) {
            return new DataItemGroup[size];
        }
    };
}

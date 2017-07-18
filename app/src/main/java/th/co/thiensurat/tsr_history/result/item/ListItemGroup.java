package th.co.thiensurat.tsr_history.result.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import th.co.thiensurat.tsr_history.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 7/17/2017.
 */

public class ListItemGroup implements Parcelable {

    private String status;
    private String message;
    private List<ListItem> data;

    public ListItemGroup() {

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

    public List<ListItem> getData() {
        return data;
    }

    public void setData(List<ListItem> data) {
        this.data = data;
    }

    public List<BaseItem> getBaseItems(){
        List<BaseItem> baseItems = new ArrayList<>(  );
        for( ListItem item : data ){
            baseItems.add(item);
        }
        return baseItems;
    }

    public ListItemGroup(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.createTypedArrayList(ListItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListItemGroup> CREATOR = new Creator<ListItemGroup>() {
        @Override
        public ListItemGroup createFromParcel(Parcel in) {
            return new ListItemGroup(in);
        }

        @Override
        public ListItemGroup[] newArray(int size) {
            return new ListItemGroup[size];
        }
    };
}

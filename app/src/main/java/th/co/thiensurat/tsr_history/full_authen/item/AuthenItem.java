package th.co.thiensurat.tsr_history.full_authen.item;

import android.os.Parcel;
import android.os.Parcelable;

import th.co.thiensurat.tsr_history.base.adapter.BaseItem;

/**
 * Created by teerayut.k on 7/20/2017.
 */

public class AuthenItem extends BaseItem implements Parcelable {

    private int loggedin;
    private String username;

    public AuthenItem() {

    }

    public int getLoggedin() {
        return loggedin;
    }

    public AuthenItem setLoggedin(int loggedin) {
        this.loggedin = loggedin;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AuthenItem setUsername(String username) {
        this.username = username;
        return this;
    }

    protected AuthenItem(Parcel in) {
        super(in);
        loggedin = in.readInt();
        username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(loggedin);
        dest.writeString(username);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenItem> CREATOR = new Creator<AuthenItem>() {
        @Override
        public AuthenItem createFromParcel(Parcel in) {
            return new AuthenItem(in);
        }

        @Override
        public AuthenItem[] newArray(int size) {
            return new AuthenItem[size];
        }
    };

    @Override
    public BaseItem clone() throws CloneNotSupportedException{
        AuthenItem item = new AuthenItem()
                .setLoggedin(loggedin)
                .setUsername(username);
        return item;
    }

    @Override
    public int hashCode(){
        int result = 0;
        result = 31 * result + loggedin;
        result = 31 * result + ( username != null ? username.hashCode() : 0 );
        return result;
    }
}

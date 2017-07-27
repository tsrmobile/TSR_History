package th.co.thiensurat.tsr_history.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by teera-s on 5/19/2016 AD.
 */
public class MyPreferenceManager {
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "APP";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setPreferrence(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreferrence(String key) {
        return pref.getString(key, null);
    }

    public void setPreferrenceTimeStamp(String key, long timestamp) {
        editor.putLong(key, timestamp);
        editor.commit();
    }

    public long getPreferrenceTimeStamp(String key) {
        return pref.getLong(key, 0);
    }

    public void setPreferrenceBoolean(String key, boolean status) {
        editor.putBoolean(key, status);
        editor.commit();
    }

    public boolean getPreferrenceBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    /*public void storeUser(Users user) {
        editor.putString(KEY_DEVICE_ID, user.getDevice_id());
        editor.putString(KEY_EMAIL_ACCOUNT, user.getEmail());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getDevice_id() + ", " + user.getEmail() + ", " + user.getToken());
    }

    public Users getUser() {
        if (pref.getString(KEY_DEVICE_ID, null) != null) {
            String device_id, email, token;
            device_id = pref.getString(KEY_DEVICE_ID, null);
            email = pref.getString(KEY_EMAIL_ACCOUNT, null);
            token = pref.getString(KEY_TOKEN, null);

            Users user = new Users(device_id, email, token);
            return user;
        }
        return null;
    }

    public void addNotificationToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getNotificationsToken() {
        return pref.getString(KEY_TOKEN, null);
    }*/

    public void clear() {
        editor.clear();
        editor.commit();
    }
}

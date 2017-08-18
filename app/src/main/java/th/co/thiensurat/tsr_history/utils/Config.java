package th.co.thiensurat.tsr_history.utils;

import android.Manifest;

/**
 * Created by teerayut.k on 7/3/2017.
 */

public class Config {

    public static final int REQUEST_SETTINGS = 01;
    public static final int REQUEST_LOGIN = 02;
    public static final int REQUEST_IMAGE_VIEW = 03;
    public static final int REQUEST_RESULT = 04;
    public static final int REQUEST_RESULT_BY_NAME = 05;
    public static final int REQUEST_SEARCH = 06;
    public static final int REQUEST_EXTERNAL_STORAGE = 07;
    public static final int REQUEST_SEND_DATA = 10;

    public static final String KEY_DATA = "DATA";
    public static final String KEY_CLASS = "CLASS";
    public static final String KEY_DEVICE_ID = "DEVICEID";
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_SESSION = "SESSION";
    public static final String KEY_BOOLEAN = "STATUS";
    public static final String KEY_SAVE_STATE_RESULT = "STATE";
    public static final String KEY_SAVE_STATE_NAME = "STATE_NAME";

    public static final String KEY_STATE_DATA = "STATE_DATA";
    public static final String KEY_STATE_PROVINCE = "STATE_PROVINCE";
    public static final String KEY_STATE_DISTRICT = "STATE_DISTRICT";
    public static final String KEY_STATE_SUB_DISTRICT = "STATE_SUB_DISTRICT";

    public static final String KEY_CONTACT_CODE = "contactCode";

    public static final String KEY_PROVINCE = "province";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_SUB_DISTRICT = "subdistrict";
    public static final String KEY_DATA_CODE = "code";

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAIL";
    public static final String ERROR = "ERROR";

    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
}

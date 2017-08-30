package th.co.thiensurat.tsr_history.send_data;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.Body;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.request.SendDataBody;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.send_data.adapter.SpinnerCustomAdapter;
import th.co.thiensurat.tsr_history.send_data.item.DataItem;
import th.co.thiensurat.tsr_history.send_data.item.DataItemGroup;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.GPSTracker;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class SendDataActivity extends BaseMvpActivity<SendDataInterface.Presenter>
        implements SendDataInterface.View, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private String coutno, name;
    private String province = "";
    private String district = "";
    private String subdistrict = "";
    private String currentLocation = "";
    private String product = "";
    private String condition = "";
    private GPSTracker gpsTracker;
    private SpinnerCustomAdapter spinnerCustomAdapter;
    private List<DataItem> dataItemList = new ArrayList<DataItem>();

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    public SendDataInterface.Presenter createPresenter() {
        return SendDataPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_send_data;
    }

    @Bind(R.id.count_no) EditText countNo;
    @Bind(R.id.addr_number) EditText addrNumber;
    @Bind(R.id.addr_moo) EditText addrMoo;
    @Bind(R.id.addr_soi) EditText addrSoi;
    @Bind(R.id.addr_road) EditText addrRoad;
    @Bind(R.id.addr_village) EditText addrVillage;
    @Bind(R.id.addr_building) EditText addrBuilding;
    @Bind(R.id.addr_province) Spinner spinnerProvince;
    @Bind(R.id.addr_district) Spinner spinnerDistrict;
    @Bind(R.id.addr_sub_district) Spinner spinnerSubDistrict;
    @Bind(R.id.addr_zipcode) EditText addrZip;
    @Bind(R.id.addr_phone) EditText addrPhone;
    @Bind(R.id.addr_work_phone) EditText addrWorkPhone;
    @Bind(R.id.addr_mobile) EditText addrMobile;
    @Bind(R.id.addr_email) EditText addrEmail;
    @Bind(R.id.button_send) Button buttonSend;
    @Bind(R.id.button_back) Button buttonBack;
    @Bind(R.id.customer_name) EditText customerName;
    @Bind(R.id.have_product) RadioButton radioButtonHave;
    @Bind(R.id.no_product) RadioButton radioButtonNo;
    @Bind(R.id.radio_group) RadioGroup radioGroup;
    @Bind(R.id.condition_1) RadioButton radioButtonCondition1;
    @Bind(R.id.condition_2) RadioButton radioButtonCondition2;
    @Bind(R.id.condition_3) RadioButton radioButtonCondition3;
    @Bind(R.id.condition_4) RadioButton radioButtonCondition4;
    @Bind(R.id.radio_group_condition) RadioGroup radioGroupCondition;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_PROVINCE)) {
            outState.putParcelable(Config.KEY_STATE_PROVINCE, getPresenter().getDataItemGroup());
        } else if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_DISTRICT)) {
            outState.putParcelable(Config.KEY_STATE_DISTRICT, getPresenter().getDataItemGroup());
        } else if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_SUB_DISTRICT)) {
            outState.putParcelable(Config.KEY_STATE_SUB_DISTRICT, getPresenter().getDataItemGroup());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_PROVINCE)) {
            getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable(Config.KEY_STATE_PROVINCE));
        } else if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_DISTRICT)) {
            getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable(Config.KEY_STATE_DISTRICT));
        } else if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_SUB_DISTRICT)) {
            getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable(Config.KEY_STATE_SUB_DISTRICT));
        }
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().onRestoreToAdapter(getPresenter().getDataItemGroup());
    }

    @Override
    public void setupInstance() {
        gpsTracker = new GPSTracker(SendDataActivity.this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public void setupView() {
        getDataFromIntent();
        addrEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addrEmail.getWindowToken(), 0);
                    buttonSend.performClick();
                    return true;
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(onSend());
        buttonBack.setOnClickListener(onBack());
        radioButtonHave.setOnClickListener(OptionClickListener);
        radioButtonNo.setOnClickListener(OptionClickListener);
        radioButtonCondition1.setOnClickListener(OptionConditionClickListener);
        radioButtonCondition2.setOnClickListener(OptionConditionClickListener);
        radioButtonCondition3.setOnClickListener(OptionConditionClickListener);
        radioButtonCondition4.setOnClickListener(OptionConditionClickListener);
    }

    private void getDataFromIntent() {
        try {
            coutno = getIntent().getStringExtra(Config.KEY_CONTACT_CODE);
            name = getIntent().getStringExtra(Config.KEY_CONTACT_NAME);
            countNo.setText(coutno);
            customerName.setText(name);
            countNo.setBackgroundDrawable(getResources().getDrawable(R.drawable.widget_disable));
            customerName.setBackgroundDrawable(getResources().getDrawable(R.drawable.widget_disable));
            countNo.setEnabled(false);
            customerName.setEnabled(false);
            Log.e("getDataFromIntent", countNo.getText().toString());
        } catch (Exception e) {
            Log.e("getDataFromIntent", e.getMessage());
            countNo.setText("");
            customerName.setText("");
        }
    }

    private View.OnClickListener onSend() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataBody();
            }
        };
    }

    private View.OnClickListener onBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }

    @Override
    public void initialize() {
        getPresenter().requestData(Config.KEY_PROVINCE, "");
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            Log.e("Location", String.valueOf(currentLatitude) + ", " + String.valueOf(currentLongitude));
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(SendDataActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    @Override
    public void onSuccess() {
        AlertDialog.dialogSaveSuccess(SendDataActivity.this);
        clearText();
    }

    @Override
    public void onFail(String fail) {
        AlertDialog.dialogSaveFail(SendDataActivity.this, fail);
    }

    @Override
    public void setProvince(final List<DataItem> dataItemList) {
        spinnerCustomAdapter = new SpinnerCustomAdapter(SendDataActivity.this,
                R.layout.spinner_item, dataItemList, getResources(), "province");

        spinnerProvince.setAdapter(spinnerCustomAdapter);
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    ((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItemList.get(position);
                    province = item.getDataId();
                    getPresenter().requestDistrict(Config.KEY_DISTRICT, item.getDataCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void setDistrict(final List<DataItem> dataItemList) {
        spinnerCustomAdapter = new SpinnerCustomAdapter(SendDataActivity.this,
                R.layout.spinner_item, dataItemList, getResources(), "district");

        spinnerDistrict.setAdapter(spinnerCustomAdapter);
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    ((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItemList.get(position);
                    district = item.getDataId();
                    getPresenter().requestSubDistrict(Config.KEY_SUB_DISTRICT, item.getDataCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void setSubDistrict(final List<DataItem> dataItemList) {
        spinnerCustomAdapter = new SpinnerCustomAdapter(SendDataActivity.this,
                R.layout.spinner_item, dataItemList, getResources(), "subdistrict");

        spinnerSubDistrict.setAdapter(spinnerCustomAdapter);
        spinnerSubDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    ((TextView) view.findViewById(R.id.row_item)).setTextColor(getResources().getColor(R.color.Black));
                    DataItem item = dataItemList.get(position);
                    subdistrict = item.getDataId();
                    addrZip.setText(item.getDataCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addDataBody() {
        //Log.e("Current location", String.valueOf(currentLatitude) + "," + String.valueOf(currentLongitude));
        currentLocation = String.valueOf(currentLatitude) + "," + String.valueOf(currentLongitude);
        List<SendDataBody.dataBody> dataBodyList = new ArrayList<>();
        dataBodyList.add(new SendDataBody.dataBody()
                .setCountno((countNo.getText().toString().equals("")) ? "-" : coutno)
                .setName((customerName.getText().toString().equals("")) ? "-" : name)
                .setNumber((addrNumber.getText().toString().equals("")) ? "-" : addrNumber.getText().toString())
                .setMoo((addrMoo.getText().toString().equals("")) ? "-" : addrMoo.getText().toString())
                .setSoi((addrSoi.getText().toString().equals("")) ? "-" : addrSoi.getText().toString())
                .setRoad((addrRoad.getText().toString().equals("")) ? "-" : addrRoad.getText().toString())
                .setVillage((addrVillage.getText().toString().equals("")) ? "-" : addrVillage.getText().toString())
                .setBuilding((addrBuilding.getText().toString().equals("")) ? "-" : addrBuilding.getText().toString())
                .setProvince((province.isEmpty()) ? "-" : province)
                .setDistrict((district.isEmpty()) ? "-" : district)
                .setSubdistrict((subdistrict.isEmpty()) ? "-" : subdistrict)
                .setZipcode((addrZip.getText().toString().equals("")) ? "-" : addrZip.getText().toString())
                .setHomephone((addrPhone.getText().toString().equals("")) ? "-" : addrPhone.getText().toString())
                .setWorkphone((addrWorkPhone.getText().toString().equals("")) ? "-" : addrWorkPhone.getText().toString())
                .setMobile((addrMobile.getText().toString().equals("")) ? "-" : addrMobile.getText().toString())
                .setEmail((addrEmail.getText().toString().equals("")) ? "-" : addrEmail.getText().toString())
                .setCreated(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME))
                .setLocation(currentLocation)
                .setProduct((product.isEmpty()) ? "-" : product)
                .setCondition((condition.isEmpty()) ? "-" : condition)
        );
        getPresenter().sendDataToServer(dataBodyList);
    }

    private void clearText() {
        countNo.setText("");
        customerName.setText("");
        addrNumber.setText("");
        addrMoo.setText("");
        addrSoi.setText("");
        addrRoad.setText("");
        addrVillage.setText("");
        addrBuilding.setText("");
        spinnerProvince.setSelection(0);
        spinnerDistrict.setSelection(0);
        spinnerSubDistrict.setSelection(0);
        addrZip.setText("");
        addrPhone.setText("");
        addrWorkPhone.setText("");
        addrMobile.setText("");
        addrEmail.setText("");
        radioGroup.clearCheck();
        radioGroupCondition.clearCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    RadioButton.OnClickListener OptionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonHave.isChecked()) {
                product = "true";
                Log.e("Product not have", "true");
            } else {
                product = "false";
                Log.e("Product not have", "false");
            }
        }
    };

    RadioButton.OnClickListener OptionConditionClickListener = new RadioButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (radioButtonCondition1.isChecked()) {
                condition = radioButtonCondition1.getText().toString();
            } else if (radioButtonCondition2.isChecked()) {
                condition = radioButtonCondition2.getText().toString();
            } else if (radioButtonCondition3.isChecked()) {
                condition = radioButtonCondition3.getText().toString();
            } else {
                condition = radioButtonCondition4.getText().toString();
            }

            Log.e("Codition choice", condition);
        }
    };
}

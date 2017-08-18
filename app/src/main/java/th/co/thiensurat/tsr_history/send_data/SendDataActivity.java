package th.co.thiensurat.tsr_history.send_data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.send_data.adapter.SpinnerCustomAdapter;
import th.co.thiensurat.tsr_history.send_data.item.DataItem;
import th.co.thiensurat.tsr_history.send_data.item.DataItemGroup;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;

public class SendDataActivity extends BaseMvpActivity<SendDataInterface.Presenter> implements SendDataInterface.View {

    private String data;
    private SpinnerCustomAdapter spinnerCustomAdapter;
    private List<DataItem> dataItemList = new ArrayList<DataItem>();

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
        //outState.putParcelable(Config.KEY_STATE_DATA, getPresenter().getDataItemGroup());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_PROVINCE)) {
            getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable( Config.KEY_STATE_PROVINCE ));
        } else if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_DISTRICT)) {
            getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable( Config.KEY_STATE_DISTRICT ));
        } else if (getPresenter().getDataItemGroup().getMessage().equals(Config.KEY_SUB_DISTRICT)) {
            getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable( Config.KEY_STATE_SUB_DISTRICT ));
        }
        //getPresenter().setDataItemGroup((DataItemGroup) savedInstanceState.getParcelable( Config.KEY_STATE_DATA ));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().onRestoreToAdapter( getPresenter().getDataItemGroup() );
    }

    @Override
    public void setupInstance() {

    }

    @Override
    public void setupView() {
        getDataFromIntent();
        buttonSend.setOnClickListener( onSend() );
        buttonBack.setOnClickListener( onBack() );
    }

    private void getDataFromIntent() {
        try {
            data = getIntent().getStringExtra(Config.KEY_CONTACT_CODE);
            countNo.setText(data);
        } catch (Exception e) {
            countNo.setText("");
        }
    }

    private View.OnClickListener onSend() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    getPresenter().requestData( Config.KEY_DISTRICT, item.getDataCode());
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
                    getPresenter().requestData( Config.KEY_SUB_DISTRICT, item.getDataCode());
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
                    addrZip.setText(item.getDataCode());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

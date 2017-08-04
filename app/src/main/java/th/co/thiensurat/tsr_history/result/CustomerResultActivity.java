package th.co.thiensurat.tsr_history.result;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.http.Body;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter> implements CustomerResultInterface.view{

    private String data;
    private Dialog dialog;
    private LinearLayoutManager layoutManager;
    private CustomerResultAdapter adapter;
    private ListItem listItem;
    private List<ListItem> listItemList = new ArrayList<ListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().requestItem();
        }
    }

    @Override
    public CustomerResultInterface.presenter createPresenter() {
        return CustomerResultPresenter.create();
    }

    public CustomerResultActivity() {
        super();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_result;
    }

    @Bind(R.id.container_service_unavailable) FrameLayout containerServiceUnvailable;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.totalSummary) TextView textViewTotal;
    @Bind(R.id.btn_save) Button save;
    @Bind(R.id.btn_cancel) Button cancel;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.idcard_number) TextView idcardNumber;
    @Override
    public void bindView() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        ButterKnife.bind(this);
        save.setOnClickListener( onSaveData() );
        cancel.setOnClickListener( onGoHome() );
    }

    @Override
    public void setupInstance() {
        getDataFromIntent();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setupView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initialize() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable( Config.KEY_SAVE_STATE_RESULT, getPresenter().onGetItemGroup() );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getPresenter().onSetItemGroup((ListItemGroup) savedInstanceState.getParcelable( Config.KEY_SAVE_STATE_RESULT ));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().onRestoreItemToAdapter( getPresenter().onGetItemGroup() );
    }

    @Override
    public String receiveItem() {
        return data;
    }

    private void getDataFromIntent() {
        data = getIntent().getStringExtra(Config.KEY_DATA);
    }

    @Override
    public void onBackToSearch() {
        this.listItemList.clear();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onGoToHome() {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
    }

    @Override
    public void setItemAdapter(List<ListItem> listItems) {
        this.listItemList = listItems;
        adapter = new CustomerResultAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        textViewTotal.setText(String.valueOf(listItemList.size()));
        AlertDialog.dialogDimiss();

        idcardNumber.setText(getResources().getString(R.string.text_idcard_title) + ": " + listItems.get(0).getIdcard());
    }

    @Override
    public void showServiceAvailableView() {
        recyclerView.setVisibility( View.VISIBLE );
        containerServiceUnvailable.setVisibility( View.GONE );
    }

    @Override
    public void showServiceUnavailableView(String fail) {
        recyclerView.setVisibility( View.GONE );
        containerServiceUnvailable.setVisibility( View.VISIBLE );
    }

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(CustomerResultActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    @Override
    public void onSuccess() {
        AlertDialog.dialogSaveSuccess(CustomerResultActivity.this);
    }

    @Override
    public void onFail(String fail) {
        AlertDialog.dialogSaveFail(CustomerResultActivity.this, fail);
    }

    private View.OnClickListener onSaveData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        };
    }

    private View.OnClickListener onGoHome() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onGoToHome();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getPresenter().onCancel();
        }

        return super.onOptionsItemSelected(item);
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        try {
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(imagePath + "/" + now + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            setHistoryValidation(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setHistoryValidation(File image) {
        List<AddHistoryBody.HistoryBody> bodyList = new ArrayList<>();
        String base64IMG = encodeImage(image);
        bodyList.add( new AddHistoryBody.HistoryBody()
                .setCustomerID(listItemList.get(0).getIdcard())
                .setSaleCode(listItemList.get(0).getSaleCode())
                .setDateContract(listItemList.get(0).getDate())
                .setImage(base64IMG)
                .setCreatedBy(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME)));

        getPresenter().addHistory(bodyList);
    }

    private String encodeImage(File imagefile) {
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Log.e("base64", encImage);
        return encImage;
    }
}

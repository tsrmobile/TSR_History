package th.co.thiensurat.tsr_history.result_customer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.result.CustomerResultActivity;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;
import th.co.thiensurat.tsr_history.result_customer.adapter.CustomerByNameAdapter;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class CustomerByNameActivity extends BaseMvpActivity<CustomerByNameInterface.Presenter>
        implements CustomerByNameInterface.View, CustomerByNameAdapter.OnCustomerItemClickListener {

    private String customerName;
    private CustomerByNameAdapter adapter;
    private LinearLayoutManager layoutManager;
    private List<ListItem> listItemList = new ArrayList<ListItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getPresenter().requestItem();
        }
    }

    @Override
    public CustomerByNameInterface.Presenter createPresenter() {
        return CustomerByNamePresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_same_name;
    }

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.ctm_name) TextView textViewName;
    @Bind(R.id.btn_save) Button buttonSave;
    @Bind(R.id.btn_cancel) Button buttonCancel;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.layoutBottom) RelativeLayout relativeLayoutBottom;
    @Bind(R.id.container_service_unavailable) FrameLayout containerServiceUnvailable;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupInstance() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setupView() {
        setToolbar();
        buttonSave.setOnClickListener( onClick() );
        buttonCancel.setOnClickListener( onCancel() );
    }

    @Override
    public void initialize() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable( Config.KEY_SAVE_STATE_NAME, getPresenter().onGetItemGroup() );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getPresenter().onSetItemGroup((ListItemGroup) savedInstanceState.getParcelable( Config.KEY_SAVE_STATE_NAME ));
    }

    @Override
    public void restoreView(Bundle savedInstanceState) {
        super.restoreView(savedInstanceState);
        getPresenter().onRestoreItemToAdapter( getPresenter().onGetItemGroup() );
    }

    @Override
    public void showServiceAvailableView() {
        recyclerView.setVisibility( View.VISIBLE );
        relativeLayoutBottom.setVisibility(View.GONE);
        containerServiceUnvailable.setVisibility( View.GONE );
    }

    @Override
    public void showServiceUnavailableView() {
        recyclerView.setVisibility( View.GONE );
        relativeLayoutBottom.setVisibility(View.VISIBLE);
        containerServiceUnvailable.setVisibility( View.VISIBLE );
    }

    @Override
    public String receiveItem() {
        customerName = getIntent().getStringExtra(Config.KEY_DATA);
        textViewName.setText(customerName);
        return customerName;
        //return data;
    }

    @Override
    public void onLoad() {
        AlertDialog.dialogLoading(CustomerByNameActivity.this);
    }

    @Override
    public void onDismiss() {
        AlertDialog.dialogDimiss();
    }

    @Override
    public void onSuccess() {
        AlertDialog.dialogSaveSuccess(CustomerByNameActivity.this);
    }

    @Override
    public void onFail(String fail) {
        AlertDialog.dialogSaveFail(CustomerByNameActivity.this, fail);
    }

    @Override
    public void setItemAdapter(List<ListItem> listItems) {
        this.listItemList = listItems;
        adapter = new CustomerByNameAdapter(CustomerByNameActivity.this, listItems);
        recyclerView.setAdapter(adapter);
        adapter.setCustomerClickListener(this);
    }

    @Override
    public void onCustomerClick(View view, int position) {
        ListItem item = listItemList.get(position);
        Intent intent = new Intent(getApplicationContext(), CustomerResultActivity.class);
        if (item.getIdcard().equals("0") || item.getIdcard().isEmpty()) {
            intent.putExtra(Config.KEY_DATA, item.getName());
        } else {
            intent.putExtra(Config.KEY_DATA, item.getIdcard());
        }
        intent.putExtra(Config.KEY_CLASS, "CustomerByNameActivity");
        startActivityForResult(intent, Config.REQUEST_RESULT);
    }

    private void setToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.dialogDenied(CustomerByNameActivity.this);
            } else {
                takeScreenshot();
            }
        }
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(CustomerByNameActivity.this);
            }
        };
    }

    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
    }

    private void takeScreenshot() {
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String imageName = df.format(now);
        try {
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(this.getWindow().findViewById(R.id.rootViewName));
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(imagePath + "/" + imageName + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            setHistoryValidation(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
            //onFail(e.getMessage());
        }
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void setHistoryValidation(File image) {
        Log.e("Replace space in name", customerName.replace(" ", ":"));
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentDate = df.format(now);
        List<AddHistoryBody.HistoryBody> bodyList = new ArrayList<>();
        String base64IMG = encodeImage(image);
        bodyList.add( new AddHistoryBody.HistoryBody()
                .setCustomerID("Nodata")
                .setCustomerName(customerName.replace(" ", "_"))
                .setSaleCode("Nodata")
                .setDateContract(currentDate)
                .setImage(base64IMG)
                .setCreatedBy(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME)));

        getPresenter().checkHistory(bodyList);
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
        return encImage;
    }

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    Config.PERMISSIONS_STORAGE,
                    Config.REQUEST_EXTERNAL_STORAGE
            );
        } else {
            takeScreenshot();
        }
    }
}

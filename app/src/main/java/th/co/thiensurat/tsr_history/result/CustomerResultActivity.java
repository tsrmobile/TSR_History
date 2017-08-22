package th.co.thiensurat.tsr_history.result;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import retrofit2.http.Body;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.api.request.AddHistoryBody;
import th.co.thiensurat.tsr_history.api.request.LogBody;
import th.co.thiensurat.tsr_history.api.result.AddHistoryItem;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.full_authen.FullAuthenActivity;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.result.item.ListItemGroup;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.send_data.SendDataActivity;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.AnimateButton;
import th.co.thiensurat.tsr_history.utils.Config;
import th.co.thiensurat.tsr_history.utils.MyApplication;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter>
        implements CustomerResultInterface.view, CustomerResultAdapter.ClickListener{

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
    @Bind(R.id.rootView) RelativeLayout relativeLayout;
    @Bind(R.id.button_send) Button buttonSend;
    @Override
    public void bindView() {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        ButterKnife.bind(this);
        save.setOnClickListener( onSaveData() );
        cancel.setOnClickListener( onGoHome() );
        buttonSend.setOnClickListener( onSendData() );
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.dialogDenied(CustomerResultActivity.this);
            } else {
                takeScreenshot();
            }
        }
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
        adapter.setClickListener(this);
        if (listItems.get(0).getCustomerStatus().equals("R")) {
            buttonSend.setVisibility(View.VISIBLE);
        } else {
            buttonSend.setVisibility(View.GONE);
        }

        saveLog(this.listItemList, "Load page");
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
                save.startAnimation(new AnimateButton().animbutton());
                verifyStoragePermissions(CustomerResultActivity.this);
            }
        };
    }

    private View.OnClickListener onGoHome() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel.startAnimation(new AnimateButton().animbutton());
                saveLog(listItemList, "Back home page");
                getPresenter().onGoToHome();
            }
        };
    }

    private View.OnClickListener onSendData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSend();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveLog(this.listItemList, "Back search page");
            getPresenter().onCancel();
        }

        return super.onOptionsItemSelected(item);
    }

    private void takeScreenshot() {
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String imageName = df.format(now);
        try {
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(this.getWindow().findViewById(R.id.rootView));
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
            onFail(e.getMessage());
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
        List<AddHistoryBody.HistoryBody> bodyList = new ArrayList<>();
        String base64IMG = encodeImage(image);
        bodyList.add( new AddHistoryBody.HistoryBody()
                .setCustomerID(listItemList.get(0).getIdcard())
                .setSaleCode(listItemList.get(0).getSaleCode())
                .setDateContract(listItemList.get(0).getDate())
                .setImage(base64IMG)
                .setCreatedBy(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME)));

        getPresenter().addHistory(bodyList);
        saveLog(this.listItemList, "Save");
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

    @Override
    public void sendClickListener(View view, int position) {
        ListItem item = this.listItemList.get(position);
        Intent intent = new Intent(CustomerResultActivity.this, SendDataActivity.class);
        intent.putExtra(Config.KEY_CONTACT_CODE, item.getCountno());
        intent.putExtra(Config.KEY_CONTACT_NAME, item.getName());
        startActivityForResult(intent, Config.REQUEST_SEND_DATA);
    }

    private void onSend() {
        ListItem item = this.listItemList.get(0);
        Intent intent = new Intent(CustomerResultActivity.this, SendDataActivity.class);
        intent.putExtra(Config.KEY_CONTACT_CODE, item.getCountno());
        intent.putExtra(Config.KEY_CONTACT_NAME, item.getName());
        startActivityForResult(intent, Config.REQUEST_SEND_DATA);
    }

    private void saveLog(List<ListItem> listItemList, String event) {
        int i = 1;
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (ListItem item : listItemList) {
            sb1.append(item.getCustomerStatus());
            sb2.append(item.getCountno());
            if (listItemList.size() > i) {
                sb1.append(",");
                sb2.append(",");
                i++;
            }
        }

        List<LogBody.logBody> logBodyList = new ArrayList<>();
        logBodyList.add(new LogBody.logBody()
                .setUsername(MyApplication.getInstance().getPrefManager().getPreferrence(Config.KEY_USERNAME))
                .setSearchBy(data)
                .setStatusValue(sb1.toString())
                .setContNo(sb2.toString())
                .setEvent(event)
        );
        getPresenter().saveLogToServer(logBodyList);
    }
}

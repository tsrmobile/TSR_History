package th.co.thiensurat.tsr_history.result;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.ListItem;
import th.co.thiensurat.tsr_history.utils.AlertDialog;
import th.co.thiensurat.tsr_history.utils.Config;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter> implements CustomerResultInterface.view{

    private Dialog dialog;
    private ListItem listItem;
    private LinearLayoutManager layoutManager;
    private CustomerResultAdapter adapter;
    private List<ListItem> listItemList = new ArrayList<ListItem>();
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
    @Bind(R.id.btn_try_again) Button tryAgain;
    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.totalSummary) TextView textViewTotal;
    @Bind(R.id.btn_save) Button save;
    @Bind(R.id.btn_cancel) Button cancel;
    @Bind(R.id.name) TextView customerName;
    @Bind(R.id.address) TextView customerAddress;
    @Bind(R.id.phone) TextView customerPhone;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        save.setOnClickListener( onSaveData() );
        cancel.setOnClickListener( onCancel() );
        tryAgain.setOnClickListener( onTryAgain() );
    }

    @Override
    public void setupInstance() {
        getPresenter().requestItem();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setupView() {
        layoutManager.setReverseLayout(true);
    }

    @Override
    public void initialize() {

    }

    @Override
    public String receiveItem() {
        String data = getIntent().getStringExtra(Config.KEY_DATA);
        return data;
    }

    @Override
    public void onBackToSearch() {
        this.listItemList.clear();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void setItemAdapter(List<ListItem> listItems) {
        this.listItemList = listItems;
        adapter = new CustomerResultAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setCustomerDetail();
    }

    private void setCustomerDetail() {
        textViewTotal.setText(String.valueOf(listItemList.size()));
        for (ListItem item : listItemList) {
            customerName.setText(item.getName());
        }
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
        AlertDialog.dialogSearchFail(CustomerResultActivity.this, fail);
    }

    private View.OnClickListener onSaveData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        };
    }

    private View.OnClickListener onCancel() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().onCancel();
            }
        };
    }

    private View.OnClickListener onTryAgain() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().requestItem();
            }
        };
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
            showImage(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void showImage(final File imageFile) {
        Uri uri = Uri.fromFile(imageFile);
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Data validation!");
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageURI(uri);
        Button dialogSave = (Button) dialog.findViewById(R.id.save);
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*listItem = new ListItem();
                listItem.setImage(encodeImage(imageFile));
                listItem.setIdcard("");
                listItem.setSaleid("");
                listItem.setDatetime("");
                checkListItemList.add(listItem);
                getPresenter().sendToServer(checkListItemList);*/
                dialog.dismiss();

            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkListItemList.clear();
                dialog.dismiss();
            }
        });
        dialog.show();
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
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }
}

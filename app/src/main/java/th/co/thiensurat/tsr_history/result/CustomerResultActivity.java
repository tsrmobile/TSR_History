package th.co.thiensurat.tsr_history.result;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.base.BaseMvpActivity;
import th.co.thiensurat.tsr_history.result.adapter.CustomerResultAdapter;
import th.co.thiensurat.tsr_history.result.item.CustomerItem;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.utils.Config;

public class CustomerResultActivity extends BaseMvpActivity<CustomerResultInterface.presenter> implements CustomerResultInterface.view{

    private LinearLayoutManager layoutManager;
    private CustomerResultAdapter adapter;
    private List<CustomerItem> customerItems = new ArrayList<CustomerItem>();
    @Override
    public CustomerResultInterface.presenter createPresenter() {
        return CustomerResultPresenter.create();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_customer_result;
    }

    @Bind(R.id.recyclerview) RecyclerView recyclerView;
    @Bind(R.id.totalSummary) TextView textViewTotal;
    @Bind(R.id.btn_save) Button save;
    @Bind(R.id.btn_cancel) Button cancel;
    @Bind(R.id.name) TextView customerName;
    @Bind(R.id.address) TextView customerAddress;
    @Bind(R.id.phone) TextView customerPhone;
    @Bind(R.id.logoToolbar) ImageView camera;
    @Override
    public void bindView() {
        ButterKnife.bind(this);
        save.setOnClickListener(onSaveData());
        cancel.setOnClickListener(onCancel());
        camera.setOnClickListener(onTakeScreen());
    }

    @Override
    public void setupInstance() {
        getPresenter().requestItem();
        adapter = new CustomerResultAdapter(this, customerItems);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setupView() {
        layoutManager.setReverseLayout(true);
        recyclerView.setAdapter(adapter);
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
    public void setItemToView(List<CustomerItem> customerItems) {
        this.customerItems = customerItems;
        textViewTotal.setText(String.valueOf(this.customerItems.size()));
    }

    @Override
    public void onBackToSearch() {
        this.customerItems.clear();
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
    }

    private View.OnClickListener onSaveData() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

    private View.OnClickListener onTakeScreen() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        };
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

            // create bitmap screen capture
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

            //openScreenshot(imageFile);
            showImage(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivityForResult(intent, Config.REQUEST_IMAGE_VIEW);
    }

    public void showImage(File imageFile) {
        Uri uri = Uri.fromFile(imageFile);
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageURI(uri);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(500,500));
        builder.show();
    }
}

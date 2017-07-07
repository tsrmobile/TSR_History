package th.co.thiensurat.tsr_history.utils;

import android.content.Context;
import android.content.Intent;

import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.search.SearchActivity;

/**
 * Created by teerayut.k on 7/6/2017.
 */

public class AlertDialog {

    public static void dialogSearchEmpty(Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getResources().getString(R.string.dialog_title_error))
                .setContentText(context.getResources().getString(R.string.dialog_msg_empty_error))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void dialogNetworkError(final Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(context.getResources().getString(R.string.dialog_title_warning))
            .setContentText(context.getResources().getString(R.string.dialog_text_msg))
            .setCancelText(context.getResources().getString(R.string.dialog_button_cancel))
            .setConfirmText(context.getResources().getString(R.string.dialog_button_confirm))
            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
                ((SearchActivity)context).finish();
            }
        })
            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                ((SearchActivity)context).startActivityForResult(intent, Config.REQUEST_SETTINGS);
            }
        })
        .show();
    }
}

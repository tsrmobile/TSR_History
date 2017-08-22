package th.co.thiensurat.tsr_history.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import cn.pedant.SweetAlert.SweetAlertDialog;
import th.co.thiensurat.tsr_history.R;
import th.co.thiensurat.tsr_history.result.CustomerResultActivity;
import th.co.thiensurat.tsr_history.search.SearchActivity;
import th.co.thiensurat.tsr_history.send_data.SendDataActivity;

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

    public static void dialogSearchFail(final Context context, String fail) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getResources().getString(R.string.dialog_title_error))
                .setContentText(fail)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void dialogSaveFail(final Context context, String fail) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getResources().getString(R.string.dialog_title_error))
                .setContentText(context.getResources().getString(R.string.dialog_msg_save_fail) + "\n" + fail)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void dialogSaveSuccess(final Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(context.getResources().getString(R.string.dialog_title_success))
                .setContentText(context.getResources().getString(R.string.dialog_msg_save_success))
                .showCancelButton(false)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                        /*((CustomerResultActivity)context).setResult(Activity.RESULT_OK);
                        ((CustomerResultActivity)context).finish();*/
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

    public static void dialogLocationError(final Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(context.getResources().getString(R.string.dialog_title_warning))
                .setContentText(context.getResources().getString(R.string.GPSAlertDialogMessage))
                .setCancelText(context.getResources().getString(R.string.dialog_button_cancel))
                .setConfirmText(context.getResources().getString(R.string.dialog_button_confirm))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        ((SendDataActivity)context).startActivityForResult(intent, Config.REQUEST_SETTINGS);
                    }
                })
                .show();
    }

    public static void dialogDenied(final Context context) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(context.getResources().getString(R.string.dialog_title_warning))
                .setContentText(context.getResources().getString(R.string.dialog_msg_permission))
                .showCancelButton(false)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private static SweetAlertDialog sweetAlertDialog;
    public static void dialogLoading(final Context context) {
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        sweetAlertDialog.setTitleText("Loading...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
    }

    public static void dialogDimiss() {
        if (sweetAlertDialog.isShowing()) {
            sweetAlertDialog.dismiss();
        }
    }
}

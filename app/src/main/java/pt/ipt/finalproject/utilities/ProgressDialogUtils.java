package pt.ipt.finalproject.utilities;

import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogUtils {

    private static ProgressDialog progressDialog;

    public static void showProgressDialog (Activity context, String message, Boolean isCancelable) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public static void dismissProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}

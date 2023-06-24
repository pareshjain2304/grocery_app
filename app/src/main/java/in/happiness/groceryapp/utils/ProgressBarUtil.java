package in.happiness.groceryapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

import in.happiness.groceryapp.R;


public class ProgressBarUtil {
    public void showProgressDialog(Context mctx) {
        ProgressDialog progressDialog = new ProgressDialog(mctx.getApplicationContext());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}

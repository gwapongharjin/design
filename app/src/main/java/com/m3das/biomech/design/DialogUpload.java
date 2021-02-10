package com.m3das.biomech.design;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class DialogUpload {
    private Activity activity;
    private AlertDialog dialog;

    public DialogUpload(Activity myActivity){

        activity = myActivity;

    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog_activity, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}

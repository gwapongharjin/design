package com.m3das.biomech.design;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogGetLocation {
    private Activity activity;
    private AlertDialog dialog;
    long mills = 00;
    boolean isDismissed;

    public DialogGetLocation(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadindDialog() {

        isDismissed = false;
        dialog = new AlertDialog.Builder(activity).create();
        LayoutInflater inflater = activity.getLayoutInflater();
        dialog.setView(inflater.inflate(R.layout.dialog_get_location, null));
        dialog.setCancelable(false);
        dialog.setMessage("");
        dialog.show();


        new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                dialog.setMessage("00:"+ (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                dismissDialog();
            }
        }.start();
    }


    public void dismissDialog() {
        dialog.dismiss();
        isDismissed = true;
    }

}
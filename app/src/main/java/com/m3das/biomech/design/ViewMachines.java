package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ViewMachines extends AppCompatActivity {
private TextView tvOutput;
private String machList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_machines_activity);

        tvOutput = findViewById(R.id.tvListForMachines);
        machList = Variable.getMachList();

        tvOutput.setText(machList);

    }
}
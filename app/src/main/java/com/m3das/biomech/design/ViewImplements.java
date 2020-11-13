package com.m3das.biomech.design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewImplements extends AppCompatActivity {
    private TextView tvOutput;
    private MachineListViewModel machineListViewModel;
    private String impList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_implements_activity);

        tvOutput = findViewById(R.id.tvListForImplements);

        impList = Variable.getImpList();
        Log.d("Value", "of profile List" + impList);


        tvOutput.setText(impList);

    }
}
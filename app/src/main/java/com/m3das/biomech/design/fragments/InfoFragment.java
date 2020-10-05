package com.m3das.biomech.design.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.viewmodels.InfoViewModel;

public class InfoFragment extends Fragment {

    private InfoViewModel mViewModel;
    private Spinner spinner, profile;
    private TextView textView;
    private ImageButton toTop;
    private Switch aSwitch;
    private DatePicker datePicker;
    private EditText addressResp, nameResp;
    private RadioButton rbMale, rbFemale;
    public static final String EXTRA_DATE = "EXTRA_DATE";

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info_fragment, container, false);

        spinner = v.findViewById(R.id.spinner);
        profile = v.findViewById(R.id.spinProfile);
        textView = v.findViewById(R.id.textView3);
        toTop = v.findViewById(R.id.btnToTop);
        aSwitch = v.findViewById(R.id.switchEditSave);
        datePicker = v.findViewById(R.id.dpDateOfSurvey);
        addressResp = v.findViewById(R.id.edtAddress);
        nameResp = v.findViewById(R.id.edtNameResp);
        rbMale = v.findViewById(R.id.radioButton);
        rbFemale = v.findViewById(R.id.radioButton2);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    aSwitch.setText("Saved");
                    spinner.setEnabled(false);
                    profile.setEnabled(false);
                    datePicker.setEnabled(false);
                    addressResp.setEnabled(false);
                    nameResp.setEnabled(false);
                    rbMale.setEnabled(false);
                    rbFemale.setEnabled(false);
                    Intent datainfo = new Intent();
                    String date = datePicker.getMonth()+"/"+datePicker.getDayOfMonth()+"/"+datePicker.getYear();




                } else {
                    aSwitch.setText("Edit");
                    spinner.setEnabled(true);
                    profile.setEnabled(true);
                    datePicker.setEnabled(true);
                    addressResp.setEnabled(true);
                    nameResp.setEnabled(true);
                    rbMale.setEnabled(true);
                    rbFemale.setEnabled(true);
                }
            }
        });

        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = v.findViewById(R.id.scrollViewInfo);
                scrollView.setScrollY(0);
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                textView.setText(tutorialsName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        // TODO: Use the ViewModel
    }


}
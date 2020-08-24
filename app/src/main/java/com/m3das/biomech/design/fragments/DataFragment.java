package com.m3das.biomech.design.fragments;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.m3das.biomech.design.R;
import com.m3das.biomech.design.ViewDataActivity;
import com.m3das.biomech.design.viewmodels.DataViewModel;

public class DataFragment extends Fragment {

    private DataViewModel mViewModel;

    public static DataFragment newInstance() {
        return new DataFragment();
    }
    private Button button;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);
        button = (Button) v.findViewById(R.id.btnView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewDataActivity();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        // TODO: Use the ViewModel
    }

    public void openViewDataActivity(){
        Intent intent = new Intent(getContext(), ViewDataActivity.class);
        getActivity().startActivity(intent);
    }

}
package com.m3das.biomech.design.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m3das.biomech.design.AddImplementActivity;
import com.m3das.biomech.design.Machines;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.viewmodels.ImplementViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

public class ImplementsFragment extends Fragment {

    private ImplementViewModel mViewModel;
    private Spinner selectFrag;
    private FloatingActionButton floatingActionButton;
    private ImplementViewModel implementViewModel;


    public static ImplementsFragment newInstance() {
        return new ImplementsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.implements_fragment, container, false);

        floatingActionButton = v.findViewById(R.id.floatingActionButtonImpAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddImplementActivity();
            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);

        // TODO: Use the ViewModel
    }
    public void openAddImplementActivity(){
        Intent intent = new Intent(getContext(), AddImplementActivity.class);
        getActivity().startActivity(intent);
    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(ImplementViewModel.class);

        mViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);

    }
*/
}
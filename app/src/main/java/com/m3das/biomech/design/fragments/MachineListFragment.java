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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.ViewDataActivity;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;
import com.m3das.biomech.design.R;

public class MachineListFragment extends Fragment {

    private MachineListViewModel mViewModel;
    private FloatingActionButton floatingActionButton;
    public static MachineListFragment newInstance() {
        return new MachineListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.machine_list_fragment, container, false);

        floatingActionButton = v.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMachinesActivity();
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MachineListViewModel.class);
        // TODO: Use the ViewModel
    }

    public void openAddMachinesActivity(){
        Intent intent = new Intent(getContext(), AddMachineActivity.class);
        getActivity().startActivity(intent);
    }

}
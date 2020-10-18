package com.m3das.biomech.design.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.MachineAdapter;
import com.m3das.biomech.design.Machines;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

public class MachineListFragment extends Fragment {

    private MachineListViewModel mViewModel;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    public static MachineListFragment newInstance() {
        return new MachineListFragment();
    }
    private MachineListViewModel machineListViewModel;
    public static final int ADD_NOTE_REQUEST = 1123;
    public static final int EDIT_NOTE_REQUEST = 2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.machine_list_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerViewML);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        MachineAdapter machineAdapter = new MachineAdapter();
        recyclerView.setAdapter(machineAdapter);

        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);

        machineListViewModel.getAllMachines().observe(getActivity(), new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {
                machineAdapter.setMachinesList(machines);
            }
        });

        machineAdapter.setOnItemClickListener(new MachineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Machines machines) {
                Intent intent = new Intent(getContext(), AddMachineActivity.class);
                intent.putExtra(AddMachineActivity.EXTRA_ID, machines.getId());
                intent.putExtra(AddMachineActivity.EXTRA_MACHINE_TYPE, machines.getMachine_type());
                intent.putExtra(AddMachineActivity.EXTRA_MACHINE_QRCODE, machines.getMachine_qrcode());
                intent.putExtra(AddMachineActivity.EXTRA_LAT, machines.getMachine_latitude());
                intent.putExtra(AddMachineActivity.EXTRA_LONG, machines.getMachine_longitude());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });



        floatingActionButton = v.findViewById(R.id.floatingActionButtonMachAdd);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMachineActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            String machineType = data.getStringExtra(AddMachineActivity.EXTRA_MACHINE_TYPE);
            String machineQRCode = data.getStringExtra(AddMachineActivity.EXTRA_MACHINE_QRCODE);
            String latitude = data.getStringExtra(AddMachineActivity.EXTRA_LAT);
            String longitude = data.getStringExtra(AddMachineActivity.EXTRA_LONG);
            Machines machines = new Machines(machineQRCode,machineType, latitude, longitude, "asdasdasdas");

            machineListViewModel.insert(machines);
            Log.d("Is note saved", "Note Saved" + machineType);
            Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra(AddMachineActivity.EXTRA_ID, -1);
            if (id == -1){
                Toast.makeText(getActivity(), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String machineType = data.getStringExtra(AddMachineActivity.EXTRA_MACHINE_TYPE);
            String machineQRCode = data.getStringExtra(AddMachineActivity.EXTRA_MACHINE_QRCODE);
            String latitude = data.getStringExtra(AddMachineActivity.EXTRA_LAT);
            String longitude = data.getStringExtra(AddMachineActivity.EXTRA_LONG);
            Machines machines = new Machines(machineQRCode,machineType, latitude, longitude, "asdasdasdas");
            machines.setId(id);
            machineListViewModel.update(machines);
            Toast.makeText(getActivity(), "Note updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Note not saved", Toast.LENGTH_SHORT).show();
            Log.d("Is note saved", "Note Not Saved");
        }

    }

}
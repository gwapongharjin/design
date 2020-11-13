package com.m3das.biomech.design.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.MachineAdapter;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.Variable;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

public class MachineListFragment extends Fragment {

    private MachineListViewModel mViewModel;
    private FloatingActionButton fabAddNewMachine, fabDeleteMachine;
    private RecyclerView recyclerView;
    private int num;

    public static MachineListFragment newInstance() {
        return new MachineListFragment();
    }

    private MachineListViewModel machineListViewModel;
    public static final int ADD_NOTE_REQUEST = 1123;
    public static final int EDIT_NOTE_REQUEST = 2;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        num = 3;
        View v = inflater.inflate(R.layout.machine_list_fragment, container, false);
        fabAddNewMachine = v.findViewById(R.id.floatingActionButtonAddMachine);
        fabDeleteMachine = v.findViewById(R.id.floatingActionButtonDeleteMachine);

        recyclerView = v.findViewById(R.id.recyclerViewML);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);


        MachineAdapter machineAdapter = new MachineAdapter();
        recyclerView.setAdapter(machineAdapter);


        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);

        machineListViewModel.getAllMachines().observe(getActivity(), new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {
                String machineList = "";
                for (int i = machines.size() - 1; i > -1; i--) {
                    machineList = machineList + "Machine ID: " + machines.get(i).getId();
                    machineList = machineList + "\nMachine QR Code: " + machines.get(i).getMachine_qrcode();
                    machineList = machineList + "\nRespondent Name: " + machines.get(i).getResName();
                    machineList = machineList + "\nMachine Type: " + machines.get(i).getMachine_type()+ "\n\n";
                }
                Variable.setMachList(machineList);

                machineAdapter.setMachinesList(machines);
            }
        });

        machineAdapter.setOnItemClickListener(new MachineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Machines machines) {
                Intent intent = new Intent(getContext(), AddMachineActivity.class);

                intent.putExtra(AddMachineActivity.EXTRA_ID, machines.getId());
                intent.putExtra(AddMachineActivity.EXTRA_MACHINE_TYPE, machines.getMachine_type());
                intent.putExtra(AddMachineActivity.EXTRA_TYPE_TUBEWELLS, machines.getType_tubewells());
                intent.putExtra(AddMachineActivity.EXTRA_TYPE_MILL, machines.getType_mill());
                intent.putExtra(AddMachineActivity.EXTRA_DATE_TIME, machines.getDate_of_survey());
                intent.putExtra(AddMachineActivity.EXTRA_BRAND, machines.getMachine_brand());
                intent.putExtra(AddMachineActivity.EXTRA_BRAND_SPECIFY, machines.getMachine_brand_specify());
                intent.putExtra(AddMachineActivity.EXTRA_MODEL, machines.getMachine_model());
                intent.putExtra(AddMachineActivity.EXTRA_MODEL_SPECIFY, machines.getMachine_model_specify());
                intent.putExtra(AddMachineActivity.EXTRA_RATED_POWER, machines.getRated_power());
                intent.putExtra(AddMachineActivity.EXTRA_SERVICE_AREA, machines.getService_area());
                intent.putExtra(AddMachineActivity.EXTRA_AVE_OP_HOURS, machines.getAve_op_hours());
                intent.putExtra(AddMachineActivity.EXTRA_AVE_OP_DAYS, machines.getAve_op_days());
                intent.putExtra(AddMachineActivity.EXTRA_CAPACITY, machines.getCapacity());
                intent.putExtra(AddMachineActivity.EXTRA_AVE_YIELD, machines.getAve_yield());
                intent.putExtra(AddMachineActivity.EXTRA_NUM_LOADS, machines.getNum_loads());
                intent.putExtra(AddMachineActivity.EXTRA_RATE, machines.getNum_loads());
                intent.putExtra(AddMachineActivity.EXTRA_OWNERSHIP, machines.getOwnership());
                intent.putExtra(AddMachineActivity.EXTRA_PURCH_GRANT_DONO, machines.getPurch_grant_dono());
                intent.putExtra(AddMachineActivity.EXTRA_AGENCY, machines.getAgency());
                intent.putExtra(AddMachineActivity.EXTRA_AGENCY_SPECIFY, machines.getAgency_specify());
                intent.putExtra(AddMachineActivity.EXTRA_NAME_OWNER, machines.getName_owner());
                intent.putExtra(AddMachineActivity.EXTRA_YEAR_ACQUIRED, machines.getYear_acquired());
                intent.putExtra(AddMachineActivity.EXTRA_CONDITION_ACQUIRED, machines.getCondition_acquired());
                intent.putExtra(AddMachineActivity.EXTRA_RENTAL, machines.getRental());
                intent.putExtra(AddMachineActivity.EXTRA_CUSTOM_RATE, machines.getCustom_rate());
                intent.putExtra(AddMachineActivity.EXTRA_CUSTOM_UNIT, machines.getCustom_unit());
                intent.putExtra(AddMachineActivity.EXTRA_CUSTOM_UNIT_SPECIFY, machines.getSpecify_custom_unit());
                intent.putExtra(AddMachineActivity.EXTRA_AVAILABILITY, machines.getAvailability());
                intent.putExtra(AddMachineActivity.EXTRA_RENT_PROV, machines.getRent_prov());
                intent.putExtra(AddMachineActivity.EXTRA_RENT_MUN, machines.getRent_mun());
                intent.putExtra(AddMachineActivity.EXTRA_RENT_BRGY, machines.getRent_brgy());
                intent.putExtra(AddMachineActivity.EXTRA_CONDITION, machines.getCondition());
                intent.putExtra(AddMachineActivity.EXTRA_PROBLEMS, machines.getProblems());
                intent.putExtra(AddMachineActivity.EXTRA_PROBLEMS_SPECIFY, machines.getSpecify_problems());
                intent.putExtra(AddMachineActivity.EXTRA_LOCATION, machines.getLocation());
                intent.putExtra(AddMachineActivity.EXTRA_PROV, machines.getProv());
                intent.putExtra(AddMachineActivity.EXTRA_MUN, machines.getMun());
                intent.putExtra(AddMachineActivity.EXTRA_BRGY, machines.getBrgy());
                intent.putExtra(AddMachineActivity.EXTRA_MACHINE_QRCODE, machines.getMachine_qrcode());
                intent.putExtra(AddMachineActivity.EXTRA_LAT, machines.getMachine_latitude());
                intent.putExtra(AddMachineActivity.EXTRA_LONG, machines.getMachine_longitude());
                Variable.setStringImage(machines.getMachine_image_base64());
                intent.putExtra(AddMachineActivity.EXTRA_ACC, machines.getAccuracy());
                intent.putExtra(AddMachineActivity.EXTRA_RES_CODE, machines.getResCode());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });


        fabAddNewMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMachineActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });


        fabDeleteMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;

                if (num % 2 == 0) {
                    fabDeleteMachine.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                    fabDeleteMachine.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    Toast.makeText(getContext(), "Now you can delete items by swiping", Toast.LENGTH_SHORT).show();

                } else {

                    fabDeleteMachine.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    fabDeleteMachine.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));

                }

                Log.d("Value of Num CLICK", String.valueOf(num));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Deleting Item")
                        .setMessage("You will be deleting this: "+ machineAdapter.getMachineAt(viewHolder.getAdapterPosition()).getMachine_qrcode())
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                machineListViewModel.delete(machineAdapter.getMachineAt(viewHolder.getAdapterPosition()));
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                machineAdapter.notifyDataSetChanged();
                            }
                        });
                builder.show();

            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                Log.d("Value of Num ITH", String.valueOf(num));
                if (num % 2 != 0) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }
        }).attachToRecyclerView(recyclerView);

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
            String datesurvey = data.getStringExtra(AddMachineActivity.EXTRA_DATE_TIME);
            String brand = data.getStringExtra(AddMachineActivity.EXTRA_BRAND);
            String brand_specify = data.getStringExtra(AddMachineActivity.EXTRA_BRAND_SPECIFY);
            String model = data.getStringExtra(AddMachineActivity.EXTRA_MODEL);
            String model_specify = data.getStringExtra(AddMachineActivity.EXTRA_MODEL_SPECIFY);
            String rated_power = data.getStringExtra(AddMachineActivity.EXTRA_RATED_POWER);
            String service_area = data.getStringExtra(AddMachineActivity.EXTRA_SERVICE_AREA);
            String ave_op_hours = data.getStringExtra(AddMachineActivity.EXTRA_AVE_OP_HOURS);
            String ave_op_days = data.getStringExtra(AddMachineActivity.EXTRA_AVE_OP_DAYS);
            String capacity = data.getStringExtra(AddMachineActivity.EXTRA_CAPACITY);
            String ave_yield = data.getStringExtra(AddMachineActivity.EXTRA_AVE_YIELD);
            String num_loads = data.getStringExtra(AddMachineActivity.EXTRA_NUM_LOADS);
            String rate = data.getStringExtra(AddMachineActivity.EXTRA_RATE);
            String ownership = data.getStringExtra(AddMachineActivity.EXTRA_OWNERSHIP);
            String purch_grant_dono = data.getStringExtra(AddMachineActivity.EXTRA_PURCH_GRANT_DONO);
            String agency = data.getStringExtra(AddMachineActivity.EXTRA_AGENCY);
            String agency_specify = data.getStringExtra(AddMachineActivity.EXTRA_AGENCY_SPECIFY);
            String name_owner = data.getStringExtra(AddMachineActivity.EXTRA_NAME_OWNER);
            String year_acquired = data.getStringExtra(AddMachineActivity.EXTRA_YEAR_ACQUIRED);
            String condition_acquired = data.getStringExtra(AddMachineActivity.EXTRA_CONDITION_ACQUIRED);
            String rental = data.getStringExtra(AddMachineActivity.EXTRA_RENTAL);
            String custom_rate = data.getStringExtra(AddMachineActivity.EXTRA_CUSTOM_RATE);
            String custom_unit = data.getStringExtra(AddMachineActivity.EXTRA_CUSTOM_UNIT);
            String custom_unit_specify = data.getStringExtra(AddMachineActivity.EXTRA_CUSTOM_UNIT_SPECIFY);
            String availablity = data.getStringExtra(AddMachineActivity.EXTRA_AVAILABILITY);
            String rent_prov = data.getStringExtra(AddMachineActivity.EXTRA_RENT_PROV);
            String rent_mun = data.getStringExtra(AddMachineActivity.EXTRA_RENT_MUN);
            String rent_brgy = data.getStringExtra(AddMachineActivity.EXTRA_RENT_BRGY);
            String condition = data.getStringExtra(AddMachineActivity.EXTRA_CONDITION);
            String problems = data.getStringExtra(AddMachineActivity.EXTRA_PROBLEMS);
            String problems_specify = data.getStringExtra(AddMachineActivity.EXTRA_PROBLEMS_SPECIFY);
            String location = data.getStringExtra(AddMachineActivity.EXTRA_LOCATION);
            String prov = data.getStringExtra(AddMachineActivity.EXTRA_PROV);
            String mun = data.getStringExtra(AddMachineActivity.EXTRA_MUN);
            String brgy = data.getStringExtra(AddMachineActivity.EXTRA_BRGY);
            String latitude = data.getStringExtra(AddMachineActivity.EXTRA_LAT);
            String longitude = data.getStringExtra(AddMachineActivity.EXTRA_LONG);
            String imageString = Variable.getStringImage();
            String accuracy = data.getStringExtra(AddMachineActivity.EXTRA_ACC);
            String tubewells = data.getStringExtra(AddMachineActivity.EXTRA_TYPE_TUBEWELLS);
            String type_mill = data.getStringExtra(AddMachineActivity.EXTRA_TYPE_MILL);
            String resCode = data.getStringExtra(AddMachineActivity.EXTRA_RES_CODE);
            String resName = data.getStringExtra(AddMachineActivity.EXTRA_RES_NAME);

            Machines machines = new Machines(machineType, tubewells, type_mill, machineQRCode, datesurvey, brand, brand_specify, model, model_specify, rated_power, service_area, ave_op_hours,
                    ave_op_days, capacity, ave_yield, num_loads, rate, ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired,
                    rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov, rent_mun, rent_brgy, condition, problems, problems_specify, location,
                    prov, mun, brgy, latitude, longitude, imageString, accuracy, resCode, resName);

            machineListViewModel.insert(machines);

            Log.d("Is note saved", "Note Saved" + machineType);
            Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra(AddMachineActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String machineType = data.getStringExtra(AddMachineActivity.EXTRA_MACHINE_TYPE);
            String machineQRCode = data.getStringExtra(AddMachineActivity.EXTRA_MACHINE_QRCODE);
            String datesurvey = data.getStringExtra(AddMachineActivity.EXTRA_DATE_TIME);
            String brand = data.getStringExtra(AddMachineActivity.EXTRA_BRAND);
            String brand_specify = data.getStringExtra(AddMachineActivity.EXTRA_BRAND_SPECIFY);
            String model = data.getStringExtra(AddMachineActivity.EXTRA_MODEL);
            String model_specify = data.getStringExtra(AddMachineActivity.EXTRA_MODEL_SPECIFY);
            String rated_power = data.getStringExtra(AddMachineActivity.EXTRA_RATED_POWER);
            String service_area = data.getStringExtra(AddMachineActivity.EXTRA_SERVICE_AREA);
            String ave_op_hours = data.getStringExtra(AddMachineActivity.EXTRA_AVE_OP_HOURS);
            String ave_op_days = data.getStringExtra(AddMachineActivity.EXTRA_AVE_OP_DAYS);
            String capacity = data.getStringExtra(AddMachineActivity.EXTRA_CAPACITY);
            String ave_yield = data.getStringExtra(AddMachineActivity.EXTRA_AVE_YIELD);
            String num_loads = data.getStringExtra(AddMachineActivity.EXTRA_NUM_LOADS);
            String rate = data.getStringExtra(AddMachineActivity.EXTRA_RATE);
            String ownership = data.getStringExtra(AddMachineActivity.EXTRA_OWNERSHIP);
            String purch_grant_dono = data.getStringExtra(AddMachineActivity.EXTRA_PURCH_GRANT_DONO);
            String agency = data.getStringExtra(AddMachineActivity.EXTRA_AGENCY);
            String agency_specify = data.getStringExtra(AddMachineActivity.EXTRA_AGENCY_SPECIFY);
            String name_owner = data.getStringExtra(AddMachineActivity.EXTRA_NAME_OWNER);
            String year_acquired = data.getStringExtra(AddMachineActivity.EXTRA_YEAR_ACQUIRED);
            String condition_acquired = data.getStringExtra(AddMachineActivity.EXTRA_CONDITION_ACQUIRED);
            String rental = data.getStringExtra(AddMachineActivity.EXTRA_RENTAL);
            String custom_rate = data.getStringExtra(AddMachineActivity.EXTRA_CUSTOM_RATE);
            String custom_unit = data.getStringExtra(AddMachineActivity.EXTRA_CUSTOM_UNIT);
            String custom_unit_specify = data.getStringExtra(AddMachineActivity.EXTRA_CUSTOM_UNIT_SPECIFY);
            String availablity = data.getStringExtra(AddMachineActivity.EXTRA_AVAILABILITY);
            String rent_prov = data.getStringExtra(AddMachineActivity.EXTRA_RENT_PROV);
            String rent_mun = data.getStringExtra(AddMachineActivity.EXTRA_RENT_MUN);
            String rent_brgy = data.getStringExtra(AddMachineActivity.EXTRA_RENT_BRGY);
            String condition = data.getStringExtra(AddMachineActivity.EXTRA_CONDITION);
            String problems = data.getStringExtra(AddMachineActivity.EXTRA_PROBLEMS);
            String problems_specify = data.getStringExtra(AddMachineActivity.EXTRA_PROBLEMS_SPECIFY);
            String location = data.getStringExtra(AddMachineActivity.EXTRA_LOCATION);
            String prov = data.getStringExtra(AddMachineActivity.EXTRA_PROV);
            String mun = data.getStringExtra(AddMachineActivity.EXTRA_MUN);
            String brgy = data.getStringExtra(AddMachineActivity.EXTRA_BRGY);
            String latitude = data.getStringExtra(AddMachineActivity.EXTRA_LAT);
            String longitude = data.getStringExtra(AddMachineActivity.EXTRA_LONG);
            String imageString = Variable.getStringImage();
            String accuracy = data.getStringExtra(AddMachineActivity.EXTRA_ACC);
            String tubewells = data.getStringExtra(AddMachineActivity.EXTRA_TYPE_TUBEWELLS);
            String type_mill = data.getStringExtra(AddMachineActivity.EXTRA_TYPE_MILL);
            String resCode = data.getStringExtra(AddMachineActivity.EXTRA_RES_CODE);
            String resName = data.getStringExtra(AddMachineActivity.EXTRA_RES_NAME);

            Machines machines = new Machines(machineType, tubewells, type_mill, machineQRCode, datesurvey, brand, brand_specify, model, model_specify, rated_power, service_area, ave_op_hours,
                    ave_op_days, capacity, ave_yield, num_loads, rate, ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired,
                    rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov, rent_mun, rent_brgy, condition, problems, problems_specify, location,
                    prov, mun, brgy, latitude, longitude, imageString, accuracy, resCode, resName);

            machines.setId(id);

            machineListViewModel.update(machines);
            Toast.makeText(getActivity(), "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Note not saved", Toast.LENGTH_SHORT).show();
            Log.d("Is note saved", "Note Not Saved");
        }

    }

}
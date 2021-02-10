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
import android.widget.Spinner;
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
import com.m3das.biomech.design.AddImplementActivity;
import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.ImplementAdapter;
import com.m3das.biomech.design.MachineAdapter;
import com.m3das.biomech.design.ProfileAdapter;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.Variable;
import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.viewmodels.ImplementViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImplementsFragment extends Fragment {

    private ImplementViewModel mViewModel;
    private Spinner selectFrag;
    private FloatingActionButton fabAddImplement, fabDeleteImplement;
    private ImplementViewModel implementViewModel;
    private RecyclerView recyclerView;
    private int num;
    public static final int ADD_IMPLEMENT_REQUEST = 19283012;
    public static final int EDIT_IMPLEMENT_REQUEST = 9123123;
    public static final String EXTRA_IMP_ID = "ADDIMPLEMENT_EXTRA_ID";
    public static final String EXTRA_IMP_TYPE = "ADDIMPLEMENT_EXTRA_IMP_TYPE";
    public static final String EXTRA_IMP_QR = "ADDIMPLEMENT_EXTRA_IMP_QR";
    public static final String EXTRA_DATE = "ADDIMPLEMENT_EXTRA_DATE";
    public static final String EXTRA_USED_ON = "ADDIMPLEMENT_EXTRA_USED_ON";
    public static final String EXTRA_USED_COMPLETE = "ADDIMPLEMENT_EXTRA_USED_COMPLETE";
    public static final String EXTRA_LAND_CLEAR = "ADDIMPLEMENT_EXTRA_LAND_CLEAR";
    public static final String EXTRA_PRE_PLANT = "ADDIMPLEMENT_EXTRA_PRE_PLANT";
    public static final String EXTRA_PLANTING = "ADDIMPLEMENT_EXTRA_PLANTING";
    public static final String EXTRA_FERT_APP = "ADDIMPLEMENT_EXTRA_FERT_APP";
    public static final String EXTRA_PEST_APP = "ADDIMPLEMENT_EXTRA_PEST_APP";
    public static final String EXTRA_IRRI_DRAIN = "ADDIMPLEMENT_EXTRA_IRRI_DRAIN";
    public static final String EXTRA_CULT = "ADDIMPLEMENT_EXTRA_CULT";
    public static final String EXTRA_RATOON = "ADDIMPLEMENT_EXTRA_RATOON";
    public static final String EXTRA_HARVEST = "ADDIMPLEMENT_EXTRA_HARVEST";
    public static final String EXTRA_POST_HARVEST = "ADDIMPLEMENT_EXTRA_POST_HARVEST";
    public static final String EXTRA_HAULING = "ADDIMPLEMENT_EXTRA_HAULING";
    public static final String EXTRA_TSA_MAIN = "ADDIMPLEMENT_EXTRA_TSA_MAIN";
    public static final String EXTRA_AVE_OP_HOURS_MAIN = "ADDIMPLEMENT_EXTRA_AVE_OP_HOURS_MAIN";
    public static final String EXTRA_AVE_OP_DAYS_MAIN = "ADDIMPLEMENT_EXTRA_AVE_OP_DAYS_MAIN";
    public static final String EXTRA_EFF_AREA_ACC_MAIN = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_MAIN";
    public static final String EXTRA_TIME_USED_OP_MAIN = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_MAIN";
    public static final String EXTRA_FIELD_CAP_MAIN = "ADDIMPLEMENT_EXTRA_FIELD_CAP_MAIN";
    public static final String EXTRA_TYPE_PLANT = "ADDIMPLEMENT_EXTRA_TYPE_PLANT";
    public static final String EXTRA_NUM_ROWS_PLANT = "ADDIMPLEMENT_EXTRA_NUM_ROWS_PLANT";
    public static final String EXTRA_DIST_MAT_PLANT = "ADDIMPLEMENT_EXTRA_DIST_MAT_PLANT";
    public static final String EXTRA_TSA_PLANT = "ADDIMPLEMENT_EXTRA_TSA_PLANT";
    public static final String EXTRA_AVE_OP_HOURS_PLANT = "ADDIMPLEMENT_EXTRA_AVE_OP_HOURS_PLANT";
    public static final String EXTRA_AVE_OP_DAYS_PLANT = "ADDIMPLEMENT_EXTRA_AVE_OP_DAYS_PLANT";
    public static final String EXTRA_EFF_AREA_ACC_PLANT = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_PLANT";
    public static final String EXTRA_TIME_USED_OP_PLANT = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_PLANT";
    public static final String EXTRA_FIELD_CAP_PLANT = "ADDIMPLEMENT_EXTRA_FIELD_CAP_PLANT";
    public static final String EXTRA_TSA_FERT = "ADDIMPLEMENT_EXTRA_TSA_FERT";
    public static final String EXTRA_AVE_OP_HOURS_FERT = "ADDIMPLEMENT_EXTRA_AVE_OP_HOURS_FERT";
    public static final String EXTRA_AVE_OP_DAYS_FERT = "ADDIMPLEMENT_EXTRA_AVE_OP_DAYS_FERT";
    public static final String EXTRA_EFF_AREA_ACC_FERT = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_FERT";
    public static final String EXTRA_TIME_USED_OP_FERT = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_FERT";
    public static final String EXTRA_FIELD_CAP_FERT = "ADDIMPLEMENT_EXTRA_FIELD_CAP_FERT";
    public static final String EXTRA_WEIGHT_FERT = "ADDIMPLEMENT_EXTRA_WEIGHT_FERT";
    public static final String EXTRA_DEL_RATE_FERT = "ADDIMPLEMENT_EXTRA_DEL_RATE_FERT";
    public static final String EXTRA_TSA_HARVEST = "ADDIMPLEMENT_EXTRA_TSA_HARVEST";
    public static final String EXTRA_AVE_OP_HOURS_HARVEST = "ADDIMPLEMENT_EXTRA_AVE_OP_HOURS_HARVEST";
    public static final String EXTRA_AVE_OP_DAYS_HARVEST = "ADDIMPLEMENT_EXTRA_AVE_OP_DAYS_HARVEST";
    public static final String EXTRA_EFF_AREA_ACC_HARVEST = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_HARVEST";
    public static final String EXTRA_TIME_USED_OP_HARVEST = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_HARVEST";
    public static final String EXTRA_FIELD_CAP_HARVEST = "ADDIMPLEMENT_EXTRA_FIELD_CAP_HARVEST";
    public static final String EXTRA_AVE_YIELD_HARVEST = "ADDIMPLEMENT_EXTRA_AVEYIELD_HARVEST";
    public static final String EXTRA_TSA_GRAB = "ADDIMPLEMENT_EXTRA_TSA_GRAB";
    public static final String EXTRA_AVE_OP_HOURS_GRAB = "ADDIMPLEMENT_EXTRA_AVE_OP_HOURS_GRAB";
    public static final String EXTRA_AVE_OP_DAYS_GRAB = "ADDIMPLEMENT_EXTRA_AVE_OP_DAYS_GRAB";
    public static final String EXTRA_LOAD_CAP_GRAB = "ADDIMPLEMENT_EXTRA_LOAD_CAP_GRAB";
    public static final String EXTRA_NUM_LOAD_GRAB = "ADDIMPLEMENT_EXTRA_NUM_LOAD_GRAB";
    public static final String EXTRA_TSA_DITCH = "ADDIMPLEMENT_EXTRA_TSA_DITCH";
    public static final String EXTRA_AVE_OP_HOURS_DITCH = "ADDIMPLEMENT_EXTRA_AVE_OP_HOURS_DITCH";
    public static final String EXTRA_AVE_OP_DAYS_DITCH = "ADDIMPLEMENT_EXTRA_AVE_OP_DAYS_DITCH";
    public static final String EXTRA_DEPTH_CUT_DITCH = "ADDIMPLEMENT_EXTRA_DEPTH_CUT_DITCH";
    public static final String EXTRA_YEAR_ACQUIRED = "ADDIMPLEMENT_EXTRA_YEAR_ACQUIRED";
    public static final String EXTRA_CONDITION = "ADDIMPLEMENT_EXTRA_CONDITION";
    public static final String EXTRA_LOCATION = "ADDIMPLEMENT_EXTRA_LOCATION";
    public static final String EXTRA_PROVINCE = "ADDIMPLEMENT_EXTRA_PROVINCE";
    public static final String EXTRA_MUNICIPALITY = "ADDIMPLEMENT_EXTRA_MUNICIPALITY";
    public static final String EXTRA_BARANGAY = "ADDIMPLEMENT_EXTRA_BARANGAY";
    public static final String EXTRA_LATITUDE = "ADDIMPLEMENT_EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "ADDIMPLEMENT_EXTRA_LONGITUDE";
    public static final String EXTRA_ACCURACY = "ADDIMPLEMENT_EXTRA_ACCURACY";
    ImplementAdapter implementAdapter;


    public static ImplementsFragment newInstance() {
        return new ImplementsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.implements_fragment, container, false);
        num = 3;
        fabAddImplement = v.findViewById(R.id.floatingActionButtonImpAdd);
        fabDeleteImplement = v.findViewById(R.id.floatingActionButtonDeleteImplement);
        fabAddImplement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddImplementActivity();
            }
        });
        recyclerView = v.findViewById(R.id.recyclerViewIL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        implementAdapter = new ImplementAdapter();
        recyclerView.setAdapter(implementAdapter);

        implementViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);

        implementViewModel.getAllImplements().observe(getActivity(), new Observer<List<Implements>>() {
            @Override
            public void onChanged(List<Implements> anImplements) {
//                ArrayList<Variable.ImplementAllList> implementArrayList = new ArrayList<>();
//                for (int i = anImplements.size() - 1; i > -1; i--) {
//                    implementList = implementList + "Implement ID: " + anImplements.get(i).getId();
//                    implementList = implementList + "\nImplement QR Code: " + anImplements.get(i).getImplement_qrcode();
//                    implementList = implementList + "\nAttached to Machine: " + anImplements.get(i).getUsed_on_machine();
//                    implementList = implementList + "\nImplement type: " + anImplements.get(i).getImplement_type() + "\n\n";
//                    implementArrayList.add(new Variable.ImplementAllList(String.valueOf(anImplements.get(i).getId()), String.valueOf(anImplements.get(i).getImplement_type()), String.valueOf(anImplements.get(i).getUsed_on_machine()), String.valueOf(anImplements.get(i).getId())));
//                }
//
//                Variable.setImpList(implementList);

                implementAdapter.setImplementsArrayListList(anImplements);
            }
        });

        implementAdapter.setOnItemClickListener(new ImplementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Implements implement) {

                Intent editImplement = new Intent(getContext(), AddImplementActivity.class);

                editImplement.putExtra(EXTRA_IMP_ID, implement.getId());
                editImplement.putExtra(EXTRA_IMP_TYPE, implement.getImplement_type());
                editImplement.putExtra(EXTRA_IMP_QR, implement.getImplement_qrcode());
                editImplement.putExtra(EXTRA_DATE, implement.getDate_of_survey());
                editImplement.putExtra(EXTRA_USED_ON, implement.getUsed_on_machine());
                editImplement.putExtra(EXTRA_USED_COMPLETE, implement.getUsed_on_machine_complete());
                editImplement.putExtra(EXTRA_LAND_CLEAR, implement.getLand_clearing());
                editImplement.putExtra(EXTRA_PRE_PLANT, implement.getPre_planting());
                editImplement.putExtra(EXTRA_PLANTING, implement.getPlanting());
                editImplement.putExtra(EXTRA_FERT_APP, implement.getFertilizer_application());
                editImplement.putExtra(EXTRA_PEST_APP, implement.getPesticide_application());
                editImplement.putExtra(EXTRA_IRRI_DRAIN, implement.getIrrigation_drainage());
                editImplement.putExtra(EXTRA_CULT, implement.getCultivation());
                editImplement.putExtra(EXTRA_RATOON, implement.getRatooning());
                editImplement.putExtra(EXTRA_HARVEST, implement.getHarvest());
                editImplement.putExtra(EXTRA_POST_HARVEST, implement.getPost_harvest());
                editImplement.putExtra(EXTRA_HAULING, implement.getHauling());
                editImplement.putExtra(EXTRA_TSA_MAIN, implement.getTotal_service_area_main());
                editImplement.putExtra(EXTRA_AVE_OP_HOURS_MAIN, implement.getAverage_operating_hours_main());
                editImplement.putExtra(EXTRA_AVE_OP_DAYS_MAIN, implement.getAverage_operating_days_main());
                editImplement.putExtra(EXTRA_EFF_AREA_ACC_MAIN, implement.getEffective_area_accomplished_main());
                editImplement.putExtra(EXTRA_TIME_USED_OP_MAIN, implement.getTime_used_during_operation_main());
                editImplement.putExtra(EXTRA_FIELD_CAP_MAIN, implement.getField_capacity_main());
                editImplement.putExtra(EXTRA_TYPE_PLANT, implement.getType_of_planter());
                editImplement.putExtra(EXTRA_NUM_ROWS_PLANT, implement.getNumber_of_rows_planter());
                editImplement.putExtra(EXTRA_DIST_MAT_PLANT, implement.getDistance_of_materials_planter());
                editImplement.putExtra(EXTRA_TSA_PLANT, implement.getTotal_service_area_planter());
                editImplement.putExtra(EXTRA_AVE_OP_HOURS_PLANT, implement.getAverage_operating_hours_planter());
                editImplement.putExtra(EXTRA_AVE_OP_DAYS_PLANT, implement.getAverage_operating_days_planter());
                editImplement.putExtra(EXTRA_EFF_AREA_ACC_PLANT, implement.getEffective_area_accomplished_planter());
                editImplement.putExtra(EXTRA_TIME_USED_OP_PLANT, implement.getTime_used_during_operation_planter());
                editImplement.putExtra(EXTRA_FIELD_CAP_PLANT, implement.getField_capacity_planter());
                editImplement.putExtra(EXTRA_TSA_FERT, implement.getTotal_service_area_fertilizer());
                editImplement.putExtra(EXTRA_AVE_OP_HOURS_FERT, implement.getAverage_operating_hours_fertilizer());
                editImplement.putExtra(EXTRA_AVE_OP_DAYS_FERT, implement.getAverage_operating_days_fertilizer());
                editImplement.putExtra(EXTRA_EFF_AREA_ACC_FERT, implement.getEffective_area_accomplished_planter());
                editImplement.putExtra(EXTRA_TIME_USED_OP_FERT, implement.getTime_used_during_operation_fertilizer());
                editImplement.putExtra(EXTRA_FIELD_CAP_FERT, implement.getField_capacity_fertilizer());
                editImplement.putExtra(EXTRA_WEIGHT_FERT, implement.getWeight_fertilizer());
                editImplement.putExtra(EXTRA_DEL_RATE_FERT,implement.getDelivery_rate_fetilizer());
                editImplement.putExtra(EXTRA_TSA_HARVEST, implement.getTotal_service_area_harvester());
                editImplement.putExtra(EXTRA_AVE_OP_HOURS_HARVEST, implement.getAverage_operating_hours_harvester());
                editImplement.putExtra(EXTRA_AVE_OP_DAYS_HARVEST, implement.getAverage_operating_days_harvester());
                editImplement.putExtra(EXTRA_EFF_AREA_ACC_HARVEST, implement.getEffective_area_accomplished_harvester());
                editImplement.putExtra(EXTRA_TIME_USED_OP_HARVEST,implement.getTime_used_during_operation_harvester());
                editImplement.putExtra(EXTRA_FIELD_CAP_HARVEST,implement.getField_capacity_harvester());
                editImplement.putExtra(EXTRA_AVE_YIELD_HARVEST, implement.getAverage_yield_harvester());
                editImplement.putExtra(EXTRA_TSA_GRAB,implement.getTotal_service_area_cane_grab_loader());
                editImplement.putExtra(EXTRA_AVE_OP_HOURS_GRAB,implement.getAverage_operating_hours_cane_grab_loader());
                editImplement.putExtra(EXTRA_AVE_OP_DAYS_GRAB,implement.getAverage_operating_days_cane_grab_loader());
//        editImplement.putExtra(EXTRA_EFF_AREA_ACC_GRAB,effe);
                editImplement.putExtra(EXTRA_LOAD_CAP_GRAB, implement.getLoading_capacity_cane_grab_loader());
                editImplement.putExtra(EXTRA_NUM_LOAD_GRAB, implement.getNumber_loads_cane_grab_loader());
                editImplement.putExtra(EXTRA_TSA_DITCH,implement.getTotal_service_area_ditcher());
                editImplement.putExtra(EXTRA_AVE_OP_HOURS_DITCH, implement.getAverage_operating_hours_ditcher());
                editImplement.putExtra(EXTRA_AVE_OP_DAYS_DITCH,implement.getAverage_operating_days_ditcher());
                editImplement.putExtra(EXTRA_DEPTH_CUT_DITCH,implement.getDepth_cut_ditcher());
                editImplement.putExtra(EXTRA_YEAR_ACQUIRED,implement.getYear_acquired());
                editImplement.putExtra(EXTRA_CONDITION, implement.getCondition());
                editImplement.putExtra(EXTRA_LOCATION, implement.getLocation());
                editImplement.putExtra(EXTRA_PROVINCE, implement.getProvince());
                editImplement.putExtra(EXTRA_MUNICIPALITY, implement.getCity());
                editImplement.putExtra(EXTRA_BARANGAY, implement.getBarangay());
                editImplement.putExtra(EXTRA_LATITUDE, implement.getLatitude());
                editImplement.putExtra(EXTRA_LONGITUDE,implement.getLongitude());
                editImplement.putExtra(EXTRA_ACCURACY, implement.getAccuracy());
                Variable.setStringImage(implement.getImage_base64());

                startActivityForResult(editImplement, EDIT_IMPLEMENT_REQUEST);

            }
        });


        implementAdapter.setOnItemClickListener(new ImplementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Implements implement) {
                Toast.makeText(getContext(), "Editing implement still unavailable", Toast.LENGTH_SHORT).show();
            }
        });

        fabDeleteImplement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = num + 1;

                if (num % 2 == 0) {
                    fabDeleteImplement.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDarker)));
                    fabDeleteImplement.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    Toast.makeText(getContext(), "Now you can delete items by swiping", Toast.LENGTH_SHORT).show();

                } else {

                    fabDeleteImplement.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
                    fabDeleteImplement.setRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDarker)));

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
                        .setMessage("You will be deleting this:\n" + implementAdapter.getImplement(viewHolder.getAdapterPosition()).getImplement_type() + "\n" + implementAdapter.getImplement(viewHolder.getAdapterPosition()).getImplement_qrcode())
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                implementViewModel.delete(implementAdapter.getImplement(viewHolder.getAdapterPosition()));
                                implementAdapter.notifyDataSetChanged();
                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                implementAdapter.notifyDataSetChanged();
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

    public void openAddImplementActivity() {
        Intent intent = new Intent(getContext(), AddImplementActivity.class);
        startActivityForResult(intent, ADD_IMPLEMENT_REQUEST);
    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(ImplementViewModel.class);

        mViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);

    }

*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_IMPLEMENT_REQUEST && resultCode == Activity.RESULT_OK) {

            String implement_type = data.getStringExtra(AddImplementActivity.EXTRA_IMP_TYPE);
            String implement_qr_code = data.getStringExtra(AddImplementActivity.EXTRA_IMP_QR);
            String date_of_survey = data.getStringExtra(AddImplementActivity.EXTRA_DATE);
            String used_on_machine = data.getStringExtra(AddImplementActivity.EXTRA_USED_ON);
            String used_on_machine_complete = data.getStringExtra(AddImplementActivity.EXTRA_USED_COMPLETE);
            String land_clearing = data.getStringExtra(AddImplementActivity.EXTRA_LAND_CLEAR);
            String pre_planting = data.getStringExtra(AddImplementActivity.EXTRA_PRE_PLANT);
            String planting = data.getStringExtra(AddImplementActivity.EXTRA_PLANTING);
            String fertilizer_application = data.getStringExtra(AddImplementActivity.EXTRA_FERT_APP);
            String pesticide_application = data.getStringExtra(AddImplementActivity.EXTRA_PEST_APP);
            String irrigation_drainage = data.getStringExtra(AddImplementActivity.EXTRA_IRRI_DRAIN);
            String cultivation = data.getStringExtra(AddImplementActivity.EXTRA_CULT);
            String ratooning = data.getStringExtra(AddImplementActivity.EXTRA_RATOON);
            String harvest = data.getStringExtra(AddImplementActivity.EXTRA_HARVEST);
            String post_harvest = data.getStringExtra(AddImplementActivity.EXTRA_POST_HARVEST);
            String hauling = data.getStringExtra(AddImplementActivity.EXTRA_HAULING);
            String total_service_area_main = data.getStringExtra(AddImplementActivity.EXTRA_TSA_MAIN);
            String average_operating_hours_main = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_MAIN);
            String average_operating_days_main = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_MAIN);
            String effective_area_accomplished_main = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_MAIN);
            String time_used_during_operation_main = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_MAIN);
            String field_capacity_main = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_MAIN);
            String type_of_planter = data.getStringExtra(AddImplementActivity.EXTRA_TYPE_PLANT);
            String number_of_rows_planter = data.getStringExtra(AddImplementActivity.EXTRA_NUM_ROWS_PLANT);
            String distance_of_materials_planter = data.getStringExtra(AddImplementActivity.EXTRA_DIST_MAT_PLANT);
            String total_service_area_planter = data.getStringExtra(AddImplementActivity.EXTRA_TSA_PLANT);
            String average_operating_hours_planter = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_PLANT);
            String average_operating_days_planter = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_PLANT);
            String effective_area_accomplished_planter = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_PLANT);
            String time_used_during_operation_planter = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_PLANT);
            String field_capacity_planter = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_PLANT);
            String total_service_area_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_TSA_FERT);
            String average_operating_hours_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_FERT);
            String average_operating_days_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_FERT);
            String effective_area_accomplished_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_FERT);
            String time_used_during_operation_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_FERT);
            String field_capacity_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_FERT);
            String weight_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_WEIGHT_FERT);
            String delivery_rate_fetilizer = data.getStringExtra(AddImplementActivity.EXTRA_DEL_RATE_FERT);
            String total_service_area_harvester = data.getStringExtra(AddImplementActivity.EXTRA_TSA_HARVEST);
            String average_operating_hours_harvester = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_HARVEST);
            String average_operating_days_harvester = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_HARVEST);
            String effective_area_accomplished_harvester = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_HARVEST);
            String time_used_during_operation_harvester = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_HARVEST);
            String field_capacity_harvester = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_HARVEST);
            String average_yield_harvester = data.getStringExtra(AddImplementActivity.EXTRA_AVE_YIELD_HARVEST);
            String total_service_area_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_TSA_GRAB);
            String average_operating_hours_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_GRAB);
            String average_operating_days_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_GRAB);
            String loading_capacity_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_LOAD_CAP_GRAB);
            String number_loads_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_NUM_LOAD_GRAB);
            String total_service_area_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_TSA_DITCH);
            String average_operating_hours_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_DITCH);
            String average_operating_days_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_DITCH);
            String depth_cut_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_DEPTH_CUT_DITCH);
            String year_acquired = data.getStringExtra(AddImplementActivity.EXTRA_YEAR_ACQUIRED);
            String condition = data.getStringExtra(AddImplementActivity.EXTRA_CONDITION);
            String location = data.getStringExtra(AddImplementActivity.EXTRA_LOCATION);
            String province = data.getStringExtra(AddImplementActivity.EXTRA_PROVINCE);
            String city = data.getStringExtra(AddImplementActivity.EXTRA_MUNICIPALITY);
            String barangay = data.getStringExtra(AddImplementActivity.EXTRA_BARANGAY);
            String image_base64 = Variable.getStringImage();
            String latitude = data.getStringExtra(AddImplementActivity.EXTRA_LATITUDE);
            String longitude = data.getStringExtra(AddImplementActivity.EXTRA_LONGITUDE);
            String accuracy = data.getStringExtra(AddImplementActivity.EXTRA_ACCURACY);

            Implements imp = new Implements(implement_type, implement_qr_code, date_of_survey, used_on_machine, used_on_machine_complete, land_clearing, pre_planting, planting, fertilizer_application,
                    pesticide_application, irrigation_drainage, cultivation, ratooning, harvest, post_harvest, hauling, total_service_area_main, average_operating_hours_main, average_operating_days_main,
                    effective_area_accomplished_main, time_used_during_operation_main, field_capacity_main, type_of_planter, number_of_rows_planter, distance_of_materials_planter, total_service_area_planter
                    , average_operating_hours_planter, average_operating_days_planter, effective_area_accomplished_planter, time_used_during_operation_planter, field_capacity_planter, total_service_area_fertilizer
                    , average_operating_hours_fertilizer, average_operating_days_fertilizer, effective_area_accomplished_fertilizer, time_used_during_operation_fertilizer, field_capacity_fertilizer,
                    weight_fertilizer, delivery_rate_fetilizer, total_service_area_harvester, average_operating_hours_harvester, average_operating_days_harvester, effective_area_accomplished_harvester,
                    time_used_during_operation_harvester, field_capacity_harvester, average_yield_harvester, total_service_area_cane_grab_loader, average_operating_hours_cane_grab_loader,
                    average_operating_days_cane_grab_loader, loading_capacity_cane_grab_loader, number_loads_cane_grab_loader, total_service_area_ditcher,
                    average_operating_hours_ditcher, average_operating_days_ditcher, depth_cut_ditcher, year_acquired, condition, location, province, city, barangay, image_base64, latitude, longitude, accuracy);

            implementViewModel.insert(imp);

            Log.d("Is note saved", "Implement Saved" + implement_type);
            Toast.makeText(getActivity(), "Implement saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_IMPLEMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            int id = data.getIntExtra(AddImplementActivity.EXTRA_IMP_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Implement can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String implement_type = data.getStringExtra(AddImplementActivity.EXTRA_IMP_TYPE);
            String implement_qr_code = data.getStringExtra(AddImplementActivity.EXTRA_IMP_QR);
            String date_of_survey = data.getStringExtra(AddImplementActivity.EXTRA_DATE);
            String used_on_machine = data.getStringExtra(AddImplementActivity.EXTRA_USED_ON);
            String used_on_machine_complete = data.getStringExtra(AddImplementActivity.EXTRA_USED_COMPLETE);
            String land_clearing = data.getStringExtra(AddImplementActivity.EXTRA_LAND_CLEAR);
            String pre_planting = data.getStringExtra(AddImplementActivity.EXTRA_PRE_PLANT);
            String planting = data.getStringExtra(AddImplementActivity.EXTRA_PLANTING);
            String fertilizer_application = data.getStringExtra(AddImplementActivity.EXTRA_FERT_APP);
            String pesticide_application = data.getStringExtra(AddImplementActivity.EXTRA_PEST_APP);
            String irrigation_drainage = data.getStringExtra(AddImplementActivity.EXTRA_IRRI_DRAIN);
            String cultivation = data.getStringExtra(AddImplementActivity.EXTRA_CULT);
            String ratooning = data.getStringExtra(AddImplementActivity.EXTRA_RATOON);
            String harvest = data.getStringExtra(AddImplementActivity.EXTRA_HARVEST);
            String post_harvest = data.getStringExtra(AddImplementActivity.EXTRA_POST_HARVEST);
            String hauling = data.getStringExtra(AddImplementActivity.EXTRA_HAULING);
            String total_service_area_main = data.getStringExtra(AddImplementActivity.EXTRA_TSA_MAIN);
            String average_operating_hours_main = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_MAIN);
            String average_operating_days_main = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_MAIN);
            String effective_area_accomplished_main = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_MAIN);
            String time_used_during_operation_main = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_MAIN);
            String field_capacity_main = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_MAIN);
            String type_of_planter = data.getStringExtra(AddImplementActivity.EXTRA_TYPE_PLANT);
            String number_of_rows_planter = data.getStringExtra(AddImplementActivity.EXTRA_NUM_ROWS_PLANT);
            String distance_of_materials_planter = data.getStringExtra(AddImplementActivity.EXTRA_DIST_MAT_PLANT);
            String total_service_area_planter = data.getStringExtra(AddImplementActivity.EXTRA_TSA_PLANT);
            String average_operating_hours_planter = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_PLANT);
            String average_operating_days_planter = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_PLANT);
            String effective_area_accomplished_planter = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_PLANT);
            String time_used_during_operation_planter = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_PLANT);
            String field_capacity_planter = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_PLANT);
            String total_service_area_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_TSA_FERT);
            String average_operating_hours_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_FERT);
            String average_operating_days_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_FERT);
            String effective_area_accomplished_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_FERT);
            String time_used_during_operation_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_FERT);
            String field_capacity_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_FERT);
            String weight_fertilizer = data.getStringExtra(AddImplementActivity.EXTRA_WEIGHT_FERT);
            String delivery_rate_fetilizer = data.getStringExtra(AddImplementActivity.EXTRA_DEL_RATE_FERT);
            String total_service_area_harvester = data.getStringExtra(AddImplementActivity.EXTRA_TSA_HARVEST);
            String average_operating_hours_harvester = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_HARVEST);
            String average_operating_days_harvester = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_HARVEST);
            String effective_area_accomplished_harvester = data.getStringExtra(AddImplementActivity.EXTRA_EFF_AREA_ACC_HARVEST);
            String time_used_during_operation_harvester = data.getStringExtra(AddImplementActivity.EXTRA_TIME_USED_OP_HARVEST);
            String field_capacity_harvester = data.getStringExtra(AddImplementActivity.EXTRA_FIELD_CAP_HARVEST);
            String average_yield_harvester = data.getStringExtra(AddImplementActivity.EXTRA_AVE_YIELD_HARVEST);
            String total_service_area_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_TSA_GRAB);
            String average_operating_hours_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_GRAB);
            String average_operating_days_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_GRAB);
            String loading_capacity_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_LOAD_CAP_GRAB);
            String number_loads_cane_grab_loader = data.getStringExtra(AddImplementActivity.EXTRA_NUM_LOAD_GRAB);
            String total_service_area_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_TSA_DITCH);
            String average_operating_hours_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_HOURS_DITCH);
            String average_operating_days_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_AVE_OP_DAYS_DITCH);
            String depth_cut_ditcher = data.getStringExtra(AddImplementActivity.EXTRA_DEPTH_CUT_DITCH);
            String year_acquired = data.getStringExtra(AddImplementActivity.EXTRA_YEAR_ACQUIRED);
            String condition = data.getStringExtra(AddImplementActivity.EXTRA_CONDITION);
            String location = data.getStringExtra(AddImplementActivity.EXTRA_LOCATION);
            String province = data.getStringExtra(AddImplementActivity.EXTRA_PROVINCE);
            String city = data.getStringExtra(AddImplementActivity.EXTRA_MUNICIPALITY);
            String barangay = data.getStringExtra(AddImplementActivity.EXTRA_BARANGAY);
            String image_base64 = Variable.getStringImage();
            String latitude = data.getStringExtra(AddImplementActivity.EXTRA_LATITUDE);
            String longitude = data.getStringExtra(AddImplementActivity.EXTRA_LONGITUDE);
            String accuracy = data.getStringExtra(AddImplementActivity.EXTRA_ACCURACY);

            Implements imp = new Implements(implement_type, implement_qr_code, date_of_survey, used_on_machine, used_on_machine_complete, land_clearing, pre_planting, planting, fertilizer_application,
                    pesticide_application, irrigation_drainage, cultivation, ratooning, harvest, post_harvest, hauling, total_service_area_main, average_operating_hours_main, average_operating_days_main,
                    effective_area_accomplished_main, time_used_during_operation_main, field_capacity_main, type_of_planter, number_of_rows_planter, distance_of_materials_planter,
                    total_service_area_planter, average_operating_hours_planter, average_operating_days_planter, effective_area_accomplished_planter, time_used_during_operation_planter, field_capacity_planter,
                    total_service_area_fertilizer, average_operating_hours_fertilizer, average_operating_days_fertilizer, effective_area_accomplished_fertilizer,
                    time_used_during_operation_fertilizer, field_capacity_fertilizer, weight_fertilizer, delivery_rate_fetilizer, total_service_area_harvester, average_operating_hours_harvester,
                    average_operating_days_harvester, effective_area_accomplished_harvester, time_used_during_operation_harvester, field_capacity_harvester, average_yield_harvester,
                    total_service_area_cane_grab_loader, average_operating_hours_cane_grab_loader, average_operating_days_cane_grab_loader, loading_capacity_cane_grab_loader, number_loads_cane_grab_loader,
                    total_service_area_ditcher, average_operating_hours_ditcher, average_operating_days_ditcher, depth_cut_ditcher, year_acquired, condition, location, province, city, barangay,
                    image_base64, latitude, longitude, accuracy);

            imp.setId(id);
            implementViewModel.update(imp);

            Toast.makeText(getActivity(), "Implement updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Implement not saved", Toast.LENGTH_SHORT).show();
            Log.d("Is note saved", "Implement Not Saved");
        }

    }
}
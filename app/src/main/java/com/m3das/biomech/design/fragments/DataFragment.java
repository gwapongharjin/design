package com.m3das.biomech.design.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.Toast;

import com.m3das.biomech.design.AddMachineActivity;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.Variable;
import com.m3das.biomech.design.ViewDataActivity;
import com.m3das.biomech.design.ViewImplements;
import com.m3das.biomech.design.ViewMachines;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.uploadtoserver.ResponsePojo;
import com.m3das.biomech.design.uploadtoserver.RetroClient;
import com.m3das.biomech.design.viewmodels.DataViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment {

    private MachineListViewModel machineListViewModel;

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    private Button btnView, btnUpload, btnViewMachines, btnViewImplements;
    private String machineType, machineQRCode, datesurvey, brand, brand_specify, model, model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate,
            ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov,
            rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude, longitude, imageString, accuracy, tubewells, type_mill, resCode, resName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);
        btnView = v.findViewById(R.id.btnView);
        btnUpload = v.findViewById(R.id.btnUpload);
        btnViewMachines = v.findViewById(R.id.btnViewMachinesList);
        btnViewImplements = v.findViewById(R.id.btnViewImplementsList);

        btnView.setVisibility(View.GONE);

        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewDataActivity();
            }
        });

        btnViewMachines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ViewMachines.class);
                getActivity().startActivity(intent);

            }
        });

        btnViewImplements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewImplements.class);
                getActivity().startActivity(intent);
            }
        });


        return v;


    }

    private void uploadData() {

        machineListViewModel.getAllMachines().observe(this, new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {

                for (int i = 0; i < machines.size(); i++) {

                    machineType = machines.get(i).getMachine_type();
                    machineQRCode = machines.get(i).getMachine_qrcode();
                    datesurvey = machines.get(i).getDate_of_survey();
                    brand = machines.get(i).getMachine_brand();
                    brand_specify = machines.get(i).getMachine_brand_specify();
                    model = machines.get(i).getMachine_model();
                    model_specify = machines.get(i).getMachine_model_specify();
                    rated_power = machines.get(i).getRated_power();
                    service_area = machines.get(i).getService_area();
                    ave_op_hours = machines.get(i).getAve_op_hours();
                    ave_op_days = machines.get(i).getAve_op_days();
                    capacity = machines.get(i).getCapacity();
                    ave_yield = machines.get(i).getAve_yield();
                    num_loads = machines.get(i).getNum_loads();
                    rate = machines.get(i).getRate();
                    ownership = machines.get(i).getOwnership();
                    purch_grant_dono = machines.get(i).getPurch_grant_dono();
                    agency = machines.get(i).getAgency();
                    agency_specify = machines.get(i).getAgency_specify();
                    name_owner = machines.get(i).getName_owner();
                    year_acquired = machines.get(i).getYear_acquired();
                    condition_acquired = machines.get(i).getCondition_acquired();
                    rental = machines.get(i).getRental();
                    custom_rate = machines.get(i).getCustom_rate();
                    custom_unit = machines.get(i).getCustom_unit();
                    custom_unit_specify = machines.get(i).getSpecify_custom_unit();
                    availablity = machines.get(i).getAvailability();
                    rent_prov = machines.get(i).getRent_prov();
                    rent_mun = machines.get(i).getRent_mun();
                    rent_brgy = machines.get(i).getRent_brgy();
                    condition = machines.get(i).getCondition();
                    problems = machines.get(i).getProblems();
                    problems_specify = machines.get(i).getSpecify_problems();
                    location = machines.get(i).getLocation();
                    prov = machines.get(i).getProv();
                    mun = machines.get(i).getMun();
                    brgy = machines.get(i).getBrgy();
                    latitude = machines.get(i).getMachine_latitude();
                    longitude = machines.get(i).getMachine_longitude();
                    imageString = machines.get(i).getMachine_image_base64();
                    accuracy = machines.get(i).getAccuracy();
                    tubewells = machines.get(i).getType_tubewells();
                    type_mill = machines.get(i).getType_mill();
                    resCode = machines.get(i).getResCode();
                    resName = machines.get(i).getResName();


                    Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getMachinesApi().uploadMachines(machineType, tubewells, type_mill, machineQRCode, datesurvey, brand, brand_specify, model,
                            model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate, ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired,
                            condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov, rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude,
                            longitude, imageString, accuracy, resCode, resName);

                    responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                        @Override
                        public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                            Toast.makeText(getActivity(), response.body().getRemarks(), Toast.LENGTH_LONG).show();
                            if (response.body().isStatus()) {

                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePojo> call, Throwable t) {
                            Toast.makeText(getActivity(), "Network Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
//                machineType = machines.get(0).getMachine_type();
//                machineQRCode = machines.get(0).getMachine_qrcode();
//                datesurvey = machines.get(0).getDate_of_survey();
//                brand = machines.get(0).getMachine_brand();
//                brand_specify = machines.get(0).getMachine_brand_specify();
//                model = machines.get(0).getMachine_model();
//                model_specify = machines.get(0).getMachine_model_specify();
//                rated_power = machines.get(0).getRated_power();
//                service_area = machines.get(0).getService_area();
//                ave_op_hours = machines.get(0).getAve_op_hours();
//                ave_op_days = machines.get(0).getAve_op_days();
//                capacity = machines.get(0).getCapacity();
//                ave_yield = machines.get(0).getAve_yield();
//                num_loads = machines.get(0).getNum_loads();
//                rate = machines.get(0).getRate();
//                ownership = machines.get(0).getOwnership();
//                purch_grant_dono = machines.get(0).getPurch_grant_dono();
//                agency = machines.get(0).getAgency();
//                agency_specify = machines.get(0).getAgency_specify();
//                name_owner = machines.get(0).getName_owner();
//                year_acquired = machines.get(0).getYear_acquired();
//                condition_acquired = machines.get(0).getCondition_acquired();
//                rental = machines.get(0).getRental();
//                custom_rate = machines.get(0).getCustom_rate();
//                custom_unit = machines.get(0).getCustom_unit();
//                custom_unit_specify = machines.get(0).getSpecify_custom_unit();
//                availablity = machines.get(0).getAvailability();
//                rent_prov = machines.get(0).getRent_prov();
//                rent_mun = machines.get(0).getRent_mun();
//                rent_brgy = machines.get(0).getRent_brgy();
//                condition = machines.get(0).getCondition();
//                problems = machines.get(0).getProblems();
//                problems_specify = machines.get(0).getSpecify_problems();
//                location = machines.get(0).getLocation();
//                prov = machines.get(0).getProv();
//                mun = machines.get(0).getMun();
//                brgy = machines.get(0).getBrgy();
//                latitude = machines.get(0).getMachine_latitude();
//                longitude = machines.get(0).getMachine_longitude();
//                imageString = machines.get(0).getMachine_image_base64();
//                accuracy = machines.get(0).getAccuracy();
//                tubewells = machines.get(0).getType_tubewells();
//                type_mill = machines.get(0).getType_mill();
//                resCode = machines.get(0).getResCode();
//                resName = machines.get(0).getResName();
            }
        });
    }

    public void openViewDataActivity() {
        Intent intent = new Intent(getContext(), ViewDataActivity.class);
        getActivity().startActivity(intent);
    }

}
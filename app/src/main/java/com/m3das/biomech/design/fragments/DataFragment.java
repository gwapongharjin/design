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
import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.uploadtoserver.ResponsePojo;
import com.m3das.biomech.design.uploadtoserver.RetroClient;
import com.m3das.biomech.design.viewmodels.DataViewModel;
import com.m3das.biomech.design.viewmodels.ImplementViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;
import com.m3das.biomech.design.viewmodels.ProfileViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment {

    private MachineListViewModel machineListViewModel;
    private ImplementViewModel implementViewModel;
    private ProfileViewModel profileViewModel;

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    private Button btnView, btnUpload, btnViewMachines, btnViewImplements;
    private String machineType, machineQRCode, datesurvey, brand, brand_specify, model, model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate,
            ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov,
            rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude, longitude, imageString, accuracy, tubewells, type_mill, resCode, resName;
    private String implementType, implementQR, implementDateSurvey, implementUsedOnMachine, implementUsedOnMachineComplete, implementLandClearing, implementPrePlant, implementPlanting, implementFertApp,
            implementPestApp, implementIrriDrain, implementCult, implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementTSAMain, implementAveOpHoursMain,
            implementAveOpDaysMain, implementEFFAAMain, implementTUDOpMain, implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementTSAPlant,
            implementAveOpHoursPlant, implementAveOpDaysPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant, implementTSAFert, implementAveOpHoursFert, implementAveOpDaysFert,
            implementEFFAAFert, implementTUDOpFert, implementFieldCapFert, implementWeightFert, implementDelRateFert, implementTSAHarvest, implementAveOpHoursHarvest, implementAveOpDaysHarvest,
            implementEFFAAHarvest, implementTUDOpHarvest, implementFieldCapHarvest, implementAveYieldHarvest, implementTSAGrab, implementAveOpHoursGrab, implementAveOpDaysGrab, implementEFFAAGrab,
            implementLoadCapGrab, implementNumLoadsGrab, implementTSADitch, implementAveOpHoursDitch, implementAveOpDaysDitch, implementDepthCutDitch, implementYearAcq, implementCondition,
            implementLocation, implementProvince, implementMunicipality, implementBrgy, implementImgBase64, implementLatitude, implementLongitude, implementAccuracy, enumCode;
    private String profileResCode, profileProfile, profileProfileSpecify, profileOwnerType, profileNameRespondent, profileAddress, profileAge, profileSex, profileContactNumber, profileMobNum1,
            profileMobNum2, profileTelNum1, profileTelNum2, profileEducAttain;

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
        implementViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


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

                    if (problems == null) {
                        problems = "";
                    }

                    Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getMachinesApi().uploadMachines(machineType, tubewells, type_mill, machineQRCode, datesurvey, brand, brand_specify, model,
                            model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate, ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired,
                            condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov, rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude,
                            longitude, imageString, accuracy, resCode, resName, "ENUM TO DO MACHINE");

                    responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                        @Override
                        public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                            Toast.makeText(getActivity(), response.body().getRemarks() + ": For Machines", Toast.LENGTH_LONG).show();
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
            }
        });

        implementViewModel.getAllImplements().observe(this, new Observer<List<Implements>>() {
            @Override
            public void onChanged(List<Implements> anImplements) {
                for (int i = 0; i < anImplements.size(); i++) {

                    implementType = anImplements.get(i).getImplement_type();
                    implementQR = anImplements.get(i).getImplement_qrcode();
                    implementDateSurvey = anImplements.get(i).getDate_of_survey();
                    implementUsedOnMachine = anImplements.get(i).getUsed_on_machine();
                    implementUsedOnMachineComplete = anImplements.get(i).getUsed_on_machine_complete();
                    implementLandClearing = anImplements.get(i).getLand_clearing();
                    implementPrePlant = anImplements.get(i).getPre_planting();
                    implementPlanting = anImplements.get(i).getPlanting();
                    implementFertApp = anImplements.get(i).getFertilizer_application();
                    implementPestApp = anImplements.get(i).getPesticide_application();
                    implementIrriDrain = anImplements.get(i).getIrrigation_drainage();
                    implementCult = anImplements.get(i).getCultivation();
                    implementRatoon = anImplements.get(i).getRatooning();
                    implementHarvest = anImplements.get(i).getHarvest();
                    implementPostHarvest = anImplements.get(i).getPost_harvest();
                    implementHaul = anImplements.get(i).getHauling();
                    implementTSAMain = anImplements.get(i).getTotal_service_area_main();
                    implementAveOpHoursMain = anImplements.get(i).getAverage_operating_hours_main();
                    implementAveOpDaysMain = anImplements.get(i).getAverage_operating_days_main();
                    implementEFFAAMain = anImplements.get(i).getEffective_area_accomplished_main();
                    implementTUDOpMain = anImplements.get(i).getTime_used_during_operation_main();
                    implementFieldCapMain = anImplements.get(i).getField_capacity_main();
                    implementTypePlant = anImplements.get(i).getType_of_planter();
                    implementNumRowsPlant = anImplements.get(i).getNumber_of_rows_planter();
                    implementDistMatPlant = anImplements.get(i).getDistance_of_materials_planter();
                    implementTSAPlant = anImplements.get(i).getTotal_service_area_planter();
                    implementAveOpHoursPlant = anImplements.get(i).getAverage_operating_hours_planter();
                    implementAveOpDaysPlant = anImplements.get(i).getAverage_operating_days_planter();
                    implementEFFAAPlant = anImplements.get(i).getEffective_area_accomplished_planter();
                    implementTUDOpPlant = anImplements.get(i).getTime_used_during_operation_planter();
                    implementFieldCapPlant = anImplements.get(i).getField_capacity_planter();
                    implementTSAFert = anImplements.get(i).getTotal_service_area_fertilizer();
                    implementAveOpHoursFert = anImplements.get(i).getAverage_operating_hours_fertilizer();
                    implementAveOpDaysFert = anImplements.get(i).getAverage_operating_days_fertilizer();
                    implementEFFAAFert = anImplements.get(i).getEffective_area_accomplished_fertilizer();
                    implementTUDOpFert = anImplements.get(i).getTime_used_during_operation_fertilizer();
                    implementFieldCapFert = anImplements.get(i).getField_capacity_fertilizer();
                    implementWeightFert = anImplements.get(i).getWeight_fertilizer();
                    implementDelRateFert = anImplements.get(i).getDelivery_rate_fetilizer();
                    implementTSAHarvest = anImplements.get(i).getTotal_service_area_harvester();
                    implementAveOpHoursHarvest = anImplements.get(i).getAverage_operating_hours_harvester();
                    implementAveOpDaysHarvest = anImplements.get(i).getAverage_operating_days_harvester();
                    implementEFFAAHarvest = anImplements.get(i).getEffective_area_accomplished_harvester();
                    implementTUDOpHarvest = anImplements.get(i).getTime_used_during_operation_harvester();
                    implementFieldCapHarvest = anImplements.get(i).getField_capacity_harvester();
                    implementAveYieldHarvest = anImplements.get(i).getAverage_yield_harvester();
                    implementTSAGrab = anImplements.get(i).getTotal_service_area_cane_grab_loader();
                    implementAveOpHoursGrab = anImplements.get(i).getAverage_operating_hours_cane_grab_loader();
                    implementAveOpDaysGrab = anImplements.get(i).getAverage_operating_days_cane_grab_loader();
                    implementEFFAAGrab = anImplements.get(i).getEffective_area_accomplished_cane_grab_loader();
                    implementLoadCapGrab = anImplements.get(i).getLoading_capacity_cane_grab_loader();
                    implementNumLoadsGrab = anImplements.get(i).getNumber_loads_cane_grab_loader();
                    implementTSADitch = anImplements.get(i).getTotal_service_area_ditcher();
                    implementAveOpHoursDitch = anImplements.get(i).getAverage_operating_hours_ditcher();
                    implementAveOpDaysDitch = anImplements.get(i).getAverage_operating_days_ditcher();
                    implementDepthCutDitch = anImplements.get(i).getDepth_cut_ditcher();
                    implementYearAcq = anImplements.get(i).getYear_acquired();
                    implementCondition = anImplements.get(i).getCondition();
                    implementLocation = anImplements.get(i).getLocation();
                    implementProvince = anImplements.get(i).getProvince();
                    implementMunicipality = anImplements.get(i).getCity();
                    implementBrgy = anImplements.get(i).getBarangay();
                    implementImgBase64 = anImplements.get(i).getImage_base64();
                    implementLatitude = anImplements.get(i).getLatitude();
                    implementLongitude = anImplements.get(i).getLongitude();
                    implementAccuracy = anImplements.get(i).getAccuracy();

                    Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getImplementsApi().uploadImplements(implementType, implementQR, implementDateSurvey, implementUsedOnMachine,
                            implementUsedOnMachineComplete, implementLandClearing, implementPrePlant, implementPlanting, implementFertApp, implementPestApp, implementIrriDrain, implementCult,
                            implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementTSAMain, implementAveOpHoursMain, implementAveOpDaysMain, implementEFFAAMain,
                            implementTUDOpMain, implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementTSAPlant, implementAveOpHoursPlant,
                            implementAveOpDaysPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant, implementTSAFert, implementAveOpHoursFert, implementAveOpDaysFert,
                            implementEFFAAFert, implementTUDOpFert, implementFieldCapFert, implementWeightFert, implementDelRateFert, implementTSAHarvest, implementAveOpHoursHarvest,
                            implementAveOpDaysHarvest, implementEFFAAHarvest, implementTUDOpHarvest, implementFieldCapHarvest, implementAveYieldHarvest, implementTSAGrab, implementAveOpHoursGrab,
                            implementAveOpDaysGrab, implementEFFAAGrab, implementLoadCapGrab, implementNumLoadsGrab, implementTSADitch, implementAveOpHoursDitch, implementAveOpDaysDitch,
                            implementDepthCutDitch, implementYearAcq, implementCondition, implementLocation, implementProvince, implementMunicipality, implementBrgy, implementImgBase64,
                            implementLatitude, implementLongitude, implementAccuracy, "ENUM CODE TO DO IMP");

                    responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                        @Override
                        public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {

                            Toast.makeText(getActivity(), response.body().getRemarks() + ": For Implements", Toast.LENGTH_LONG).show();

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
            }
        });

        profileViewModel.getAllProfiles().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {

                for (int i = 0; i < profiles.size(); i++) {
                    profileResCode = profiles.get(i).getResCode();
                    profileProfile = profiles.get(i).getProfile();
                    profileProfileSpecify = profiles.get(i).getProfile_specify();
                    profileOwnerType = profiles.get(i).getOwner_type();
                    profileNameRespondent = profiles.get(i).getName_respondent();
                    profileAddress = profiles.get(i).getAddress();
                    profileAge = profiles.get(i).getAge();
                    profileSex = profiles.get(i).getSex();
                    profileContactNumber = profiles.get(i).getContact_number();
                    profileMobNum1 = profiles.get(i).getMobnum1();
                    profileMobNum2 = profiles.get(i).getMobnum2();
                    profileTelNum1 = profiles.get(i).getTelnum1();
                    profileTelNum2 = profiles.get(i).getTelnum2();
                    profileEducAttain = profiles.get(i).getEducational_attainment();

                    Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getProfilesApi().uploadProfiles(profileResCode, profileProfile, profileProfileSpecify, profileOwnerType, profileNameRespondent,
                            profileAddress, profileAge, profileSex, profileContactNumber, profileMobNum1, profileMobNum2, profileTelNum1, profileTelNum2, profileEducAttain);

                    responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                        @Override
                        public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {

                            Toast.makeText(getActivity(), response.body().getRemarks() + ": For Profiles", Toast.LENGTH_LONG).show();

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


            }
        });


    }

    public void openViewDataActivity() {
        Intent intent = new Intent(getContext(), ViewDataActivity.class);
        getActivity().startActivity(intent);
    }

}
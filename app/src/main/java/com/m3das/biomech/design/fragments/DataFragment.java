package com.m3das.biomech.design.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.m3das.biomech.design.BuildConfig;
import com.m3das.biomech.design.DialogEnumName;
import com.m3das.biomech.design.DialogUpload;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.ViewImplements;
import com.m3das.biomech.design.ViewMachines;
import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.machinedb.MachinesDAO;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.uploadtoserver.ResponsePojo;
import com.m3das.biomech.design.uploadtoserver.RetroClient;
import com.m3das.biomech.design.viewmodels.ImplementViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;
import com.m3das.biomech.design.viewmodels.ProfileViewModel;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment implements DialogEnumName.DialogEnumNameListener {

    private MachineListViewModel machineListViewModel;
    private ImplementViewModel implementViewModel;
    private ProfileViewModel profileViewModel;

    public DataFragment() {
    }

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    private Integer countImplement, countProf, countMachine;

    private Button btnExport, btnUpload, btnViewMachines, btnViewImplements, btnDelete;
    private String machineType, machineQRCode, datesurvey, brand, brand_specify, model, model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, rate,
            ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov,
            rent_mun, rent_brgy, condition_present, problems, problems_specify, location, prov, mun, brgy, latitude, longitude, imageString, accuracy, tubewells, type_mill, resCode, resName;
    private String implementType, implementQR, implementDateSurvey, implementUsedOnMachine, implementUsedOnMachineComplete, implementLandClearing, implementPrePlant, implementPlanting,
            implementFertApp, implementPestApp, implementIrriDrain, implementCult, implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementEFFAAMain,
            implementTUDOpMain, implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant,
            implementEFFAAFert, implementTUDOpFert, implementFieldCapFert, implementWeightFert, implementDelRateFert, implementEFFAAHarvest, implementTUDOpHarvest, implementFieldCapHarvest,
            implementAveYieldHarvest, implementTUDOGrab, implementFieldCapGrab, implementEFFAAGrab, implementLoadCapGrab, implementDepthCutDitch, implementYearAcq, implementCondition,
            implementLocation, implementProvince, implementMunicipality, implementBrgy, implementImgBase64, implementLatitude, implementLongitude, implementAccuracy, enumCode, plow_rate,
            plow_unit, plow_unit_specify, harr_rate, harr_unit, harr_unit_specify, furr_rate, furr_unit, furr_unit_specify, othr_rent_operation, othr_rate, othr_unit, othr_unit_specify,
            ave_fuel_plow, ave_fuel_harr, ave_fuel_furr, year_inoperable, implementBrand, implementModel, newly_planted_area, ratooned_area, implementAgency, implementOwnership,
            implementPurch_grant_dono, waterpump_unit, ave_fuel_main, implementEFFAAFert2,
            implementAgency_specify, implementModifications, implementProblems, implementProblemsSpecify, implementYearInoperable, time_used_harvester, effaa_harvester,
            operationsSelected;
    private String profileResCode, profileProfile, profileProfileSpecify, profileOwnerType, profileNameRespondent, profileAddress, profileAge, profileSex, profileContactNumber, profileMobNum1,
            profileMobNum2, profileTelNum1, profileTelNum2, profileEducAttain;
    private ArrayList<Uri> uriArrayList;

    private int profileID;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);
        btnExport = v.findViewById(R.id.btnExportData);
        btnUpload = v.findViewById(R.id.btnUpload);
        btnViewMachines = v.findViewById(R.id.btnViewMachinesList);
        btnViewImplements = v.findViewById(R.id.btnViewImplementsList);
        btnDelete = v.findViewById(R.id.btnDelete);

        enumCode = "";
//        countImplement = "";
//        countMachine = "";
//        countProf = "";

//        exportDbUtil = new ExportDbUtil(getActivity(), "sampleDbExporter", "library Test", getContext());

        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);
        implementViewModel = new ViewModelProvider(this).get(ImplementViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to upload?");
                builder.setMessage("All data will be deleted after upload. Also input your Enumerator Code");
                final EditText input = new EditText(getContext());
                input.setHint("Input Enumerator Code here");
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enumCode = input.getText().toString();

                        Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().checkEnumeratorCode().checkEnumerator(enumCode);

                        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                            @Override
                            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                                if (response.body().isStatus()) {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                    builder.setTitle("Uploading Data")
//                                            .setMessage("You will be Uploading your data")
//                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                }
//                                            }).show();
                                    Log.d("DFUPLNEWERR", "Inside UPLOAD DATA");
                                    uploadData(response.body().isStatus());
                                } else {
                                    Toast.makeText(getContext(), "Enumerator Code is " + response.body().getRemarks() + "\n\nPlease check the spelling of your code or\ncontact your coordinator to verify your code", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                                Toast.makeText(getContext(), "Network Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d("DFUPLFAIL", "Error: " + t.getMessage());
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();


            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportData();
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

//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                machineListViewModel.deleteAll();
//                implementViewModel.deleteAll();
//                profileViewModel.deleteAll();
//            }
//        });


        return v;
    }

    @Override
    public void applyTexts(String username) {
        enumCode = username;
    }

    private void uploadData(boolean status) {

        if (status) {
            Log.d("DFUPLNEWERR", "UPLOADING DATA");
            Log.d("ENUM", enumCode);
            uploadingProfiles(status);
        } else {
            Toast.makeText(getContext(), "TOAST no upload", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadMachines(boolean status) {


        if (status) {
            DialogUpload dialogUpload = new DialogUpload(getActivity());
            dialogUpload.startLoadingDialog();
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

                        effaa_harvester = machines.get(i).getEffective_area();
                        time_used_harvester = machines.get(i).getTime_used();

                        capacity = machines.get(i).getCapacity();
                        ave_yield = machines.get(i).getAve_yield();
//                        num_loads = machines.get(i).getNum_loads();
                        rate = machines.get(i).getRate();
                        waterpump_unit = machines.get(i).getWaterpump_unit();

                        ownership = machines.get(i).getOwnership();
                        purch_grant_dono = machines.get(i).getPurch_grant_dono();
                        agency = machines.get(i).getAgency();
                        agency_specify = machines.get(i).getAgency_specify();
                        name_owner = machines.get(i).getName_owner();
                        year_acquired = machines.get(i).getYear_acquired();
                        condition_acquired = machines.get(i).getCondition_acquired();
                        rental = machines.get(i).getRental();
                        custom_rate = machines.get(i).getMain_custom_rent();
                        custom_unit = machines.get(i).getMain_custom_rent_unit();
                        custom_unit_specify = machines.get(i).getMain_custom_rent_unit_specify();
                        ave_fuel_main = machines.get(i).getMain_ave_fuel();
                        plow_rate = machines.get(i).getPlow_custom_rent();
                        plow_unit = machines.get(i).getPlow_custom_rent_unit();
                        plow_unit_specify = machines.get(i).getPlow_custom_rent_unit_specify();
                        harr_rate = machines.get(i).getHarr_custom_rent();
                        harr_unit = machines.get(i).getHarr_custom_rent_unit();
                        harr_unit_specify = machines.get(i).getHarr_custom_rent_unit_specify();
                        furr_rate = machines.get(i).getFurr_custom_rent();
                        furr_unit = machines.get(i).getFurr_custom_rent_unit();
                        furr_unit_specify = machines.get(i).getFurr_custom_rent_unit_specify();
                        othr_rent_operation = machines.get(i).getOther_unit_operation();
                        othr_rate = machines.get(i).getOther_custom_rent();
                        othr_unit = machines.get(i).getOther_custom_rent_unit();
                        othr_unit_specify = machines.get(i).getOther_custom_rent_unit_specify();
                        ave_fuel_plow = machines.get(i).getPlow_ave_fuel();
                        ave_fuel_harr = machines.get(i).getHarr_ave_fuel();
                        ave_fuel_furr = machines.get(i).getFurr_ave_fuel();
                        availablity = machines.get(i).getAvailability();
                        rent_prov = machines.get(i).getRent_prov();
                        rent_mun = machines.get(i).getRent_mun();
                        rent_brgy = machines.get(i).getRent_brgy();
                        condition_present = machines.get(i).getCondition();
                        problems = machines.get(i).getProblems();
                        problems_specify = machines.get(i).getSpecify_problems();
                        year_inoperable = machines.get(i).getYear_inoperable();
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

                        if (accuracy == null) {
                            accuracy = "";
                        }

                        Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getMachinesApi().uploadMachines(machineType, tubewells, type_mill, machineQRCode, datesurvey, brand,
                                brand_specify, model, model_specify, rated_power, service_area, newly_planted_area, ratooned_area, ave_op_hours, ave_op_days, effaa_harvester, time_used_harvester, capacity, ave_yield, rate, waterpump_unit,
                                ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, ave_fuel_main,
                                plow_rate, plow_unit, plow_unit_specify, harr_rate, harr_unit, harr_unit_specify, furr_rate, furr_unit, furr_unit_specify, othr_rent_operation, othr_rate,
                                othr_unit, othr_unit_specify, ave_fuel_plow, ave_fuel_harr, ave_fuel_furr, availablity, rent_prov, rent_mun, rent_brgy, condition_present, problems, problems_specify,
                                year_inoperable, location, prov, mun, brgy, latitude, longitude, imageString, accuracy, resCode, resName, enumCode);

                        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                            @Override
                            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                                Toast.makeText(getContext(), response.body().getRemarks() + " For Machines", Toast.LENGTH_SHORT).show();

                                if (response.body().isStatus()) {
                                } else {
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePojo> call, Throwable t) {
//                            Toast.makeText(getActivity(), "Network Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Log.d("Count FORLOOP", machines.size() + ", " + countMachine);
                    machineListViewModel.deleteAll();
                    dialogUpload.dismissDialog();
                }
            });

            uploadImplements(status);

        } else {
            Toast.makeText(getContext(), "TOAST no upload", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImplements(boolean status) {


        if (status) {

            DialogUpload dialogUpload = new DialogUpload(getActivity());

            dialogUpload.startLoadingDialog();
            countImplement = 0;
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

                        implementBrand = anImplements.get(i).getBrand();
                        implementModel = anImplements.get(i).getModel();

                        implementEFFAAMain = anImplements.get(i).getEffective_area_accomplished_main();
                        implementTUDOpMain = anImplements.get(i).getTime_used_during_operation_main();
                        implementFieldCapMain = anImplements.get(i).getField_capacity_main();
                        implementTypePlant = anImplements.get(i).getType_of_planter();
                        implementNumRowsPlant = anImplements.get(i).getNumber_of_rows_planter();
                        implementDistMatPlant = anImplements.get(i).getDistance_of_materials_planter();

                        implementEFFAAPlant = anImplements.get(i).getEffective_area_accomplished_planter();
                        implementTUDOpPlant = anImplements.get(i).getTime_used_during_operation_planter();
                        implementFieldCapPlant = anImplements.get(i).getField_capacity_planter();

                        implementEFFAAFert = anImplements.get(i).getEffective_area_accomplished_fertilizer();
                        implementTUDOpFert = anImplements.get(i).getTime_used_during_operation_fertilizer();
                        implementEFFAAFert2 = anImplements.get(i).getEffective_area_accomplished_fetilizer2();
                        implementFieldCapFert = anImplements.get(i).getField_capacity_fertilizer();
                        implementWeightFert = anImplements.get(i).getWeight_fertilizer();
                        implementDelRateFert = anImplements.get(i).getDelivery_rate_fetilizer();

                        implementEFFAAHarvest = anImplements.get(i).getEffective_area_accomplished_harvester();
                        implementTUDOpHarvest = anImplements.get(i).getTime_used_during_operation_harvester();
                        implementFieldCapHarvest = anImplements.get(i).getField_capacity_harvester();
                        implementAveYieldHarvest = anImplements.get(i).getAverage_yield_harvester();

                        implementEFFAAGrab = anImplements.get(i).getEffective_area_accomplished_cane_grab_loader();
                        implementTUDOGrab = anImplements.get(i).getTime_used_during_operation_cane_grab_loader();
                        implementLoadCapGrab = anImplements.get(i).getLoading_capacity_cane_grab_loader();
                        implementFieldCapGrab = anImplements.get(i).getField_capacity_cane_grab_loader();

                        implementDepthCutDitch = anImplements.get(i).getDepth_cut_ditcher();

                        implementOwnership = anImplements.get(i).getOwnership();
                        implementPurch_grant_dono = anImplements.get(i).getPurchase_grant_donation();
                        implementAgency = anImplements.get(i).getAgency();
                        implementAgency_specify = anImplements.get(i).getAgency_specify();

                        implementYearAcq = anImplements.get(i).getYear_acquired();
                        implementCondition = anImplements.get(i).getCondition();

                        implementModifications = anImplements.get(i).getModifications();
                        implementProblems = anImplements.get(i).getProblems();
                        implementProblemsSpecify = anImplements.get(i).getProblems_specify();
                        implementYearInoperable = anImplements.get(i).getYear_inoperable();

                        implementLocation = anImplements.get(i).getLocation();
                        implementProvince = anImplements.get(i).getProvince();
                        implementMunicipality = anImplements.get(i).getCity();
                        implementBrgy = anImplements.get(i).getBarangay();
                        implementImgBase64 = anImplements.get(i).getImage_base64();
                        implementLatitude = anImplements.get(i).getLatitude();
                        implementLongitude = anImplements.get(i).getLongitude();
                        implementAccuracy = anImplements.get(i).getAccuracy();

                        Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getImplementsApi().uploadImplements(implementType, implementQR, implementDateSurvey, implementUsedOnMachine,
                                implementUsedOnMachineComplete, implementBrand, implementModel, implementLandClearing, implementPrePlant, implementPlanting, implementFertApp, implementPestApp,
                                implementIrriDrain, implementCult, implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementEFFAAMain, implementTUDOpMain,
                                implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant,
                                implementEFFAAFert, implementTUDOpFert, implementFieldCapFert, implementEFFAAFert2, implementWeightFert, implementDelRateFert, implementEFFAAHarvest, implementTUDOpHarvest,
                                implementFieldCapHarvest, implementAveYieldHarvest, implementEFFAAGrab, implementTUDOGrab, implementLoadCapGrab, implementFieldCapGrab, implementDepthCutDitch,
                                implementOwnership, implementPurch_grant_dono, implementAgency, implementAgency_specify, implementYearAcq, implementCondition, implementModifications,
                                implementProblems, implementProblemsSpecify, implementYearInoperable, implementLocation, implementProvince, implementMunicipality, implementBrgy, implementImgBase64,
                                implementLatitude, implementLongitude, implementAccuracy, enumCode);

                        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                            @Override
                            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {

                                Toast.makeText(getContext(), response.body().getRemarks() + ": For Implements", Toast.LENGTH_SHORT).show();
                                if (response.body().isStatus()) {
                                    countImplement++;
                                } else {
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePojo> call, Throwable t) {
//                            Toast.makeText(getActivity(), "Network Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        Log.d("DFUPLIMP", anImplements.size() + ", " + countImplement.toString());
//                        if (anImplements.size() == countImplement) {
//
//                        }
                    }
                }
            });
            implementViewModel.deleteAll();
            dialogUpload.dismissDialog();
        } else {
            Toast.makeText(getContext(), "TOAST no upload", Toast.LENGTH_LONG).show();
        }

        DialogUpload dialogUpload = new DialogUpload(getActivity());

        dialogUpload.startLoadingDialog();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogUpload.dismissDialog();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Upload Complete")
                        .setMessage("You have uploaded all your data.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        }, 60000);
    }

    private void uploadingProfiles(boolean status) {
        if (status) {
            Log.d("DFUPLNEWERR", "UPLOADING PROFILES");
            DialogUpload dialogUpload = new DialogUpload(getActivity());

            dialogUpload.startLoadingDialog();
            countProf = 0;
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

                                Toast.makeText(getContext(), response.body().getRemarks() + ": For Profiles", Toast.LENGTH_SHORT).show();
                                if (response.body().isStatus()) {
//                                profileViewModel.delete(profiles.get(profileID));
                                } else {
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePojo> call, Throwable t) {
//                            Toast.makeText(getActivity(), "Network Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Log.d("DFUPLIMP", profiles.size() + ", " + countProf.toString());
//                    if (profiles.size() == countProf) {
//                        implementViewModel.deleteAll();
//                        dialogUpload.dismissDialog();
//                    }
                }
            });
            profileViewModel.deleteAll();
            dialogUpload.dismissDialog();
            uploadMachines(status);
        } else {
            Toast.makeText(getContext(), "TOAST no upload", Toast.LENGTH_LONG).show();
        }
    }

    public void exportData() {

        checkViewModels();
        uriArrayList = new ArrayList<>();
        Log.d("DFEXP", "Exporting Data");

        String fileNameProf = "profile.csv";
        String fileNameMach = "machine.csv";
        String fileNameImp = "implement.csv";
        String filePath = getContext().getExternalFilesDir(null).getPath() + "/";


        File fileProfile = new File(filePath + fileNameProf);
        File fileMachine = new File(filePath + fileNameMach);
        File fileImplement = new File(filePath + fileNameImp);

        Log.d("DFEXP", "File Path: " + filePath);

        Uri u1 = FileProvider.getUriForFile(
                getContext(),
                BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
                fileProfile);

        Uri u2 = FileProvider.getUriForFile(
                getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                fileMachine);

        Uri u3 = FileProvider.getUriForFile(
                getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                fileImplement);

//        ArrayList<Uri> uris = new ArrayList<>();

//            Log.d("DFEXPPLS", profileListSend.get(1).toString());
//            Log.d("DFEXPMLS", machineListSend.get(1).toString());
//            Log.d("DFEXPILS", implementListSend.get(1).toString());
        uriArrayList.add(u1);
        uriArrayList.add(u2);
        uriArrayList.add(u3);

        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Data for " + new SimpleDateFormat("MMM dd, yyyy").format(new Date()));
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"biomech.m3das@gmail.com"});
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriArrayList);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    public void exportDataProfile(List<String[]> profilesList) {

        Log.d("DFEXPPROF", "Exporting Data");

        String fileNameProf = "profile.csv";
        String filePath = getContext().getExternalFilesDir(null).getPath() + "/";

        CSVWriter csvWriterProf;

        File fileProfile = new File(filePath + fileNameProf);


        try {
            if (fileProfile.exists()) {
                fileProfile.delete();
            }
            fileProfile.createNewFile();

            FileWriter fp = new FileWriter(fileProfile.getAbsoluteFile(), true);
            csvWriterProf = new CSVWriter(fp);
            csvWriterProf.writeAll(profilesList); // data is adding to csv
            csvWriterProf.flush();
            csvWriterProf.close();

        } catch (IOException e) {
            //error
            Log.d("DFEXPPROF", "Error: " + e.getMessage());
        }

        Log.d("DFEXPPROF", "File Path: " + filePath);
    }

    public void exportDataMachine(List<String[]> machineList) {

        Log.d("DFEXPPROF", "Exporting Data");

        String fileNameProf = "profile.csv";
        String filePath = getContext().getExternalFilesDir(null).getPath() + "/";

        CSVWriter csvWriterMachine;
        String fileNameMach = "machine.csv";
        File fileMachine = new File(filePath + fileNameMach);


        try {
            if (fileMachine.exists()) {
                fileMachine.delete();
            }
            fileMachine.createNewFile();

            FileWriter fm = new FileWriter(fileMachine.getAbsoluteFile(), true);
            csvWriterMachine = new CSVWriter(fm);
            csvWriterMachine.writeAll(machineList); // data is adding to csv
            csvWriterMachine.flush();
            csvWriterMachine.close();

        } catch (IOException e) {
            //error
            Log.d("DFEXPPROF", "Error: " + e.getMessage());
        }

        Log.d("DFEXPPROF", "File Path: " + filePath);
    }

    public void exportDataImplement(List<String[]> implementList) {

        Log.d("DFEXPPROF", "Exporting Data");

        String filePath = getContext().getExternalFilesDir(null).getPath() + "/";

        String fileNameImp = "implement.csv";
        File fileImp = new File(filePath + fileNameImp);
        CSVWriter csvWriterImplement;

        try {
            if (fileImp.exists()) {
                fileImp.delete();
            }
            fileImp.createNewFile();

            FileWriter fi = new FileWriter(fileImp.getAbsoluteFile(), true);
            csvWriterImplement = new CSVWriter(fi);
            csvWriterImplement.writeAll(implementList); // data is adding to csv
            csvWriterImplement.flush();
            csvWriterImplement.close();

        } catch (IOException e) {
            //error
            Log.d("DFEXPPROF", "Error: " + e.getMessage());
        }

        Log.d("DFEXPPROF", "File Path: " + filePath);
    }


    private void checkViewModels() {
        List<String[]> profileListSend = new ArrayList<>();
        List<String[]> implementListSend = new ArrayList<>();
        List<String[]> machineListSend = new ArrayList<>();

        profileListSend.add(new String[]{"Respondent Code", "Respondent Profile", "Profile Specify", "Owner Type", "Name Respondent", "Address", "Age", "Sex", "Contact Number", "Mobile Number 1",
                "Mobile Number 2", "Telephone Number 1", "Telephone Number 2", "Educational Attainment"});

        profileViewModel.getAllProfiles().observe(getActivity(), new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                for (int i = 0; i < profiles.size(); i++) {
                    Gson gson = new Gson();
                    String json = gson.toJson(profiles.get(i));
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


                    profileListSend.add(new String[]{profileResCode, profileProfile, profileProfileSpecify, profileOwnerType, profileNameRespondent,
                            profileAddress, profileAge, profileSex, profileContactNumber, profileMobNum1, profileMobNum2, profileTelNum1, profileTelNum2, profileEducAttain});
                }
                exportDataProfile(profileListSend);
            }
        });

        machineListSend.add(new String[]{"machineType", "machineQRCode", "typeOfTubewells", "Date of Survey",
                "machineBrand", "machineBrandSpecify", "machineModel", "machineModelSpecify", "ratedPower (hp)",
                "serviceArea (ha)", "newlyPlantedArea (ha)", "ratoonedArea (ha)", "Average Operating Hours (hours/day)", "Average Operating Days (days/season)",
                "Effective Area Accomplished (ha)", "Time Used Working (hours)", "Capacity", "Capacity Unit", "averageYield (ton/ha)", "rate",
                "waterpumpUnit", "ownership", "purchGrantDono", "agency", "agency_specify",
                "nameOwnerOrg", "yearAcquired", "conditionAcquired", "rental", "mainRent",
                "mainRateRentUnit", "mainRateRentUnitSpecify", "Main Ave Fuel Consumption (L/ha)", "plowingRent", "plowingRentUnit",
                "plowingRentUnitSpecify", "harrowingRent", "harrowingRentUnit", "harrowingRentUnitSpecify", "furrowingRent",
                "furrowingRentUnit", "furrowingRentUnitSpecify", "otherRentOperation", "otherOperationRent", "otherOperationRentUnit",
                "otherOperationRentUnitSpecify", "Plowing Ave Fuel Consumption (L/ha)", "Harrowing Ave Fuel Consumption (L/ha)", "Furrowing Ave Fuel Consumption (L/ha)", "availability",
                "rentProvince", "rentMunicipality", "rentBarangay", "problems", "problemsSpecify",
                "yearInoperable", "ConditionPresent", "Location", "Province", "Municipality",
                "Barangay", "Latitude", "Longitude", "Accuracy", "Respondent Code",
                "Respondent Name", "Image"});


        machineListViewModel.getAllMachines().observe(getActivity(), new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {
                String capUnit;
                for (int i = 0; i < machines.size(); i++) {

                    machineType = machines.get(i).getMachine_type();
                    machineQRCode = machines.get(i).getMachine_qrcode();
                    tubewells = machines.get(i).getType_tubewells();
                    type_mill = machines.get(i).getType_mill();
                    datesurvey = machines.get(i).getDate_of_survey();
                    brand = machines.get(i).getMachine_brand();
                    brand_specify = machines.get(i).getMachine_brand_specify();
                    model = machines.get(i).getMachine_model();
                    model_specify = machines.get(i).getMachine_model_specify();
                    rated_power = machines.get(i).getRated_power();

                    switch (machineType) {
                        case "BOOM SPRAYER":
                        case "POWER SPRAYER":
                            capUnit = "Liters";
                            break;

                        case "CANE GRAB LOADER":
                        case "INFIELD HAULER":
                            capUnit = "Tons/Load";
                            break;
                        case "MECHANICAL PLANTER":
                        case "REAPER":
                        case "PICKER":
                        case "COMBINE HARVESTER":
                        case "HARVESTER":
                            capUnit = "Hectare/Hour";
                            break;
                        case "MILL":
                        case "SHELLER":
                        case "THRESHER":
                        case "DRYER":
                            capUnit = "kilogram";
                            break;
                        default:
                            capUnit = "";
                            break;
                    }

                    service_area = machines.get(i).getService_area();
                    newly_planted_area = machines.get(i).getNewly_planted_area();
                    ratooned_area = machines.get(i).getRatooned_area();
                    ave_op_hours = machines.get(i).getAve_op_hours();
                    ave_op_days = machines.get(i).getAve_op_days();
                    effaa_harvester = machines.get(i).getEffective_area();
                    time_used_harvester = machines.get(i).getTime_used();
                    capacity = machines.get(i).getCapacity();
                    ave_yield = machines.get(i).getAve_yield();
//                    num_loads = machines.get(i).getNum_loads();
                    rate = machines.get(i).getRate();
                    waterpump_unit = machines.get(i).getWaterpump_unit();
                    ownership = machines.get(i).getOwnership();

                    purch_grant_dono = machines.get(i).getPurch_grant_dono();
                    agency = machines.get(i).getAgency();
                    agency_specify = machines.get(i).getAgency_specify();
                    name_owner = machines.get(i).getName_owner();
                    year_acquired = machines.get(i).getYear_acquired();
                    condition_acquired = machines.get(i).getCondition_acquired();
                    rental = machines.get(i).getRental();
                    custom_rate = machines.get(i).getMain_custom_rent();
                    custom_unit = machines.get(i).getMain_custom_rent_unit();
                    custom_unit_specify = machines.get(i).getMain_custom_rent_unit_specify();
                    ave_fuel_main = machines.get(i).getMain_ave_fuel();

                    plow_rate = machines.get(i).getPlow_custom_rent();
                    plow_unit = machines.get(i).getPlow_custom_rent_unit();
                    plow_unit_specify = machines.get(i).getPlow_custom_rent_unit_specify();
                    harr_rate = machines.get(i).getHarr_custom_rent();
                    harr_unit = machines.get(i).getHarr_custom_rent_unit();
                    harr_unit_specify = machines.get(i).getHarr_custom_rent_unit_specify();
                    furr_rate = machines.get(i).getFurr_custom_rent();
                    furr_unit = machines.get(i).getFurr_custom_rent_unit();
                    furr_unit_specify = machines.get(i).getFurr_custom_rent_unit_specify();

                    othr_rent_operation = machines.get(i).getOther_unit_operation();
                    othr_rate = machines.get(i).getOther_custom_rent();
                    othr_unit = machines.get(i).getOther_custom_rent_unit();
                    othr_unit_specify = machines.get(i).getOther_custom_rent_unit_specify();

                    ave_fuel_plow = machines.get(i).getPlow_ave_fuel();
                    ave_fuel_harr = machines.get(i).getHarr_ave_fuel();
                    ave_fuel_furr = machines.get(i).getFurr_ave_fuel();

                    availablity = machines.get(i).getAvailability();
                    rent_prov = machines.get(i).getRent_prov();
                    rent_mun = machines.get(i).getRent_mun();
                    rent_brgy = machines.get(i).getRent_brgy();
                    condition_present = machines.get(i).getCondition();

                    problems = machines.get(i).getProblems();
                    problems_specify = machines.get(i).getSpecify_problems();
                    year_inoperable = machines.get(i).getYear_inoperable();
                    location = machines.get(i).getLocation();
                    prov = machines.get(i).getProv();
                    mun = machines.get(i).getMun();
                    brgy = machines.get(i).getBrgy();
                    latitude = machines.get(i).getMachine_latitude();
                    longitude = machines.get(i).getMachine_longitude();
                    imageString = machines.get(i).getMachine_image_base64().substring(0, 16);

                    accuracy = machines.get(i).getAccuracy();
                    resCode = machines.get(i).getResCode();
                    resName = machines.get(i).getResName();

                    if (problems == null) {
                        problems = "";
                    }

                    if (accuracy == null) {
                        accuracy = "";
                    }

                    machineListSend.add(new String[]{
                            machineType, machineQRCode, tubewells, datesurvey,
                            brand, brand_specify, model, model_specify, rated_power,
                            service_area, newly_planted_area, ratooned_area, ave_op_hours, ave_op_days,
                            effaa_harvester, time_used_harvester, capacity, capUnit, ave_yield, rate,
                            waterpump_unit, ownership, purch_grant_dono, agency, agency_specify,
                            name_owner, year_acquired, condition_acquired, rental, custom_rate,
                            custom_unit, custom_unit_specify, ave_fuel_main, plow_rate, plow_unit,
                            plow_unit_specify, harr_rate, harr_unit, harr_unit_specify, furr_rate,
                            furr_unit, furr_unit_specify, othr_rent_operation, othr_rate, othr_unit,
                            othr_unit_specify, ave_fuel_plow, ave_fuel_harr, ave_fuel_furr, availablity,
                            rent_prov, rent_mun, rent_brgy, problems, problems_specify,
                            year_inoperable, condition_present, location, prov, mun,
                            brgy, latitude, longitude, accuracy, resCode,
                            resName, imageString});
                }
                exportDataMachine(machineListSend);
            }
        });


        implementListSend.add(new String[]{"implementType", "implementQRCode", "implementUsedOnMachine", "implementUsedOnMachineComplete", "dateOfSurvey",
                "Brand", "Model", "Operations", "LandClearing", "LandPreparation", "Planting",
                "FertilizerApplication", "PesticideApplication", "IrrigationDrainage", "Cultivation", "Ratooning",
                "Harvesting", "PostHarvesting", "Hauling", "EffectiveAreaAccomplishedMain (ha)", "TimeUsedDuringOperationMain (hours)",
                "FieldCapacityMain (ha/hours)", "TypeOfPlanter", "NumberOfRowsPlanter", "DistanceOfMaterialsPlanter (cm)", "EffectiveAreaAccomplishedPlanter (ha)",
                "TimeUsedDuringOperationPlanter (hours)", "FieldCapacityPlanter (ha/hours)", "EffectiveAreaAccomplishedFertilizerApplicator (ha)", "TimeUsedDuringOperationFertilizerApplicator (hours)", "FieldCapacityFertilizerApplicator (ha/hours)",
                "EffectiveAreaAccomplishedFertilizerApplicatorForWeight (ha)", "WeightOfFertilizer (kg)", "DeliveryRateOfFertilizerApplicator (kg/ha)", "EffectiveAreaAccomplishedHarvester (ha)", "TimeUsedDuringOperationHarvester (hours)", "FieldCapacityHarvester (ha/hours)",
                "AverageYieldHarvester (ton of cannes/ha)", "EffectiveAreaAccomplishedCaneGrabLoader (ha)", "TimeUsedDuringOperationCaneGrabLoader (hours)", "LoadCapacityCaneGrabLoader (tons/load)", "FieldCapacityCaneGrabLoader (ha/hours)",
                "DepthOfCutDitcher (cm)", "ownership", "purchGrantDono", "agency", "agencySpecify",
                "YearAcquired", "ConditionPresentImplement", "modifications", "problems", "problemsSpecify",
                "yearInoperable", "Location", "Province", "Munincipality", "Barangay",
                "Latitude", "Longitude", "Accuracy", "Image"});

        implementViewModel.getAllImplements().observe(getActivity(), new Observer<List<Implements>>() {
            @Override
            public void onChanged(List<Implements> anImplements) {

                for (int i = 0; i < anImplements.size(); i++) {
                    Log.d("DFEXVALIMPVM", anImplements.get(i).getImplement_type() + ":" + i);

                    implementLandClearing = "";
                    implementPrePlant = "";
                    implementPlanting = "";
                    implementFertApp = "";
                    implementPestApp = "";
                    implementIrriDrain = "";
                    implementCult = "";
                    implementRatoon = "";
                    implementHarvest = "";
                    implementPostHarvest = "";
                    implementHaul = "";

                    implementType = anImplements.get(i).getImplement_type();
                    implementQR = anImplements.get(i).getImplement_qrcode();
                    implementUsedOnMachine = anImplements.get(i).getUsed_on_machine();
                    implementUsedOnMachineComplete = anImplements.get(i).getUsed_on_machine_complete();
                    implementDateSurvey = anImplements.get(i).getDate_of_survey();

                    implementBrand = anImplements.get(i).getBrand();
                    implementModel = anImplements.get(i).getModel();

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

                    List<String> listOperations = new ArrayList<>();
                    String ops = "";

                    if (implementLandClearing.equals("LAND CLEAR")) {
                        listOperations.add("LAND CLEAR");
                    }

                    if (implementPrePlant.equals("LAND PREPARATION")) {
                        listOperations.add("LAND PREPARATION");
                    }
                    if (implementPlanting.equals("PLANTING")) {
                        listOperations.add("PLANTING");
                    }
                    if (implementFertApp.equals("FERTILIZER APPLICATION")) {
                        listOperations.add("FERTILIZER APPLICATION");
                    }
                    if (implementPestApp.equals("PESTICIDE APPLICATION")) {
                        listOperations.add("PESTICIDE APPLICATION");
                    }
                    if (implementIrriDrain.equals("IRRIGATION & DRAINAGE")) {
                        listOperations.add("IRRIGATION & DRAINAGE");
                    }
                    if (implementCult.equals("CULTIVATION")) {
                        listOperations.add("CULTIVATION");
                    }
                    if (implementRatoon.equals("RATOONING")) {
                        listOperations.add("RATOONING");
                    }
                    if (implementHarvest.equals("HARVESTING")) {
                        listOperations.add("HARVESTING");
                    }
                    if (implementPostHarvest.equals("POST HARVEST")) {
                        listOperations.add("POST HARVEST");
                    }
                    if (implementHaul.equals("HAULING")) {
                        listOperations.add("HAULING");
                    }


                    for (int j = 0; j < listOperations.size(); j++) {
                        ops = listOperations.get(j) + "\n" + ops;
                    }

                    implementEFFAAMain = anImplements.get(i).getEffective_area_accomplished_main();
                    implementTUDOpMain = anImplements.get(i).getTime_used_during_operation_main();
                    implementFieldCapMain = anImplements.get(i).getField_capacity_main();

                    implementTypePlant = anImplements.get(i).getType_of_planter();
                    implementNumRowsPlant = anImplements.get(i).getNumber_of_rows_planter();
                    implementDistMatPlant = anImplements.get(i).getDistance_of_materials_planter();
                    implementEFFAAPlant = anImplements.get(i).getEffective_area_accomplished_planter();
                    implementTUDOpPlant = anImplements.get(i).getTime_used_during_operation_planter();
                    implementFieldCapPlant = anImplements.get(i).getField_capacity_planter();

                    implementEFFAAFert = anImplements.get(i).getEffective_area_accomplished_fertilizer();
                    implementTUDOpFert = anImplements.get(i).getTime_used_during_operation_fertilizer();
                    implementFieldCapFert = anImplements.get(i).getField_capacity_fertilizer();
                    implementEFFAAFert2 = anImplements.get(i).getEffective_area_accomplished_fetilizer2();
                    implementWeightFert = anImplements.get(i).getWeight_fertilizer();
                    implementDelRateFert = anImplements.get(i).getDelivery_rate_fetilizer();

                    implementEFFAAHarvest = anImplements.get(i).getEffective_area_accomplished_harvester();
                    implementTUDOpHarvest = anImplements.get(i).getTime_used_during_operation_harvester();
                    implementFieldCapHarvest = anImplements.get(i).getField_capacity_harvester();
                    implementAveYieldHarvest = anImplements.get(i).getAverage_yield_harvester();

                    implementEFFAAGrab = anImplements.get(i).getEffective_area_accomplished_cane_grab_loader();
                    implementTUDOGrab = anImplements.get(i).getTime_used_during_operation_cane_grab_loader();
                    implementLoadCapGrab = anImplements.get(i).getLoading_capacity_cane_grab_loader();
                    implementFieldCapGrab = anImplements.get(i).getField_capacity_cane_grab_loader();

                    implementDepthCutDitch = anImplements.get(i).getDepth_cut_ditcher();
                    implementOwnership = anImplements.get(i).getOwnership();
                    implementPurch_grant_dono = anImplements.get(i).getPurchase_grant_donation();
                    implementAgency = anImplements.get(i).getAgency();
                    implementAgency_specify = anImplements.get(i).getAgency_specify();

                    implementYearAcq = anImplements.get(i).getYear_acquired();
                    implementCondition = anImplements.get(i).getCondition();
                    implementModifications = anImplements.get(i).getModifications();
                    implementProblems = anImplements.get(i).getProblems();
                    implementProblemsSpecify = anImplements.get(i).getProblems_specify();
                    implementYearInoperable = anImplements.get(i).getYear_inoperable();

                    implementLocation = anImplements.get(i).getLocation();
                    implementProvince = anImplements.get(i).getProvince();
                    implementMunicipality = anImplements.get(i).getCity();
                    implementBrgy = anImplements.get(i).getBarangay();
                    implementLatitude = anImplements.get(i).getLatitude();
                    implementLongitude = anImplements.get(i).getLongitude();
                    implementAccuracy = anImplements.get(i).getAccuracy();
                    implementImgBase64 = anImplements.get(i).getImage_base64().substring(0, 16);


                    implementListSend.add(new String[]{implementType, implementQR, implementUsedOnMachine, implementUsedOnMachineComplete, implementDateSurvey, implementBrand, implementModel, ops, implementLandClearing, implementPrePlant, implementPlanting,
                            implementFertApp, implementPestApp, implementIrriDrain, implementCult, implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementEFFAAMain, implementTUDOpMain,
                            implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant, implementEFFAAFert, implementTUDOpFert, implementFieldCapFert,
                            implementEFFAAFert2, implementWeightFert, implementDelRateFert, implementEFFAAHarvest, implementTUDOpHarvest, implementFieldCapHarvest, implementAveYieldHarvest, implementEFFAAGrab, implementTUDOGrab, implementLoadCapGrab, implementFieldCapGrab,
                            implementDepthCutDitch, implementOwnership, implementPurch_grant_dono, implementAgency, implementAgency_specify, implementYearAcq, implementCondition, implementModifications, implementProblems, implementProblemsSpecify,
                            implementYearInoperable, implementLocation, implementProvince, implementMunicipality, implementBrgy, implementLatitude, implementLongitude, implementAccuracy, implementImgBase64});
                    Log.d("DFEXVALIMPVM", String.valueOf(implementListSend.size()));
                }
                exportDataImplement(implementListSend);
            }
        });

//        exportData(machineListSend, implementListSend, profileListSend);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
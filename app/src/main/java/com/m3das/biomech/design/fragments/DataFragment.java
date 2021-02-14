package com.m3das.biomech.design.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.m3das.biomech.design.BuildConfig;
import com.m3das.biomech.design.DialogEnumName;
import com.m3das.biomech.design.DialogUpload;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.ViewImplements;
import com.m3das.biomech.design.ViewMachines;
import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.machinedb.Machines;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFragment extends Fragment implements DialogEnumName.DialogEnumNameListener {

    private MachineListViewModel machineListViewModel;
    private ImplementViewModel implementViewModel;
    private ProfileViewModel profileViewModel;

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    private String countImplement, countProf, countMachine;

    private Button btnExport, btnUpload, btnViewMachines, btnViewImplements;
    private String machineType, machineQRCode, datesurvey, brand, brand_specify, model, model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate,
            ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired, condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov,
            rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude, longitude, imageString, accuracy, tubewells, type_mill, resCode, resName;
    private String implementType, implementQR, implementDateSurvey, implementUsedOnMachine, implementUsedOnMachineComplete, implementLandClearing, implementPrePlant, implementPlanting, implementFertApp,
            implementPestApp, implementIrriDrain, implementCult, implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementTSAMain, implementAveOpHoursMain,
            implementAveOpDaysMain, implementEFFAAMain, implementTUDOpMain, implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementTSAPlant,
            implementAveOpHoursPlant, implementAveOpDaysPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant, implementTSAFert, implementAveOpHoursFert, implementAveOpDaysFert,
            implementEFFAAFert, implementTUDOpFert, implementFieldCapFert, implementWeightFert, implementDelRateFert, implementTSAHarvest, implementAveOpHoursHarvest, implementAveOpDaysHarvest,
            implementEFFAAHarvest, implementTUDOpHarvest, implementFieldCapHarvest, implementAveYieldHarvest, implementTUDOGrab, implementFieldCapGrab, implementTSAGrab, implementAveOpHoursGrab, implementAveOpDaysGrab, implementEFFAAGrab,
            implementLoadCapGrab, implementNumLoadsGrab, implementTSADitch, implementAveOpHoursDitch, implementAveOpDaysDitch, implementDepthCutDitch, implementYearAcq, implementCondition,
            implementLocation, implementProvince, implementMunicipality, implementBrgy, implementImgBase64, implementLatitude, implementLongitude, implementAccuracy, enumCode, plow_rate,
            plow_unit, plow_unit_specify, harr_rate, harr_unit, harr_unit_specify, furr_rate, furr_unit, furr_unit_specify, othr_rate, othr_unit, othr_unit_specify, ave_fuel_plow, ave_fuel_harr,
            ave_fuel_furr, year_inoperable, implementBrand, implementModel;
    private String profileResCode, profileProfile, profileProfileSpecify, profileOwnerType, profileNameRespondent, profileAddress, profileAge, profileSex, profileContactNumber, profileMobNum1,
            profileMobNum2, profileTelNum1, profileTelNum2, profileEducAttain;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);
        btnExport = v.findViewById(R.id.btnExportData);
        btnUpload = v.findViewById(R.id.btnUpload);
        btnViewMachines = v.findViewById(R.id.btnViewMachinesList);
        btnViewImplements = v.findViewById(R.id.btnViewImplementsList);

        enumCode = "";
        countImplement = "";
        countMachine = "";
        countProf = "";

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
                                    Toast.makeText(getActivity(), "Enumerator Code is " + response.body().getRemarks(), Toast.LENGTH_LONG).show();
                                    uploadData();
                                } else {
                                    Toast.makeText(getActivity(), "Enumerator Code is " + response.body().getRemarks() + "\n\nPlease check the spelling of your code or\ncontact your coordinator to verify your code", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                                Toast.makeText(getActivity(), "Network Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
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
                checkViewModels();
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

    @Override
    public void applyTexts(String username) {
        enumCode = username;
    }

    private void uploadData() {

        Log.d("ENUM", enumCode);

        uploadProfiles();


    }

    private void uploadMachines() {

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
                    custom_rate = machines.get(i).getMain_custom_rent();
                    custom_unit = machines.get(i).getMain_custom_rent_unit();
                    custom_unit_specify = machines.get(i).getMain_custom_rent_unit_specify();
                    plow_rate = machines.get(i).getPlow_custom_rent();
                    plow_unit = machines.get(i).getPlow_custom_rent_unit();
                    plow_unit_specify = machines.get(i).getPlow_custom_rent_unit_specify();
                    harr_rate = machines.get(i).getHarr_custom_rent();
                    harr_unit = machines.get(i).getHarr_custom_rent_unit();
                    harr_unit_specify = machines.get(i).getHarr_custom_rent_unit_specify();
                    furr_rate = machines.get(i).getFurr_custom_rent();
                    furr_unit = machines.get(i).getFurr_custom_rent_unit();
                    furr_unit_specify = machines.get(i).getFurr_custom_rent_unit_specify();
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
                    condition = machines.get(i).getCondition();
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

                    Call<ResponsePojo> responsePojoCall = RetroClient.getInstance().getMachinesApi().uploadMachines(machineType, tubewells, type_mill, machineQRCode, datesurvey, brand, brand_specify, model,
                            model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate, ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired,
                            condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov, rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude,
                            longitude, imageString, accuracy, resCode, resName, enumCode);

                    responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                        @Override
                        public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                            Toast.makeText(getActivity(), response.body().getRemarks() + " For Machines", Toast.LENGTH_SHORT).show();

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

        uploadImplements();
    }

    private void uploadImplements() {
        DialogUpload dialogUpload = new DialogUpload(getActivity());

        dialogUpload.startLoadingDialog();

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
                            implementLatitude, implementLongitude, implementAccuracy, enumCode);

                    responsePojoCall.enqueue(new Callback<ResponsePojo>() {
                        @Override
                        public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {

                            Toast.makeText(getActivity(), response.body().getRemarks() + ": For Implements", Toast.LENGTH_SHORT).show();
                            countImplement = countImplement + ": " + implementQR;
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
                Log.d("Count FORLOOP", anImplements.size() + ", " + countImplement);
                implementViewModel.deleteAll();
                dialogUpload.dismissDialog();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Upload Complete")
                .setMessage("You have uploaded all your data.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }

    private void uploadProfiles() {
        DialogUpload dialogUpload = new DialogUpload(getActivity());

        dialogUpload.startLoadingDialog();

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

                            Toast.makeText(getActivity(), response.body().getRemarks() + ": For Profiles", Toast.LENGTH_SHORT).show();
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
                Log.d("Count FORLOOP", profiles.size() + ", " + countProf);
                profileViewModel.deleteAll();
                dialogUpload.dismissDialog();
            }
        });
        uploadMachines();
    }

    public void exportData(List<String[]> machineList, List<String[]> implementList, List<String[]> profilesList) {

        Log.d("DFEXP", "Exporting Data");

        String fileNameProf = "profile.csv";
        String fileNameMach = "machine.csv";
        String fileNameImp = "implement.csv";
        String filePath = getContext().getExternalFilesDir(null).getPath() + "/";

        CSVWriter csvWriterProf, csvWriterMachine, csvWriterImplement;

        File fileProfile = new File(filePath + fileNameProf);
        File fileMachine = new File(filePath + fileNameMach);
        File fileImplement = new File(filePath + fileNameImp);

        try {
            if (fileProfile.exists()) {
                fileProfile.delete();
            }
            fileProfile.createNewFile();

            if (fileMachine.exists()) {
                fileMachine.delete();
            }
            fileMachine.createNewFile();

            if (fileImplement.exists()) {
                fileImplement.delete();
            }
            fileImplement.createNewFile();

            FileWriter fp = new FileWriter(fileProfile.getAbsoluteFile(),true);
            csvWriterProf = new CSVWriter(fp);
            csvWriterProf.writeAll(profilesList); // data is adding to csv
            csvWriterProf.flush();
            csvWriterProf.close();

            FileWriter fm = new FileWriter(fileMachine.getAbsoluteFile(),true);
            csvWriterMachine = new CSVWriter(fm);
            csvWriterMachine.writeAll(machineList); // data is adding to csv
            csvWriterMachine.close();

            FileWriter fi = new FileWriter(fileImplement.getAbsoluteFile(),true);
            csvWriterImplement = new CSVWriter(fi);
            csvWriterImplement.writeAll(implementList); // data is adding to csv
            csvWriterImplement.close();

        } catch (IOException e) {
            //error
            Log.d("DFEXP", "Error: " + e.getMessage());
        }
//        Log.d("DFEXP", "Exporting Data");
//
//        String fileName = "profile.csv";
//        String filePath = getContext().getExternalFilesDir(null).getPath() + "/" + fileName;
//        CSVWriter csvWriter;
//
//
//        try {
//            File fileProfile = new File(filePath);
//
//            if (fileProfile.exists()) {
//                fileProfile.delete();
//            }
//            fileProfile.createNewFile();
//
//            FileWriter fw = new FileWriter(fileProfile.getAbsoluteFile());
//            csvWriter = new CSVWriter(fw);
//
//            csvWriter.writeAll(profileListSend); // data is adding to csv
//            csvWriter.close();
//
//            Log.d("DFEXP", "File Path: " + filePath);
//
//            Uri u1 = FileProvider.getUriForFile(
//                    getContext(),
//                    BuildConfig.APPLICATION_ID + ".provider", //(use your app signature + ".provider" )
//                    fileProfile);
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Data for " + new SimpleDateFormat("MMM dd, yyyy").format(new Date()));
//            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"biomech.m3das@gmail.com"});
//            sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
//            sendIntent.setType("text/richtext");
//            startActivity(sendIntent);
//
//        } catch (IOException e) {
//            //error
//            Log.d("DFEXP", "Error: " + e.getMessage());
//        }

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

        ArrayList<Uri> uris = new ArrayList<>();

//            Log.d("DFEXPPLS", profileListSend.get(1).toString());
//            Log.d("DFEXPMLS", machineListSend.get(1).toString());
//            Log.d("DFEXPILS", implementListSend.get(1).toString());
        uris.add(u1);
        uris.add(u2);
        uris.add(u3);

        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Data for " + new SimpleDateFormat("MMM dd, yyyy").format(new Date()));
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"biomech.m3das@gmail.com"});
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

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
            }
        });

        machineListSend.add(new String[]{".", ".", ".", ".", "Name Respondent", "Address", "Age", "Sex", "Contact Number", "Mobile Number 1",
                "Mobile Number 2", "Telephone Number 1", "Telephone Number 2", "Educational Attainment"});

        machineListViewModel.getAllMachines().observe(getActivity(), new Observer<List<Machines>>() {
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
                    custom_rate = machines.get(i).getMain_custom_rent();
                    custom_unit = machines.get(i).getMain_custom_rent_unit();
                    custom_unit_specify = machines.get(i).getMain_custom_rent_unit_specify();
                    plow_rate = machines.get(i).getPlow_custom_rent();
                    plow_unit = machines.get(i).getPlow_custom_rent_unit();
                    plow_unit_specify = machines.get(i).getPlow_custom_rent_unit_specify();
                    harr_rate = machines.get(i).getHarr_custom_rent();
                    harr_unit = machines.get(i).getHarr_custom_rent_unit();
                    harr_unit_specify = machines.get(i).getHarr_custom_rent_unit_specify();
                    furr_rate = machines.get(i).getFurr_custom_rent();
                    furr_unit = machines.get(i).getFurr_custom_rent_unit();
                    furr_unit_specify = machines.get(i).getFurr_custom_rent_unit_specify();
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
                    condition = machines.get(i).getCondition();
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

                    machineListSend.add(new String[]{machineType, tubewells, type_mill, machineQRCode, datesurvey, brand, brand_specify, model,
                            model_specify, rated_power, service_area, ave_op_hours, ave_op_days, capacity, ave_yield, num_loads, rate, ownership, purch_grant_dono, agency, agency_specify, name_owner, year_acquired,
                            condition_acquired, rental, custom_rate, custom_unit, custom_unit_specify, availablity, rent_prov, rent_mun, rent_brgy, condition, problems, problems_specify, location, prov, mun, brgy, latitude,
                            longitude, imageString, accuracy, resCode, resName});
                }
            }
        });


        implementListSend.add(new String[]{"Implement Type", "Implement QR Code", "Date of Survey", "Used on Machine", "Used on Machine Complete", "Land Clearing", "Pre Planting", "Planting",//8
                "Fertilizer Application", "Pesticide Application", "Irrigation and Drainage", "Cultivation", "Ratooning", "Harvesting", "Post Harvest", "Hauling", "Total Service Area (Main)",//9
                "Average Operating Hours (Main)", "Average Operating Days (Main)", "Effective Area Accomplished (Main)", "Time Used During Operation (Main)", "Field Capacity (Main)",//5
                "Type of Planter", "Number of Rows (Planter)", "Distance of Materials (Planter)", "Total Service Area (Planter)", "Average Operating Hours (Planter)",//5
                "Average Operating Days (Planter)", "Effective Area Accomplished (Planter)", "Time Used During Operation (Planter)", "Field Capacity (Planter)",//4
                "Total Service Area (Fertilizer Applicator)", "Average Operating Hours (Fertilizer Applicator)", "Average Operating Days (Fertilizer Applicator)",//3
                "Effective Area Accomplished (Fertilizer Applicator)", "Time Used During Operation (Fertilizer Applicator)", "Field Capacity (Fertilizer Applicator)",//3
                "Weight of Fertilizer (Fertilizer Applicator)", "Delivery Rate (Fertilizer Applicator)", "Total Service Area (Harvester)", "Average Operating Hours (Harvester)",//4
                "Average Operating Days (Harvester)", "Effective Area Accomplished (Harvester)", "Time Used During Operation (Harvester)", "Field Capacity (Harvester)", "Average Yield (Harvester)",//5
                "Total Service Area (Cane Grab Loader)", "Average Operating Hours (Cane Grab Loader)", "Average Operating Days (Cane Grab Loader)", "Effective Area Accomplished (Cane Grab Loader)",//4
                "Load Capacity (Cane Grab Loader)", "Number of Loads (Cane Grab Loader)", "Total Service Area (Ditcher)", "Average Operating Hours (Ditcher)", "Average Operating Days (Ditcher)",//5
                "Depth of Cut (Ditcher)", "Year Acquired", "Condition", "Location", "Province", "Municipality", "Barangay", "Image", "Latitude", "Longitude", "Accuracy"});//12

        implementViewModel.getAllImplements().observe(getActivity(), new Observer<List<Implements>>() {
            @Override
            public void onChanged(List<Implements> anImplements) {

                for (int i = 0; i < anImplements.size(); i++) {
                    Log.d("DFEXVALIMPVM", anImplements.get(i).getImplement_type() + ":" + i);
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


                    implementListSend.add(new String[]{implementType, implementQR, implementDateSurvey, implementUsedOnMachine,
                            implementUsedOnMachineComplete, implementLandClearing, implementPrePlant, implementPlanting, implementFertApp, implementPestApp, implementIrriDrain, implementCult,
                            implementRatoon, implementHarvest, implementPostHarvest, implementHaul, implementTSAMain, implementAveOpHoursMain, implementAveOpDaysMain, implementEFFAAMain,
                            implementTUDOpMain, implementFieldCapMain, implementTypePlant, implementNumRowsPlant, implementDistMatPlant, implementTSAPlant, implementAveOpHoursPlant,
                            implementAveOpDaysPlant, implementEFFAAPlant, implementTUDOpPlant, implementFieldCapPlant, implementTSAFert, implementAveOpHoursFert, implementAveOpDaysFert,
                            implementEFFAAFert, implementTUDOpFert, implementFieldCapFert, implementWeightFert, implementDelRateFert, implementTSAHarvest, implementAveOpHoursHarvest,
                            implementAveOpDaysHarvest, implementEFFAAHarvest, implementTUDOpHarvest, implementFieldCapHarvest, implementAveYieldHarvest, implementTSAGrab, implementAveOpHoursGrab,
                            implementAveOpDaysGrab, implementEFFAAGrab, implementLoadCapGrab, implementNumLoadsGrab, implementTSADitch, implementAveOpHoursDitch, implementAveOpDaysDitch,
                            implementDepthCutDitch, implementYearAcq, implementCondition, implementLocation, implementProvince, implementMunicipality, implementBrgy, implementImgBase64,
                            implementLatitude, implementLongitude, implementAccuracy});
                    Log.d("DFEXVALIMPVM", String.valueOf(implementListSend.size()));
                }
            }
        });

        exportData(machineListSend, implementListSend, profileListSend);
    }

}
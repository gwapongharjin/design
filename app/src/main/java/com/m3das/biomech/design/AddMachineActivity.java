package com.m3das.biomech.design;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.m3das.biomech.design.profiledb.Profile;
import com.m3das.biomech.design.viewmodels.ProfileViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class AddMachineActivity extends AppCompatActivity {
    private ImageButton camera, gallery, getLocation, btnScanQR;
    private Button btnSave;
    private int bigMargin, smallMargin, biggerMargin;
    private int stringArrayValue;
    private ImageView selectedImage;

    private EditText edtQRCode, edtCapacity, edtAveYield, edtNumLoads, edtRate, edtAveOpHours, edtAveOpDays, edtNewlyPlantedArea, edtRatoonArea, edtNameOfOwnerOrg, edtCustomRate,
            edtOtherProblems, edtOtherAgency, edtOtherBrand, edtOtherModel, edtRatedPower, edtPlowingRent, edtHarrowingRent, edtFurrowingRent, edtOtherRent, edtAveFuelConsPlow,
            edtAveFuelConsHarr, edtAveFuelConsFurr, edtPlowSpecifyUnit, edtHarrSpecifyUnit, edtFurrSpecifyUnit, edtOthrSpecifyUnit, edtCustomRateUnit, edtOthrSpecifyOperation,
            edtTotalServiceArea, edtTimeUsedWorking, edtEffectiveArea;
    private TextView tvLat, tvLong, tvTypeOfMill, tvBrand, tvOwnership, tvTypeOfTubewells, tvMachineAvailability, tvConditionPresent, tvLocation, tvModel, tvProvRent, tvMunRent, tvBrgyRent,
            tvCustomRate, tvCustomUnit, tvMachineUnused, tvCapacity, tvAveYield, tvNumLoads, tvRate, tvPurchGrantDono, tvAgency, tvAcc, tvPlowingRent, tvCustomUnitOther, tvHarrowingRent,
            tvFurrowingRent, tvOtherRent, tvAveFuelConsPlow, tvAveFuelConsHarr, tvAveFuelConsFurr, tvYearInoperable, tvPrevious, tvPrevResp, tvNewlyPlantedArea, tvRatoonArea, tvTotalServiceArea,
            tvNewlyPlantedAreaInfo, tvRatoonAreaInfo, tvPlowingRentInfo, tvHarrowingRentInfo, tvFurrowingRentInfo, tvMachineAvailabilityInfo, tvRatedPower, tvAveOpHours, tvAveOpDays,
            tvNameOfOwnerOrOrganization, tvConditionAcquired, tvRatedPowerInfo, tvAveOpHoursInfo, tvAveOpDaysInfo, tvNameOfOwnerOrOrganizationInfo, tvConditionAcquiredInfo, tvTimeUsedWorking,
            tvEffectiveArea;
    private Spinner spinMachineType, spinTypeOfMill, spinRental, spinCustomUnit, spinAvailability, spinConditionPresent, spinRespName, spinTypeofTubeWells, spinOwnership,
            spinPurchGrantDono, spinAgency, spinBrand, spinModel, spinYearAcquired, spinConditionAcquired, spinLocationOfMachine, spinPlowingRentUnit, spinHarrowingRentUnit,
            spinFurrowingRentUnit, spinOtherRentUnit, spinYearInoperable
            //spinRentProv, spinRentMun, spinProvince, spinMunicipality
            ;
    private RadioGroup rgLoanCash;
    private RadioButton rbLoan, rbCash;
    private String encodedImage, listOfProblems, dateToStr, resCode, resName, listOfBrgyRent, munRent, provRent, loanCash, purchGrantDono;
    private Double totalServiceArea;
    private MultiSpinnerSearch multspinProblemsUnused, multSpinBrgyRent, multSpinProvRent, multSpinMunRent
            //multspinRentBrgy
            ;
    private SingleSpinnerSearch singleSpinnerSearch, singlespinProvince, singlespinMunicipality, singlespinBarangay
            //singlespinBarangay
            ;
    private Intent intentFromDb;
    private boolean machineTypeInfoCheck, machineTypeSpecsCheck, respCheck, qrCheck, ownershipCheck, yearSelectCheck, conditionAcquiredCheck, hasOtherProblems, loanCashCheck,
            purchGrantDonoCheck, agencyCheck, rentSelectCheck, rentCustomCheck, rentAvailCheck, conditionPresentCheck, otherProblemsCheck, locationMachineCheck, locationGarageCheck,
            machineTypeInfoBrandCheck, machineTypeInfoModelCheck, typeMillCheck, typeTubewellsCheck, spinYearInoperableCheck;
    private ConstraintLayout.LayoutParams paramstvBrand, paramstvOwnership, paramsedtCapacity, paramsedtNumLoads, paramstvConditionPresent, paramstvLocation, paramstvModel, paramsedtRatedPower,
            paramsedtAveYield, paramsedtRate, paramstvTypeTubewells, paramsedtNameOfOwnerOrg, paramstvMachineAvailability, paramstvMachineUnused, paramstvAveOpHours;
    private String machineSelected;
    private MachineListViewModel machineListViewModel;
    private ProfileViewModel profileViewModel;
    private ArrayList<String> profilesListAfterSet = new ArrayList<>();
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int LOCATION_REQUEST_CODE = 127;
    public static final int WRITE_PERM_CODE = 279;
    public static final String EXTRA_MACHINE_TYPE = "ADDMACHINE_EXTRA_MACHINE_TYPE";
    public static final String EXTRA_TYPE_TUBEWELLS = "ADDMACHINE_EXTRA_TYPE_TUBEWELLS";
    public static final String EXTRA_TYPE_MILL = "ADDMACHINE_EXTRA_TYPE_MILL";
    public static final String EXTRA_MACHINE_QRCODE = "ADDMACHINE_EXTRA_MACHINE_QRCODE";
    public static final String EXTRA_LAT = "ADDMACHINE_EXTRA_LAT";
    public static final String EXTRA_LONG = "ADDMACHINE_EXTRA_LONG";
    public static final String EXTRA_ID = "ADDMACHINE_EXTRA_ID";
    public static final String EXTRA_DATE_TIME = "ADDMACHINE_EXTRA_DATE_TIME";
    public static final String EXTRA_BRAND = "ADDMACHINE_EXTRA_BRAND";
    public static final String EXTRA_BRAND_SPECIFY = "ADDMACHINE_EXTRA_BRAND_SPECIFY";
    public static final String EXTRA_MODEL = "ADDMACHINE_EXTRA_MODEL";
    public static final String EXTRA_MODEL_SPECIFY = "ADDMACHINE_EXTRA_MODEL_SPECIFY";
    public static final String EXTRA_RATED_POWER = "ADDMACHINE_EXTRA_RATED_POWER";
    public static final String EXTRA_SERVICE_AREA = "ADDMACHINE_EXTRA_SERVICE_AREA";
    public static final String EXTRA_NEWLY_PLANTED_AREA = "ADDMACHINE_EXTRA_NEWLY_PLANTED_AREA";
    public static final String EXTRA_RATOONED_AREA = "ADDMACHINE_EXTRA_RATOONED_AREA";
    public static final String EXTRA_AVE_OP_HOURS = "ADDMACHINE_EXTRA_AVE_OP_HOURS";
    public static final String EXTRA_AVE_OP_DAYS = "ADDMACHINE_EXTRA_AVE_OP_DAYS";
    public static final String EXTRA_EFFECTIVE_AREA_HARVEST = "ADDMACHINE_EXTRA_EFFECTIVE_AREA_HARVEST";
    public static final String EXTRA_TIME_USED_WORKING = "ADDMACHINE_EXTRA_TIME_USED_WORKING";
    public static final String EXTRA_CAPACITY = "ADDMACHINE_EXTRA_CAPACITY";
    public static final String EXTRA_AVE_YIELD = "ADDMACHINE_EXTRA_AVE_YIELD";
    public static final String EXTRA_NUM_LOADS = "ADDMACHINE_EXTRA_NUM_LOADS";
    public static final String EXTRA_RATE = "ADDMACHINE_EXTRA_RATE";
    public static final String EXTRA_OWNERSHIP = "ADDMACHINE_EXTRA_OWNERSHIP";
    public static final String EXTRA_PURCH_GRANT_DONO = "ADDMACHINE_EXTRA_PURCH_GRANT_DONO";
    public static final String EXTRA_AGENCY = "ADDMACHINE_EXTRA_AGENCY";
    public static final String EXTRA_AGENCY_SPECIFY = "ADDMACHINE_EXTRA_AGENCY_SPECIFY";
    public static final String EXTRA_NAME_OWNER = "ADDMACHINE_EXTRA_NAME_OWNER";
    public static final String EXTRA_YEAR_ACQUIRED = "ADDMACHINE_EXTRA_YEAR_ACQUIRED";
    public static final String EXTRA_CONDITION_ACQUIRED = "ADDMACHINE_EXTRA_CONDITION_ACQUIRED";
    public static final String EXTRA_RENTAL = "ADDMACHINE_EXTRA_RENTAL";
    //    public static final String EXTRA_CUSTOM_RATE = "ADDMACHINE_EXTRA_CUSTOM_RATE";
    //    public static final String EXTRA_CUSTOM_UNIT = "ADDMACHINE_EXTRA_CUSTOM_UNIT";
    public static final String EXTRA_MAIN_RENT_RATE = "ADDMACHINE_EXTRA_MAIN_RATE";
    public static final String EXTRA_MAIN_RENT_UNIT = "ADDMACHINE_EXTRA_MAIN_UNIT";
    public static final String EXTRA_MAIN_RENT_UNIT_SPECIFY = "ADDMACHINE_EXTRA_MAIN_RATE_SPECIFY";
    public static final String EXTRA_PLOW_RENT_RATE = "ADDMACHINE_EXTRA_PLOW_RATE";
    public static final String EXTRA_PLOW_RENT_UNIT = "ADDMACHINE_EXTRA_PLOW_UNIT";
    public static final String EXTRA_PLOW_RENT_UNIT_SPECIFY = "ADDMACHINE_EXTRA_PLOW_RATE_SPECIFY";
    public static final String EXTRA_HARR_RENT_RATE = "ADDMACHINE_EXTRA_HARR_RATE";
    public static final String EXTRA_HARR_RENT_UNIT = "ADDMACHINE_EXTRA_HARR_UNIT";
    public static final String EXTRA_HARR_RENT_UNIT_SPECIFY = "ADDMACHINE_EXTRA_HARR_RATE_SPECIFY";
    public static final String EXTRA_FURR_RENT_RATE = "ADDMACHINE_EXTRA_FURR_RATE";
    public static final String EXTRA_FURR_RENT_UNIT = "ADDMACHINE_EXTRA_FURR_UNIT";
    public static final String EXTRA_FURR_RENT_UNIT_SPECIFY = "ADDMACHINE_EXTRA_FURR_RATE_SPECIFY";
    public static final String EXTRA_OTHR_RENT_OPERATION = "ADDMACHINE_EXTRA_OTHR_OPERATION";
    public static final String EXTRA_OTHR_RENT_RATE = "ADDMACHINE_EXTRA_OTHR_RATE";
    public static final String EXTRA_OTHR_RENT_UNIT = "ADDMACHINE_EXTRA_OTHR_UNIT";
    public static final String EXTRA_OTHR_RENT_UNIT_SPECIFY = "ADDMACHINE_EXTRA_OTHR_RATE_SPECIFY";
    //    public static final String EXTRA_CUSTOM_UNIT_SPECIFY = "ADDMACHINE_EXTRA_CUSTOM_UNIT_SPECIFY";
    public static final String EXTRA_AVE_FUEL_PLOW = "ADDMACHINE_EXTRA_AVE_FUEL_PLOW";
    public static final String EXTRA_AVE_FUEL_HARR = "ADDMACHINE_EXTRA_AVE_FUEL_HARR";
    public static final String EXTRA_AVE_FUEL_FURR = "ADDMACHINE_EXTRA_AVE_FUEL_FURR";
    public static final String EXTRA_AVAILABILITY = "ADDMACHINE_EXTRA_AVAILABILITY";
    public static final String EXTRA_RENT_PROV = "ADDMACHINE_EXTRA_RENT_PROV";
    public static final String EXTRA_RENT_MUN = "ADDMACHINE_EXTRA_RENT_MUN";
    public static final String EXTRA_RENT_BRGY = "ADDMACHINE_EXTRA_RENT_BRGY";
    public static final String EXTRA_CONDITION = "ADDMACHINE_EXTRA_CONDITION";
    public static final String EXTRA_PROBLEMS = "ADDMACHINE_EXTRA_PROBLEMS";
    public static final String EXTRA_PROBLEMS_SPECIFY = "ADDMACHINE_EXTRA_PROBLEMS_SPECIFY";
    public static final String EXTRA_YEAR_INOPERABLE = "ADDMACHINE_EXTRA_YEAR_INOPERABLE";
    public static final String EXTRA_LOCATION = "ADDMACHINE_EXTRA_LOCATION";
    public static final String EXTRA_PROV = "ADDMACHINE_EXTRA_PROV";
    public static final String EXTRA_MUN = "ADDMACHINE_EXTRA_MUN";
    public static final String EXTRA_BRGY = "ADDMACHINE_EXTRA_BRGY";
    public static final String EXTRA_ACC = "ADDMACHINE_EXTRA_ACCURACY";
    public static final String EXTRA_RES_CODE = "ADDMACHINE_EXTRA_RES_CODE";
    public static final String EXTRA_RES_NAME = "ADDMACHINE_EXTRA_RES_NAME";
    static Uri capturedImageUri = null;
    float dpWidth;
    ArrayList<Respondent> respondentArrayList = new ArrayList<Respondent>();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_machine_activity);

        askCameraPermission();
        requestExternalPermission();

        if (!checkExternalPermission()) {
            // Code for above or equal 23 API Oriented Device
            // Your Permission granted already .Do next code
            requestExternalPermission();
        }  // Code for permission


        setMargins();
        initViews();
        hide();

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;


        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);

        setInputFilters();


        Log.d("DEBADDMLAYOUT", "Launching set params");
        initAllLayoutParameters();

        initVariables();


        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getAllProfiles().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(List<Profile> profiles) {
                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add("Please Select...");
                for (int i = 0; i < profiles.size(); i++) {
                    stringArrayList.add((i + 1) + " | " + profiles.get(i).getName_respondent());
                    respondentArrayList.add(new Respondent(profiles.get(i).getName_respondent(), profiles.get(i).getResCode()));
//                    Log.d("XRES LOOP1", respondentArrayList.get(i).name + " " + respondentArrayList.get(i).code);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stringArrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinRespName.setAdapter(adapter);
            }
        });

        ArrayList<String> years = new ArrayList<String>();
        years.add("Please Select...");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYearAcquired.setAdapter(adapter);
        spinYearInoperable.setAdapter(adapter);

        multspinProblemsUnused.setHintText("Select Problems...");

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                values.put(MediaStore.Images.Media.TITLE, imageFileName);
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                capturedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                //Camera intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

            }
        });

        gallery.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);

        });

        btnScanQR.setOnClickListener(view -> {
            Intent intent = new Intent(AddMachineActivity.this, ScanBarcodeActivity.class);
            startActivityForResult(intent, 0);
            Toast.makeText(AddMachineActivity.this, "Scanning QR", Toast.LENGTH_SHORT).show();
        });

        getLocation.setOnClickListener(view -> {
            Toast.makeText(AddMachineActivity.this, "Map Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), LocationMapsActivity.class);
            startActivityForResult(intent, LOCATION_REQUEST_CODE);
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
//        multspinRentBrgy.setHintText("Select Barangays");
//        singlespinBarangay.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank))), new SingleSpinnerListener() {
//            @Override
//            public void onItemsSelected(KeyPairBoolData selectedItem) {
//
//            }
//
//            @Override
//            public void onClear() {
//
//            }
//        });
//        multspinRentBrgy.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank))), new MultiSpinnerListener() {
//            @Override
//            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
//                for (int i = 0; i < selectedItems.size(); i++) {
//                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
//                }
//            }
//        });
//        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                provMunSort(i);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        spinBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandSelect(spinBrand.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelSelect(spinModel.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinConditionPresent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                problemsUnused(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinAvailability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                availabilitySelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinCustomUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customUnitSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinPlowingRentUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customUnitSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinHarrowingRentUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customUnitSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinFurrowingRentUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customUnitSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinOtherRentUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customUnitSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinRental.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rentSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinOwnership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ownershipSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ownershipSelect(0);
            }
        });

        spinPurchGrantDono.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                purchGrantDonoSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                otherAgency(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinMachineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                machineSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<KeyPairBoolData> allProvinces = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.provinces)));
        singlespinProvince.setItems(allProvinces, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

                Log.d("Single Prov", selectedItem.getName());
                if (selectedItem.getName().contains("BATANGAS")) {
                    sortBatangasSingle();
                } else {
                    sortUnavailableSingle();
                }
            }

            @Override
            public void onClear() {

            }
        });

        List<KeyPairBoolData> allMunicipalities = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.municipalities)));

        List<KeyPairBoolData> allBarangays = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.barangays)));

        multSpinProvRent.setSearchHint("Please Select...");

        multSpinMunRent.setSearchHint("Please Select...");
        multSpinMunRent.setShowSelectAllButton(true);

        multSpinBrgyRent.setHintText("Please Select...");
        multSpinBrgyRent.setShowSelectAllButton(true);


        multSpinProvRent.setItems(allProvinces, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = new String();
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = selectedItems.get(i).getName() + "; " + pos;
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    Log.d("MULT SPIN", pos);
                }
                provRent = pos;
                if (provRent.contains("BATANGAS")) {
                    sortAdaptabilityBatangas();
                } else {
                    sortUnavailable();
                }
            }

        });

        rbCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanCashSelect();
            }
        });
        rbLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanCashSelect();
            }
        });

        edtTimeUsedWorking.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getApplicationContext(), spinMachineType.getSelectedItem().toString() + " AREA: " + edtServiceArea.getText().toString() + " HOURS: " + edtAveOpHours.getText().toString(), Toast.LENGTH_SHORT).show();
                if (spinMachineType.getSelectedItem().toString().contains("HARVESTER")) {
                    Log.d("DEBHARONCH", "Inside onchanged ave op hours");
                    edtCapacity.setText(getFieldCapacity(edtEffectiveArea.getText().toString(), edtTimeUsedWorking.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvRatedPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang hP ng makinarya", v);
            }
        });
        tvRatedPowerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang hP ng makinarya", v);
            }
        });

        tvNewlyPlantedArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ektarya para sa bagong pananim na tubo", v);
            }
        });
        tvNewlyPlantedAreaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ektarya para sa bagong pananim na tubo", v);
            }
        });

        tvRatoonArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ektarya para sa niratoon na tubo", v);
            }
        });
        tvRatoonAreaInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ektarya para sa niratoon na tubo", v);
            }
        });

        tvAveOpHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Karaniwang oras ng operasyon ng makinarya kada araw", v);
            }
        });

        tvAveOpHoursInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Karaniwang oras ng operasyon ng makinarya kada araw", v);
            }
        });

        tvAveOpDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Sumatutal na araw na ginagamit ang makinarya sa lahat ng operasyon sa bukid", v);
            }
        });

        tvAveOpDaysInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Sumatutal na araw na ginagamit ang makinarya sa lahat ng operasyon sa bukid", v);
            }
        });

        tvNameOfOwnerOrOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang pangalan ng benepisyaryong organisasyon o pribadong indibidwal", v);
            }
        });

        tvNameOfOwnerOrOrganizationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang pangalan ng benepisyaryong organisasyon o pribadong indibidwal", v);
            }
        });

        tvConditionAcquired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Kondisyon ng makinarya noon ito ay iyong nakuha", v);
            }
        });

        tvConditionAcquiredInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Kondisyon ng makinarya noon ito ay iyong nakuha", v);
            }
        });

        tvPlowingRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang halaga ng renta sa pagpapaararo", v);
            }
        });

        tvPlowingRentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang halaga ng renta sa pagpapaararo", v);
            }
        });

        tvHarrowingRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang halaga ng renta sa pagrarastilyo", v);
            }
        });

        tvHarrowingRentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang halaga ng renta sa pagrarastilyo", v);
            }
        });

        tvFurrowingRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang halaga ng renta sa pagtutudling", v);
            }
        });

        tvFurrowingRentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay ang halaga ng renta sa pagtutudling", v);
            }
        });

        tvMachineAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay kung ipinaparenta ang makinarya sa ibang barangay", v);
            }
        });
        tvMachineAvailabilityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilagay kung ipinaparenta ang makinarya sa ibang barangay", v);
            }
        });

        setAllLayoutParameters();

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {

            editItemSelected(intent);

        } else {
            setTitle("Adding Machine");
        }

    }

    private void toolTipShow(String toShow, View v) {
        new SimpleTooltip.Builder(getApplicationContext())
                .anchorView(v)
                .text(toShow)
                .gravity(Gravity.TOP)
                .backgroundColor(getResources().getColor(R.color.white))
                .arrowColor(getResources().getColor(R.color.white))
                .maxWidth(getResources().getDimension(R.dimen.maxwidth))
                .build()
                .show();
    }

    private void sortUnavailableSingle() {
        List<KeyPairBoolData> allMunicipalities = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.municipalities)));
        List<KeyPairBoolData> allBarangays = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.barangays)));

        singlespinMunicipality.setItems(allMunicipalities, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());
            }

            @Override
            public void onClear() {
            }
        });

        singlespinBarangay.setItems(allBarangays, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());
            }

            @Override
            public void onClear() {

            }
        });
    }

    private void sortBatangasSingle() {
        List<KeyPairBoolData> allMunBatangas = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.batangas_municipalities)));


        List<String> barangaysStringList = new ArrayList<>();

        singlespinMunicipality.setItems(allMunBatangas, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());

                if (selectedItem.getName().contains("AGONCILLO")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_agoncillo_brgy)));
                }
                if (selectedItem.getName().contains("ALITAGTAG")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_alitagtag_brgy)));
                }
                if (selectedItem.getName().contains("BALAYAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_balayan_brgy)));
                }
                if (selectedItem.getName().contains("BALETE")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_balete_brgy)));
                }
                if (selectedItem.getName().contains("BATANGAS CITY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_batangascity_brgy)));
                }
                if (selectedItem.getName().contains("BAUAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_bauan_brgy)));
                }
                if (selectedItem.getName().contains("CALACA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_calaca_brgy)));
                }
                if (selectedItem.getName().contains("CALATAGAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_calatagan_brgy)));
                }
                if (selectedItem.getName().contains("CUENCA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_cuenca_brgy)));
                }
                if (selectedItem.getName().contains("IBAAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_ibaan_brgy)));
                }
                if (selectedItem.getName().contains("LAUREL")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_laurel_brgy)));
                }
                if (selectedItem.getName().contains("LEMERY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lemery_brgy)));
                }
                if (selectedItem.getName().contains("LIAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lian_brgy)));
                }
                if (selectedItem.getName().contains("LIPA CITY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lipacity_brgy)));
                }
                if (selectedItem.getName().contains("LOBO")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lobo_brgy)));
                }
                if (selectedItem.getName().contains("MABINI")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_mabini_brgy)));
                }
                if (selectedItem.getName().contains("MALVAR")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_malvar_brgy)));
                }
                if (selectedItem.getName().contains("MATAASNAKAHOY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_mataasnakahoy_brgy)));
                }
                if (selectedItem.getName().contains("NASUGBU")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_nasugbu_brgy)));
                }
                if (selectedItem.getName().contains("PADRE GARCIA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_padregarcia_brgy)));
                }
                if (selectedItem.getName().contains("ROSARIO")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_rosario_brgy)));
                }
                if (selectedItem.getName().contains("SAN JOSE")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanjose_brgy)));
                }
                if (selectedItem.getName().contains("SAN JUAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanjuan_brgy)));
                }
                if (selectedItem.getName().contains("SAN LUIS")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanluis_brgy)));
                }
                if (selectedItem.getName().contains("SAN NICOLAS")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sannicolas_brgy)));
                }
                if (selectedItem.getName().contains("SAN PASCUAL")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanpascual_brgy)));
                }
                if (selectedItem.getName().contains("STA. TERESITA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_staterisita_brgy)));
                }
                if (selectedItem.getName().contains("STO. TOMAS")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_stotomas_brgy)));
                }
                if (selectedItem.getName().contains("TAAL")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_taal_brgy)));
                }
                if (selectedItem.getName().contains("TALISAY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_talisay_brgy)));
                }
                if (selectedItem.getName().contains("TANAUAN CITY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tanauancity_brgy)));
                }
                if (selectedItem.getName().contains("TAYSAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_taysan_brgy)));
                }
                if (selectedItem.getName().contains("TINGLOY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tingloy_brgy)));
                }
                if (selectedItem.getName().contains("TUY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tuy_brgy)));
                }

                singlespinBarangay.setItems(pairingOfList(barangaysStringList), new SingleSpinnerListener() {
                    @Override
                    public void onItemsSelected(KeyPairBoolData selectedItem) {
                        Log.d("Single Brgy", selectedItem.getName());
                    }

                    @Override
                    public void onClear() {

                    }
                });
            }

            @Override
            public void onClear() {
            }
        });
    }

    private void sortUnavailable() {
        List<KeyPairBoolData> allMunicipalities = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.municipalities)));
        List<KeyPairBoolData> allBarangays = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.barangays)));

        multSpinMunRent.setItems(allMunicipalities, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = new String();
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = selectedItems.get(i).getName() + "; " + pos;
                    Log.d("ADMMULTSPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    Log.d("ADMMULTSPIN", pos);
                }
                munRent = pos;
            }

        });
        multSpinBrgyRent.setItems(allBarangays, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = new String();
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = selectedItems.get(i).getName() + "; " + pos;
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    Log.d("MULT SPIN", pos);
                }
                listOfBrgyRent = pos;
            }
        });
    }

    private void sortAdaptabilityBatangas() {

        List<KeyPairBoolData> allBarangays = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.barangays)));

        List<String> barangaysStringList = new ArrayList<>();

//        stringList.addAll(Arrays.asList(getResources().getStringArray(R.array.barangays)));
//        stringList.add(getResources().getStringArray(R.array.batangas_agoncillo_brgy));
//        selectedProb = pairingOfList(stringList);

        multSpinMunRent.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.batangas_municipalities))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = new String();
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = selectedItems.get(i).getName() + "; " + pos;
                    Log.d("ADMMULTSPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                    Log.d("ADMMULTSPIN", pos);
                }
                munRent = pos;
                if (munRent.contains("AGONCILLO")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_agoncillo_brgy)));
                }
                if (munRent.contains("ALITAGTAG")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_alitagtag_brgy)));
                }
                if (munRent.contains("BALAYAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_balayan_brgy)));
                }
                if (munRent.contains("BALETE")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_balete_brgy)));
                }
                if (munRent.contains("BATANGAS CITY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_batangascity_brgy)));
                }
                if (munRent.contains("BAUAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_bauan_brgy)));
                }
                if (munRent.contains("CALACA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_calaca_brgy)));
                }
                if (munRent.contains("CALATAGAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_calatagan_brgy)));
                }
                if (munRent.contains("CUENCA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_cuenca_brgy)));
                }
                if (munRent.contains("IBAAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_ibaan_brgy)));
                }
                if (munRent.contains("LAUREL")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_laurel_brgy)));
                }
                if (munRent.contains("LEMERY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lemery_brgy)));
                }
                if (munRent.contains("LIAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lian_brgy)));
                }
                if (munRent.contains("LIPA CITY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lipacity_brgy)));
                }
                if (munRent.contains("LOBO")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lobo_brgy)));
                }
                if (munRent.contains("MABINI")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_mabini_brgy)));
                }
                if (munRent.contains("MALVAR")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_malvar_brgy)));
                }
                if (munRent.contains("MATAASNAKAHOY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_mataasnakahoy_brgy)));
                }
                if (munRent.contains("NASUGBU")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_nasugbu_brgy)));
                }
                if (munRent.contains("PADRE GARCIA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_padregarcia_brgy)));
                }
                if (munRent.contains("ROSARIO")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_rosario_brgy)));
                }
                if (munRent.contains("SAN JOSE")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanjose_brgy)));
                }
                if (munRent.contains("SAN JUAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanjuan_brgy)));
                }
                if (munRent.contains("SAN LUIS")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanluis_brgy)));
                }
                if (munRent.contains("SAN NICOLAS")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sannicolas_brgy)));
                }
                if (munRent.contains("SAN PASCUAL")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanpascual_brgy)));
                }
                if (munRent.contains("STA. TERESITA")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_staterisita_brgy)));
                }
                if (munRent.contains("STO. TOMAS")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_stotomas_brgy)));
                }
                if (munRent.contains("TAAL")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_taal_brgy)));
                }
                if (munRent.contains("TALISAY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_talisay_brgy)));
                }
                if (munRent.contains("TANAUAN CITY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tanauancity_brgy)));
                }
                if (munRent.contains("TAYSAN")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_taysan_brgy)));
                }
                if (munRent.contains("TINGLOY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tingloy_brgy)));
                }
                if (munRent.contains("TUY")) {
                    barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tuy_brgy)));
                }

                multSpinBrgyRent.setItems(pairingOfList(barangaysStringList), new MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                        String pos = new String();
                        for (int i = 0; i < selectedItems.size(); i++) {
                            pos = selectedItems.get(i).getName() + "; " + pos;
                            Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                            Log.d("MULT SPIN", pos);
                        }
                        listOfBrgyRent = pos;
                    }
                });
            }
        });
    }

    private void initVariables() {
        tvLong.setText("Not yet Acquired");
        tvLat.setText("Not yet Acquired");
        tvAcc.setText("Not yet Acquired");
        listOfProblems = "";
        provRent = "";
        munRent = "";
        listOfBrgyRent = "";
        loanCash = "";
        respondentArrayList = new ArrayList<>();
    }

    private void initAllLayoutParameters() {
        paramstvBrand = (ConstraintLayout.LayoutParams) tvBrand.getLayoutParams();
        paramstvMachineAvailability = (ConstraintLayout.LayoutParams) tvMachineAvailability.getLayoutParams();
        paramsedtNameOfOwnerOrg = (ConstraintLayout.LayoutParams) edtNameOfOwnerOrg.getLayoutParams();
        paramstvConditionPresent = (ConstraintLayout.LayoutParams) tvConditionPresent.getLayoutParams();
        paramstvLocation = (ConstraintLayout.LayoutParams) tvLocation.getLayoutParams();
        paramstvModel = (ConstraintLayout.LayoutParams) tvModel.getLayoutParams();
        paramsedtRatedPower = (ConstraintLayout.LayoutParams) edtRatedPower.getLayoutParams();
        paramstvMachineUnused = (ConstraintLayout.LayoutParams) tvMachineUnused.getLayoutParams();
        paramstvAveOpHours = (ConstraintLayout.LayoutParams) tvAveOpHours.getLayoutParams();
//        paramstvMachineAvailability.topToBottom = R.id.spinRental;
//        paramstvMachineAvailability.topMargin = bigMargin;
//        tvMachineAvailability.setLayoutParams(paramstvMachineAvailability);
//        paramstvConditionPresent.topToBottom = R.id.spinRental;
//        paramstvConditionPresent.topMargin = bigMargin;
//        tvConditionPresent.setLayoutParams(paramstvConditionPresent);
    }

    private void setAllLayoutParameters() {
        parameterBrand();
    }

    private void parameterBrand() {
        paramstvBrand.topToBottom = R.id.edtQRCode;
        paramstvBrand.topMargin = bigMargin;
        tvBrand.setLayoutParams(paramstvBrand);
        parameterAveOpHours();

    }

    private void parameterAveOpHours() {
        paramstvAveOpHours.topToBottom = R.id.edtTotalServiceAreaMachine;
        paramstvBrand.topMargin = bigMargin;
        tvAveOpHours.setLayoutParams(paramstvAveOpHours);
        parameterNameOwnerOrg();

    }

    private void parameterNameOwnerOrg() {
        paramsedtNameOfOwnerOrg.topToBottom = R.id.spinOwnership;
        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
        parameterConditionPresent();
    }

    private void parameterConditionPresent() {
        Log.d("DEBADDMLAYOUT", "Setting paramstvCondition");
        paramstvConditionPresent.topToBottom = R.id.spinRental;
        paramstvConditionPresent.topMargin = bigMargin;
        tvConditionPresent.setLayoutParams(paramstvConditionPresent);
        Log.d("DEBADDMLAYOUT", "POST" + paramstvConditionPresent.topToBottom + " to " + R.id.spinRental);
    }

    private void setInputFilters() {
//        edtAveOpHours.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
        edtRatedPower.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtNewlyPlantedArea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtRatoonArea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtAveOpHours.setFilters(new InputFilter[]{new MinMaxFilter("0", "24")});
        edtAveOpDays.setFilters(new InputFilter[]{new MinMaxFilter("1", "365")});

        edtAveFuelConsPlow.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtAveFuelConsHarr.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtAveFuelConsFurr.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

//        edittext.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "1000"), new DecimalDigitsInputFilter(3,2)});
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void setMargins() {
        smallMargin = (int) pxFromDp(this, 8);
        bigMargin = (int) pxFromDp(this, 40);
        biggerMargin = (int) pxFromDp(this, 45);
    }

    public List<KeyPairBoolData> pairingOfList(List<String> stringList) {
        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(stringList.get(i));
            listArray1.add(h);
        }
        return listArray1;
    }

    public List<KeyPairBoolData> pairBoolDataSelect(List<String> stringList, String compare) {

        final List<KeyPairBoolData> listArray1 = new ArrayList<>();

        for (int i = 0; i < stringList.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(stringList.get(i));
            if (stringList.get(i).equals(compare)) {
                h.setSelected(true);
                Log.d("Selected item", "Item is: " + compare + " = " + stringList.get(i));
            }
            listArray1.add(h);
        }
        return listArray1;
    }

    public List<KeyPairBoolData> pairBoolDataSelectMulti(List<String> stringList, String compare, int valueOfString) {

        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(stringList.get(i));
            if (compare.contains(stringList.get(i))) {
                h.setSelected(true);
                Log.d("Selected item", "Item is: " + compare + " = " + stringList.get(i));
                if (valueOfString == 1) {
                    listOfProblems = listOfProblems + stringList.get(i);
                }
            }
            listArray1.add(h);
        }
        return listArray1;
    }

    public List<KeyPairBoolData> pairBoolDataSelectMultiDoubleLoop(List<String> stringList, String compare) {

        List<String> comparisonList = Arrays.asList(compare.split(","));
        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(stringList.get(i));
            for (int j = 0; j < comparisonList.size(); j++) {
                if (comparisonList.get(j).equals(stringList.get(i))) {
                    h.setSelected(true);
                    Log.d("Selected item", "Item is: " + compare + " = " + stringList.get(i));
                }
            }
            listArray1.add(h);
        }
        return listArray1;
    }

    private void brandSelect(String position) {
        String pos = position;
        String typeMachine = spinMachineType.getSelectedItem().toString();
        if ("OTHERS".equals(pos)) {
            edtOtherBrand.setVisibility(View.VISIBLE);
            paramstvModel.topToBottom = R.id.edtOtherBrand;
            paramstvModel.topToBottom = R.id.edtOtherBrand;
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield)));
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinModel.setAdapter(dataAdapter);
        } else {
            edtOtherBrand.setVisibility(View.INVISIBLE);
            paramstvModel.topToBottom = R.id.spinBrand;
            switch (typeMachine) {
                case "2 WHEEL TRACTOR":
                    sortingBrand2WheelTractor(pos);
                    break;
                case "4 WHEEL TRACTOR":
                    sortingBrand4WheelTractor(pos);
                    break;
                case "WATER PUMP":
                    sortingBrandWaterPump(pos);
                    break;

                case "HARVESTER":
                    sortingBrandHarvester(pos);
                    break;
                default:
                    break;
            }
        }
        paramstvModel.topMargin = bigMargin;
        tvModel.setLayoutParams(paramstvModel);


    }

    private void editItemSelected(Intent intent1) {
        int position = -1;

        intentFromDb = intent1;
        tvPrevResp.setVisibility(View.VISIBLE);
        tvPrevious.setVisibility(View.VISIBLE);
        tvPrevResp.setText(intent1.getStringExtra(EXTRA_RES_NAME));

        String stringCompare = intent1.getStringExtra(EXTRA_MACHINE_TYPE);
        ArrayAdapter<CharSequence> adaptercompare = ArrayAdapter.createFromResource(this, R.array.machine_types, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (stringCompare != null) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position MACHINE TYPE", "Position is: " + intent1.getStringExtra(EXTRA_MACHINE_TYPE) + " " + position);
        spinMachineType.setSelection(position);

        edtQRCode.setText(intent1.getStringExtra(EXTRA_MACHINE_QRCODE));

        stringCompare = intent1.getStringExtra(EXTRA_TYPE_TUBEWELLS);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.type_of_tubewells, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare.trim())) {
            position = adaptercompare.getPosition(stringCompare);
        } else {
            position = 0;
        }
        Log.d("Position TUBEWELL", "Position is: " + intent1.getStringExtra(EXTRA_TYPE_TUBEWELLS) + " " + position);
        spinTypeofTubeWells.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_TYPE_MILL);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.type_of_mill, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare.trim())) {
            position = adaptercompare.getPosition(stringCompare);
        } else {
            position = 0;
        }
        Log.d("Position TYPE MILL", "Position is: " + intent1.getStringExtra(EXTRA_TYPE_MILL) + " " + position);
        spinTypeOfMill.setSelection(position);
//        stringCompare = intent.getStringExtra(EXTRA_BRAND);
//        adaptercompare = ArrayAdapter.createFromResource(this, R.array.all_machine_brands, android.R.layout.simple_spinner_item);
//        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if (!isNullOrEmpty(stringCompare)) {
//
//            spinBrand.getAdapter().getItem();
//            position = adaptercompare.getPosition(stringCompare);
//        }
//
//        for (int i = 0; i < adaptercompare.getCount(); i++) {
//            Log.d("Array:", adaptercompare.getItem(i).toString());
//        }
//
//        Log.d("Position BRAND", "Position is: " + intent.getStringExtra(EXTRA_BRAND) + " " + position);
//        spinBrand.setSelection(position);
        edtOtherBrand.setText(intent1.getStringExtra(EXTRA_BRAND_SPECIFY));
//        stringCompare = intent.getStringExtra(EXTRA_MODEL);
//        adaptercompare = ArrayAdapter.createFromResource(this, R.array.all_models_machines, android.R.layout.simple_spinner_item);
//        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if (!isNullOrEmpty(stringCompare)) {
//            position = adaptercompare.getPosition(stringCompare);
//        }
//        Log.d("Position MODEL", "Position is: " + intent.getStringExtra(EXTRA_MODEL) + " " + position);
//        spinModel.setSelection(position);
        edtOtherModel.setText(intent1.getStringExtra(EXTRA_MODEL_SPECIFY));

        edtRatedPower.setText(intent1.getStringExtra(EXTRA_RATED_POWER));
        if (spinMachineType.getSelectedItem().toString().contains("TRACTOR")) {
            totalServiceArea = Double.parseDouble(intent1.getStringExtra(EXTRA_SERVICE_AREA));
        } else {
            edtTotalServiceArea.setText(intent1.getStringExtra(EXTRA_SERVICE_AREA));
        }

        edtNewlyPlantedArea.setText(intent1.getStringExtra(EXTRA_NEWLY_PLANTED_AREA));
        edtRatoonArea.setText(intent1.getStringExtra(EXTRA_RATOONED_AREA));
        edtAveOpHours.setText(intent1.getStringExtra(EXTRA_AVE_OP_HOURS));
        edtAveOpDays.setText(intent1.getStringExtra(EXTRA_AVE_OP_DAYS));
        edtTimeUsedWorking.setText(intent1.getStringExtra(EXTRA_TIME_USED_WORKING));
        edtEffectiveArea.setText(intent1.getStringExtra(EXTRA_EFFECTIVE_AREA_HARVEST));
        edtCapacity.setText(intent1.getStringExtra(EXTRA_CAPACITY));
        edtAveYield.setText(intent1.getStringExtra(EXTRA_AVE_YIELD));
        edtNumLoads.setText(intent1.getStringExtra(EXTRA_NUM_LOADS));
        edtRate.setText(intent1.getStringExtra(EXTRA_RATE));

        //TODO PURCHGRANTDONO AGENCY

        stringCompare = intent1.getStringExtra(EXTRA_OWNERSHIP);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.ownership_of_machine, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position OWNERSHIP", "Position is: " + intent1.getStringExtra(EXTRA_OWNERSHIP) + " " + position);
        spinOwnership.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_PURCH_GRANT_DONO);
        if (stringCompare.contains("LOAN")) {
            rbLoan.setChecked(true);
            rbCash.setChecked(false);
            loanCashSelect();
//            spinAgency.setVisibility(View.VISIBLE);
//            tvAgency.setVisibility(View.VISIBLE);
//            paramsedtNameOfOwnerOrg.topToBottom = R.id.spinAgency;
//
//            paramsedtNameOfOwnerOrg.topMargin = biggerMargin;
//            edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
        } else if (stringCompare.contains("CASH")) {
            rbCash.setChecked(true);
            rbLoan.setChecked(false);
            loanCashSelect();
//            spinAgency.setVisibility(View.INVISIBLE);
//            paramsedtNameOfOwnerOrg.topToBottom = R.id.rgLoanCash;
//            tvAgency.setVisibility(View.INVISIBLE);
//
//            paramsedtNameOfOwnerOrg.topMargin = biggerMargin;
//            edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
        } else {
            adaptercompare = ArrayAdapter.createFromResource(this, R.array.purchasing_method, android.R.layout.simple_spinner_item);
            adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (!isNullOrEmpty(stringCompare)) {
                position = adaptercompare.getPosition(stringCompare);
            }
            Log.d("Position PURCHGRANTDONO", "Position is: " + intent1.getStringExtra(EXTRA_PURCH_GRANT_DONO) + " " + position);
            spinPurchGrantDono.setSelection(position);
        }


//        stringCompare = intent1.getStringExtra(EXTRA_AGENCY);
//        adaptercompare = ArrayAdapter.createFromResource(this, R.array.agency, android.R.layout.simple_spinner_item);
//        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if (!isNullOrEmpty(stringCompare)) {
//            position = adaptercompare.getPosition(stringCompare);
//        }
//        Log.d("Position AGENCY", "Position is: " + intent1.getStringExtra(EXTRA_AGENCY) + " " + position);
//        spinAgency.setSelection(position);

        edtOtherAgency.setText(intent1.getStringExtra(EXTRA_AGENCY_SPECIFY));

        edtNameOfOwnerOrg.setText(intent1.getStringExtra(EXTRA_NAME_OWNER));

        ArrayList<String> years = new ArrayList<String>();
        years.add("");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        stringCompare = intent1.getStringExtra(EXTRA_YEAR_ACQUIRED);
        String stringCompare2 = intent1.getStringExtra(EXTRA_YEAR_INOPERABLE);
        Integer position2 = 0;
        if (!isNullOrEmpty(stringCompare)) {
            position = adapter.getPosition(stringCompare);
        }
        Log.d("Position YEAR", "Position is: " + intent1.getStringExtra(EXTRA_YEAR_ACQUIRED) + " " + position);
        spinYearAcquired.setSelection(position);
        spinYearInoperable.setSelection(adapter.getPosition(stringCompare2));

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Variable.getListResCode());
//        stringCompare = intent.getStringExtra(EXTRA_RES_CODE);
//        if (!isNullOrEmpty(stringCompare)) {
//            position = adapter.getPosition(stringCompare);
//        }
//        Log.d("Position RESCODE", "Position is: " + stringCompare + " " + position);
//        spinRespName.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_CONDITION_ACQUIRED);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.condition_when_bought, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position CONDITION ACQ", "Position is: " + intent1.getStringExtra(EXTRA_CONDITION_ACQUIRED) + " " + position);
        spinConditionAcquired.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_RENTAL);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.yes_no, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position RENTAL", "Position is: " + intent1.getStringExtra(EXTRA_RENTAL) + " " + position);
        spinRental.setSelection(position);

        edtCustomRate.setText(intent1.getStringExtra(EXTRA_MAIN_RENT_RATE));
        edtPlowingRent.setText(intent1.getStringExtra(EXTRA_PLOW_RENT_RATE));
        edtHarrowingRent.setText(intent1.getStringExtra(EXTRA_HARR_RENT_RATE));
        edtFurrowingRent.setText(intent1.getStringExtra(EXTRA_FURR_RENT_RATE));
        edtOtherRent.setText(intent1.getStringExtra(EXTRA_OTHR_RENT_RATE));

        edtPlowSpecifyUnit.setText(intent1.getStringExtra(EXTRA_PLOW_RENT_UNIT_SPECIFY));
        edtHarrSpecifyUnit.setText(intent1.getStringExtra(EXTRA_HARR_RENT_UNIT_SPECIFY));
        edtFurrSpecifyUnit.setText(intent1.getStringExtra(EXTRA_FURR_RENT_UNIT_SPECIFY));
        edtOthrSpecifyOperation.setText(intent1.getStringExtra(EXTRA_OTHR_RENT_OPERATION));
        edtOthrSpecifyUnit.setText(intent1.getStringExtra(EXTRA_OTHR_RENT_UNIT_SPECIFY));

        edtAveFuelConsPlow.setText(intent1.getStringExtra(EXTRA_AVE_FUEL_PLOW));
        edtAveFuelConsHarr.setText(intent1.getStringExtra(EXTRA_AVE_FUEL_HARR));
        edtAveFuelConsFurr.setText(intent1.getStringExtra(EXTRA_AVE_FUEL_FURR));

        stringCompare = intent1.getStringExtra(EXTRA_MAIN_RENT_UNIT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.custom_rate_units, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        spinCustomUnit.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_PLOW_RENT_UNIT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.custom_rate_units, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        spinPlowingRentUnit.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_HARR_RENT_UNIT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.custom_rate_units, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        spinHarrowingRentUnit.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_FURR_RENT_UNIT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.custom_rate_units, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        spinFurrowingRentUnit.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_OTHR_RENT_UNIT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.custom_rate_units, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        spinOtherRentUnit.setSelection(position);

//        edtCustomRateUnit.setText(intent1.getStringExtra(EXTRA_CUSTOM_UNIT_SPECIFY));

        stringCompare = intent1.getStringExtra(EXTRA_AVAILABILITY);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.rent_availablity, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position AGENCY", "Position is: " + intent1.getStringExtra(EXTRA_AVAILABILITY) + " " + position);
        spinAvailability.setSelection(position);

//        edtRentProv.setText(intent.getStringExtra(EXTRA_RENT_PROV));
//        edtRentMun.setText(intent.getStringExtra(EXTRA_RENT_MUN));
//        edtRentBrgy.setText(intent.getStringExtra(EXTRA_RENT_BRGY));

        stringCompare = intent1.getStringExtra(EXTRA_CONDITION);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.condition, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position CONDITION", "Position is: " + intent1.getStringExtra(EXTRA_CONDITION) + " " + position);
        spinConditionPresent.setSelection(position);

        stringCompare = intent1.getStringExtra(EXTRA_LOCATION);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position LOCATION", "Position is: " + intent1.getStringExtra(EXTRA_LOCATION) + " " + position);
        spinLocationOfMachine.setSelection(position);

//        edtProvince.setText(intent.getStringExtra(EXTRA_PROV));
//        edtMunicipality.setText(intent.getStringExtra(EXTRA_MUN));
//        edtBarangay.setText(intent.getStringExtra(EXTRA_BRGY));

//        adaptercompare = ArrayAdapter.createFromResource(this, R.array.provinces, android.R.layout.simple_spinner_item);
//        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        if (!isNullOrEmpty(stringCompare)) {
//            position = adaptercompare.getPosition(stringCompare);
//        }
//        Log.d("Position LOCATION", "Position is: " + intent1.getStringExtra(EXTRA_PROV) + " " + position)
        stringCompare = intent1.getStringExtra(EXTRA_PROBLEMS);
        List<KeyPairBoolData> problems = null;
        if (spinConditionPresent.getSelectedItemPosition() == 2) {
            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_unused)), stringCompare, 1);
        } else if (spinConditionPresent.getSelectedItemPosition() == 3) {
            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_nonfunctional)), stringCompare, 1);
        } else {
            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_unused)), stringCompare, 1);
        }

        if (stringCompare.contains("OTHERS")) {
            edtOtherProblems.setVisibility(View.VISIBLE);
            paramstvLocation.topToBottom = R.id.edtOtherProblems;
            hasOtherProblems = true;
            edtOtherProblems.setText(intent1.getStringExtra(EXTRA_PROBLEMS_SPECIFY));
        } else {
            hasOtherProblems = false;
            edtOtherProblems.setVisibility(View.INVISIBLE);
        }
        multspinProblemsUnused.setItems(problems, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
            }
        });
        listOfProblems = intent1.getStringExtra(EXTRA_PROBLEMS);

        List<KeyPairBoolData> rentProvinces = null;
        stringCompare = intent1.getStringExtra(EXTRA_RENT_PROV);
        if (stringCompare.contains("BATANGAS")) {
            rentProvinces = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.provinces)), stringCompare, 2);
            multSpinProvRent.setItems(rentProvinces, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                    String pos = new String();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        pos = selectedItems.get(i).getName() + "; " + pos;
                        Log.d("MULTSPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                        Log.d("MULTSPIN", pos);
                    }
                }
            });


            stringCompare = intent1.getStringExtra(EXTRA_RENT_MUN);
            List<KeyPairBoolData> rentMunicipalities = null;
            rentMunicipalities = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.batangas_municipalities)), stringCompare, 3);
            multSpinMunRent.setItems(rentMunicipalities, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                    String pos = new String();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        pos = selectedItems.get(i).getName() + "; " + pos;
                        Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                        Log.d("MULT SPIN", pos);
                    }
                    munRent = pos;
                }
            });

            stringCompare = intent1.getStringExtra(EXTRA_RENT_BRGY);
            List<KeyPairBoolData> rentBarangays = null;
            List<String> barangaysStringList = new ArrayList<>();

            if (stringCompare.contains("AGONCILLO")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_agoncillo_brgy)));
            }
            if (stringCompare.contains("ALITAGTAG")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_alitagtag_brgy)));
            }
            if (stringCompare.contains("BALAYAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_balayan_brgy)));
            }
            if (stringCompare.contains("BALETE")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_balete_brgy)));
            }
            if (stringCompare.contains("BATANGAS CITY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_batangascity_brgy)));
            }
            if (stringCompare.contains("BAUAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_bauan_brgy)));
            }
            if (stringCompare.contains("CALACA")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_calaca_brgy)));
            }
            if (stringCompare.contains("CALATAGAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_calatagan_brgy)));
            }
            if (stringCompare.contains("CUENCA")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_cuenca_brgy)));
            }
            if (stringCompare.contains("IBAAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_ibaan_brgy)));
            }
            if (stringCompare.contains("LAUREL")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_laurel_brgy)));
            }
            if (stringCompare.contains("LEMERY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lemery_brgy)));
            }
            if (stringCompare.contains("LIAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lian_brgy)));
            }
            if (stringCompare.contains("LIPA CITY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lipacity_brgy)));
            }
            if (stringCompare.contains("LOBO")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_lobo_brgy)));
            }
            if (stringCompare.contains("MABINI")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_mabini_brgy)));
            }
            if (stringCompare.contains("MALVAR")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_malvar_brgy)));
            }
            if (stringCompare.contains("MATAASNAKAHOY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_mataasnakahoy_brgy)));
            }
            if (stringCompare.contains("NASUGBU")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_nasugbu_brgy)));
            }
            if (stringCompare.contains("PADRE GARCIA")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_padregarcia_brgy)));
            }
            if (stringCompare.contains("ROSARIO")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_rosario_brgy)));
            }
            if (stringCompare.contains("SAN JOSE")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanjose_brgy)));
            }
            if (stringCompare.contains("SAN JUAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanjuan_brgy)));
            }
            if (stringCompare.contains("SAN LUIS")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanluis_brgy)));
            }
            if (stringCompare.contains("SAN NICOLAS")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sannicolas_brgy)));
            }
            if (stringCompare.contains("SAN PASCUAL")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_sanpascual_brgy)));
            }
            if (stringCompare.contains("STA. TERESITA")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_staterisita_brgy)));
            }
            if (stringCompare.contains("STO. TOMAS")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_stotomas_brgy)));
            }
            if (stringCompare.contains("TAAL")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_taal_brgy)));
            }
            if (stringCompare.contains("TALISAY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_talisay_brgy)));
            }
            if (stringCompare.contains("TANAUAN CITY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tanauancity_brgy)));
            }
            if (stringCompare.contains("TAYSAN")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_taysan_brgy)));
            }
            if (stringCompare.contains("TINGLOY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tingloy_brgy)));
            }
            if (stringCompare.contains("TUY")) {
                barangaysStringList.addAll(Arrays.asList(getResources().getStringArray(R.array.batangas_tuy_brgy)));
            }

            rentBarangays = pairBoolDataSelectMulti(barangaysStringList, stringCompare, 4);
            multSpinBrgyRent.setItems(rentBarangays, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                    String pos = new String();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        pos = selectedItems.get(i).getName() + "; " + pos;
                        Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                        Log.d("MULT SPIN", pos);
                    }
                    listOfBrgyRent = pos;
                }
            });


        } else {

            stringCompare = intent1.getStringExtra(EXTRA_RENT_PROV);
            rentProvinces = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.provinces)), stringCompare, 2);
            multSpinProvRent.setItems(rentProvinces, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                    String pos = new String();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        pos = selectedItems.get(i).getName() + "; " + pos;
                        Log.d("MULTSPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                        Log.d("MULTSPIN", pos);
                    }


                }
            });
            provRent = intent1.getStringExtra(EXTRA_RENT_PROV);

            stringCompare = intent1.getStringExtra(EXTRA_RENT_MUN);
            List<KeyPairBoolData> rentMunicipalities = null;
            rentMunicipalities = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.municipalities)), stringCompare, 3);
            multSpinMunRent.setItems(rentMunicipalities, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                    String pos = new String();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        pos = selectedItems.get(i).getName() + "; " + pos;
                        Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                        Log.d("MULT SPIN", pos);
                    }
                    munRent = pos;
                }
            });
            munRent = intent1.getStringExtra(EXTRA_RENT_MUN);

            stringCompare = intent1.getStringExtra(EXTRA_RENT_BRGY);
            List<KeyPairBoolData> rentBarangays = null;
            rentBarangays = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.barangays)), stringCompare, 4);
            multSpinBrgyRent.setItems(rentBarangays, new MultiSpinnerListener() {
                @Override
                public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                    String pos = new String();
                    for (int i = 0; i < selectedItems.size(); i++) {
                        pos = selectedItems.get(i).getName() + "; " + pos;
                        Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                        Log.d("MULT SPIN", pos);
                    }
                    listOfBrgyRent = pos;
                }
            });

        }
        provRent = intent1.getStringExtra(EXTRA_RENT_PROV);
        munRent = intent1.getStringExtra(EXTRA_RENT_MUN);
        listOfBrgyRent = intent1.getStringExtra(EXTRA_RENT_BRGY);

        stringCompare = intent1.getStringExtra(EXTRA_PROV);
        List<KeyPairBoolData> selectProvinces = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.provinces)), stringCompare);
        singlespinProvince.setItems(selectProvinces, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

            }

            @Override
            public void onClear() {

            }
        });

        stringCompare = intent1.getStringExtra(EXTRA_MUN);
        List<KeyPairBoolData> selectMunicipalities = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.municipalities)), stringCompare);
        singlespinMunicipality.setItems(selectMunicipalities, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

            }

            @Override
            public void onClear() {

            }
        });

        stringCompare = intent1.getStringExtra(EXTRA_BRGY);
        List<KeyPairBoolData> selectBarangays = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.barangays)), stringCompare);
        singlespinBarangay.setItems(selectBarangays, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

            }

            @Override
            public void onClear() {

            }
        });


        tvLat.setText(intent1.getStringExtra(EXTRA_LAT));
        tvLong.setText(intent1.getStringExtra(EXTRA_LONG));

        tvAcc.setText(intent1.getStringExtra(EXTRA_ACC));

        encodedImage = Variable.getStringImage();

        if (encodedImage.contains("Not yet Acquired")) {
            selectedImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon));
        } else {
            byte[] decodedString = Base64.decode(Variable.getStringImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            selectedImage.setImageBitmap(decodedByte);
        }

//        multSpinBrgyRent.setItems(selectBarangays = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.barangays)),stringCompare));


        dateToStr = intent1.getStringExtra(EXTRA_DATE_TIME);
    }

    private void sortingBrand4WheelTractor(String position) {
        List<String> stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.blank));
        switch (position) {
            case "AGRINDO":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.agrindo_models_4wheel_tractor));
                break;
            case "BRANSON":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.branson_models_4wheel_tractor));
                break;
            case "CASE IH":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.cash_ih_models_4wheel_tractor));
                break;
            case "DEUTZ-FAHR":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.deutz_fahr_models_4wheel_tractor));
                break;
            case "EUROTRAC":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.eurotrac_models_4wheel_tactor));
                break;
            case "FARM ROVER":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.farm_rover_models_4wheel_tractor));
                break;
            case "FIAT":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.fiat_models_4wheel_tractor));
                break;
            case "FORD":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.ford_models_4wheel_tractor));
                break;
            case "FORDSON":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.fordson_models_4wheel_tractor));
                break;
            case "FOTON":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.foton_models_4wheel_tractor));
                break;
            case "GLOBAL FARM":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.global_farm_models_4wheel_tractor));
                break;
            case "INTERNATIONAL HARVESTER IH":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.international_harvester_ih_models_4wheel_tractor));
                break;
            case "ISEKI":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.iseki_models_4wheel_tractor));
                break;
            case "ITMCO":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.itmco_models_4wheel_tractor));
                break;
            case "JOHN DEERE":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.john_deere_models_4wheel_tractor));
                break;
            case "KAMOL":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kamol_models_4hwheel_tractors));
                break;
            case "KIOTI":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kioti_models_4wheel_tractors));
                break;
            case "KUBOTA":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kubota_models_4wheel_tractors));
                break;
            case "LANDINI":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.landini_models_4wheel_tractors));
                break;
            case "LEYLAND":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.leyland_models_4wheel_tractors));
                break;
            case "LOVOL":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.lovol_models_4wheel_tractors));
                break;
            case "LS":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.ls_models_4wheel_tractors));
                break;
            case "MAHINDRA":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.mahindra_models_4wheel_tractors));
                break;
            case "MASSEY FERGUSON":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.massey_ferguson_models_4wheel_tractors));
                break;
            case "MC CORMICK":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.mc_cormick_models_4wheel_tractors));
                break;
            case "NEW HOLLAND":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.new_holland_models_4wheel_tractors));
                break;
            case "PREET":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.preet_models_4wheel_tractors));
                break;
            case "SHUHE":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.shuhe_models_4wheel_tractors));
                break;
            case "SONALIKA":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.sonalika_models_4wheel_tractors));
                break;
            case "VALTRA":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.valtra_models_4wheel_tractors));
                break;
            case "WEITAI":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.weitai_models_4wheel_tractors));
                break;
            case "YANMAR":
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.yanmar_models_4wheel_tractors));
                break;
            default:
                stringListModel4WheelTractor = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
                break;

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListModel4WheelTractor);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModel.setAdapter(dataAdapter);

        Intent intent = intentFromDb;
        int pos = 0;
        if (intent != null && intent.hasExtra(EXTRA_ID)) {

            String stringCompare = intent.getStringExtra(EXTRA_MODEL);

            if (!isNullOrEmpty(stringCompare)) {
                pos = dataAdapter.getPosition(stringCompare);
            }
            spinModel.setSelection(pos);
        }

    }

    private void sortingBrand2WheelTractor(String position) {
        List<String> stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.blank));
        int stringArrayId = 0;

        switch (position) {
            case "ACT":
                stringArrayId = R.array.act_models_2wheel_tractors;
                break;
            case "AIMS":
                stringArrayId = R.array.aims_models_2wheel_tractors;
                break;
            case "APT":
                stringArrayId = R.array.apt_models_2wheel_tractors;
                break;
            case "BOWA":
                stringArrayId = R.array.bowa_models_2wheel_tractors;
                break;
            case "BUFFALO":
                stringArrayId = R.array.buffalo_models_2wheel_tractors;
                break;
            case "D.U.A":
                stringArrayId = R.array.dua_models_2wheel_tractors;
                break;
            case "FARM MASTER":
                stringArrayId = R.array.farm_master_models_2wheel_tractors;
                break;
            case "KAPITAN":
                stringArrayId = R.array.kapitan_models_2wheel_tractors;
                break;
            case "KASAMA HARABAS":
                stringArrayId = R.array.kasama_harabas_models_2wheel_tractors;
                break;
            case "KATO":
                stringArrayId = R.array.kato_models_2wheel_tractors;
                break;
            case "KELLY":
                stringArrayId = R.array.kelly_models_2wheel_tractors;
                break;
            case "KULIGLIG":
                stringArrayId = R.array.kuliglig_models_2wheel_tractors;
                break;
            case "LONG FOONG":
                stringArrayId = R.array.long_foong_models_2wheel_tractors;
                break;
            case "MITSUBOMAR":
                stringArrayId = R.array.mitsubomar_models_2wheel_tractors;
                break;
            case "NICHINO":
                stringArrayId = R.array.nichino_models_2wheel_tractors;
                break;
            case "SUMO PLUS":
                stringArrayId = R.array.sumo_plus_models_2wheel_tractors;
                break;
            case "SUPER":
                stringArrayId = R.array.super_models_2wheel_tractors;
                break;
            case "TIBAY KULIGLIG":
                stringArrayId = R.array.tibay_kuliglig_models_2wheel_tractors;
                break;
            case "TRIPLE J":
                stringArrayId = R.array.triple_j_models_2wheel_tractors;
                break;
            case "WEST WIND":
                stringArrayId = R.array.west_wind_models_2wheel_tractors;
                break;
            default:
                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield;
                break;

        }
        stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(stringArrayId));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListModel2WheelTractor);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModel.setAdapter(dataAdapter);

        Intent intent = intentFromDb;
        int pos = 0;
        if (intent != null && intent.hasExtra(EXTRA_ID)) {

            String stringCompare = intent.getStringExtra(EXTRA_MODEL);

            if (!isNullOrEmpty(stringCompare)) {
                pos = dataAdapter.getPosition(stringCompare);
            }
            spinModel.setSelection(pos);
        }
    }

    private void sortingBrandWaterPump(String position) {
        List<String> stringListModelWaterPump;

        switch (position) {

            case "BLUMAX":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.blumax_models_water_pump));
                break;
            case "BRIGGS AND STRATTON":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.briggs_stratton_models_water_pump));
                break;
            case "COMET":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.comet_models_water_pump));
                break;
            case "EAGLE":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.eagle_models_water_pump));
                break;
            case "EXTREME":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.extreme_models_water_pump));
                break;
            case "GAUDI":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.gaudi_models_water_pump));
                break;
            case "GRUNDFOS":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.grundfos_models_water_pump));
                break;
            case "HAKATA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.hakata_models_water_pump));
                break;
            case "HINOKI":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.hinoki_models_water_pump));
                break;
            case "HONDA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.honda_models_water_pump));
                break;
            case "INNOVA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.innova_models_water_pump));
                break;
            case "ISHII":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.ishii_models_water_pump));
                break;
            case "KAIAO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.kaiao_models_water_pump));
                break;
            case "KAITO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.kaito_models_water_pump));
                break;
            case "KAMA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.kama_models_water_pump));
                break;
            case "KATO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.kato_models_water_pump));
                break;
            case "KENBO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.kenbo_models_water_pump));
                break;
            case "KITACO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.kiataco_models_water_pump));
                break;
            case "LAUNTOP":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.launtop_models_water_pump));
                break;
            case "LORENTZ":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.lorentz_models_water_pump));
                break;
            case "MAXIPRO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.maxipro_models_water_pump));
                break;
            case "MIYATA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.miyata_models_water_pump));
                break;
            case "MOTORSTAR":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.motorstar_models_water_pump));
                break;
            case "NITTOO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.nittoo_models_water_pump));
                break;
            case "OKAMA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.okama_models_water_pump));
                break;
            case "PLATINUM":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.platinum_models_water_pump));
                break;
            case "ROBIN":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.robin_models_water_pump));
                break;
            case "SAKAI":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.sakai_models_water_pump));
                break;
            case "SOLAR PUMP":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.solar_pump_models_water_pump));
                break;
            case "SUMO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.sumo_models_water_pump));
                break;
            case "SUPER KASAMA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.super_kasama_models_water_pump));
                break;
            case "TARO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.taro_models_water_pump));
                break;
            case "TAY":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.tay_models_water_pump));
                break;
            case "TSURUMI":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.tsurumi_models_water_pump));
                break;
            case "VIKYNO":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.vikyno_models_water_pump));
                break;
            case "YAMADA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.yamada_models_water_pump));
                break;
            case "YAMAHA":
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.yamaha_models_water_pump));
                break;
            default:
                stringListModelWaterPump = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
                break;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListModelWaterPump);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModel.setAdapter(dataAdapter);

        Intent intent = intentFromDb;
        int pos = 0;
        if (intent != null && intent.hasExtra(EXTRA_ID)) {

            String stringCompare = intent.getStringExtra(EXTRA_MODEL);

            if (!isNullOrEmpty(stringCompare)) {
                pos = dataAdapter.getPosition(stringCompare);
            }
            spinModel.setSelection(pos);
        }
    }

    private void sortingBrandHarvester(String position) {
        List<String> stringListModelHarvester;


        switch (position) {

            case "CAMECO":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.cameco_models_harvester));
                break;
            case "CASE IH":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.case_ih_models_harvester));
                break;
            case "CATERPILLAR":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.caterpillar_models_harvester));
                break;
            case "ESMECH":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.esmech_models_harvester));
                break;
            case "FORD":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.ford_models_harvester));
                break;
            case "JOHN DEERE":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.john_deere_models_harvester));
                break;
            case "KUBOTA":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.kubota_models_harvester));
                break;
            case "SHAKTIMAN":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.shaktiman_models_harvester));
                break;
            case "TOFT":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.toft_models_harvester));
                break;
            case "YANMAR":
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.yanmar_models_harvester));
                break;
            default:
                stringListModelHarvester = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
                break;


        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListModelHarvester);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModel.setAdapter(dataAdapter);
        Intent intent = intentFromDb;
        int pos = 0;
        if (intent != null && intent.hasExtra(EXTRA_ID)) {

            String stringCompare = intent.getStringExtra(EXTRA_MODEL).trim();

            if (!isNullOrEmpty(stringCompare)) {
                pos = dataAdapter.getPosition(stringCompare);
            } else {
                pos = 0;
            }
            spinModel.setSelection(pos);
        }
    }

    private void modelSelect(String position) {
        String pos = position;

        if ("OTHERS".equals(pos)) {
            edtOtherModel.setVisibility(View.VISIBLE);
            paramsedtRatedPower.topToBottom = R.id.edtOtherModel;

//                List<String> stringList = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
//                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringList);
//                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinModel.setAdapter(dataAdapter);
//
//                Intent intent = intentFromDb;
//                int positions = 0;
//                if (intent != null && intent.hasExtra(EXTRA_ID)) {
//
//                    String stringCompare = intent.getStringExtra(EXTRA_MODEL);
//
//                    if (!isNullOrEmpty(stringCompare)) {
//                        positions = dataAdapter.getPosition(stringCompare);
//                    }
//                    spinModel.setSelection(positions);
//                }
        } else {
            edtOtherModel.setVisibility(View.INVISIBLE);
            paramsedtRatedPower.topToBottom = R.id.spinModel;
        }
        paramsedtRatedPower.topMargin = bigMargin;
        edtRatedPower.setLayoutParams(paramsedtRatedPower);


    }

    //    private void provMunSort(int position) {
//        String pos = spinProvince.getItemAtPosition(position).toString();
//        ArrayAdapter<String> adaptermun;
//        List<KeyPairBoolData> selectedBrgy = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));
//
//        switch (pos) {
//            case "CAPIZ":
//                adaptermun = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.capiz_mun));
//                break;
//            case "AKLAN":
//                adaptermun = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.aklan_mun));
//                selectedBrgy = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.aklan_all_brgy)));
//                break;
//            case "ILOILO":
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.iloilo_mun));
//                break;
//            case "ANTIQUE":
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.antique_mun));
//                break;
//            case "NEGROS OCCIDENTAL":
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.negros_occidental_mun));
//                break;
//            case "BATANGAS":
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.batangas_mun));
//                break;
//            case "NEGROS ORIENTAL":
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.negros_oriental_mun));
//                break;
//            case "BUKIDNON":
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bukidnon_mun));
//                break;
//            default:
//                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bukidnon_mun));
//                selectedBrgy = selectedBrgy = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));
//                break;
//        }
//        adaptermun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinMunicipality.setAdapter(adaptermun);
//        singlespinBarangay.setItems(selectedBrgy, new SingleSpinnerListener() {
//            @Override
//            public void onItemsSelected(KeyPairBoolData selectedItem) {
//
//            }
//
//            @Override
//            public void onClear() {
//
//            }
//        });
//
//    }

    private void problemsUnused(int position) {
        String pos = spinConditionPresent.getItemAtPosition(position).toString();
        List<KeyPairBoolData> selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));
        int stringArray = R.array.blank;

        switch (pos) {
            case "FUNCTIONAL USED":
            default:
                multspinProblemsUnused.setVisibility(View.INVISIBLE);
                edtOtherProblems.setVisibility(View.INVISIBLE);
                tvMachineUnused.setVisibility(View.INVISIBLE);
                tvYearInoperable.setVisibility(View.INVISIBLE);
                spinYearInoperable.setVisibility(View.INVISIBLE);
                paramstvLocation.topToBottom = R.id.spinConditionPresent;
                break;
            case "FUNCTIONAL UNUSED":
                multspinProblemsUnused.setVisibility(View.VISIBLE);
                tvMachineUnused.setText("Why is this machine unused?");
                tvMachineUnused.setVisibility(View.VISIBLE);
                tvYearInoperable.setVisibility(View.VISIBLE);
                spinYearInoperable.setVisibility(View.VISIBLE);
                stringArray = R.array.problems_unused;
                paramstvMachineUnused.topToBottom = R.id.spinYearInoperable;
                paramstvLocation.topToBottom = R.id.multspinProblemsUnused;
                break;
            case "NON-FUNCTIONAL":
                multspinProblemsUnused.setVisibility(View.VISIBLE);
                tvMachineUnused.setText("Why is this machine non-functional?");
                tvYearInoperable.setVisibility(View.VISIBLE);
                spinYearInoperable.setVisibility(View.VISIBLE);
                tvMachineUnused.setVisibility(View.VISIBLE);
                stringArray = R.array.problems_nonfunctional;
                paramstvMachineUnused.topToBottom = R.id.spinYearInoperable;
                paramstvLocation.topToBottom = R.id.multspinProblemsUnused;
                break;
        }

        selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(stringArray)));
        multspinProblemsUnused.setItems(selectedProb, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = pos + " " + selectedItems.get(i).getName();
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
                listOfProblems = pos;
                if (pos.contains("OTHERS")) {
                    edtOtherProblems.setVisibility(View.VISIBLE);
                    paramstvLocation.topToBottom = R.id.edtOtherProblems;
                    hasOtherProblems = true;
                } else {
                    hasOtherProblems = false;
                    edtOtherProblems.setVisibility(View.INVISIBLE);
                    edtOtherProblems.setText("");
                }

            }
        });
        paramstvLocation.topMargin = bigMargin;
        tvLocation.setLayoutParams(paramstvLocation);

    }


    private void availabilitySelected(int position) {
        String pos = spinAvailability.getItemAtPosition(position).toString();

        switch (pos) {
            case "OUTSIDE BARANGAY":
//                spinRentProv.setVisibility(View.VISIBLE);
//                spinRentMun.setVisibility(View.VISIBLE);
//                multspinRentBrgy.setVisibility(View.VISIBLE);
//                paramstvConditionPresent.topToBottom = R.id.multispinRentBrgy;
                multSpinProvRent.setVisibility(View.VISIBLE);
                multSpinMunRent.setVisibility(View.VISIBLE);
                multSpinBrgyRent.setVisibility(View.VISIBLE);
                tvBrgyRent.setVisibility(View.VISIBLE);
                tvMunRent.setVisibility(View.VISIBLE);
                tvProvRent.setVisibility(View.VISIBLE);

                paramstvConditionPresent.topToBottom = R.id.multSpinBrgyRent;
                break;
            case "WITHIN BARANGAY":
            default:
//                spinRentProv.setVisibility(View.INVISIBLE);
//                spinRentMun.setVisibility(View.INVISIBLE);
//                multspinRentBrgy.setVisibility(View.INVISIBLE);
                tvBrgyRent.setVisibility(View.INVISIBLE);
                tvMunRent.setVisibility(View.INVISIBLE);
                tvProvRent.setVisibility(View.INVISIBLE);
                multSpinProvRent.setVisibility(View.INVISIBLE);
                multSpinMunRent.setVisibility(View.INVISIBLE);
                multSpinBrgyRent.setVisibility(View.INVISIBLE);
                paramstvConditionPresent.topToBottom = R.id.spinAvailability;
                break;
        }
        paramstvConditionPresent.topMargin = bigMargin;
        tvConditionPresent.setLayoutParams(paramstvConditionPresent);
    }

    private void customUnitSelect(int position) {
        String pos = spinCustomUnit.getItemAtPosition(position).toString();

        if (spinMachineType.getSelectedItem().toString().contains("TRACTOR")) {
            if (spinPlowingRentUnit.getSelectedItemPosition() == 4) {
                edtPlowSpecifyUnit.setVisibility(View.VISIBLE);
            } else {
                edtPlowSpecifyUnit.setVisibility(View.INVISIBLE);
            }
            if (spinFurrowingRentUnit.getSelectedItemPosition() == 4) {
                edtFurrSpecifyUnit.setVisibility(View.VISIBLE);
            } else {
                edtFurrSpecifyUnit.setVisibility(View.INVISIBLE);
            }
            if (spinHarrowingRentUnit.getSelectedItemPosition() == 4) {
                edtHarrSpecifyUnit.setVisibility(View.VISIBLE);
            } else {
                edtHarrSpecifyUnit.setVisibility(View.INVISIBLE);
            }
            if (spinOtherRentUnit.getSelectedItemPosition() == 4) {
                edtOthrSpecifyUnit.setVisibility(View.VISIBLE);
            } else {
                edtOthrSpecifyUnit.setVisibility(View.INVISIBLE);
            }
            paramstvMachineAvailability.topToBottom = R.id.edtAveFuelConsumptionFurrowing;
        } else {
            switch (pos) {
                case "SPECIFY":
                    edtCustomRateUnit.setVisibility(View.VISIBLE);
                    paramstvMachineAvailability.topToBottom = R.id.edtCustomRateUnit;
                    break;
                default:
                    edtCustomRateUnit.setVisibility(View.INVISIBLE);
                    paramstvMachineAvailability.topToBottom = R.id.edtPlowingRent;
                    break;
            }
        }


        paramstvMachineAvailability.topMargin = bigMargin;
        tvMachineAvailability.setLayoutParams(paramstvMachineAvailability);

    }

    private void rentSelect(int position) {
        String pos = spinRental.getItemAtPosition(position).toString();

        switch (pos) {
            case "NO":
            default:
//                edtCustomRateUnit.setVisibility(View.INVISIBLE);
                edtCustomRate.setVisibility(View.INVISIBLE);
                spinCustomUnit.setVisibility(View.INVISIBLE);
                tvMachineAvailability.setVisibility(View.INVISIBLE);
                spinAvailability.setVisibility(View.INVISIBLE);

                tvMachineAvailabilityInfo.setVisibility(View.INVISIBLE);

                tvBrgyRent.setVisibility(View.INVISIBLE);
                tvMunRent.setVisibility(View.INVISIBLE);
                tvProvRent.setVisibility(View.INVISIBLE);
                multSpinProvRent.setVisibility(View.INVISIBLE);
                multSpinMunRent.setVisibility(View.INVISIBLE);
                multSpinBrgyRent.setVisibility(View.INVISIBLE);
                tvCustomRate.setVisibility(View.INVISIBLE);
                tvCustomUnit.setVisibility(View.INVISIBLE);

                tvPlowingRent.setVisibility(View.INVISIBLE);
                tvHarrowingRent.setVisibility(View.INVISIBLE);
                tvFurrowingRent.setVisibility(View.INVISIBLE);
                tvOtherRent.setVisibility(View.INVISIBLE);
                tvCustomUnitOther.setVisibility(View.INVISIBLE);

                tvPlowingRentInfo.setVisibility(View.INVISIBLE);
                tvHarrowingRentInfo.setVisibility(View.INVISIBLE);
                tvFurrowingRentInfo.setVisibility(View.INVISIBLE);

                edtPlowingRent.setVisibility(View.INVISIBLE);
                edtFurrowingRent.setVisibility(View.INVISIBLE);
                edtHarrowingRent.setVisibility(View.INVISIBLE);
                edtOtherRent.setVisibility(View.INVISIBLE);

                spinPlowingRentUnit.setVisibility(View.INVISIBLE);
                spinFurrowingRentUnit.setVisibility(View.INVISIBLE);
                spinHarrowingRentUnit.setVisibility(View.INVISIBLE);
                spinOtherRentUnit.setVisibility(View.INVISIBLE);

                tvAveFuelConsPlow.setVisibility(View.INVISIBLE);
                tvAveFuelConsHarr.setVisibility(View.INVISIBLE);
                tvAveFuelConsFurr.setVisibility(View.INVISIBLE);

                edtAveFuelConsPlow.setVisibility(View.INVISIBLE);
                edtAveFuelConsHarr.setVisibility(View.INVISIBLE);
                edtAveFuelConsFurr.setVisibility(View.INVISIBLE);

                edtPlowSpecifyUnit.setVisibility(View.INVISIBLE);
                edtFurrSpecifyUnit.setVisibility(View.INVISIBLE);
                edtHarrSpecifyUnit.setVisibility(View.INVISIBLE);
                edtOthrSpecifyUnit.setVisibility(View.INVISIBLE);
                edtOthrSpecifyOperation.setVisibility(View.INVISIBLE);

                paramstvConditionPresent.topToBottom = R.id.spinRental;

                break;
            case "YES":

                if (spinMachineType.getSelectedItem().toString().contains("TRACTOR")) {
                    tvPlowingRent.setVisibility(View.VISIBLE);
                    tvHarrowingRent.setVisibility(View.VISIBLE);
                    tvFurrowingRent.setVisibility(View.VISIBLE);
                    tvOtherRent.setVisibility(View.VISIBLE);
                    tvCustomUnitOther.setVisibility(View.VISIBLE);

                    tvPlowingRentInfo.setVisibility(View.VISIBLE);
                    tvHarrowingRentInfo.setVisibility(View.VISIBLE);
                    tvFurrowingRentInfo.setVisibility(View.VISIBLE);

                    edtPlowingRent.setVisibility(View.VISIBLE);
                    edtFurrowingRent.setVisibility(View.VISIBLE);
                    edtHarrowingRent.setVisibility(View.VISIBLE);
                    edtOtherRent.setVisibility(View.VISIBLE);
                    edtOthrSpecifyOperation.setVisibility(View.VISIBLE);

                    spinPlowingRentUnit.setVisibility(View.VISIBLE);
                    spinFurrowingRentUnit.setVisibility(View.VISIBLE);
                    spinHarrowingRentUnit.setVisibility(View.VISIBLE);
                    spinOtherRentUnit.setVisibility(View.VISIBLE);

                    tvAveFuelConsPlow.setVisibility(View.VISIBLE);
                    tvAveFuelConsHarr.setVisibility(View.VISIBLE);
                    tvAveFuelConsFurr.setVisibility(View.VISIBLE);

                    edtAveFuelConsPlow.setVisibility(View.VISIBLE);
                    edtAveFuelConsHarr.setVisibility(View.VISIBLE);
                    edtAveFuelConsFurr.setVisibility(View.VISIBLE);

                    tvMachineAvailability.setVisibility(View.VISIBLE);

                    tvMachineAvailabilityInfo.setVisibility(View.VISIBLE);
                    spinAvailability.setVisibility(View.VISIBLE);

                    paramstvMachineAvailability.topToBottom = R.id.edtAveFuelConsumptionFurrowing;
                } else {
                    edtCustomRate.setVisibility(View.VISIBLE);
                    spinCustomUnit.setVisibility(View.VISIBLE);
                    tvMachineAvailability.setVisibility(View.VISIBLE);
                    spinAvailability.setVisibility(View.VISIBLE);
                    tvCustomRate.setVisibility(View.VISIBLE);
                    tvCustomUnit.setVisibility(View.VISIBLE);

                    tvPlowingRent.setVisibility(View.INVISIBLE);
                    tvHarrowingRent.setVisibility(View.INVISIBLE);
                    tvFurrowingRent.setVisibility(View.INVISIBLE);
                    tvOtherRent.setVisibility(View.INVISIBLE);
                    tvCustomUnitOther.setVisibility(View.INVISIBLE);

                    edtPlowingRent.setVisibility(View.INVISIBLE);
                    edtFurrowingRent.setVisibility(View.INVISIBLE);
                    edtHarrowingRent.setVisibility(View.INVISIBLE);
                    edtOtherRent.setVisibility(View.INVISIBLE);
                    edtOthrSpecifyOperation.setVisibility(View.INVISIBLE);

                    spinPlowingRentUnit.setVisibility(View.INVISIBLE);
                    spinFurrowingRentUnit.setVisibility(View.INVISIBLE);
                    spinHarrowingRentUnit.setVisibility(View.INVISIBLE);
                    spinOtherRentUnit.setVisibility(View.INVISIBLE);

                    tvAveFuelConsPlow.setVisibility(View.INVISIBLE);
                    tvAveFuelConsHarr.setVisibility(View.INVISIBLE);
                    tvAveFuelConsFurr.setVisibility(View.INVISIBLE);

                    edtAveFuelConsPlow.setVisibility(View.INVISIBLE);
                    edtAveFuelConsHarr.setVisibility(View.INVISIBLE);
                    edtAveFuelConsFurr.setVisibility(View.INVISIBLE);

                    paramstvMachineAvailability.topToBottom = R.id.spinMainRentUnit;
                }
                paramstvConditionPresent.topToBottom = R.id.spinAvailability;
                break;

        }

        paramstvMachineAvailability.topMargin = bigMargin;
        tvMachineAvailability.setLayoutParams(paramstvMachineAvailability);

        paramstvConditionPresent.topMargin = bigMargin;
        tvConditionPresent.setLayoutParams(paramstvConditionPresent);

    }

    private void machineSelect(int position) {
        String machineType = spinMachineType.getItemAtPosition(position).toString();
        List<String> stringListBrand = Arrays.asList(getResources().getStringArray(R.array.blank));
        int stringArrayId = 0;

        switch (machineType) {
            case "2 WHEEL TRACTOR":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                tvCapacity.setVisibility(View.INVISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.INVISIBLE);
                edtTotalServiceArea.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.VISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.VISIBLE);
                tvRatoonArea.setVisibility(View.VISIBLE);
                tvRatoonAreaInfo.setVisibility(View.VISIBLE);
                edtNewlyPlantedArea.setVisibility(View.VISIBLE);
                edtRatoonArea.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                getParams(1);
                stringArrayId = R.array.wheel2_tractor_brand;
                break;
            case "4 WHEEL TRACTOR":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                tvCapacity.setVisibility(View.INVISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.INVISIBLE);
                edtTotalServiceArea.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.VISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.VISIBLE);
                tvRatoonArea.setVisibility(View.VISIBLE);
                tvRatoonAreaInfo.setVisibility(View.VISIBLE);
                edtNewlyPlantedArea.setVisibility(View.VISIBLE);
                edtRatoonArea.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                stringArrayId = R.array.wheel4_tractor_brand;
                getParams(1);
                break;
            case "BOOM SPRAYER":
            case "POWER SPRAYER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);
                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtCapacity.setVisibility(View.VISIBLE);
                edtCapacity.setHint("Liters");

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setText("Tank Capacity");
                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield;            //TODO CHANGE THIS
                getParams(2);
                break;


            case "CANE GRAB LOADER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);
                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                edtCapacity.setVisibility(View.VISIBLE);
                edtNumLoads.setVisibility(View.VISIBLE);
                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvNumLoads.setVisibility(View.VISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Tons/Load");
                tvCapacity.setText("Loading Capacity");

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield;
                getParams(3);
                break;
            case "COMBINE HARVESTER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                edtAveYield.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);
                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                tvCapacity.setText("Capacity");
                edtCapacity.setHint("Hectares/Hour");
                edtAveYield.setHint("Tons/Hectares");

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield; //TODO CHANGE THIS

                getParams(4);
                break;
            case "HARVESTER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                edtAveYield.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);
                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtCapacity.setEnabled(false);
                edtCapacity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNullOrEmpty(edtCapacity.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "This will be automatically computed after you input Total Service Area and Average Operating Hours", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "This is automatically computed. Unable to edit.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.VISIBLE);
                tvTimeUsedWorking.setVisibility(View.VISIBLE);
                edtTimeUsedWorking.setVisibility(View.VISIBLE);
                edtEffectiveArea.setVisibility(View.VISIBLE);

                edtCapacity.setHint("Hectares/Hour");
                tvCapacity.setText("Capacity (ha/hr)");
                edtAveYield.setHint("Ton of Cannes/Hectares");

                stringArrayId = R.array.harvester_brands;
                getParams(8);
                break;
            case "DRYER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.VISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtCapacity.setVisibility(View.VISIBLE);
                edtRate.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Kilograms");
                tvCapacity.setText("Capacity");
                tvRate.setText("Drying Rate");
                edtRate.setHint("Kilogram/Hour");

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield; //TODO CHANGE THIS

                getParams(5);
                break;
            case "INFIELD HAULER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                edtCapacity.setVisibility(View.VISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Tons/Load");
                tvCapacity.setText("Capacity");
                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield;
                getParams(2);
                break;
            case "MECHANICAL PLANTER":
            case "REAPER":
            case "PICKER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Hectares/Hour");
                tvCapacity.setText("Capacity");

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield; //TODO CHANGE THIS

                getParams(2);
                break;
            case "MILL":
                tvTypeOfMill.setVisibility(View.VISIBLE);
                spinTypeOfMill.setVisibility(View.VISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.VISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.VISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Kilograms");
                tvCapacity.setText("Capacity");
                tvRate.setText("Milling Rate");
                edtRate.setHint("Tons/Hour");

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield; //TODO CHANGE THIS

                getParams(7);
                break;
            case "SHELLER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.VISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.VISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Kilograms");
                tvCapacity.setText("Capacity");
                tvRate.setText("Shelling Rate");
                edtRate.setHint("Tons/Hour");

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield; //TODO CHANGE THIS

                getParams(5);
                break;
            case "THRESHER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.VISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.VISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Kilograms");
                tvCapacity.setText("Capacity");
                tvRate.setText("Threshing Rate");
                edtRate.setHint("Tons/Hour");

                stringArrayId = R.array.specify_only_brand_boom_sprayer_cane_grab_infield; //TODO CHANGE THIS

                getParams(5);
                break;
            case "WATER PUMP":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                tvTypeOfTubewells.setVisibility(View.VISIBLE);
                spinTypeofTubeWells.setVisibility(View.VISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);


                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setHint("Liters/Second");
                tvCapacity.setText("Capacity");
                stringArrayId = R.array.waterpump_brands;
                getParams(6);
                break;
            default:
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                tvCapacity.setVisibility(View.VISIBLE);
                tvRate.setVisibility(View.INVISIBLE);
                tvNumLoads.setVisibility(View.INVISIBLE);
                tvAveYield.setVisibility(View.INVISIBLE);

                tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
                tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
                tvRatoonArea.setVisibility(View.INVISIBLE);
                tvRatoonAreaInfo.setVisibility(View.INVISIBLE);
                edtNewlyPlantedArea.setVisibility(View.INVISIBLE);
                edtRatoonArea.setVisibility(View.INVISIBLE);

                tvTotalServiceArea.setVisibility(View.VISIBLE);
                edtTotalServiceArea.setVisibility(View.VISIBLE);

                tvEffectiveArea.setVisibility(View.INVISIBLE);
                tvTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtTimeUsedWorking.setVisibility(View.INVISIBLE);
                edtEffectiveArea.setVisibility(View.INVISIBLE);

                edtCapacity.setEnabled(true);
                edtCapacity.setOnClickListener(null);

                edtCapacity.setHint("Hectares/Hour");
                tvCapacity.setText("Capacity");

                stringArrayId = R.array.blank;
                getParams(2);
                break;
        }

        stringListBrand = Arrays.asList(getResources().getStringArray(stringArrayId));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListBrand);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBrand.setAdapter(dataAdapter);

        Intent intent = intentFromDb;
        if (intent != null && intent.hasExtra(EXTRA_ID)) {

            String stringCompare = intent.getStringExtra(EXTRA_BRAND);

            if (!isNullOrEmpty(stringCompare)) {
                position = dataAdapter.getPosition(stringCompare);
            }
            spinBrand.setSelection(position);
        }


    }

    private void loanCashSelect() {
        spinPurchGrantDono.setSelection(0);

        if (rbCash.isChecked()) {
            loanCash = "CASH";
            spinAgency.setVisibility(View.INVISIBLE);
            tvAgency.setVisibility(View.INVISIBLE);
            paramsedtNameOfOwnerOrg.topToBottom = R.id.rgLoanCash;
        } else if (rbLoan.isChecked()) {
            loanCash = "LOAN";
            spinAgency.setVisibility(View.VISIBLE);
            tvAgency.setVisibility(View.VISIBLE);
            paramsedtNameOfOwnerOrg.topToBottom = R.id.spinAgency;
        } else {
            loanCash = "";
        }

        paramsedtNameOfOwnerOrg.topMargin = biggerMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
    }

    private void purchGrantDonoSelect(int position) {
        String pos = spinPurchGrantDono.getItemAtPosition(position).toString();

        switch (pos) {
            case "PURCHASED":
                Log.d("INSIDE PURCHASE", pos);
                purchGrantDono = "PURCHASED";
                spinAgency.setVisibility(View.INVISIBLE);
                tvAgency.setVisibility(View.INVISIBLE);

                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinPurchGrantDono;
                break;
            default:
                Log.d("INSIDE PURCHASE", pos);
                purchGrantDono = "";
//                spinAgency.setVisibility(View.INVISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.rgLoanCash;
//                tvAgency.setVisibility(View.INVISIBLE);
                break;
            case "GRANT":
            case "DONATION":
                if (pos.contains("GRANT")) {
                    purchGrantDono = "GRANT";
                } else if (pos.contains("DONATION")) {
                    purchGrantDono = "DONATION";
                }
                spinAgency.setVisibility(View.VISIBLE);
                tvAgency.setVisibility(View.VISIBLE);


                if (spinAgency.getVisibility() == View.INVISIBLE) {
                    paramsedtNameOfOwnerOrg.topToBottom = R.id.spinPurchGrantDono;
                } else {
                    paramsedtNameOfOwnerOrg.topToBottom = R.id.spinAgency;
                }
                break;
        }

        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
    }

    private void ownershipSelect(int position) {
        String pos = spinOwnership.getItemAtPosition(position).toString();
        List<String> stringListAgency = Arrays.asList(getResources().getStringArray(R.array.blank));

        Log.d("GOT TO OWNERSHIP", pos);
        switch (pos) {

            case "PRIVATELY OWNED":
                spinPurchGrantDono.setVisibility(View.INVISIBLE);
                tvPurchGrantDono.setVisibility(View.VISIBLE);
                spinAgency.setVisibility(View.INVISIBLE);
                tvAgency.setVisibility(View.INVISIBLE);
                rgLoanCash.setVisibility(View.VISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.rgLoanCash;
                stringListAgency = Arrays.asList(getResources().getStringArray(R.array.agency_loan));
                break;
            case "COOPERATIVE/ASSOCIATION":
            case "CUSTOM PROVIDER":
            case "LGU":
                loanCash = "";
                rbCash.setChecked(false);
                rbLoan.setChecked(false);
                Log.d("LOANCASH", loanCash + ": " + rbCash.isChecked() + ": " + rbLoan.isChecked());
                spinPurchGrantDono.setVisibility(View.VISIBLE);
                tvPurchGrantDono.setVisibility(View.VISIBLE);
                rgLoanCash.setVisibility(View.INVISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinPurchGrantDono;
                stringListAgency = Arrays.asList(getResources().getStringArray(R.array.agency));
                break;
            default:
                spinPurchGrantDono.setVisibility(View.INVISIBLE);
                spinAgency.setVisibility(View.INVISIBLE);
                tvPurchGrantDono.setVisibility(View.INVISIBLE);
                tvAgency.setVisibility(View.INVISIBLE);
                rgLoanCash.setVisibility(View.INVISIBLE);
                loanCash = "";
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinOwnership;
                break;
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListAgency);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAgency.setAdapter(dataAdapter);
//TODO ADD INENT AGENCY
        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);

        Intent intent = intentFromDb;
        if (intent != null && intent.hasExtra(EXTRA_ID)) {
            String stringCompare = intent.getStringExtra(EXTRA_AGENCY);

            if (!isNullOrEmpty(stringCompare)) {
                position = dataAdapter.getPosition(stringCompare);
            }
            spinAgency.setSelection(position);
        }
    }

    private void otherAgency(int position) {
        String pos = spinAgency.getItemAtPosition(position).toString();
        Log.d("POSITION OF AGENCY", pos);

        if ("OTHERS".equals(pos)) {
            paramsedtNameOfOwnerOrg.topToBottom = R.id.edtOtherAgency;
            edtOtherAgency.setVisibility(View.VISIBLE);
        } else {
            paramsedtNameOfOwnerOrg.topToBottom = R.id.spinAgency;
            edtOtherAgency.setVisibility(View.INVISIBLE);
        }
        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
    }

    private void getParams(int input) {
        int type = 0;

        paramstvOwnership = (ConstraintLayout.LayoutParams) tvOwnership.getLayoutParams();
        paramsedtCapacity = (ConstraintLayout.LayoutParams) edtCapacity.getLayoutParams();
        paramsedtNumLoads = (ConstraintLayout.LayoutParams) edtNumLoads.getLayoutParams();
        paramsedtAveYield = (ConstraintLayout.LayoutParams) edtAveYield.getLayoutParams();
        paramsedtRate = (ConstraintLayout.LayoutParams) edtRate.getLayoutParams();
        paramstvTypeTubewells = (ConstraintLayout.LayoutParams) tvTypeOfTubewells.getLayoutParams();

        type = input;

        switch (type) {
            case 1:
            default:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtAverageOperatingDays;
                paramstvAveOpHours.topToBottom = R.id.tvRatoonArea;
                break;
            case 2:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtCapacity;
                paramsedtCapacity.topToBottom = R.id.edtAverageOperatingDays;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
            case 3:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtNumberOfLoads;
                paramsedtCapacity.topToBottom = R.id.edtAverageOperatingDays;
                paramsedtNumLoads.topToBottom = R.id.edtCapacity;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
            case 4:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtAveYield;
                paramsedtCapacity.topToBottom = R.id.edtAverageOperatingDays;
                paramsedtAveYield.topToBottom = R.id.edtCapacity;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
            case 5:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtRate;
                paramsedtCapacity.topToBottom = R.id.edtAverageOperatingDays;
                paramsedtRate.topToBottom = R.id.edtCapacity;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
            case 6:
                paramstvTypeTubewells.topToBottom = R.id.edtQRCode;
                paramstvBrand.topToBottom = R.id.spinTypeOfTubewells;
                paramstvOwnership.topToBottom = R.id.edtCapacity;
                paramsedtCapacity.topToBottom = R.id.edtAverageOperatingDays;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
            case 7:
                paramstvBrand.topToBottom = R.id.spinTypeMill;
                paramstvOwnership.topToBottom = R.id.edtRate;
                paramsedtCapacity.topToBottom = R.id.edtAverageOperatingDays;
                paramsedtRate.topToBottom = R.id.edtCapacity;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
            case 8:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtAveYield;
                paramsedtCapacity.topToBottom = R.id.edtTimeUsedWorkingHarvester;
                paramstvAveOpHours.topToBottom = R.id.tvTotalServiceAreaMachine;
                break;
        }

        paramstvBrand.topMargin = bigMargin;
        paramstvOwnership.topMargin = bigMargin;
        paramsedtCapacity.topMargin = bigMargin;
        paramsedtNumLoads.topMargin = bigMargin;
        paramsedtAveYield.topMargin = bigMargin;
        paramsedtRate.topMargin = bigMargin;
        paramstvAveOpHours.topMargin = bigMargin;

        tvAveOpHours.setLayoutParams(paramstvAveOpHours);
        tvBrand.setLayoutParams(paramstvBrand);
        tvOwnership.setLayoutParams(paramstvOwnership);
        edtCapacity.setLayoutParams(paramsedtCapacity);
        edtNumLoads.setLayoutParams(paramsedtNumLoads);
        edtAveYield.setLayoutParams(paramsedtAveYield);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrPleaseSelect(String str) {
        return (str == null || str.isEmpty() || str.contains("Please Select"));
    }

    private void saveNote() {
        List<String> listIncomplete = new ArrayList<>();
        String machineType = spinMachineType.getSelectedItem().toString();
        String machineQRCode = edtQRCode.getText().toString();
        String latitude = tvLat.getText().toString();
        String longitude = tvLong.getText().toString();

//        infoCheck();


        if (infoCheck()) {
//        if (true) {

            Intent dataAddMachine = new Intent();

//            if (respCheck) {
//                profileViewModel.getAllProfiles().observe(this, new Observer<List<Profile>>() {
//                    @Override
//                    public void onChanged(List<Profile> profiles) {
//                        List<String> profileList = new ArrayList<String>();
//                        for (int i = 0; i < profiles.size(); i++) {
//                            int count = 1;
//                            profileList.add(count + " " + profiles.get(i).getName_respondent());
//                            count++;
//                            respondentArrayList = new ArrayList<>();
//                            respondentArrayList.add(new Respondent(profiles.get(i).getName_respondent(), profiles.get(i).getResCode()));
//
//                        }
//                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, profileList);
//
//                        int posRespName;
//                        posRespName = spinnerArrayAdapter.getPosition(spinRespName.getSelectedItem().toString());
////                        resName = profiles.get(posRespName).getName_respondent();
////                        resCode = profiles.get(posRespName).getResCode();
//
//                        resName = respondentArrayList.get(posRespName).name;
//                        resCode = respondentArrayList.get(posRespName).code;
//
//                        Log.d("XRES", resName + " : " + resCode + " : " + posRespName);
//                    }
//                });
//            }

//            for (int i = 0; i < respondentArrayList.size(); i++) {
//                respondentArrayList.add(new Respondent(profiles.get(i).getName_respondent(), profiles.get(i).getResCode()));
//                Log.d("XRES LOOP", respondentArrayList.get(i).name + " " + respondentArrayList.get(i).code);
//            }

            resName = respondentArrayList.get(spinRespName.getSelectedItemPosition() - 1).name;
            resCode = respondentArrayList.get(spinRespName.getSelectedItemPosition() - 1).code;
            Log.d("XRES", resName + " : " + resCode + " : " + spinRespName.getSelectedItemPosition());

            if (isNullOrEmpty(encodedImage)) {

                encodedImage = "Not yet Acquired";
            }

            int id = getIntent().getIntExtra(EXTRA_ID, -1);

            if (id != -1) {
                dataAddMachine.putExtra(EXTRA_ID, id);

            } else {
                dateToStr = new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(new Date());
            }

            String modeOfPurchase = "";
            if (spinPurchGrantDono.getSelectedItemPosition() == 0) {
                modeOfPurchase = loanCash;
            } else {
                modeOfPurchase = spinPurchGrantDono.getSelectedItem().toString();
            }

            Log.d("XSAVX", loanCash + " : " + spinPurchGrantDono.getSelectedItem().toString() + " : " + modeOfPurchase);


            dataAddMachine.putExtra(EXTRA_MACHINE_TYPE, spinMachineType.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_MACHINE_QRCODE, edtQRCode.getText().toString());
            dataAddMachine.putExtra(EXTRA_TYPE_TUBEWELLS, spinTypeofTubeWells.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_TYPE_MILL, spinTypeOfMill.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_DATE_TIME, dateToStr);
            dataAddMachine.putExtra(EXTRA_BRAND, spinBrand.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_BRAND_SPECIFY, edtOtherBrand.getText().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_MODEL, spinModel.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_MODEL_SPECIFY, edtOtherModel.getText().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_RATED_POWER, edtRatedPower.getText().toString());

            if (spinMachineType.getSelectedItem().toString().contains("TRACTOR")) {
                totalServiceArea = (Double.parseDouble(edtNewlyPlantedArea.getText().toString()) + Double.parseDouble(edtRatoonArea.getText().toString()));
                dataAddMachine.putExtra(EXTRA_SERVICE_AREA, totalServiceArea.toString());
            } else {
                dataAddMachine.putExtra(EXTRA_SERVICE_AREA, edtTotalServiceArea.getText().toString());
            }

            dataAddMachine.putExtra(EXTRA_NEWLY_PLANTED_AREA, edtNewlyPlantedArea.getText().toString());
            dataAddMachine.putExtra(EXTRA_RATOONED_AREA, edtRatoonArea.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVE_OP_HOURS, edtAveOpHours.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVE_OP_DAYS, edtAveOpDays.getText().toString());
            dataAddMachine.putExtra(EXTRA_EFFECTIVE_AREA_HARVEST, edtEffectiveArea.getText().toString());
            dataAddMachine.putExtra(EXTRA_TIME_USED_WORKING, edtTimeUsedWorking.getText().toString());
            dataAddMachine.putExtra(EXTRA_CAPACITY, edtCapacity.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVE_YIELD, edtAveYield.getText().toString());
            dataAddMachine.putExtra(EXTRA_NUM_LOADS, edtNumLoads.getText().toString());
            dataAddMachine.putExtra(EXTRA_RATE, edtRate.getText().toString());
            dataAddMachine.putExtra(EXTRA_OWNERSHIP, spinOwnership.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_PURCH_GRANT_DONO, modeOfPurchase);
            dataAddMachine.putExtra(EXTRA_AGENCY, spinAgency.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_AGENCY_SPECIFY, edtOtherAgency.getText().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_NAME_OWNER, edtNameOfOwnerOrg.getText().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_YEAR_ACQUIRED, spinYearAcquired.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_CONDITION_ACQUIRED, spinConditionAcquired.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_RENTAL, spinRental.getSelectedItem().toString());
//            dataAddMachine.putExtra(EXTRA_CUSTOM_RATE, edtCustomRate.getText().toString());
//            dataAddMachine.putExtra(EXTRA_CUSTOM_UNIT, spinCustomUnit.getSelectedItem().toString());
//            dataAddMachine.putExtra(EXTRA_CUSTOM_UNIT_SPECIFY, edtCustomRateUnit.getText().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_MAIN_RENT_RATE, edtCustomRate.getText().toString());
            dataAddMachine.putExtra(EXTRA_MAIN_RENT_UNIT, spinCustomUnit.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_MAIN_RENT_UNIT_SPECIFY, edtCustomRateUnit.getText().toString());
            dataAddMachine.putExtra(EXTRA_PLOW_RENT_RATE, edtPlowingRent.getText().toString());
            dataAddMachine.putExtra(EXTRA_PLOW_RENT_UNIT, spinPlowingRentUnit.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_PLOW_RENT_UNIT_SPECIFY, edtPlowSpecifyUnit.getText().toString());
            dataAddMachine.putExtra(EXTRA_HARR_RENT_RATE, edtHarrowingRent.getText().toString());
            dataAddMachine.putExtra(EXTRA_HARR_RENT_UNIT, spinHarrowingRentUnit.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_HARR_RENT_UNIT_SPECIFY, edtHarrSpecifyUnit.getText().toString());
            dataAddMachine.putExtra(EXTRA_FURR_RENT_RATE, edtFurrowingRent.getText().toString());
            dataAddMachine.putExtra(EXTRA_FURR_RENT_UNIT, spinFurrowingRentUnit.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_FURR_RENT_UNIT_SPECIFY, edtFurrSpecifyUnit.getText().toString());
            dataAddMachine.putExtra(EXTRA_OTHR_RENT_OPERATION, edtOthrSpecifyOperation.getText().toString());
            dataAddMachine.putExtra(EXTRA_OTHR_RENT_RATE, edtOtherRent.getText().toString());
            dataAddMachine.putExtra(EXTRA_OTHR_RENT_UNIT, spinOtherRentUnit.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_OTHR_RENT_UNIT_SPECIFY, edtOthrSpecifyUnit.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVE_FUEL_PLOW, edtAveFuelConsPlow.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVE_FUEL_HARR, edtAveFuelConsHarr.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVE_FUEL_FURR, edtAveFuelConsFurr.getText().toString());
            dataAddMachine.putExtra(EXTRA_AVAILABILITY, spinAvailability.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_RENT_PROV, provRent);
            dataAddMachine.putExtra(EXTRA_RENT_MUN, munRent);
            dataAddMachine.putExtra(EXTRA_RENT_BRGY, listOfBrgyRent);
            dataAddMachine.putExtra(EXTRA_CONDITION, spinConditionPresent.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_PROBLEMS, listOfProblems);
            dataAddMachine.putExtra(EXTRA_PROBLEMS_SPECIFY, edtOtherProblems.getText().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_YEAR_INOPERABLE, spinYearInoperable.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_LOCATION, spinLocationOfMachine.getSelectedItem().toString());
            dataAddMachine.putExtra(EXTRA_PROV, singlespinProvince.getSelectedItem().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_MUN, singlespinMunicipality.getSelectedItem().toString().toUpperCase());
            dataAddMachine.putExtra(EXTRA_BRGY, singlespinBarangay.getSelectedItem().toString().toUpperCase());
            Variable.setStringImage(encodedImage);
            dataAddMachine.putExtra(EXTRA_LAT, tvLat.getText().toString());
            dataAddMachine.putExtra(EXTRA_LONG, tvLong.getText().toString());
            dataAddMachine.putExtra(EXTRA_ACC, tvAcc.getText().toString());
            dataAddMachine.putExtra(EXTRA_RES_CODE, resCode.toUpperCase());
            dataAddMachine.putExtra(EXTRA_RES_NAME, resName.toUpperCase());

            setResult(RESULT_OK, dataAddMachine);
            finish();
        } else {

            if (!machineTypeInfoCheck) {
                listIncomplete.add("Machine Brand/Model");
            }
            if (!respCheck) {
                listIncomplete.add("Respondent");
            }
            if (!qrCheck) {
                listIncomplete.add("QR Code");
            }
            if (!machineTypeSpecsCheck) {
                listIncomplete.add("Machine Specifications");
            }
            if (!ownershipCheck) {
                listIncomplete.add("Type of Ownership");
            }
            if (!loanCashCheck) {
                listIncomplete.add("Mode of Acquisition");
            }
            if (!purchGrantDonoCheck) {
                listIncomplete.add("Mode of Acquisition");
            }
            if (!agencyCheck) {
                listIncomplete.add("Agency");
            }
            if (!yearSelectCheck) {
                listIncomplete.add("Year Acquired");
            }
            if (!conditionAcquiredCheck) {
                listIncomplete.add("Condition when Acquired");
            }
            if (!rentSelectCheck) {
                listIncomplete.add("Machine for Rent");
            }
            if (!rentCustomCheck) {
                listIncomplete.add("Custom Rate/Unit");
            }
            if (!rentAvailCheck) {
                listIncomplete.add("Machine Availability");
            }
            if (!conditionPresentCheck) {
                listIncomplete.add("Present Condition");
            }
            if (!otherProblemsCheck) {
                listIncomplete.add("Problems with Machine");
            }
            if (!locationMachineCheck) {
                listIncomplete.add("Location of Machine");
            }
            if (!locationGarageCheck) {
                listIncomplete.add("Locatiton of Garage");
            }
            String inc = "";

            for (int i = 0; i < listIncomplete.size(); i++)
                inc = inc + listIncomplete.get(i) + "\n";

            Toast.makeText(this, "Incomplete Data!\nPlease Check\n\n" + inc, Toast.LENGTH_LONG).show();
            Log.d("Prof Check", String.valueOf(listIncomplete) + ": " + inc);
        }
//
//        if (machineType.trim().isEmpty() ||
//                machineQRCode.trim().isEmpty() ||
//                isNullOrEmpty(latitude) ||
//                isNullOrEmpty(longitude)) {
//            Toast.makeText(this, "Incomplete Data", Toast.LENGTH_SHORT).show();
//        } else {
//
//        }m

    }

    private boolean infoCheck() {

        machineTypeInfoCheck = false;
        respCheck = false;
        qrCheck = false;
        machineTypeSpecsCheck = false;
        ownershipCheck = false;
        loanCashCheck = false;
        purchGrantDonoCheck = false;
        agencyCheck = false;
        yearSelectCheck = false;
        conditionAcquiredCheck = false;
        rentSelectCheck = false;
        rentCustomCheck = false;
        rentAvailCheck = false;
        conditionPresentCheck = false;
        otherProblemsCheck = false;
        locationMachineCheck = false;
        locationGarageCheck = false;
        machineTypeInfoBrandCheck = false;
        machineTypeInfoModelCheck = false;
        spinYearInoperableCheck = false;


        respCheck = spinRespName.getSelectedItemPosition() != 0;

        qrCheck = !isNullOrEmpty(edtQRCode.getText().toString()) && edtQRCode.getText().toString().length() == 12;
        //R04BATM00001
        switch (spinMachineType.getSelectedItem().toString()) {
            case "2 WHEEL TRACTOR":
            case "4 WHEEL TRACTOR":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }
                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck;

                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtNewlyPlantedArea.getText().toString()) &&
                        !isNullOrEmpty(edtRatoonArea.getText().toString()) && !isNullOrEmpty(edtAveOpHours.getText().toString());
                // 2 Wheel Tractor
                // 4 Wheel Tractor
                break;
            case "BOOM SPRAYER":
            case "POWER SPRAYER":
            case "INFIELD HAULER":
            case "MECHANICAL PLANTER":
            case "REAPER":
            case "PICKER":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }
                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck;
                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString()) &&
                        !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtCapacity.getText().toString());
                // BoomSprayer
                // PowerSprayer
                // MechPlant
                // Reaper
                // Picker
                // InfieldHauler

                break;
            case "CANE GRAB LOADER":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }
                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck;
                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString()) &&
                        !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtNumLoads.getText().toString());
                // Cane Grab Loader
                break;
            case "COMBINE HARVESTER":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }
                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck;
                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString()) &&
                        !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtCapacity.getText().toString()) &&
                        !isNullOrEmpty(edtAveYield.getText().toString());
                break;
            case "HARVESTER":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }
                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck;
                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTimeUsedWorking.getText().toString())
                        && !isNullOrEmpty(edtEffectiveArea.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString())
                        && !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtCapacity.getText().toString()) &&
                        !isNullOrEmpty(edtAveYield.getText().toString());
                // CombineHarvester
                // Harvester
                break;
            case "DRYER":
            case "SHELLER":
            case "THRESHER":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }

                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck;

                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString()) &&
                        !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtCapacity.getText().toString()) && !isNullOrEmpty(edtRate.getText().toString());
                //Dryer
                //Sheller
                //Thresher
                break;
            case "MILL":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }

                typeMillCheck = spinTypeOfMill.getSelectedItemPosition() != 0;

                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck && typeMillCheck;

                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString())
                        && !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtCapacity.getText().toString()) && !isNullOrEmpty(edtRate.getText().toString());
                //Mill
                break;
            case "WATER PUMP":
                if (spinBrand.getSelectedItemPosition() == 0) {
                    machineTypeInfoBrandCheck = false;
                } else {
                    machineTypeInfoBrandCheck = true;
                    if (spinBrand.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoBrandCheck = !isNullOrEmpty(edtOtherBrand.getText().toString());
                    }
                }
                if (spinModel.getSelectedItemPosition() == 0) {
                    machineTypeInfoModelCheck = false;
                } else {
                    machineTypeInfoModelCheck = true;
                    if (spinModel.getSelectedItem().toString().equals("OTHERS")) {
                        machineTypeInfoModelCheck = !isNullOrEmpty(edtOtherModel.getText().toString());
                    }
                }

                typeTubewellsCheck = spinTypeofTubeWells.getSelectedItemPosition() != 0;

                machineTypeInfoCheck = machineTypeInfoBrandCheck && machineTypeInfoModelCheck && typeTubewellsCheck;

                machineTypeSpecsCheck = !isNullOrEmpty(edtRatedPower.getText().toString()) && !isNullOrEmpty(edtTotalServiceArea.getText().toString()) &&
                        !isNullOrEmpty(edtAveOpHours.getText().toString()) && !isNullOrEmpty(edtCapacity.getText().toString());
                //WaterPump
                break;
            default:
                //Default
                break;

        }

        switch (spinOwnership.getSelectedItemPosition()) {
            case 0:
                ownershipCheck = false;
                break;
            //Please Select
            case 1:
                ownershipCheck = true;
                purchGrantDonoCheck = true;
                agencyCheck = true;
                switch (loanCash) {
                    case "LOAN":
                        loanCashCheck = true;
                        switch (spinAgency.getSelectedItemPosition()) {
                            case 0:
                                agencyCheck = false;
                                break;
                            case 1:
                                agencyCheck = true;
                                break;
                            case 2:
                                agencyCheck = !isNullOrEmpty(edtOtherAgency.getText().toString());
                                break;
                        }
                        break;
                    case "CASH":
                        loanCashCheck = true;
                        agencyCheck = true;
                        break;
                    default:
                        loanCashCheck = false;
                        break;
                }
                break;
            //Private
            case 2:
            case 3:
            case 4:
                ownershipCheck = true;
                loanCashCheck = true;
                switch (spinPurchGrantDono.getSelectedItemPosition()) {
                    case 0:
                        purchGrantDonoCheck = false;
                        agencyCheck = false;
                        break;
                    case 1:
                    default:
                        purchGrantDonoCheck = true;
                        agencyCheck = true;
                        break;
                    case 2:
                    case 3:
                        purchGrantDonoCheck = true;
                        switch (spinAgency.getSelectedItemPosition()) {
                            case 0:
                                agencyCheck = false;
                                break;
                            case 5:
                                agencyCheck = !isNullOrEmpty(edtOtherAgency.getText().toString());
                                break;
                            default:
                                agencyCheck = true;
                                break;

                        }
                        break;

                }
                break;
            //CoopCustomLgu
            default:
                ownershipCheck = true;
                break;
        }

        yearSelectCheck = spinYearAcquired.getSelectedItemPosition() != 0;

        conditionAcquiredCheck = spinConditionAcquired.getSelectedItemPosition() != 0;

        switch (spinRental.getSelectedItemPosition()) {
            case 0:
                rentSelectCheck = false;
                break;
            case 1:
                rentSelectCheck = true;
                if (spinMachineType.getSelectedItem().toString().contains("TRACTOR")) {
                    if (isNullOrEmpty(edtPlowingRent.getText().toString()) || isNullOrEmpty(edtHarrowingRent.getText().toString()) ||
                            isNullOrEmpty(edtHarrowingRent.getText().toString()) || spinPlowingRentUnit.getSelectedItemPosition() == 0 ||
                            spinHarrowingRentUnit.getSelectedItemPosition() == 0 || spinFurrowingRentUnit.getSelectedItemPosition() == 0 ||
                            isNullOrEmpty(edtAveFuelConsPlow.getText().toString()) || isNullOrEmpty(edtAveFuelConsHarr.getText().toString()) ||
                            isNullOrEmpty(edtAveFuelConsFurr.getText().toString())) {
                        rentCustomCheck = false;
                    } else {
                        rentCustomCheck = spinPlowingRentUnit.getSelectedItemPosition() != 4 || !isNullOrEmpty(edtPlowSpecifyUnit.getText().toString()) ||
                                spinHarrowingRentUnit.getSelectedItemPosition() != 4 || !isNullOrEmpty(edtHarrSpecifyUnit.getText().toString()) ||
                                spinFurrowingRentUnit.getSelectedItemPosition() != 4 || !isNullOrEmpty(edtFurrSpecifyUnit.getText().toString());
                    }
                } else {
                    if (isNullOrEmpty(edtCustomRate.getText().toString()) || spinCustomUnit.getSelectedItemPosition() == 0) {
                        rentCustomCheck = false;
                    } else {
                        rentCustomCheck = spinCustomUnit.getSelectedItemPosition() != 4 || !isNullOrEmpty(edtCustomRateUnit.getText().toString());
//                        rentCustomCheck = spinCustomUnit.getSelectedItemPosition() != 3;
                    }
                }
                switch (spinAvailability.getSelectedItemPosition()) {
                    case 0:
                        rentAvailCheck = false;
                        break;
                    case 1:
                        rentAvailCheck = true;
                        break;
                    case 2:
                        rentAvailCheck = listOfBrgyRent.length() >= 2 && munRent.length() >= 2 && provRent.length() >= 2;
                        break;
                }
                break;
            case 2:
                rentSelectCheck = true;
                rentCustomCheck = true;
                rentAvailCheck = true;
                break;
        }

        switch (spinConditionPresent.getSelectedItemPosition()) {
            case 0:
                conditionPresentCheck = false;
                break;
            case 1:
            default:
                conditionPresentCheck = true;
                otherProblemsCheck = true;

                break;
            case 2:
            case 3:
                conditionPresentCheck = listOfProblems.length() >= 5;
                if (hasOtherProblems) {
                    otherProblemsCheck = !isNullOrEmpty(edtOtherProblems.getText().toString());
                } else {
                    otherProblemsCheck = true;
                }
                spinYearInoperableCheck = spinYearInoperable.getSelectedItemPosition() != 0;
                break;
        }
        Log.d("PRBMACH", "hasProblems: " + hasOtherProblems + "ConditionPresent: " + conditionPresentCheck + "All List of Problems" + listOfProblems);

        locationMachineCheck = spinLocationOfMachine.getSelectedItemPosition() != 0;

        locationGarageCheck = !isNullOrPleaseSelect(singlespinProvince.getSelectedItem().toString()) && !isNullOrPleaseSelect(singlespinMunicipality.getSelectedItem().toString())
                && !isNullOrPleaseSelect(singlespinBarangay.getSelectedItem().toString());

        Log.d("Machine Check", "Resp Name: " + respCheck);
        Log.d("Machine Check", "QR Code: " + qrCheck);
        Log.d("Machine Check", "Machine Info: " + machineTypeInfoCheck);
        Log.d("Machine Check", "Machine Brand: " + machineTypeInfoBrandCheck);
        Log.d("Machine Check", "Machine Model: " + machineTypeInfoModelCheck);
        Log.d("Machine Check", "Machine Specs: " + machineTypeSpecsCheck);
        Log.d("Machine Check", "Ownership Check: " + ownershipCheck);
        Log.d("Machine Check", "LoanCash: " + loanCashCheck);
        Log.d("Machine Check", "PurchGrantDono: " + purchGrantDonoCheck);
        Log.d("Machine Check", "Agency: " + agencyCheck);
        Log.d("Machine Check", "Year: " + yearSelectCheck);
        Log.d("Machine Check", "Condition Acquired: " + conditionAcquiredCheck);
        Log.d("Machine Check", "Rent: " + rentSelectCheck);
        Log.d("Machine Check", "Rent Custom: " + rentCustomCheck);
        Log.d("Machine Check", "Rent Avail: " + rentAvailCheck);
        Log.d("Machine Check", "Condition Present: " + conditionPresentCheck);
        Log.d("Machine Check", "Other Problems: " + otherProblemsCheck);
        Log.d("Machine Check", "Machine Loc: " + locationMachineCheck + spinLocationOfMachine.getSelectedItemPosition());
        Log.d("Machine Check", "Garage Loc: " + locationGarageCheck + singlespinProvince.getSelectedItem().toString() + singlespinMunicipality.getSelectedItem().toString() +
                singlespinBarangay.getSelectedItem().toString());

        return (respCheck && qrCheck && machineTypeInfoCheck && machineTypeSpecsCheck && ownershipCheck && loanCashCheck && purchGrantDonoCheck && agencyCheck &&
                yearSelectCheck && conditionAcquiredCheck && rentSelectCheck && rentCustomCheck && rentAvailCheck && conditionPresentCheck && otherProblemsCheck &&
                locationMachineCheck && locationGarageCheck);
    }

    private Bitmap scale(Bitmap bitmap) {
        // Determine the constrained dimension, which determines both dimensions.
        int width;
        int height;
        float widthRatio = (float) bitmap.getWidth() / 1080;
        float heightRatio;
        heightRatio = (float) bitmap.getHeight() / 1920;
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = 1080;
            height = (int) (((float) width / bitmap.getWidth()) * bitmap.getHeight());
        }
        // Height constrained.
        else {
            height = 1920;
            width = (int) (((float) height / bitmap.getHeight()) * bitmap.getWidth());
        }
        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float ratioX = (float) width / bitmap.getWidth();
        float ratioY = (float) height / bitmap.getHeight();
        float middleX = width / 2.0f;
        float middleY = height / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

    public void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }  //TODO ADD PICTURE INTENT


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WRITE_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Write Permission granted");
            } else {
                Log.e("value", "Write Permission not granted");
            }

        }
    }

    private boolean checkExternalPermission() {
        int result = ContextCompat.checkSelfPermission(AddMachineActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestExternalPermission() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(AddMachineActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(AddMachineActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERM_CODE);
        }  //            Toast.makeText(AddMachineActivity.this, "NECESITAMOS QUE NOS CONCEDAS LOS PERMISOS DE ALMACENAMIENTO PARA GUARDAR NOTICIAS O RADIOS COMO FAVORITOS.", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
//                File f = new File(currentPhotoPath);
//                selectedImage.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));
//
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap = scale(bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgInByte = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(imgInByte, Base64.DEFAULT);
                selectedImage.setImageBitmap(bitmap);

            }

        }

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            tvLat.setText(data.getStringExtra("strLat"));
            tvLong.setText(data.getStringExtra("StrLong"));
            tvAcc.setText(data.getStringExtra("StrAcc"));
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //Code for getting image as URI
                /* Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri); */

                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                bitmap = scale(bitmap, 576, 1024);
                bitmap = scale(bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgInByte = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(imgInByte, Base64.DEFAULT);
                selectedImage.setImageBitmap(bitmap);
            }

        }

        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Log.e("Check", "Receiving data");
                    Barcode barcode = data.getParcelableExtra("barcode");
                    edtQRCode.setText(barcode.displayValue);
                } else {
                    edtQRCode.setHint("No Barcode found");
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initViews() {
        camera = findViewById(R.id.btnCamera);
        gallery = findViewById(R.id.btnGallery);
        selectedImage = findViewById(R.id.imgMachine);
        getLocation = findViewById(R.id.btnGetLocation);
        edtQRCode = findViewById(R.id.edtQRCode);
        btnScanQR = findViewById(R.id.btnScanQRCodeMach);
        tvPrevResp = findViewById(R.id.tvPrevResp);
        tvPrevious = findViewById(R.id.tvPrevious);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        tvAcc = findViewById(R.id.tvAccuracy);
        spinMachineType = findViewById(R.id.spinMachineType);
        btnSave = findViewById(R.id.btnSaveNewMachine);
        multspinProblemsUnused = findViewById(R.id.multspinProblemsUnused);
        rbCash = findViewById(R.id.rbCash);
        rbLoan = findViewById(R.id.rbLoan);
        rgLoanCash = findViewById(R.id.rgLoanCash);
//        multspinRentBrgy = findViewById(R.id.multispinRentBrgy);
//        spinRentMun = findViewById(R.id.spinRentMunicipality);
//        spinRentProv = findViewById(R.id.spinRentProvince);
        singlespinProvince = findViewById(R.id.singlespinProvince);
        singlespinMunicipality = findViewById(R.id.singlespinMunicipalities);
        singlespinBarangay = findViewById(R.id.singlespinBarangays);

//        edtRentProv = findViewById(R.id.edtRentProv);
//        edtRentMun = findViewById(R.id.edtRentMun);
//        edtRentBrgy = findViewById(R.id.edtRentBrgy);
        tvProvRent = findViewById(R.id.tvProvinceRent);
        tvMunRent = findViewById(R.id.tvMunicipalityRent);
        tvBrgyRent = findViewById(R.id.tvBarangayRent);
        multSpinProvRent = findViewById(R.id.multSpinProvRent);
        multSpinMunRent = findViewById(R.id.multSpinMunRent);
        multSpinBrgyRent = findViewById(R.id.multSpinBrgyRent);

        tvMachineUnused = findViewById(R.id.tvMachineUnused);


        tvTypeOfMill = findViewById(R.id.tvTypeMill);
        spinTypeOfMill = findViewById(R.id.spinTypeMill);
        tvBrand = findViewById(R.id.tvBrand);
        edtCapacity = findViewById(R.id.edtCapacity);
        tvOwnership = findViewById(R.id.tvOwnership);
        edtAveYield = findViewById(R.id.edtAveYield);
        edtNumLoads = findViewById(R.id.edtNumberOfLoads);
        edtRate = findViewById(R.id.edtRate);
        tvTypeOfTubewells = findViewById(R.id.tvTypeOfTubewells);
        spinTypeofTubeWells = findViewById(R.id.spinTypeOfTubewells);
        edtAveOpDays = findViewById(R.id.edtAverageOperatingDays);
        edtAveOpHours = findViewById(R.id.edtAverageOperatingHours);
        tvNewlyPlantedArea = findViewById(R.id.tvNewlyPlantedArea);
        tvTotalServiceArea = findViewById(R.id.tvTotalServiceAreaMachine);
        edtTotalServiceArea = findViewById(R.id.edtTotalServiceAreaMachine);
        edtNewlyPlantedArea = findViewById(R.id.edtNewlyPlantedArea);
        tvRatoonArea = findViewById(R.id.tvRatoonArea);
        edtRatoonArea = findViewById(R.id.edtRatoonArea);
        spinOwnership = findViewById(R.id.spinOwnership);
        spinPurchGrantDono = findViewById(R.id.spinPurchGrantDono);
        spinAgency = findViewById(R.id.spinAgency);
        edtNameOfOwnerOrg = findViewById(R.id.edtNameOfOwnerOrOrganization);
        spinYearAcquired = findViewById(R.id.spinYearAcquired);
        spinRental = findViewById(R.id.spinRental);

        tvCustomRate = findViewById(R.id.tvCustomRateMain);
        tvCustomUnit = findViewById(R.id.tvCustomUnitMain);
        edtCustomRate = findViewById(R.id.edtMainRent);
        spinCustomUnit = findViewById(R.id.spinMainRentUnit);
        tvPlowingRent = findViewById(R.id.tvPlowingRent);
        tvCustomUnitOther = findViewById(R.id.tvCustomUnitOther);
        edtPlowingRent = findViewById(R.id.edtPlowingRent);
        spinPlowingRentUnit = findViewById(R.id.spinPlowingRentUnit);
        tvHarrowingRent = findViewById(R.id.tvHarrowingRent);
        edtHarrowingRent = findViewById(R.id.edtHarrowingRent);
        spinHarrowingRentUnit = findViewById(R.id.spinHarrowingRentUnit);
        tvFurrowingRent = findViewById(R.id.tvFurrowingRent);
        edtFurrowingRent = findViewById(R.id.edtFurrowingRent);
        spinFurrowingRentUnit = findViewById(R.id.spinFurrowingRentUnit);
        tvOtherRent = findViewById(R.id.tvOtherRent);
        edtOtherRent = findViewById(R.id.edtOtherRent);
        spinOtherRentUnit = findViewById(R.id.spinOtherRentUnit);

        tvAveFuelConsPlow = findViewById(R.id.tvAveFuelConsumptionPlowing);
        edtAveFuelConsPlow = findViewById(R.id.edtAveFuelConsumptionPlowing);
        tvAveFuelConsHarr = findViewById(R.id.tvAveFuelConsumptionHarrowing);
        edtAveFuelConsHarr = findViewById(R.id.edtAveFuelConsumptionHarrowing);
        tvAveFuelConsFurr = findViewById(R.id.tvAveFuelConsumptionFurrowing);
        edtAveFuelConsFurr = findViewById(R.id.edtAveFuelConsumptionFurrowing);

        edtCustomRateUnit = findViewById(R.id.edtCustomRateUnit);//TODO Remove this
        tvMachineAvailability = findViewById(R.id.tvMachineAvailability);
        spinAvailability = findViewById(R.id.spinAvailability);
        tvConditionPresent = findViewById(R.id.tvConditionPresent);
        spinConditionPresent = findViewById(R.id.spinConditionPresent);
        tvYearInoperable = findViewById(R.id.tvYearInoperable);
        spinYearInoperable = findViewById(R.id.spinYearInoperable);
        tvLocation = findViewById(R.id.tvLocationOfMachine);
        spinConditionAcquired = findViewById(R.id.spinConditionAcquired);

        tvCapacity = findViewById(R.id.tvCapacity);
        tvRate = findViewById(R.id.tvRate);
        tvNumLoads = findViewById(R.id.tvNumLoads);
        tvAveYield = findViewById(R.id.tvAveYield);
//        spinProvince = findViewById(R.id.spinProvince);
//        spinMunicipality = findViewById(R.id.spinMunicipality);
//        singlespinBarangay = findViewById(R.id.singlespinBrgy);
        edtOtherAgency = findViewById(R.id.edtOtherAgency);
        edtOtherBrand = findViewById(R.id.edtOtherBrand);
        edtOtherModel = findViewById(R.id.edtOtherModel);
        spinBrand = findViewById(R.id.spinBrand);
        spinModel = findViewById(R.id.spinModel);
        tvModel = findViewById(R.id.tvModel);
        edtRatedPower = findViewById(R.id.edtRatedPower);
        edtOtherProblems = findViewById(R.id.edtOtherProblems);
//        tvCommas = findViewById(R.id.tvCommas);
        spinLocationOfMachine = findViewById(R.id.spinLocationOfMachine);
        spinRespName = findViewById(R.id.spinRespondentName);

        tvPurchGrantDono = findViewById(R.id.tvPurchGrantDono);
        tvAgency = findViewById(R.id.tvAgency);
        edtPlowSpecifyUnit = findViewById(R.id.edtPlowSpecifyUnit);
        edtHarrSpecifyUnit = findViewById(R.id.edtHarrSpecifyUnit);
        edtFurrSpecifyUnit = findViewById(R.id.edtFurrSpecifyUnit);
        edtOthrSpecifyUnit = findViewById(R.id.edtOthrSpecifyUnit);
        edtOthrSpecifyOperation = findViewById(R.id.edtOthrSpecifyOperation);

        tvRatedPowerInfo = findViewById(R.id.tvRatedPoweInfo);
        tvAveOpHoursInfo = findViewById(R.id.tvAveOpHoursInfo);
        tvAveOpDaysInfo = findViewById(R.id.tvAveOpDaysInfo);
        tvNameOfOwnerOrOrganizationInfo = findViewById(R.id.tvNameOfOwnerOrOrganizationInfo);
        tvConditionAcquiredInfo = findViewById(R.id.tvConditionAcquiredInfo);
        tvNewlyPlantedAreaInfo = findViewById(R.id.tvNewlyPlantedAreaInfo);
        tvRatoonAreaInfo = findViewById(R.id.tvRatoonAreaInfo);
        tvPlowingRentInfo = findViewById(R.id.tvPlowingRentInfo);
        tvHarrowingRentInfo = findViewById(R.id.tvHarrowingRentInfo);
        tvFurrowingRentInfo = findViewById(R.id.tvFurrowingRentInfo);
        tvMachineAvailabilityInfo = findViewById(R.id.tvMachineAvailabilityInfo);
        tvRatedPower = findViewById(R.id.tvRatedPower);
        tvAveOpHours = findViewById(R.id.tvAveOpHours);
        tvAveOpDays = findViewById(R.id.tvAveOpDays);
        tvNameOfOwnerOrOrganization = findViewById(R.id.tvNameOfOwnerOrOrganization);

        tvConditionAcquired = findViewById(R.id.tvConditionAcquired);

        tvTimeUsedWorking = findViewById(R.id.tvTimeUsedWorkingHarvester);
        edtTimeUsedWorking = findViewById(R.id.edtTimeUsedWorkingHarvester);
        tvEffectiveArea = findViewById(R.id.edtEffectiveAreaHarvester);
        edtEffectiveArea = findViewById(R.id.edtEffectiveAreaHarvester);

    }

    private void hide() {
        tvTypeOfMill.setVisibility(View.INVISIBLE);
        spinTypeOfMill.setVisibility(View.INVISIBLE);
        tvTypeOfTubewells.setVisibility(View.INVISIBLE);
        spinTypeofTubeWells.setVisibility(View.INVISIBLE);
        spinPurchGrantDono.setVisibility(View.INVISIBLE);
        spinAgency.setVisibility(View.INVISIBLE);
        edtCustomRate.setVisibility(View.INVISIBLE);
        spinCustomUnit.setVisibility(View.INVISIBLE);
        multspinProblemsUnused.setVisibility(View.INVISIBLE);
        tvYearInoperable.setVisibility(View.INVISIBLE);
        spinYearInoperable.setVisibility(View.INVISIBLE);
        edtOtherAgency.setVisibility(View.INVISIBLE);
        edtOtherModel.setVisibility(View.INVISIBLE);
        edtOtherBrand.setVisibility(View.INVISIBLE);
        edtOtherProblems.setVisibility(View.INVISIBLE);
        tvBrgyRent.setVisibility(View.INVISIBLE);
        tvMunRent.setVisibility(View.INVISIBLE);
        tvProvRent.setVisibility(View.INVISIBLE);
        multSpinProvRent.setVisibility(View.INVISIBLE);
        multSpinMunRent.setVisibility(View.INVISIBLE);
        multSpinBrgyRent.setVisibility(View.INVISIBLE);
        tvCustomRate.setVisibility(View.INVISIBLE);
        tvCustomUnit.setVisibility(View.INVISIBLE);
        tvMachineUnused.setVisibility(View.INVISIBLE);
        tvCapacity.setVisibility(View.INVISIBLE);
        tvRate.setVisibility(View.INVISIBLE);
        tvNumLoads.setVisibility(View.INVISIBLE);
        tvAveYield.setVisibility(View.INVISIBLE);
        tvPurchGrantDono.setVisibility(View.INVISIBLE);
        tvAgency.setVisibility(View.INVISIBLE);
        rgLoanCash.setVisibility(View.INVISIBLE);
        edtCustomRateUnit.setVisibility(View.INVISIBLE);


        tvPlowingRent.setVisibility(View.INVISIBLE);
        tvHarrowingRent.setVisibility(View.INVISIBLE);
        tvFurrowingRent.setVisibility(View.INVISIBLE);
        tvOtherRent.setVisibility(View.INVISIBLE);
        tvCustomUnitOther.setVisibility(View.INVISIBLE);

        edtPlowingRent.setVisibility(View.INVISIBLE);
        edtFurrowingRent.setVisibility(View.INVISIBLE);
        edtHarrowingRent.setVisibility(View.INVISIBLE);
        edtOtherRent.setVisibility(View.INVISIBLE);

        spinPlowingRentUnit.setVisibility(View.INVISIBLE);
        spinFurrowingRentUnit.setVisibility(View.INVISIBLE);
        spinHarrowingRentUnit.setVisibility(View.INVISIBLE);
        spinOtherRentUnit.setVisibility(View.INVISIBLE);

        tvAveFuelConsPlow.setVisibility(View.INVISIBLE);
        tvAveFuelConsHarr.setVisibility(View.INVISIBLE);
        tvAveFuelConsFurr.setVisibility(View.INVISIBLE);

        edtAveFuelConsPlow.setVisibility(View.INVISIBLE);
        edtAveFuelConsHarr.setVisibility(View.INVISIBLE);
        edtAveFuelConsFurr.setVisibility(View.INVISIBLE);

        tvPrevious.setVisibility(View.INVISIBLE);
        tvPrevResp.setVisibility(View.INVISIBLE);

        edtPlowSpecifyUnit.setVisibility(View.INVISIBLE);
        edtFurrSpecifyUnit.setVisibility(View.INVISIBLE);
        edtHarrSpecifyUnit.setVisibility(View.INVISIBLE);
        edtOthrSpecifyUnit.setVisibility(View.INVISIBLE);
        edtOthrSpecifyOperation.setVisibility(View.INVISIBLE);

        tvPlowingRentInfo.setVisibility(View.INVISIBLE);
        tvHarrowingRentInfo.setVisibility(View.INVISIBLE);
        tvFurrowingRentInfo.setVisibility(View.INVISIBLE);

        tvMachineAvailabilityInfo.setVisibility(View.INVISIBLE);
        tvNewlyPlantedArea.setVisibility(View.INVISIBLE);
        tvNewlyPlantedAreaInfo.setVisibility(View.INVISIBLE);
        tvRatoonArea.setVisibility(View.INVISIBLE);
        tvRatoonAreaInfo.setVisibility(View.INVISIBLE);


        //Hiding Location Spinners
//        spinProvince.setVisibility(View.INVISIBLE);
//        spinMunicipality.setVisibility(View.INVISIBLE);
//        singlespinBarangay.setVisibility(View.INVISIBLE);
        //Hiding Rent Locations
//        spinRentProv.setVisibility(View.INVISIBLE);
//        spinRentMun.setVisibility(View.INVISIBLE);
//        multspinRentBrgy.setVisibility(View.INVISIBLE);
//        edtRentProv.setVisibility(View.INVISIBLE);
//        edtRentMun.setVisibility(View.INVISIBLE);
//        edtRentBrgy.setVisibility(View.INVISIBLE);
//        tvCommas.setVisibility(View.INVISIBLE);
        //TODO Hide all new TextViews Added
    }

    private String getFieldCapacity(String area, String time) {
        String fieldCapacity;
        if (!isNullOrEmpty(area) && !isNullOrEmpty(time)) {
            fieldCapacity = String.valueOf(Double.parseDouble(area) / Double.parseDouble(time));
        } else {
            fieldCapacity = "0.0";
        }

        return fieldCapacity;
    }

    class Respondent {
        String name;
        String code;

        Respondent(String name, String code) {
            this.name = name;
            this.code = code;
        }
    }
}
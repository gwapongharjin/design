package com.m3das.biomech.design;

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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import org.apache.commons.lang3.math.NumberUtils;

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

public class AddImplementActivity extends AppCompatActivity {

    private Spinner spinImplementType, spinMachineUsing, spinTypeOfPlanter, spinYearAcquired, spinLocation, spinConditionAcquired, spinYearInoperable, spinConditionPresent, spinOwnership, spinPurchGrantDono, spinAgency;
    ImageButton camera, gallery, getLocation, btnScanQR;
    String dateToStr, machineComplete, landClear, prePlant, planting, fertilizer, pesticide, irrigationDrainage, listOfProblems, purchGrantDono,
            cultivation, ratooning, harvest, postHarvest, hauling, encodedImage, locProvince, locMunicipality, locBarangay;
    ImageView selectedImage;
    Button btnSave;
    private Intent intentFromDb;
    SingleSpinnerSearch singlespinProvinces, singlespinMunicipalities, singlespinBarangays;
    MultiSpinnerSearch multspinProblemsUnused;
    ConstraintLayout.LayoutParams paramsYearAcquired, paramsEAMain, paramstvPlanter, paramsEAFert, paramsEAHarvest, paramsEAGrab, paramsDCDitch, paramstvImplementUnused, paramstvLocation, paramstvConditionPresent, paramstvOwnership;
    EditText edtQRCode, edtOtherProblems, edtEffectiveAreaAccompMain, edtTimeUsedDuringOpMain, edtOtherAgency, edtModifications, edtModel, edtBrand,
            edtNumberofRowsPlant, edtDistanceofPlantMat, edtEffectiveAreaAccompPlant, edtTimeUsedDuringOpPlant,
            edtEffectiveAreaAccompFert, edtTimeUsedDuringOpFert, edtWeightOfFert,
            edtEffectiveAreaAccompHarvest, edtTimeUsedDuringOpHarvest, edtAveYieldHarvest,
            edtEffectiveAreaAccompGrab, edtTimeUsedDuringOpGrab, edtLoadCapacityGrab,
            edtDepthOfCutDitch, edtEffectiveAreaAccompFertForWeight;
    TextView tvYearAcquired, tvYearInoperable, tvLat, tvLong, tvAcc, tvLocation, tvImplementUnused, tvOwnership, tvPurchGrantDono, tvAgency, tvConditionPresent,
            tvFieldCapHeader, tvFieldCapHeaderInfo, tvDelRateHeader, tvDelRateHeaderInfo, tvFieldCapHeaderPlant, tvFieldCapHeaderInfoPlant,
            tvHaEAMain, tvHoursPDayOpMain, tvFieldCapacityMain, tvFieldCapacityResultMain,
            tvTypeofPlant, tvDistanceofPlant, tvNumRowsPlant, tvHaEAPlant, tvHoursPDayOpPlant, tvFieldCapacityPlant, tvFieldCapacityResultPlant,
            tvHaEAFert, tvHoursPDayOpFert, tvFieldCapacityFert, tvFieldCapacityResultFert, tvWeightOfFert, tvDeliveryRateFert, tvDeliveryRateResultFert,
            tvHaEAHarvest, tvHoursPDayOpHarvest, tvFieldCapacityHarvest, tvFieldCapacityResultHarvest, tvTonCannesPHaHarvest,
            tvHaEAGrab, tvHoursPDayOpGrab, tvLoadCapacityGrab, tvFieldCapacityGrab, tvFieldCapacityResultGrab,
            tvDepthCutDitch, tvHaEAFert2;
    CheckBox cbLandClear, cbPrePlant, cbPlant, cbFert, cbPest, cbIrriDrain, cbCult, cbRatoon, cbHarvest, cbPostHarvest, cbHaul;
    int bigMargin, smallMargin;
    View topLine, botLineMain, topLinePlant, botLinePlant, botLineFertDR, botLineFert, botLineHarvest, botLineGrab;
    Double fieldCap = null;
    Double deliveryCap = null;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int LOCATION_REQUEST_CODE = 127;
    public static final int WRITE_PERM_CODE = 279;
    static Uri capturedImageUri;
    public static final String EXTRA_IMP_ID = "ADDIMPLEMENT_EXTRA_ID";
    public static final String EXTRA_IMP_TYPE = "ADDIMPLEMENT_EXTRA_IMP_TYPE";
    public static final String EXTRA_IMP_QR = "ADDIMPLEMENT_EXTRA_IMP_QR";
    public static final String EXTRA_DATE = "ADDIMPLEMENT_EXTRA_DATE";
    public static final String EXTRA_USED_ON = "ADDIMPLEMENT_EXTRA_USED_ON";
    public static final String EXTRA_USED_COMPLETE = "ADDIMPLEMENT_EXTRA_USED_COMPLETE";

    public static final String EXTRA_BRAND = "ADDIMPLEMENT_EXTRA_BRAND";
    public static final String EXTRA_MODEL = "ADDIMPLEMENT_EXTRA_MODEL";

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

    public static final String EXTRA_EFF_AREA_ACC_FERT_WEIGHT = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_FERT_WEIGHT";
    public static final String EXTRA_EFF_AREA_ACC_MAIN = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_MAIN";
    public static final String EXTRA_TIME_USED_OP_MAIN = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_MAIN";
    public static final String EXTRA_FIELD_CAP_MAIN = "ADDIMPLEMENT_EXTRA_FIELD_CAP_MAIN";

    public static final String EXTRA_TYPE_PLANT = "ADDIMPLEMENT_EXTRA_TYPE_PLANT";
    public static final String EXTRA_NUM_ROWS_PLANT = "ADDIMPLEMENT_EXTRA_NUM_ROWS_PLANT";
    public static final String EXTRA_DIST_MAT_PLANT = "ADDIMPLEMENT_EXTRA_DIST_MAT_PLANT";
    public static final String EXTRA_EFF_AREA_ACC_PLANT = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_PLANT";
    public static final String EXTRA_TIME_USED_OP_PLANT = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_PLANT";
    public static final String EXTRA_FIELD_CAP_PLANT = "ADDIMPLEMENT_EXTRA_FIELD_CAP_PLANT";

    public static final String EXTRA_EFF_AREA_ACC_FERT = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_FERT";
    public static final String EXTRA_TIME_USED_OP_FERT = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_FERT";
    public static final String EXTRA_FIELD_CAP_FERT = "ADDIMPLEMENT_EXTRA_FIELD_CAP_FERT";
    public static final String EXTRA_WEIGHT_FERT = "ADDIMPLEMENT_EXTRA_WEIGHT_FERT";
    public static final String EXTRA_DEL_RATE_FERT = "ADDIMPLEMENT_EXTRA_DEL_RATE_FERT";

    public static final String EXTRA_EFF_AREA_ACC_HARVEST = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_HARVEST";
    public static final String EXTRA_TIME_USED_OP_HARVEST = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_HARVEST";
    public static final String EXTRA_FIELD_CAP_HARVEST = "ADDIMPLEMENT_EXTRA_FIELD_CAP_HARVEST";
    public static final String EXTRA_AVE_YIELD_HARVEST = "ADDIMPLEMENT_EXTRA_AVEYIELD_HARVEST";

    public static final String EXTRA_EFF_AREA_ACC_GRAB = "ADDIMPLEMENT_EXTRA_EFF_AREA_ACC_GRAB";
    public static final String EXTRA_TIME_USED_OP_GRAB = "ADDIMPLEMENT_EXTRA_TIME_USED_OP_GRAB";
    public static final String EXTRA_LOAD_CAP_GRAB = "ADDIMPLEMENT_EXTRA_LOAD_CAP_GRAB";
    public static final String EXTRA_FIELD_CAP_GRAB = "ADDIMPLEMENT_EXTRA_FIELD_CAP_GRAB";

    public static final String EXTRA_DEPTH_CUT_DITCH = "ADDIMPLEMENT_EXTRA_DEPTH_CUT_DITCH";

    public static final String EXTRA_OWNERSHIP = "ADDIMPLEMENT_EXTRA_OWNERSHIP";
    public static final String EXTRA_PURCH_GRANT_DONO = "ADDIMPLEMENT_EXTRA_PURCH_GRANT_DONO";
    public static final String EXTRA_AGENCY = "ADDIMPLEMENT_EXTRA_AGENCY";
    public static final String EXTRA_AGENCY_SPECIFY = "ADDIMPLEMENT_EXTRA_AGENCY_SPECIFY";

    public static final String EXTRA_YEAR_ACQUIRED = "ADDIMPLEMENT_EXTRA_YEAR_ACQUIRED";
    public static final String EXTRA_CONDITION_ACQUIRED = "ADDIMPLEMENT_EXTRA_CONDITION_ACQUIRED";
    public static final String EXTRA_CONDITION_PRESENT = "ADDIMPLEMENT_EXTRA_CONDITION_PRESENT";


    public static final String EXTRA_MODIFICATIONS = "ADDIMPLEMENT_EXTRA_MODIFICATIONS";
    public static final String EXTRA_PROBLEMS = "ADDIMPLEMENT_EXTRA_PROBLEMS";
    public static final String EXTRA_PROBLEMS_SPECIFY = "ADDIMPLEMENT_EXTRA_PROBLEMS_SPECIFY";
    public static final String EXTRA_YEAR_INOPERABLE = "ADDIMPLEMENT_EXTRA_YEAR_INOPERABLE";

    public static final String EXTRA_LOCATION = "ADDIMPLEMENT_EXTRA_LOCATION";
    public static final String EXTRA_PROVINCE = "ADDIMPLEMENT_EXTRA_PROVINCE";
    public static final String EXTRA_MUNICIPALITY = "ADDIMPLEMENT_EXTRA_MUNICIPALITY";
    public static final String EXTRA_BARANGAY = "ADDIMPLEMENT_EXTRA_BARANGAY";
    public static final String EXTRA_LATITUDE = "ADDIMPLEMENT_EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "ADDIMPLEMENT_EXTRA_LONGITUDE";
    public static final String EXTRA_ACCURACY = "ADDIMPLEMENT_EXTRA_ACCURACY";
    ArrayList<MachineClass> machineArrayList = new ArrayList<>();
    String temp1, temp2;
    private boolean implementSpecsCheck, qrCheck, machineUsingCheck, operationCheck, locationGarageCheck, locationImplementCheck, conditionPresentCheck, yearSelectCheck,
            hasOtherProblems, ownershipCheck, purchGrantDonoCheck, agencyCheck, conditionAcquiredCheck, otherProblemsCheck, brandCheck, modelCheck, fieldCapCheck, qrFormatCheck;
    ;


    private String resLat, resLong;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitByBackKey() {
        new AlertDialog.Builder(this)
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
        setContentView(R.layout.add_implement_activity);


        initViews();
        initAllLayoutParameters();
        setMarginSize();
        hideAll();
        setCheckBoxData();
        initTextViewData();
        setOnTouchEditText();
        setInputFilters();


        paramsYearAcquired.topToBottom = R.id.cbHauling;
        paramsYearAcquired.topMargin = bigMargin;
        tvYearAcquired.setLayoutParams(paramsYearAcquired);
        machineArrayList = new ArrayList<MachineClass>();

        spinYearsSetAdapter();


        MachineListViewModel machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);
        machineListViewModel.getAllMachines().observe(this, new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {
                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add("Please Select...");
                for (int i = 0; i < machines.size(); i++) {
//                    stringArrayList.add(counter + machines.get(i).getId() + " " + machines.get(i).getMachine_qrcode() + " | " + machines.get(i).getMachine_type());
                    stringArrayList.add((i + 1) + " | " + machines.get(i).getMachine_qrcode() + " | " + machines.get(i).getMachine_type());
                    machineArrayList.add(new MachineClass(machines.get(i).getMachine_qrcode(), machines.get(i).getMachine_type()));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stringArrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinMachineUsing.setAdapter(adapter);


                Log.d("ADMEDTRESC", "size" + machineArrayList.size());
                Log.d("ADMEDTRESC", "extra" + getIntent().getStringExtra(EXTRA_USED_ON));
                if (getIntent().hasExtra(EXTRA_IMP_ID)) {
                    String comp = getIntent().getStringExtra(EXTRA_USED_ON);
                    int position = 0;
                    for (int i = 0; i < machineArrayList.size(); i++) {
                        Log.d("ADMEDTRESC", "name" + machineArrayList.get(i).type);
                        Log.d("ADMEDTRESC", "code" + machineArrayList.get(i).code);
                        if (machineArrayList.get(i).code.equals(comp)) {
                            position = i + 1;
                        }
                    }
                    spinMachineUsing.setSelection(position);
                    spinMachineUsing.setEnabled(false);
                }
            }
        });

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
                cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, "720000");
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                Toast.makeText(AddImplementActivity.this, "Camera Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        gallery.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);

            Toast.makeText(AddImplementActivity.this, "Gallery Clicked", Toast.LENGTH_SHORT).show();
        });

        btnScanQR.setOnClickListener(view -> {
            Intent intent = new Intent(AddImplementActivity.this, ScanBarcodeActivity.class);
            startActivityForResult(intent, 0);
            Toast.makeText(AddImplementActivity.this, "Scanning QR", Toast.LENGTH_SHORT).show();
        });

        getLocation.setOnClickListener(view -> {
            Toast.makeText(AddImplementActivity.this, "Map Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LocationMapsActivity.class);
            startActivityForResult(intent, LOCATION_REQUEST_CODE);
        });

        spinMachineUsing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String used = "";
                clearCursor();
                used = spinMachineUsing.getSelectedItem().toString();
                Log.d("Machine using", used.substring(7, 8) + used.substring(2, 14));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinImplementType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectImplement(spinImplementType.getSelectedItem().toString());
                clearCursor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        singlespinMunicipalities.setEnabled(false);
        singlespinBarangays.setEnabled(false);

        List<KeyPairBoolData> allProvinces = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.provinces)));
        singlespinProvinces.setItems(allProvinces, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                clearCursor();

                Log.d("Single Prov", selectedItem.getName());
                locProvince = selectedItem.getName();

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImplement();
            }
        });

        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearCursor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinTypeOfPlanter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearCursor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cbLandClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbLandClear.isChecked()) {
                    landClear = "LAND CLEAR";
                } else {
                    landClear = "";
                }

            }
        });

        cbPrePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbPrePlant.isChecked()) {
                    prePlant = "LAND PREPARATION";
                } else {
                    prePlant = "";
                }

            }
        });

        cbPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbPlant.isChecked()) {
                    planting = "PLANTING";
                } else {
                    planting = "";
                }

            }
        });

        cbFert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbFert.isChecked()) {
                    fertilizer = "FERTILIZER APPLICATION";
                } else {
                    fertilizer = "";
                }

            }
        });

        cbPest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbPest.isChecked()) {
                    pesticide = "PESTICIDE APPLICATION";
                } else {
                    pesticide = "";
                }

            }
        });

        cbIrriDrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbIrriDrain.isChecked()) {
                    irrigationDrainage = "IRRIGATION & DRAINAGE";
                } else {
                    irrigationDrainage = "";
                }

            }
        });

        cbCult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbCult.isChecked()) {
                    cultivation = "CULTIVATION";
                } else {
                    cultivation = "";
                }

            }
        });

        cbRatoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRatoon.isChecked()) {
                    ratooning = "RATOONING";
                } else {
                    ratooning = "";
                }

            }
        });

        cbHarvest.setOnClickListener(v -> {

            if (cbHarvest.isChecked()) {
                harvest = "HARVESTING";
            } else {
                harvest = "";
            }


        });

        cbPostHarvest.setOnClickListener(v -> {
            if (cbPostHarvest.isChecked()) {
                postHarvest = "POST HARVEST";
            } else {
                postHarvest = "";
            }

        });

        cbHaul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbHaul.isChecked()) {
                    hauling = "HAULING";
                } else {
                    hauling = "";
                }

            }
        });

        edtEffectiveAreaAccompMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())) {

                    tvFieldCapacityResultMain.setText(getFieldCapacity(edtEffectiveAreaAccompMain.getText().toString(), edtTimeUsedDuringOpMain.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())) {

                    tvFieldCapacityResultMain.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTimeUsedDuringOpMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())) {
                    tvFieldCapacityResultMain.setText(getFieldCapacity(edtEffectiveAreaAccompMain.getText().toString(), edtTimeUsedDuringOpMain.getText().toString()));
                } else if (isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())) {

                    tvFieldCapacityResultMain.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEffectiveAreaAccompPlant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompPlant.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpPlant.getText().toString())) {

                    tvFieldCapacityResultPlant.setText(getFieldCapacity(edtEffectiveAreaAccompPlant.getText().toString(), edtTimeUsedDuringOpPlant.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompPlant.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpPlant.getText().toString())) {

                    tvFieldCapacityResultPlant.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTimeUsedDuringOpPlant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompPlant.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpPlant.getText().toString())) {

                    tvFieldCapacityResultPlant.setText(getFieldCapacity(edtEffectiveAreaAccompPlant.getText().toString(), edtTimeUsedDuringOpPlant.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompPlant.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpPlant.getText().toString())) {

                    tvFieldCapacityResultPlant.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEffectiveAreaAccompFert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompFert.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpFert.getText().toString())) {

                    tvFieldCapacityResultFert.setText(getFieldCapacity(edtEffectiveAreaAccompFert.getText().toString(), edtTimeUsedDuringOpFert.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompFert.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpFert.getText().toString())) {

                    tvFieldCapacityResultFert.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTimeUsedDuringOpFert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompFert.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpFert.getText().toString())) {

                    tvFieldCapacityResultFert.setText(getFieldCapacity(edtEffectiveAreaAccompFert.getText().toString(), edtTimeUsedDuringOpFert.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompFert.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpFert.getText().toString())) {

                    tvFieldCapacityResultFert.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtWeightOfFert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompFertForWeight.getText().toString()) && !isNullOrEmpty(edtWeightOfFert.getText().toString())) {

                    tvDeliveryRateResultFert.setText(getFieldCapacity(edtWeightOfFert.getText().toString(), edtEffectiveAreaAccompFertForWeight.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompFertForWeight.getText().toString()) || isNullOrEmpty(edtWeightOfFert.getText().toString())) {

                    tvDeliveryRateResultFert.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEffectiveAreaAccompFertForWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompFertForWeight.getText().toString()) && !isNullOrEmpty(edtWeightOfFert.getText().toString())) {

                    tvDeliveryRateResultFert.setText(getFieldCapacity(edtWeightOfFert.getText().toString(), edtEffectiveAreaAccompFertForWeight.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompFertForWeight.getText().toString()) || isNullOrEmpty(edtWeightOfFert.getText().toString())) {

                    tvDeliveryRateResultFert.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEffectiveAreaAccompHarvest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompHarvest.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpHarvest.getText().toString())) {
                    tvFieldCapacityResultHarvest.setText(getFieldCapacity(edtEffectiveAreaAccompHarvest.getText().toString(), edtTimeUsedDuringOpHarvest.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompHarvest.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpHarvest.getText().toString())) {

                    tvFieldCapacityResultHarvest.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTimeUsedDuringOpHarvest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompHarvest.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpHarvest.getText().toString())) {
                    tvFieldCapacityResultHarvest.setText(getFieldCapacity(edtEffectiveAreaAccompHarvest.getText().toString(), edtTimeUsedDuringOpHarvest.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompHarvest.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpHarvest.getText().toString())) {

                    tvFieldCapacityResultHarvest.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtEffectiveAreaAccompGrab.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompGrab.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpGrab.getText().toString())) {
                    tvFieldCapacityResultGrab.setText(getFieldCapacity(edtEffectiveAreaAccompGrab.getText().toString(), edtTimeUsedDuringOpGrab.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompGrab.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpGrab.getText().toString())) {

                    tvFieldCapacityResultGrab.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTimeUsedDuringOpGrab.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompGrab.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpGrab.getText().toString())) {
                    tvFieldCapacityResultGrab.setText(getFieldCapacity(edtEffectiveAreaAccompGrab.getText().toString(), edtTimeUsedDuringOpGrab.getText().toString()));

                } else if (isNullOrEmpty(edtEffectiveAreaAccompGrab.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpGrab.getText().toString())) {

                    tvFieldCapacityResultGrab.setText(R.string.not_yet_acq);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinConditionPresent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clearCursor();
                if (i == 1) {
                    spinConditionPresent.setBackground(getDrawable(R.drawable.custom_spinner_tobeacq));
                } else {
                    spinConditionPresent.setBackground(getDrawable(R.drawable.custom_spinner));
                }
                problemsUnused(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinOwnership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clearCursor();
                if (i == 1) {
                    spinOwnership.setBackground(getDrawable(R.drawable.custom_spinner_tobeacq));
                } else {
                    spinOwnership.setBackground(getDrawable(R.drawable.custom_spinner));
                }
                ownershipSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinPurchGrantDono.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clearCursor();
                purchGrantDonoSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinAgency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearCursor();
                otherAgency(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinConditionAcquired.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearCursor();
                if (position == 1) {
                    spinConditionAcquired.setBackground(getDrawable(R.drawable.custom_spinner_tobeacq));
                } else {
                    spinConditionAcquired.setBackground(getDrawable(R.drawable.custom_spinner));
                }
                conditionModifications(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinYearAcquired.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearCursor();
                if (position == 1) {
                    spinYearAcquired.setBackground(getDrawable(R.drawable.custom_spinner_tobeacq));
                } else {
                    spinYearAcquired.setBackground(getDrawable(R.drawable.custom_spinner));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtQRCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 12) {
                    if (!(s.toString().matches("^[R]\\d{2}[A-Z]{3}[I]\\d{5}"))) {
                        Toast.makeText(getApplicationContext(), "Invalid QR Code format. Please Check!", Toast.LENGTH_SHORT).show();
                        qrFormatCheck = false;
                    } else {
                        qrFormatCheck = true;
                    }
                } else {
                    qrFormatCheck = false;
                }
            }
        });

        tvFieldCapHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilang ektarya ang natapos ng implement sa itinakdang oras?", v);
            }
        });
        tvFieldCapHeaderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilang ektarya ang natapos ng implement sa itinakdang oras?", v);
            }
        });
        tvFieldCapHeaderPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilang ektarya ang natapos ng implement sa itinakdang oras?", v);
            }
        });
        tvFieldCapHeaderInfoPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilang ektarya ang natapos ng implement sa itinakdang oras?", v);
            }
        });

        tvDelRateHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilang kilo ng abono ang nagagamit para sa itinakdang ektarya?", v);
            }
        });

        tvDelRateHeaderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipShow("Ilang kilo ng abono ang nagagamit para sa itinakdang ektarya", v);
            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMP_ID)) {
            editItemSelected(intent);

        } else {
            setTitle("Adding Implement");
        }

    }

    private void toolTipShow(String toShow, View v) {
        new SimpleTooltip.Builder(getApplicationContext())
                .anchorView(v)
                .text(toShow)
                .gravity(Gravity.TOP)
                .textColor(getResources().getColor(R.color.white))
                .backgroundColor(getResources().getColor(R.color.lightMaroon))
                .arrowColor(getResources().getColor(R.color.lightMaroon))
                .maxWidth(getResources().getDimension(R.dimen.maxwidth))
                .build()
                .show();
    }

    private void sortUnavailableSingle() {
        clearCursor();
        List<KeyPairBoolData> allMunicipalities = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.municipalities)));
        List<KeyPairBoolData> allBarangays = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.barangays)));


        singlespinMunicipalities.setEnabled(true);
        singlespinBarangays.setEnabled(true);
        singlespinMunicipalities.setItems(allMunicipalities, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());
                locMunicipality = selectedItem.getName();
            }

            @Override
            public void onClear() {
                locMunicipality = "";
            }
        });

        singlespinBarangays.setItems(allBarangays, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());
                locBarangay = selectedItem.getName();
            }

            @Override
            public void onClear() {
                locBarangay = "";
            }
        });
    }

    private void sortBatangasSingle() {
        List<KeyPairBoolData> allMunBatangas = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.batangas_municipalities)));


        List<String> barangaysStringList = new ArrayList<>();
        singlespinMunicipalities.setEnabled(true);

        singlespinMunicipalities.setItems(allMunBatangas, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());
                locMunicipality = selectedItem.getName();
                clearCursor();
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
                singlespinBarangays.setEnabled(true);
                singlespinBarangays.setItems(pairingOfList(barangaysStringList), new SingleSpinnerListener() {
                    @Override
                    public void onItemsSelected(KeyPairBoolData selectedItem) {
                        clearCursor();
                        Log.d("Single Brgy", selectedItem.getName());
                        locBarangay = selectedItem.getName();
                    }

                    @Override
                    public void onClear() {
                        locBarangay = "";
                    }
                });
            }

            @Override
            public void onClear() {
                locMunicipality = "";
            }
        });
    }

    private void editItemSelected(Intent intent) {

        int position = -1;

        intentFromDb = intent;

        singlespinMunicipalities.setEnabled(true);
        singlespinBarangays.setEnabled(true);

        String stringCompare = intent.getStringExtra(EXTRA_IMP_TYPE);
        ArrayAdapter<CharSequence> adaptercompare = ArrayAdapter.createFromResource(this, R.array.implements1, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (stringCompare != null) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("DEBEDTIMPTYPE", "Position is: " + intent.getStringExtra(EXTRA_IMP_TYPE) + " " + position);
        spinImplementType.setSelection(position);
        spinImplementType.setEnabled(false);

        edtQRCode.setText(intent.getStringExtra(EXTRA_IMP_QR));

        edtBrand.setText(intent.getStringExtra(EXTRA_BRAND));
        edtModel.setText(intent.getStringExtra(EXTRA_MODEL));

        dateToStr = intent.getStringExtra(EXTRA_DATE);

        cbHaul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbHaul.isChecked()) {
                    hauling = "HAULING";
                } else {
                    hauling = "";
                }

            }
        });

        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_LAND_CLEAR))) {
            cbLandClear.setChecked(true);
            landClear = "LAND CLEAR";
        } else {
            landClear = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_PRE_PLANT))) {
            cbPrePlant.setChecked(true);
            prePlant = "LAND PREPARATION";
        } else {
            prePlant = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_PLANTING))) {
            cbPlant.setChecked(true);
            planting = "PLANTING";
        } else {
            planting = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_FERT_APP))) {
            cbFert.setChecked(true);
            fertilizer = "FERTILIZER APPLICATION";
        } else {
            fertilizer = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_PEST_APP))) {
            cbPest.setChecked(true);
            pesticide = "PESTICIDE APPLICATION";
        } else {
            pesticide = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_IRRI_DRAIN))) {
            cbIrriDrain.setChecked(true);
            irrigationDrainage = "IRRIGATION & DRAINAGE";
        } else {
            irrigationDrainage = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_CULT))) {
            cbCult.setChecked(true);
            cultivation = "CULTIVATION";
        } else {
            cultivation = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_RATOON))) {
            cbRatoon.setChecked(true);
            ratooning = "RATOONING";
        } else {
            ratooning = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_HARVEST))) {
            cbHarvest.setChecked(true);
            harvest = "HARVESTING";
        } else {
            harvest = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_POST_HARVEST))) {
            cbPostHarvest.setChecked(true);
            postHarvest = "POST HARVEST";
        } else {
            postHarvest = "";
        }
        if (!isNullOrEmpty(intent.getStringExtra(EXTRA_HAULING))) {
            cbHaul.setChecked(true);
            hauling = "HAULING";
        } else {
            hauling = "";
        }
//        edtTotalServiceAreaMain.setText(intent.getStringExtra(EXTRA_TSA_MAIN));
//        edtHoursPDayMain.setText(intent.getStringExtra(EXTRA_AVE_OP_HOURS_MAIN));
//        edtDaysPSeasonMain.setText(intent.getStringExtra(EXTRA_AVE_OP_DAYS_MAIN));
        edtEffectiveAreaAccompMain.setText(intent.getStringExtra(EXTRA_EFF_AREA_ACC_MAIN));
        edtTimeUsedDuringOpMain.setText(intent.getStringExtra(EXTRA_TIME_USED_OP_MAIN));
        tvFieldCapacityResultMain.setText(intent.getStringExtra(EXTRA_FIELD_CAP_MAIN));

        stringCompare = intent.getStringExtra(EXTRA_TYPE_PLANT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.type_of_planter, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (stringCompare != null) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("DEBEDTPLANTTYPE", "Position is: " + intent.getStringExtra(EXTRA_TYPE_PLANT) + " " + position);
        spinTypeOfPlanter.setSelection(position);


        edtNumberofRowsPlant.setText(intent.getStringExtra(EXTRA_NUM_ROWS_PLANT));
        edtDistanceofPlantMat.setText(intent.getStringExtra(EXTRA_DIST_MAT_PLANT));
//        edtTotalServiceAreaPlant.setText(intent.getStringExtra(EXTRA_TSA_PLANT));
//        edtHoursPDayPlant.setText(intent.getStringExtra(EXTRA_AVE_OP_HOURS_PLANT));
//        edtDaysPSeasonPlant.setText(intent.getStringExtra(EXTRA_AVE_OP_DAYS_PLANT));
        edtEffectiveAreaAccompPlant.setText(intent.getStringExtra(EXTRA_EFF_AREA_ACC_PLANT));
        edtTimeUsedDuringOpPlant.setText(intent.getStringExtra(EXTRA_TIME_USED_OP_PLANT));
        tvFieldCapacityResultPlant.setText(intent.getStringExtra(EXTRA_FIELD_CAP_PLANT));

//        edtTotalServiceAreaFert.setText(intent.getStringExtra(EXTRA_TSA_FERT));
//        edtHoursPDayFert.setText(intent.getStringExtra(EXTRA_AVE_OP_HOURS_FERT));
//        edtDaysPSeasonFert.setText(intent.getStringExtra(EXTRA_AVE_OP_DAYS_FERT));
        edtEffectiveAreaAccompFert.setText(intent.getStringExtra(EXTRA_EFF_AREA_ACC_FERT));
        edtTimeUsedDuringOpFert.setText(intent.getStringExtra(EXTRA_TIME_USED_OP_FERT));
        tvFieldCapacityResultFert.setText(intent.getStringExtra(EXTRA_FIELD_CAP_FERT));
        edtEffectiveAreaAccompFertForWeight.setText(intent.getStringExtra(EXTRA_EFF_AREA_ACC_FERT_WEIGHT));
        edtWeightOfFert.setText(intent.getStringExtra(EXTRA_WEIGHT_FERT));
        tvDeliveryRateResultFert.setText(intent.getStringExtra(EXTRA_DEL_RATE_FERT));

//        edtTotalServiceAreaHarvest.setText(intent.getStringExtra(EXTRA_TSA_HARVEST));
//        edtHoursPDayHarvest.setText(intent.getStringExtra(EXTRA_AVE_OP_HOURS_HARVEST));
//        edtDaysPSeasonHarvest.setText(intent.getStringExtra(EXTRA_AVE_OP_DAYS_HARVEST));
        edtEffectiveAreaAccompHarvest.setText(intent.getStringExtra(EXTRA_EFF_AREA_ACC_HARVEST));
        edtTimeUsedDuringOpHarvest.setText(intent.getStringExtra(EXTRA_TIME_USED_OP_HARVEST));
        tvFieldCapacityResultHarvest.setText(intent.getStringExtra(EXTRA_FIELD_CAP_HARVEST));
        edtAveYieldHarvest.setText(intent.getStringExtra(EXTRA_AVE_YIELD_HARVEST));

//        edtTotalServiceAreaGrab.setText(intent.getStringExtra(EXTRA_TSA_GRAB));
//        edtHoursPDayGrab.setText(intent.getStringExtra(EXTRA_AVE_OP_HOURS_GRAB));
//        edtDaysPSeasonGrab.setText(intent.getStringExtra(EXTRA_AVE_OP_DAYS_GRAB));
//        edtNumberofLoadsGrab.setText(intent.getStringExtra(EXTRA_NUM_LOAD_GRAB));
        edtEffectiveAreaAccompGrab.setText(intent.getStringExtra(EXTRA_EFF_AREA_ACC_GRAB));
        edtTimeUsedDuringOpGrab.setText(intent.getStringExtra(EXTRA_TIME_USED_OP_GRAB));
        edtLoadCapacityGrab.setText(intent.getStringExtra(EXTRA_LOAD_CAP_GRAB));
        tvFieldCapacityResultGrab.setText(intent.getStringExtra(EXTRA_FIELD_CAP_GRAB));

//        edtTotalServiceAreaDitch.setText(intent.getStringExtra(EXTRA_TSA_DITCH));
//        edtHoursPDayDitch.setText(intent.getStringExtra(EXTRA_AVE_OP_HOURS_DITCH));
//        edtDaysPSeasonDitch.setText(intent.getStringExtra(EXTRA_AVE_OP_DAYS_DITCH));
        edtDepthOfCutDitch.setText(intent.getStringExtra(EXTRA_DEPTH_CUT_DITCH));

        stringCompare = intent.getStringExtra(EXTRA_OWNERSHIP);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.ownership_of_machine, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position OWNERSHIP", "Position is: " + intent.getStringExtra(EXTRA_OWNERSHIP) + " " + position);
        spinOwnership.setSelection(position);

        stringCompare = intent.getStringExtra(EXTRA_PURCH_GRANT_DONO);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.purchasing_method, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position PURCHGRANTDONO", "Position is: " + intent.getStringExtra(EXTRA_PURCH_GRANT_DONO) + " " + position);
        spinPurchGrantDono.setSelection(position);

        edtOtherAgency.setText(intent.getStringExtra(EXTRA_AGENCY_SPECIFY));

        edtModifications.setText(intent.getStringExtra(EXTRA_MODIFICATIONS));

        edtOtherProblems.setText(intent.getStringExtra(EXTRA_PROBLEMS_SPECIFY));

        stringCompare = intent.getStringExtra(EXTRA_CONDITION_PRESENT);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.condition, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position CONDITION", "Position is: " + intent.getStringExtra(EXTRA_CONDITION_PRESENT) + " " + position);
        spinConditionPresent.setSelection(position);

        ArrayList<String> years = new ArrayList<String>();
        years.add("Please Select...");
        years.add("To Be Acquired...");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        stringCompare = intent.getStringExtra(EXTRA_YEAR_ACQUIRED);
        if (!isNullOrEmpty(stringCompare)) {
            position = adapter.getPosition(stringCompare);
        }
        Log.d("Position YEAR", "Position is: " + intent.getStringExtra(EXTRA_YEAR_ACQUIRED) + " " + position);
        spinYearAcquired.setSelection(position);

        stringCompare = intent.getStringExtra(EXTRA_CONDITION_ACQUIRED);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.conditionImplement, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position CONDITION", "Position is: " + intent.getStringExtra(EXTRA_CONDITION_ACQUIRED) + " " + position);
        spinConditionAcquired.setSelection(position);

//TODO PROBLEMS

//        stringCompare = intent.getStringExtra(EXTRA_PROBLEMS);
//        List<KeyPairBoolData> problems = null;
//        if (spinConditionPresent.getSelectedItemPosition() == 2) {
//            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_unused)), stringCompare, 1);
//        } else if (spinConditionPresent.getSelectedItemPosition() == 3) {
//            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_nonfunctional)), stringCompare, 1);
//        } else {
//            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_unused)), stringCompare, 1);
//        }
//
//        if (stringCompare.contains("OTHERS")) {
//            edtOtherProblems.setVisibility(View.VISIBLE);
//            paramstvLocation.topToBottom = R.id.edtOtherProblems;
//            hasOtherProblems = true;
//            edtOtherProblems.setText(intent.getStringExtra(EXTRA_PROBLEMS_SPECIFY));
//        } else {
//            hasOtherProblems = false;
//            edtOtherProblems.setVisibility(View.INVISIBLE);
//        }
//        multspinProblemsUnused.setItems(problems, new MultiSpinnerListener() {
//            @Override
//            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
//            }
//        });
//        listOfProblems = intent.getStringExtra(EXTRA_PROBLEMS);

        stringCompare = intent.getStringExtra(EXTRA_PROBLEMS);
        List<KeyPairBoolData> problems = null;
        Log.d("EDTIMPPRBLM", "This is problem " + stringCompare);
        if (spinConditionPresent.getSelectedItemPosition() == 3) {
            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_unused_implement)), stringCompare, 1);
        } else if (spinConditionPresent.getSelectedItemPosition() == 4) {
            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_nonfunctional)), stringCompare, 1);
        } else {
            problems = pairBoolDataSelectMulti(Arrays.asList(getResources().getStringArray(R.array.problems_unused)), stringCompare, 1);
        }

        if (stringCompare.contains("OTHERS")) {
            edtOtherProblems.setVisibility(View.VISIBLE);
            paramstvLocation.topToBottom = R.id.edtOtherProblems;
            hasOtherProblems = true;
            edtOtherProblems.setText(intent.getStringExtra(EXTRA_PROBLEMS_SPECIFY));
        } else {
            hasOtherProblems = false;
            edtOtherProblems.setVisibility(View.INVISIBLE);
        }

        multspinProblemsUnused.setItems(problems, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = selectedItems.get(i).getName() + " : " + pos;
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
        listOfProblems = intent.getStringExtra(EXTRA_PROBLEMS);

        years = new ArrayList<String>();
        years.add("Please Select...");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        stringCompare = intent.getStringExtra(EXTRA_YEAR_INOPERABLE);
        if (!isNullOrEmpty(stringCompare)) {
            position = adapter2.getPosition(stringCompare);
        }
        Log.d("Position YEAR", "Position is: " + intent.getStringExtra(EXTRA_YEAR_INOPERABLE) + " " + position);
        spinYearInoperable.setSelection(position);

        stringCompare = intent.getStringExtra(EXTRA_LOCATION);
        adaptercompare = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (!isNullOrEmpty(stringCompare)) {
            position = adaptercompare.getPosition(stringCompare);
        }
        Log.d("Position LOCATION", "Position is: " + intent.getStringExtra(EXTRA_LOCATION) + " " + position);
        spinLocation.setSelection(position);


        stringCompare = intent.getStringExtra(EXTRA_PROVINCE);
        List<KeyPairBoolData> selectProvinces = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.provinces)), stringCompare);
        singlespinProvinces.setItems(selectProvinces, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                locProvince = selectedItem.getName();
            }

            @Override
            public void onClear() {

            }
        });
        locProvince = intent.getStringExtra(EXTRA_PROVINCE);

        stringCompare = intent.getStringExtra(EXTRA_MUNICIPALITY);
        List<KeyPairBoolData> selectMunicipalities = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.municipalities)), stringCompare);
        singlespinMunicipalities.setItems(selectMunicipalities, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                locMunicipality = selectedItem.getName();
            }

            @Override
            public void onClear() {

            }
        });
        locMunicipality = intent.getStringExtra(EXTRA_MUNICIPALITY);

        stringCompare = intent.getStringExtra(EXTRA_BARANGAY);
        List<KeyPairBoolData> selectBarangays = pairBoolDataSelect(Arrays.asList(getResources().getStringArray(R.array.barangays)), stringCompare);
        singlespinBarangays.setItems(selectBarangays, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                locBarangay = selectedItem.getName();
            }

            @Override
            public void onClear() {

            }
        });
        locBarangay = intent.getStringExtra(EXTRA_BARANGAY);


        tvLat.setText(intent.getStringExtra(EXTRA_LATITUDE));
        tvLong.setText(intent.getStringExtra(EXTRA_LONGITUDE));
        tvAcc.setText(intent.getStringExtra(EXTRA_ACCURACY));

        encodedImage = Variable.getStringImage();

        if (encodedImage.contains("Not yet Acquired")) {
            selectedImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon));
        } else {
            byte[] decodedString = Base64.decode(Variable.getStringImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            selectedImage.setImageBitmap(decodedByte);
        }

        //        stringCompare = intent.getStringExtra(EXTRA_IMP_TYPE);
        //        adaptercompare = ArrayAdapter.createFromResource(this, R.array.implements1, android.R.layout.simple_spinner_item);
        //        adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //        if (stringCompare != null) {
        //            position = adaptercompare.getPosition(stringCompare);
        //        }
        //        Log.d("DEBPOSIMPTYPE", "Position is: " + intent.getStringExtra(EXTRA_IMP_TYPE) + " " + position);
        //        spinImplementType.setSelection(position);
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

    private void initTextViewData() {
        tvFieldCapacityResultMain.setText(R.string.not_yet_acq);
        tvFieldCapacityResultPlant.setText(R.string.not_yet_acq);
        tvFieldCapacityResultFert.setText(R.string.not_yet_acq);
        tvDeliveryRateResultFert.setText(R.string.not_yet_acq);
        tvFieldCapacityResultHarvest.setText(R.string.not_yet_acq);
        tvLat.setText(R.string.not_yet_acq);
        tvLong.setText(R.string.not_yet_acq);
        tvAcc.setText(R.string.not_yet_acq);

        locProvince = "";
        locMunicipality = "";
        locBarangay = "";
    }

    private void setCheckBoxData() {
        landClear = "";
        prePlant = "";
        planting = "";
        fertilizer = "";
        pesticide = "";
        irrigationDrainage = "";
        cultivation = "";
        ratooning = "";
        harvest = "";
        postHarvest = "";
        hauling = "";
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private void spinYearsSetAdapter() {

        ArrayList<String> years = new ArrayList<String>();
        years.add("Please Select...");
        years.add("To Be Acquired...");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYearAcquired.setAdapter(adapter);

        years = new ArrayList<String>();
        years.add("Please Select...");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYearInoperable.setAdapter(adapter);
    }

    private void setMarginSize() {
        smallMargin = (int) pxFromDp(this, 16);
        bigMargin = (int) pxFromDp(this, 50);
    }

    private String getFieldCapacity(String area, String time) {
        String fieldCapacity;
        if (!isNullOrEmpty(area) && !isNullOrEmpty(time)) {
            if (area.startsWith(".")) {
                area = "0" + area + "0";
            }
            if (time.startsWith(".")) {
                time = "0" + time + "0";
            }
            fieldCapacity = String.valueOf(Double.parseDouble(area) / Double.parseDouble(time));
        } else {
            fieldCapacity = "0.0";
        }
        return fieldCapacity;
    }

    public void saveImplement() {
//        if (machineType.trim().isEmpty() ||
//                machineQRCode.trim().isEmpty() ||
//                isNullOrEmpty(latitude) ||
//                isNullOrEmpty(longitude)) {
//            Toast.makeText(this, "Incomplete Data", Toast.LENGTH_SHORT).show();
//        }
//        else {

        List<String> listIncomplete = new ArrayList<>();


//        if (infoCheck()) {
        if (infoCheck()) {

            Intent dataAddImplement = new Intent();


            if (isNullOrEmpty(encodedImage)) {
                encodedImage = "Not yet Acquired";
            }

            int id = getIntent().getIntExtra(EXTRA_IMP_ID, -1);

            if (id != -1) {
                dataAddImplement.putExtra(EXTRA_IMP_ID, id);

            } else {
                dateToStr = new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(new Date());
            }


            temp1 = machineArrayList.get(spinMachineUsing.getSelectedItemPosition() - 1).code;
            temp2 = machineArrayList.get(spinMachineUsing.getSelectedItemPosition() - 1).code + "\n" + machineArrayList.get(spinMachineUsing.getSelectedItemPosition() - 1).type;
//        machineListViewModel.getAllMachines().observe(this, new Observer<List<Machines>>() {
//            @Override
//            public void onChanged(List<Machines> machines) {
//                ArrayList<String> stringArrayList = new ArrayList<>();
////                for (int i = machines.size() - 1; i >= 0; i--) {
////                    stringArrayList.add(machines.get(i).getId() + " " + machines.get(i).getMachine_qrcode() + " | " + machines.get(i).getMachine_type());
////                }
////
////                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stringArrayList);
////                int posMachQR = adapter.getPosition(temp);
//                machineQR = machines.get(Integer.parseInt(temp.substring(0,1))-1).getMachine_qrcode();
//            }
//        });
//            for (int i = 0; i < machineArrayList.size(); i++) {
//                respondentArrayList.add(new Respondent(profiles.get(i).getName_respondent(), profiles.get(i).getResCode()));
//                Log.d("XRES LOOP", machineArrayList.get(i).code + " : " + machineArrayList.get(i).type);
//            }
            Log.d("XRES", temp1 + " : " + temp2 + " : " + spinMachineUsing.getSelectedItemPosition());

            dataAddImplement.putExtra(EXTRA_IMP_TYPE, spinImplementType.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_IMP_QR, edtQRCode.getText().toString());
            dataAddImplement.putExtra(EXTRA_DATE, dateToStr);
            dataAddImplement.putExtra(EXTRA_USED_ON, temp1);
            dataAddImplement.putExtra(EXTRA_USED_COMPLETE, temp2);
            dataAddImplement.putExtra(EXTRA_BRAND, edtBrand.getText().toString().toUpperCase());
            dataAddImplement.putExtra(EXTRA_MODEL, edtModel.getText().toString().toUpperCase());
            dataAddImplement.putExtra(EXTRA_LAND_CLEAR, landClear);
            dataAddImplement.putExtra(EXTRA_PRE_PLANT, prePlant);
            dataAddImplement.putExtra(EXTRA_PLANTING, planting);
            dataAddImplement.putExtra(EXTRA_FERT_APP, fertilizer);
            dataAddImplement.putExtra(EXTRA_PEST_APP, pesticide);
            dataAddImplement.putExtra(EXTRA_IRRI_DRAIN, irrigationDrainage);
            dataAddImplement.putExtra(EXTRA_CULT, cultivation);
            dataAddImplement.putExtra(EXTRA_RATOON, ratooning);
            dataAddImplement.putExtra(EXTRA_HARVEST, harvest);
            dataAddImplement.putExtra(EXTRA_POST_HARVEST, postHarvest);
            dataAddImplement.putExtra(EXTRA_HAULING, hauling);
//            dataAddImplement.putExtra(EXTRA_TSA_MAIN, edtTotalServiceAreaMain.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_MAIN, edtHoursPDayMain.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_MAIN, edtDaysPSeasonMain.getText().toString());
            dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_MAIN, edtEffectiveAreaAccompMain.getText().toString());
            dataAddImplement.putExtra(EXTRA_TIME_USED_OP_MAIN, edtTimeUsedDuringOpMain.getText().toString());
            dataAddImplement.putExtra(EXTRA_FIELD_CAP_MAIN, tvFieldCapacityResultMain.getText().toString());
            dataAddImplement.putExtra(EXTRA_TYPE_PLANT, spinTypeOfPlanter.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_NUM_ROWS_PLANT, edtNumberofRowsPlant.getText().toString());
            dataAddImplement.putExtra(EXTRA_DIST_MAT_PLANT, edtDistanceofPlantMat.getText().toString());
//            dataAddImplement.putExtra(EXTRA_TSA_PLANT, edtTotalServiceAreaPlant.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_PLANT, edtHoursPDayPlant.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_PLANT, edtDaysPSeasonPlant.getText().toString());
            dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_PLANT, edtEffectiveAreaAccompPlant.getText().toString());
            dataAddImplement.putExtra(EXTRA_TIME_USED_OP_PLANT, edtTimeUsedDuringOpPlant.getText().toString());
            dataAddImplement.putExtra(EXTRA_FIELD_CAP_PLANT, tvFieldCapacityResultPlant.getText().toString());
//            dataAddImplement.putExtra(EXTRA_TSA_FERT, edtTotalServiceAreaFert.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_FERT, edtHoursPDayFert.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_FERT, edtHoursPDayFert.getText().toString());
            dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_FERT, edtEffectiveAreaAccompFert.getText().toString());
            dataAddImplement.putExtra(EXTRA_TIME_USED_OP_FERT, edtTimeUsedDuringOpFert.getText().toString());
            dataAddImplement.putExtra(EXTRA_FIELD_CAP_FERT, tvFieldCapacityResultFert.getText().toString());
            dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_FERT_WEIGHT, edtEffectiveAreaAccompFertForWeight.getText().toString());
            dataAddImplement.putExtra(EXTRA_WEIGHT_FERT, edtWeightOfFert.getText().toString());
            dataAddImplement.putExtra(EXTRA_DEL_RATE_FERT, tvDeliveryRateResultFert.getText().toString());
//            dataAddImplement.putExtra(EXTRA_TSA_HARVEST, edtTotalServiceAreaHarvest.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_HARVEST, edtHoursPDayHarvest.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_HARVEST, edtDaysPSeasonHarvest.getText().toString());
            dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_HARVEST, edtEffectiveAreaAccompHarvest.getText().toString());
            dataAddImplement.putExtra(EXTRA_TIME_USED_OP_HARVEST, edtTimeUsedDuringOpHarvest.getText().toString());
            dataAddImplement.putExtra(EXTRA_FIELD_CAP_HARVEST, tvFieldCapacityResultHarvest.getText().toString());
            dataAddImplement.putExtra(EXTRA_AVE_YIELD_HARVEST, edtAveYieldHarvest.getText().toString());
//            dataAddImplement.putExtra(EXTRA_TSA_GRAB, edtTotalServiceAreaGrab.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_GRAB, edtHoursPDayGrab.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_GRAB, edtDaysPSeasonGrab.getText().toString());
//        dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_GRAB,effe);
            dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_GRAB, edtEffectiveAreaAccompGrab.getText().toString());
            dataAddImplement.putExtra(EXTRA_TIME_USED_OP_GRAB, edtTimeUsedDuringOpGrab.getText().toString());
            dataAddImplement.putExtra(EXTRA_LOAD_CAP_GRAB, edtLoadCapacityGrab.getText().toString());
            dataAddImplement.putExtra(EXTRA_FIELD_CAP_GRAB, tvFieldCapacityResultGrab.getText().toString());
//            dataAddImplement.putExtra(EXTRA_NUM_LOAD_GRAB, edtNumberofLoadsGrab.getText().toString());
//            dataAddImplement.putExtra(EXTRA_TSA_DITCH, edtTotalServiceAreaDitch.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_DITCH, edtHoursPDayDitch.getText().toString());
//            dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_DITCH, edtDaysPSeasonDitch.getText().toString());
            dataAddImplement.putExtra(EXTRA_OWNERSHIP, spinOwnership.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_PURCH_GRANT_DONO, spinPurchGrantDono.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_AGENCY, spinAgency.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_AGENCY_SPECIFY, edtOtherAgency.getText().toString());
            dataAddImplement.putExtra(EXTRA_MODIFICATIONS, edtModifications.getText().toString());
            dataAddImplement.putExtra(EXTRA_YEAR_INOPERABLE, spinYearInoperable.getSelectedItem().toString());


            if (isNullOrEmpty(listOfProblems)) {
                listOfProblems = "";
            }

            dataAddImplement.putExtra(EXTRA_PROBLEMS, listOfProblems);
            dataAddImplement.putExtra(EXTRA_PROBLEMS_SPECIFY, edtOtherProblems.getText().toString().toUpperCase());

            dataAddImplement.putExtra(EXTRA_DEPTH_CUT_DITCH, edtDepthOfCutDitch.getText().toString());
            dataAddImplement.putExtra(EXTRA_YEAR_ACQUIRED, spinYearAcquired.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_CONDITION_ACQUIRED, spinConditionAcquired.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_CONDITION_PRESENT, spinConditionPresent.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_LOCATION, spinLocation.getSelectedItem().toString());
            dataAddImplement.putExtra(EXTRA_PROVINCE, locProvince);
            dataAddImplement.putExtra(EXTRA_MUNICIPALITY, locMunicipality);
            dataAddImplement.putExtra(EXTRA_BARANGAY, locBarangay);
            dataAddImplement.putExtra(EXTRA_LATITUDE, tvLat.getText().toString());
            dataAddImplement.putExtra(EXTRA_LONGITUDE, tvLong.getText().toString());
            dataAddImplement.putExtra(EXTRA_ACCURACY, tvAcc.getText().toString());
            Variable.setStringImage(encodedImage);

            setResult(RESULT_OK, dataAddImplement);
            finish();
        } else {

            if (!machineUsingCheck) {
                listIncomplete.add("Machine Using this Implement");
            }
            if (!brandCheck) {
                listIncomplete.add("Brand");
            }
            if (!modelCheck) {
                listIncomplete.add("Model");
            }
            if (!qrCheck) {
                listIncomplete.add("QR Code");
            }
            if(!qrFormatCheck){
                listIncomplete.add("QR Code Format");
            }
            if (!implementSpecsCheck) {
                listIncomplete.add("Implement Specifications");
            }
            if (!fieldCapCheck) {
                listIncomplete.add("Field Capacity is Invalid");
            }
            if (!operationCheck) {
                listIncomplete.add("Operations");
            }
            if (!ownershipCheck) {
                listIncomplete.add("Type of Ownership");
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
            if (!conditionPresentCheck) {
                listIncomplete.add("Present Condition");
            }
            if (!locationImplementCheck) {
                listIncomplete.add("Location of Implement");
            }
            if (!locationGarageCheck) {
                listIncomplete.add("Location of Garage");
            }
            if (!yearSelectCheck) {
                listIncomplete.add("Year Acquired");
            }
            if (!conditionAcquiredCheck) {
                listIncomplete.add("Condition when Acquired");
            }
            if (!otherProblemsCheck) {
                listIncomplete.add("Problems with Machine");
            }

            String inc = "";
            for (int i = 0; i < listIncomplete.size(); i++) {
                inc = inc + listIncomplete.get(i) + "\n";
            }
            Toast.makeText(this, "Incomplete Data!\nPlease Check\n\n" + inc, Toast.LENGTH_LONG).show();
            Log.d("IMPXCH", String.valueOf(listIncomplete) + ": " + inc);
        }
//        }
    }

    private boolean infoCheck() {

        switch (spinImplementType.getSelectedItem().toString()) {
            case "SUBSOILER":
            case "MOLDBOARD PLOW":
            case "DISC PLOW":
            case "CHISEL PLOW":
            case "DISC HARROW":
            case "POWER HARROW":
            case "SPIKE-TOOTH HARROW":
            case "FURROWER":
            case "FIELD CULTIVATOR":
            case "INTERROW CULTIVATOR":
            case "CUTAWAY CULTIVATOR":
            case "RATOON MANAGER / STUBBLE SHAVER":
            case "THRASH RAKE":
            case "WEEDER":
            case "ROTAVATOR":
            case "TRASH INCORPORATOR":

                implementSpecsCheck = !isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) &&
                        !isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString());
                fieldCapCheck = !tvFieldCapacityResultMain.getText().toString().contains("NaN") && !tvFieldCapacityResultMain.getText().toString().contains("Infinity") &&
                        Double.parseDouble(isNumberOrString(tvFieldCapacityResultMain)) != 0;
                break;
            case "MECHANICAL PLANTER":

                implementSpecsCheck = spinTypeOfPlanter.getSelectedItemPosition() != 0 && !isNullOrEmpty(edtNumberofRowsPlant.getText().toString()) &&
                        !isNullOrEmpty(edtDistanceofPlantMat.getText().toString()) && !isNullOrEmpty(edtEffectiveAreaAccompPlant.getText().toString()) &&
                        !isNullOrEmpty(edtTimeUsedDuringOpPlant.getText().toString());

                fieldCapCheck = !tvFieldCapacityResultPlant.getText().toString().contains("NaN") && !tvFieldCapacityResultPlant.getText().toString().contains("Infinity") &&
                        Double.parseDouble(isNumberOrString(tvFieldCapacityResultPlant)) != 0;

                break;
            case "GRANULAR FERTILIZER APPLICATOR":
            case "FERTILIZER APPLICATOR WITH CUTAWAY":

                implementSpecsCheck = !isNullOrEmpty(edtEffectiveAreaAccompFert.getText().toString()) &&
                        !isNullOrEmpty(edtTimeUsedDuringOpFert.getText().toString()) && !isNullOrEmpty(edtWeightOfFert.getText().toString()) &&
                        !isNullOrEmpty(edtEffectiveAreaAccompFertForWeight.getText().toString());

                fieldCapCheck = !tvFieldCapacityResultFert.getText().toString().contains("NaN") && !tvFieldCapacityResultFert.getText().toString().contains("Infinity") &&
                        Double.parseDouble(isNumberOrString(tvFieldCapacityResultFert)) != 0;


                break;
            case "MECHANICAL HARVESTER":

                implementSpecsCheck = !isNullOrEmpty(edtEffectiveAreaAccompHarvest.getText().toString()) &&
                        !isNullOrEmpty(edtTimeUsedDuringOpHarvest.getText().toString()) && !isNullOrEmpty(edtAveYieldHarvest.getText().toString());

                fieldCapCheck = !tvFieldCapacityResultHarvest.getText().toString().contains("NaN") && !tvFieldCapacityResultHarvest.getText().toString().contains("Infinity") &&
                        Double.parseDouble(isNumberOrString(tvFieldCapacityResultHarvest)) != 0;

                break;
            case "CANE GRAB LOADERS":

                implementSpecsCheck = !isNullOrEmpty(edtEffectiveAreaAccompGrab.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpGrab.getText().toString()) &&
                        !isNullOrEmpty(edtLoadCapacityGrab.getText().toString());

                fieldCapCheck = !tvFieldCapacityResultGrab.getText().toString().contains("NaN") && !tvFieldCapacityResultGrab.getText().toString().contains("Infinity") &&
                        Double.parseDouble(isNumberOrString(tvFieldCapacityResultGrab)) != 0;

                break;
            case "DITCHER":

                implementSpecsCheck = !isNullOrEmpty(edtDepthOfCutDitch.getText().toString());
                fieldCapCheck = true;
                break;
            default:
                implementSpecsCheck = false;
                break;
        }

        qrCheck = !isNullOrEmpty(edtQRCode.getText().toString()) && qrFormatCheck;

        machineUsingCheck = spinMachineUsing.getSelectedItemPosition() != 0;

        operationCheck = !isNullOrEmpty(landClear) || !isNullOrEmpty(prePlant) || !isNullOrEmpty(planting) || !isNullOrEmpty(fertilizer) || !isNullOrEmpty(pesticide) ||
                !isNullOrEmpty(irrigationDrainage) || !isNullOrEmpty(cultivation) || !isNullOrEmpty(ratooning) || !isNullOrEmpty(harvest) || !isNullOrEmpty(postHarvest) ||
                !isNullOrEmpty(hauling);

        switch (spinOwnership.getSelectedItemPosition()) {
            case 0:
                ownershipCheck = false;
                break;
            //Please Select
            case 2:
            default:
                ownershipCheck = true;
                purchGrantDonoCheck = true;
                agencyCheck = true;
                break;
            //Private
            case 3:
            case 4:
            case 5:
                ownershipCheck = true;
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
                            case 4:
                                agencyCheck = !isNullOrEmpty(edtOtherAgency.getText().toString());
                                break;
                            default:
                                agencyCheck = true;
                                agencyCheck = true;
                                break;

                        }
                        break;

                }
                break;
            //CoopCustomLgu
        }

        brandCheck = !isNullOrPleaseSelect(edtBrand.getText().toString());
        modelCheck = !isNullOrPleaseSelect(edtModel.getText().toString());

        yearSelectCheck = spinYearAcquired.getSelectedItemPosition() != 0;

        switch (spinConditionAcquired.getSelectedItemPosition()) {
            case 0:
                conditionAcquiredCheck = false;
                break;
            case 3:
                conditionAcquiredCheck = !isNullOrEmpty(edtModifications.getText().toString());
                break;
            case 1:
            case 2:
            case 4:
            case 5:
                conditionAcquiredCheck = true;
                break;
        }

        switch (spinConditionPresent.getSelectedItemPosition()) {
            case 0:
                conditionPresentCheck = false;
                break;
            case 2:
            default:
                conditionPresentCheck = true;
                otherProblemsCheck = true;
                break;
            case 3:
            case 4:
                conditionPresentCheck = listOfProblems.length() >= 5 && spinYearInoperable.getSelectedItemPosition() != 0;
                if (hasOtherProblems) {
                    otherProblemsCheck = !isNullOrEmpty(edtOtherProblems.getText().toString());
                } else {
                    otherProblemsCheck = true;
                }
                break;
        }

        locationImplementCheck = spinLocation.getSelectedItemPosition() != 0;

        locationGarageCheck = !isNullOrPleaseSelect(singlespinProvinces.getSelectedItem().toString()) && !isNullOrPleaseSelect(singlespinMunicipalities.getSelectedItem().toString()) &&
                !isNullOrPleaseSelect(singlespinBarangays.getSelectedItem().toString());
        Log.d("IMXCH", "Machine Using: " + machineUsingCheck);
        Log.d("IMXCH", "QR Code: " + qrCheck);
        Log.d("IMXCH", "Machine Specs: " + implementSpecsCheck);
        Log.d("IMXCH", "OperationCheck: " + operationCheck);
        Log.d("IMXCH", "Year: " + yearSelectCheck);
        Log.d("IMXCH", "Condition Present: " + conditionPresentCheck);
        Log.d("IMXCH", "Machine Loc: " + locationImplementCheck);
        Log.d("IMXCH", "Garage Loc: " + locationGarageCheck);

        return brandCheck && modelCheck && machineUsingCheck && implementSpecsCheck && fieldCapCheck && qrCheck && operationCheck && yearSelectCheck && conditionPresentCheck && locationImplementCheck && locationGarageCheck &&
                ownershipCheck && purchGrantDonoCheck && agencyCheck && conditionAcquiredCheck && otherProblemsCheck;
    }

    private String isNumberOrString(TextView textView) {
        String string = textView.getText().toString();

        try
        {
            Double.parseDouble(string);
        }
        catch(NumberFormatException e)
        {
            string = "0";
        }

        return string;
    }

    public static boolean isNullOrPleaseSelect(String str) {
        return (str == null || str.isEmpty() || str.contains("Please Select"));
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
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


    private void selectImplement(String pos) {

        Log.d("Implement type", pos);
        switch (pos) {
            case "SUBSOILER":
            case "MOLDBOARD PLOW":
            case "DISC PLOW":
            case "CHISEL PLOW":
            case "DISC HARROW":
            case "POWER HARROW":
            case "SPIKE-TOOTH HARROW":
            case "FURROWER":
            case "FIELD CULTIVATOR":
            case "INTERROW CULTIVATOR":
            case "CUTAWAY CULTIVATOR":
            case "RATOON MANAGER / STUBBLE SHAVER":
            case "THRASH RAKE":
            case "WEEDER":
            case "ROTAVATOR":
            case "TRASH INCORPORATOR":
                hideAll();
                showMainImplements();
                paramsEAMain.topToBottom = R.id.tvFieldCapHeader;
                paramsEAMain.topMargin = smallMargin;
                tvHaEAMain.setLayoutParams(paramsEAMain);
                paramstvOwnership.topToBottom = R.id.botLineMain;
                break;
            case "MECHANICAL PLANTER":
                hideAll();
                showPlanter();
                paramstvPlanter.topToBottom = R.id.cbHauling;
                paramstvPlanter.topMargin = bigMargin;
                tvTypeofPlant.setLayoutParams(paramstvPlanter);
                paramstvOwnership.topToBottom = R.id.botLinePlant;
                break;
            case "GRANULAR FERTILIZER APPLICATOR":
            case "FERTILIZER APPLICATOR WITH CUTAWAY":
                hideAll();
                showFert();
                paramsEAFert.topToBottom = R.id.tvFieldCapHeader;
                paramsEAFert.topMargin = smallMargin;
                tvHaEAFert.setLayoutParams(paramsEAFert);
                paramstvOwnership.topToBottom = R.id.botLineFert;
                break;
            case "MECHANICAL HARVESTER":
                hideAll();
                showHarvest();
                paramsEAHarvest.topToBottom = R.id.tvFieldCapHeader;
                paramsEAHarvest.topMargin = smallMargin;
                tvHaEAHarvest.setLayoutParams(paramsEAHarvest);
                paramstvOwnership.topToBottom = R.id.edtAveYieldHarvest;
                break;
            case "CANE GRAB LOADERS":
                hideAll();
                showGrab();
                paramsEAGrab.topToBottom = R.id.tvFieldCapHeader;
                paramsEAGrab.topMargin = smallMargin;
                tvHaEAGrab.setLayoutParams(paramsEAGrab);
                paramstvOwnership.topToBottom = R.id.edtLoadCapacityGrab;
                break;
            case "DITCHER":
                hideAll();
                showDitch();
                paramsDCDitch.topToBottom = R.id.cbHauling;
                paramsDCDitch.topMargin = smallMargin;
                tvDepthCutDitch.setLayoutParams(paramsDCDitch);
                paramstvOwnership.topToBottom = R.id.edtDepthOfCutDitch;
                break;
            default:
                hideAll();
                paramstvOwnership.topToBottom = R.id.cbHauling;
                break;
        }
        paramstvOwnership.topMargin = bigMargin;
        tvOwnership.setLayoutParams(paramstvOwnership);
    }

    private void showDitch() {
//        edtTotalServiceAreaDitch.setVisibility(View.VISIBLE);
//        edtHoursPDayDitch.setVisibility(View.VISIBLE);
//        edtDaysPSeasonDitch.setVisibility(View.VISIBLE);
//
//        tvHaDitch.setVisibility(View.VISIBLE);
//        tvHoursPDayDitch.setVisibility(View.VISIBLE);
//        tvDaysPSeasonDitch.setVisibility(View.VISIBLE);
        tvDepthCutDitch.setVisibility(View.VISIBLE);
        edtDepthOfCutDitch.setVisibility(View.VISIBLE);
    }

    private void showGrab() {
//        edtTotalServiceAreaGrab.setVisibility(View.VISIBLE);
//        edtHoursPDayGrab.setVisibility(View.VISIBLE);
//        edtDaysPSeasonGrab.setVisibility(View.VISIBLE);
//        edtNumberofLoadsGrab.setVisibility(View.VISIBLE);
//
//        tvHaGrab.setVisibility(View.VISIBLE);
//        tvHoursPDayGrab.setVisibility(View.VISIBLE);
//        tvDaysPSeasonGrab.setVisibility(View.VISIBLE);
//        tvLoadPHaGrab.setVisibility(View.VISIBLE);
//        tvLoadCapGrab.setVisibility(View.VISIBLE);

        tvFieldCapHeader.setVisibility(View.VISIBLE);
        tvFieldCapHeaderInfo.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompGrab.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpGrab.setVisibility(View.VISIBLE);
        edtLoadCapacityGrab.setVisibility(View.VISIBLE);
        tvHaEAGrab.setVisibility(View.VISIBLE);
        tvHoursPDayOpGrab.setVisibility(View.VISIBLE);
        tvLoadCapacityGrab.setVisibility(View.VISIBLE);
        tvFieldCapacityGrab.setVisibility(View.VISIBLE);
        tvFieldCapacityResultGrab.setVisibility(View.VISIBLE);

        topLine.setVisibility(View.VISIBLE);
        botLineGrab.setVisibility(View.VISIBLE);
    }

    private void showHarvest() {
//        edtTotalServiceAreaHarvest.setVisibility(View.VISIBLE);
//        edtHoursPDayHarvest.setVisibility(View.VISIBLE);
//        edtDaysPSeasonHarvest.setVisibility(View.VISIBLE);
//        tvHaHarvest.setVisibility(View.VISIBLE);
//        tvHoursPDayHarvest.setVisibility(View.VISIBLE);
//        tvDaysPSeasonHarvest.setVisibility(View.VISIBLE);
        tvFieldCapHeader.setVisibility(View.VISIBLE);
        tvFieldCapHeaderInfo.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompHarvest.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpHarvest.setVisibility(View.VISIBLE);
        edtAveYieldHarvest.setVisibility(View.VISIBLE);


        tvHaEAHarvest.setVisibility(View.VISIBLE);
        tvHoursPDayOpHarvest.setVisibility(View.VISIBLE);
        tvFieldCapacityHarvest.setVisibility(View.VISIBLE);
        tvFieldCapacityResultHarvest.setVisibility(View.VISIBLE);
        tvTonCannesPHaHarvest.setVisibility(View.VISIBLE);

        topLine.setVisibility(View.VISIBLE);
        botLineHarvest.setVisibility(View.VISIBLE);
    }

    private void showFert() {
//        edtTotalServiceAreaFert.setVisibility(View.VISIBLE);
//        edtHoursPDayFert.setVisibility(View.VISIBLE);
//        edtDaysPSeasonFert.setVisibility(View.VISIBLE);
//        tvHaFert.setVisibility(View.VISIBLE);
//        tvHoursPDayFert.setVisibility(View.VISIBLE);
//        tvDaysPSeasonfFert.setVisibility(View.VISIBLE);
        tvFieldCapHeader.setVisibility(View.VISIBLE);
        tvFieldCapHeaderInfo.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompFert.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpFert.setVisibility(View.VISIBLE);
        edtWeightOfFert.setVisibility(View.VISIBLE);

        tvHaEAFert2.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompFertForWeight.setVisibility(View.VISIBLE);

        tvDelRateHeader.setVisibility(View.VISIBLE);
        tvDelRateHeaderInfo.setVisibility(View.VISIBLE);
        tvHaEAFert.setVisibility(View.VISIBLE);
        tvHoursPDayOpFert.setVisibility(View.VISIBLE);
        tvFieldCapacityFert.setVisibility(View.VISIBLE);
        tvFieldCapacityResultFert.setVisibility(View.VISIBLE);
        tvWeightOfFert.setVisibility(View.VISIBLE);
        tvDeliveryRateFert.setVisibility(View.VISIBLE);
        tvDeliveryRateResultFert.setVisibility(View.VISIBLE);

        topLine.setVisibility(View.VISIBLE);
        botLineFert.setVisibility(View.VISIBLE);
        botLineFertDR.setVisibility(View.VISIBLE);
    }

    private void showPlanter() {
//        edtTotalServiceAreaPlant.setVisibility(View.VISIBLE);
//        edtHoursPDayPlant.setVisibility(View.VISIBLE);
//        edtDaysPSeasonPlant.setVisibility(View.VISIBLE);
//        tvTypeofPlant.setVisibility(View.VISIBLE);
//        tvDistanceofPlant.setVisibility(View.VISIBLE);
//        tvHaPlant.setVisibility(View.VISIBLE);
//        tvHoursPDayPlant.setVisibility(View.VISIBLE);
//        tvDaysPSeasonPlant.setVisibility(View.VISIBLE);
        spinTypeOfPlanter.setVisibility(View.VISIBLE);
        edtNumberofRowsPlant.setVisibility(View.VISIBLE);
        edtDistanceofPlantMat.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompPlant.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpPlant.setVisibility(View.VISIBLE);

        tvFieldCapHeaderPlant.setVisibility(View.VISIBLE);
        tvFieldCapHeaderInfoPlant.setVisibility(View.VISIBLE);
        tvHaEAPlant.setVisibility(View.VISIBLE);
        tvHoursPDayOpPlant.setVisibility(View.VISIBLE);
        tvFieldCapacityPlant.setVisibility(View.VISIBLE);
        tvFieldCapacityResultPlant.setVisibility(View.VISIBLE);
        tvNumRowsPlant.setVisibility(View.VISIBLE);
        tvDistanceofPlant.setVisibility(View.VISIBLE);
        tvTypeofPlant.setVisibility(View.VISIBLE);

        topLinePlant.setVisibility(View.VISIBLE);
        botLinePlant.setVisibility(View.VISIBLE);
    }

    private void showMainImplements() {
//        edtTotalServiceAreaMain.setVisibility(View.VISIBLE);
//        edtHoursPDayMain.setVisibility(View.VISIBLE);
//        edtDaysPSeasonMain.setVisibility(View.VISIBLE);
//        tvHaMain.setVisibility(View.VISIBLE);
//        tvHoursPDayMain.setVisibility(View.VISIBLE);
//        tvDaysPSeasonMain.setVisibility(View.VISIBLE);
        tvFieldCapHeader.setVisibility(View.VISIBLE);
        tvFieldCapHeaderInfo.setVisibility(View.VISIBLE);
        tvHaEAMain.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompMain.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpMain.setVisibility(View.VISIBLE);

        tvHoursPDayOpMain.setVisibility(View.VISIBLE);
        tvFieldCapacityMain.setVisibility(View.VISIBLE);
        tvFieldCapacityResultMain.setVisibility(View.VISIBLE);

        topLine.setVisibility(View.VISIBLE);
        botLineMain.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
//        edtTotalServiceAreaGrab.setVisibility(View.GONE);
//        edtHoursPDayGrab.setVisibility(View.GONE);
//        edtDaysPSeasonGrab.setVisibility(View.GONE);
//        edtNumberofLoadsGrab.setVisibility(View.GONE);
//
//        edtTotalServiceAreaDitch.setVisibility(View.GONE);
//        edtHoursPDayDitch.setVisibility(View.GONE);
//        edtDaysPSeasonDitch.setVisibility(View.GONE);
//
//
//        tvHaMain.setVisibility(View.GONE);
//        tvHoursPDayMain.setVisibility(View.GONE);
//        tvDaysPSeasonMain.setVisibility(View.GONE);
//
//
//        tvTypeofPlant.setVisibility(View.GONE);
//        tvDistanceofPlant.setVisibility(View.GONE);
//        tvHaPlant.setVisibility(View.GONE);
//        tvHoursPDayPlant.setVisibility(View.GONE);
//        tvDaysPSeasonPlant.setVisibility(View.GONE);
//
//
//        tvHaFert.setVisibility(View.GONE);
//        tvHoursPDayFert.setVisibility(View.GONE);
//        tvDaysPSeasonfFert.setVisibility(View.GONE);
//
//        tvHaHarvest.setVisibility(View.GONE);
//        tvHoursPDayHarvest.setVisibility(View.GONE);
//        tvDaysPSeasonHarvest.setVisibility(View.GONE);
//
//        tvHaGrab.setVisibility(View.GONE);
//        tvHoursPDayGrab.setVisibility(View.GONE);
//        tvDaysPSeasonGrab.setVisibility(View.GONE);
//        tvLoadPHaGrab.setVisibility(View.GONE);
//        tvLoadCapGrab.setVisibility(View.GONE);
//
//        tvHaDitch.setVisibility(View.GONE);
//        tvHoursPDayDitch.setVisibility(View.GONE);
//        tvDaysPSeasonDitch.setVisibility(View.GONE);
//
//        edtTotalServiceAreaMain.setVisibility(View.GONE);
//        edtHoursPDayMain.setVisibility(View.GONE);
//        edtDaysPSeasonMain.setVisibility(View.GONE);
//        edtTotalServiceAreaPlant.setVisibility(View.GONE);
//        edtHoursPDayPlant.setVisibility(View.GONE);
//        edtDaysPSeasonPlant.setVisibility(View.GONE);
//
//        edtTotalServiceAreaFert.setVisibility(View.GONE);
//        edtHoursPDayFert.setVisibility(View.GONE);
//        edtDaysPSeasonFert.setVisibility(View.GONE);
//
//        edtTotalServiceAreaHarvest.setVisibility(View.GONE);
//        edtHoursPDayHarvest.setVisibility(View.GONE);
//        edtDaysPSeasonHarvest.setVisibility(View.GONE);
        tvFieldCapHeader.setVisibility(View.GONE);
        tvFieldCapHeaderInfo.setVisibility(View.GONE);
        tvFieldCapHeaderPlant.setVisibility(View.GONE);
        tvFieldCapHeaderInfoPlant.setVisibility(View.GONE);
        tvDelRateHeader.setVisibility(View.GONE);
        tvDelRateHeaderInfo.setVisibility(View.GONE);

        edtEffectiveAreaAccompMain.setVisibility(View.GONE);
        edtTimeUsedDuringOpMain.setVisibility(View.GONE);

        spinTypeOfPlanter.setVisibility(View.GONE);
        edtNumberofRowsPlant.setVisibility(View.GONE);
        edtDistanceofPlantMat.setVisibility(View.GONE);

        edtEffectiveAreaAccompPlant.setVisibility(View.GONE);
        edtTimeUsedDuringOpPlant.setVisibility(View.GONE);

        edtEffectiveAreaAccompFertForWeight.setVisibility(View.GONE);
        edtEffectiveAreaAccompFert.setVisibility(View.GONE);
        edtTimeUsedDuringOpFert.setVisibility(View.GONE);
        edtWeightOfFert.setVisibility(View.GONE);

        edtEffectiveAreaAccompHarvest.setVisibility(View.GONE);
        edtTimeUsedDuringOpHarvest.setVisibility(View.GONE);
        edtAveYieldHarvest.setVisibility(View.GONE);

        edtEffectiveAreaAccompGrab.setVisibility(View.GONE);
        edtTimeUsedDuringOpGrab.setVisibility(View.GONE);
        edtLoadCapacityGrab.setVisibility(View.GONE);
        tvHaEAGrab.setVisibility(View.GONE);
        tvHoursPDayOpGrab.setVisibility(View.GONE);
        tvLoadCapacityGrab.setVisibility(View.GONE);
        tvFieldCapacityGrab.setVisibility(View.GONE);
        tvFieldCapacityResultGrab.setVisibility(View.GONE);

        edtDepthOfCutDitch.setVisibility(View.GONE);

        tvHaEAMain.setVisibility(View.GONE);
        tvHoursPDayOpMain.setVisibility(View.GONE);
        tvFieldCapacityMain.setVisibility(View.GONE);
        tvFieldCapacityResultMain.setVisibility(View.GONE);

        tvHaEAPlant.setVisibility(View.GONE);
        tvHoursPDayOpPlant.setVisibility(View.GONE);
        tvFieldCapacityPlant.setVisibility(View.GONE);
        tvFieldCapacityResultPlant.setVisibility(View.GONE);
        tvNumRowsPlant.setVisibility(View.GONE);
        tvDistanceofPlant.setVisibility(View.GONE);
        tvTypeofPlant.setVisibility(View.GONE);

        tvHaEAFert.setVisibility(View.GONE);
        tvHaEAFert2.setVisibility(View.GONE);
        tvHoursPDayOpFert.setVisibility(View.GONE);
        tvFieldCapacityFert.setVisibility(View.GONE);
        tvFieldCapacityResultFert.setVisibility(View.GONE);
        tvWeightOfFert.setVisibility(View.GONE);
        tvDeliveryRateFert.setVisibility(View.GONE);
        tvDeliveryRateResultFert.setVisibility(View.GONE);

        tvHaEAHarvest.setVisibility(View.GONE);
        tvHoursPDayOpHarvest.setVisibility(View.GONE);
        tvFieldCapacityHarvest.setVisibility(View.GONE);
        tvFieldCapacityResultHarvest.setVisibility(View.GONE);
        tvTonCannesPHaHarvest.setVisibility(View.GONE);

        tvDepthCutDitch.setVisibility(View.GONE);

        topLine.setVisibility(View.GONE);
        botLineMain.setVisibility(View.GONE);
        topLinePlant.setVisibility(View.GONE);
        botLinePlant.setVisibility(View.GONE);
        botLineFertDR.setVisibility(View.GONE);
        botLineFert.setVisibility(View.GONE);
        botLineHarvest.setVisibility(View.GONE);
        botLineGrab.setVisibility(View.GONE);

    }

    private void initViews() {

        spinImplementType = findViewById(R.id.spinImplementType);
        camera = findViewById(R.id.btnCameraImp);
        gallery = findViewById(R.id.btnGalleryImp);
        selectedImage = findViewById(R.id.imgMachineImp);
        getLocation = findViewById(R.id.btnGetLocationImp);
        edtQRCode = findViewById(R.id.edtQRCodeImp);
        btnScanQR = findViewById(R.id.btnScanQRCodeImp);
        spinMachineUsing = findViewById(R.id.spinMachineUsing);
        spinYearAcquired = findViewById(R.id.spinYearAcquiredImp);
        tvYearAcquired = findViewById(R.id.tvYearAcquiredImp);

        edtBrand = findViewById(R.id.edtBrandImp);
        edtModel = findViewById(R.id.edtModelImp);

        tvOwnership = findViewById(R.id.tvOwnershipImp);
        spinOwnership = findViewById(R.id.spinOwnershipImp);
        tvPurchGrantDono = findViewById(R.id.tvPurchGrantDonoImp);
        spinPurchGrantDono = findViewById(R.id.spinPurchGrantDonoImp);
        tvAgency = findViewById(R.id.tvAgencyImp);
        spinAgency = findViewById(R.id.spinAgencyImp);
        edtOtherAgency = findViewById(R.id.edtOtherAgencyImp);

        tvYearInoperable = findViewById(R.id.tvYearInoperableImp);
        spinYearInoperable = findViewById(R.id.spinYearInoperableImp);

        tvConditionPresent = findViewById(R.id.tvConditionPresentImp);
        spinConditionPresent = findViewById(R.id.spinConditionPresentImp);
        tvImplementUnused = findViewById(R.id.tvImplementUnused);
        multspinProblemsUnused = findViewById(R.id.multspinProblemsUnusedImp);
        edtOtherProblems = findViewById(R.id.edtOtherProblemsImp);
        tvLocation = findViewById(R.id.tvLocImp);

        singlespinProvinces = findViewById(R.id.singlespinProvincesImp);
        singlespinMunicipalities = findViewById(R.id.singlespinMunicipalitiesImp);
        singlespinBarangays = findViewById(R.id.singlespinBarangaysImp);
        tvLat = findViewById(R.id.tvLatitudeImp);
        tvLong = findViewById(R.id.tvLongitudeImp);
        tvAcc = findViewById(R.id.tvAccImp);
        spinLocation = findViewById(R.id.spinLocationImp);
        spinConditionAcquired = findViewById(R.id.spinConditionAcquiredImp);
        edtModifications = findViewById(R.id.edtModifications);
        btnSave = findViewById(R.id.btnSaveImp);


        cbLandClear = findViewById(R.id.cbLandClearing);
        cbPrePlant = findViewById(R.id.cbPrePlanting);
        cbPlant = findViewById(R.id.cbPlanting);
        cbFert = findViewById(R.id.cbFertilizer);
        cbPest = findViewById(R.id.cbPesticide);
        cbIrriDrain = findViewById(R.id.cbIrriDrain);
        cbCult = findViewById(R.id.cbCultivation);
        cbRatoon = findViewById(R.id.cbRatooning);
        cbHarvest = findViewById(R.id.cbHarvest);
        cbPostHarvest = findViewById(R.id.cbPostHarvest);
        cbHaul = findViewById(R.id.cbHauling);

        tvFieldCapHeader = findViewById(R.id.tvFieldCapHeader);
        tvFieldCapHeaderInfo = findViewById(R.id.tvFieldCapHeaderInfo);

//        edtTotalServiceAreaMain = findViewById(R.id.edtTotalServiceAreaMain);
//        edtHoursPDayMain = findViewById(R.id.edtHoursPDayMain);
//        edtDaysPSeasonMain = findViewById(R.id.edtDaysPSeasonMain);
//        tvHaMain = findViewById(R.id.tvHaMain);
//        tvHoursPDayMain = findViewById(R.id.tvHoursPDayMain);
//        tvDaysPSeasonMain = findViewById(R.id.tvDaysPSeasonMain);
//        edtTotalServiceAreaPlant = findViewById(R.id.edtTotalServiceAreaPlant);
//        edtHoursPDayPlant = findViewById(R.id.edtHoursPDayPlant);
//        edtDaysPSeasonPlant = findViewById(R.id.edtDaysPSeasonPlant);

//        tvHaPlant = findViewById(R.id.tvHaPlant);
//        tvHoursPDayPlant = findViewById(R.id.tvHoursPDayPlant);
//        tvDaysPSeasonPlant = findViewById(R.id.tvDaysPSeasonPlant);
//        edtTotalServiceAreaFert = findViewById(R.id.edtTotalServiceAreaFert);
//        edtHoursPDayFert = findViewById(R.id.edtHoursPDayFert);
//        edtDaysPSeasonFert = findViewById(R.id.edtDaysPSeasonFert);
//        tvHaFert = findViewById(R.id.tvHaFert);
//        tvHoursPDayFert = findViewById(R.id.tvHoursPDayFert);
//        tvDaysPSeasonfFert = findViewById(R.id.tvDaysPSeasonFert);
//        edtTotalServiceAreaHarvest = findViewById(R.id.edtTotalServiceAreaHarvest);
//        edtHoursPDayHarvest = findViewById(R.id.edtHoursPDayHarvest);
//        edtDaysPSeasonHarvest = findViewById(R.id.edtDaysPSeasonHarvest);
//        tvHaHarvest = findViewById(R.id.tvHaHarvest);
//        tvHoursPDayHarvest = findViewById(R.id.tvHoursPDayHarvest);
//        tvDaysPSeasonHarvest = findViewById(R.id.tvDaysPSeasonHarvest);
//        edtTotalServiceAreaGrab = findViewById(R.id.edtTotalServiceAreaGrab);
//        edtHoursPDayGrab = findViewById(R.id.edtHoursPDayGrab);
//        edtDaysPSeasonGrab = findViewById(R.id.edtDaysPSeasonGrab);
//        edtNumberofLoadsGrab = findViewById(R.id.edtNumberofLoadsGrab);
//        tvHaGrab = findViewById(R.id.tvHaGrab);
//        tvHoursPDayGrab = findViewById(R.id.tvHoursPDayGrab);
//        tvDaysPSeasonGrab = findViewById(R.id.tvDaysPSeasonGrab);
//        tvLoadPHaGrab = findViewById(R.id.tvLoadPHaGrab);
//        tvLoadCapGrab = findViewById(R.id.tvLoadCapGrab);
//        edtTotalServiceAreaDitch = findViewById(R.id.edtTotalServiceAreaDitch);
//        edtHoursPDayDitch = findViewById(R.id.edtHoursPDayDitch);
//        edtDaysPSeasonDitch = findViewById(R.id.edtDaysPSeasonDitch);
//        tvHoursPDayDitch = findViewById(R.id.tvHoursPDayDitch);
//        tvDaysPSeasonDitch = findViewById(R.id.tvDaysPSeasonDitch);
//        tvHaDitch = findViewById(R.id.tvHaDitch);
        edtEffectiveAreaAccompMain = findViewById(R.id.edtEffectiveAreaAccompMain);
        edtTimeUsedDuringOpMain = findViewById(R.id.edtTimeUsedDuringOpMain);
        tvHaEAMain = findViewById(R.id.tvHaEAMain);
        tvHoursPDayOpMain = findViewById(R.id.tvHoursPDayOpMain);
        tvFieldCapacityMain = findViewById(R.id.tvFieldCapacityMain);
        tvFieldCapacityResultMain = findViewById(R.id.tvFieldCapacityResultMain);

        spinTypeOfPlanter = findViewById(R.id.spinTypeOfPlanterPlant);
        edtNumberofRowsPlant = findViewById(R.id.edtNumberofRowsPlant);
        edtDistanceofPlantMat = findViewById(R.id.edtDistanceOfPlantMatPlant);
        edtEffectiveAreaAccompPlant = findViewById(R.id.edtEffectiveAreaAccompPlant);
        edtTimeUsedDuringOpPlant = findViewById(R.id.edtTimeUsedDuringOpPlant);
        tvHaEAPlant = findViewById(R.id.tvHaEAPlant);
        tvHoursPDayOpPlant = findViewById(R.id.tvHoursPDayOpPlant);
        tvFieldCapHeaderPlant = findViewById(R.id.tvFieldCapHeaderPlanter);
        tvFieldCapHeaderInfoPlant = findViewById(R.id.tvFieldCapHeaderInfoPlanter);
        tvFieldCapacityPlant = findViewById(R.id.tvFieldCapacityPlant);
        tvFieldCapacityResultPlant = findViewById(R.id.tvFieldCapacityResultPlant);
        tvNumRowsPlant = findViewById(R.id.tvNumRowsPlant);
        tvTypeofPlant = findViewById(R.id.tvTypeofPlant);
        tvDistanceofPlant = findViewById(R.id.tvDistancePlant);

        edtEffectiveAreaAccompFert = findViewById(R.id.edtEffectiveAreaAccompFert);
        edtTimeUsedDuringOpFert = findViewById(R.id.edtTimeUsedDuringOpFert);
        edtWeightOfFert = findViewById(R.id.edtWeightOfFertFert);
        edtEffectiveAreaAccompFertForWeight = findViewById(R.id.edtEffectiveAreaAccompFert2);
        tvHaEAFert = findViewById(R.id.tvHaEAFert);
        tvHaEAFert2 = findViewById(R.id.tvHaEAFert2);
        tvHoursPDayOpFert = findViewById(R.id.tvHoursPDayOpFert);
        tvFieldCapacityFert = findViewById(R.id.tvFieldCapacityFert);
        tvFieldCapacityResultFert = findViewById(R.id.tvFieldCapacityResultFert);

        tvDelRateHeader = findViewById(R.id.tvDelRateHeader);
        tvDelRateHeaderInfo = findViewById(R.id.tvDelRateHeaderInfo);
        tvWeightOfFert = findViewById(R.id.tvWeightFert);
        tvDeliveryRateFert = findViewById(R.id.tvDeliveryRateFert);
        tvDeliveryRateResultFert = findViewById(R.id.tvDeliveryRateResultFert);

        edtEffectiveAreaAccompHarvest = findViewById(R.id.edtEffectiveAreaAccompHarvest);
        edtTimeUsedDuringOpHarvest = findViewById(R.id.edtTimeUsedDuringOpHarvest);
        edtAveYieldHarvest = findViewById(R.id.edtAveYieldHarvest);
        tvHaEAHarvest = findViewById(R.id.tvHaEAHarvest);
        tvHoursPDayOpHarvest = findViewById(R.id.tvHoursPDayOpHarvest);
        tvFieldCapacityHarvest = findViewById(R.id.tvFieldCapacityHarvest);
        tvFieldCapacityResultHarvest = findViewById(R.id.tvFieldCapacityResultHarvest);
        tvTonCannesPHaHarvest = findViewById(R.id.tvTonCannesPHaHarvest);

        edtEffectiveAreaAccompGrab = findViewById(R.id.edtEffectiveAreaAccompGrab);
        edtTimeUsedDuringOpGrab = findViewById(R.id.edtTimeUsedDuringOpGrab);
        edtLoadCapacityGrab = findViewById(R.id.edtLoadCapacityGrab);
        tvHaEAGrab = findViewById(R.id.tvHaEAGrab);
        tvHoursPDayOpGrab = findViewById(R.id.tvHoursPdayOpGrab);
        tvLoadCapacityGrab = findViewById(R.id.tvLoadCapGrab);
        tvFieldCapacityGrab = findViewById(R.id.tvFieldCapacityGrab);
        tvFieldCapacityResultGrab = findViewById(R.id.tvFieldCapacityResultGrab);

        edtDepthOfCutDitch = findViewById(R.id.edtDepthOfCutDitch);
        tvDepthCutDitch = findViewById(R.id.tvDepthCutDitch);


        topLine = findViewById(R.id.topLine);
        botLineMain = findViewById(R.id.botLineMain);
        topLinePlant = findViewById(R.id.topLinePlant);
        botLinePlant = findViewById(R.id.botLinePlant);
        botLineFertDR = findViewById(R.id.botLineFertDR);
        botLineFert = findViewById(R.id.botLineFert);
        botLineHarvest = findViewById(R.id.botLineHarvest);
        botLineGrab = findViewById(R.id.botLineGrab);
    }

    private void initAllLayoutParameters() {


        paramsEAMain = (ConstraintLayout.LayoutParams) tvHaEAMain.getLayoutParams();
        paramstvPlanter = (ConstraintLayout.LayoutParams) tvTypeofPlant.getLayoutParams();
        paramsEAFert = (ConstraintLayout.LayoutParams) tvHaEAFert.getLayoutParams();
        paramsEAHarvest = (ConstraintLayout.LayoutParams) tvHaEAHarvest.getLayoutParams();
        paramsEAGrab = (ConstraintLayout.LayoutParams) tvHaEAGrab.getLayoutParams();
        paramsDCDitch = (ConstraintLayout.LayoutParams) tvDepthCutDitch.getLayoutParams();
        paramsYearAcquired = (ConstraintLayout.LayoutParams) tvYearAcquired.getLayoutParams();

        paramstvLocation = (ConstraintLayout.LayoutParams) tvLocation.getLayoutParams();
        paramstvImplementUnused = (ConstraintLayout.LayoutParams) tvImplementUnused.getLayoutParams();
        paramstvConditionPresent = (ConstraintLayout.LayoutParams) tvConditionPresent.getLayoutParams();

        paramstvOwnership = (ConstraintLayout.LayoutParams) tvOwnership.getLayoutParams();
    }

    private void conditionModifications(int position) {
        switch (position) {
            default:
                edtModifications.setVisibility(View.GONE);
                paramstvConditionPresent.topToBottom = R.id.spinConditionAcquiredImp;
                break;
            case 3:
                edtModifications.setVisibility(View.VISIBLE);
                paramstvConditionPresent.topToBottom = R.id.edtModifications;
                break;
        }
        paramstvConditionPresent.topMargin = bigMargin;
        tvConditionPresent.setLayoutParams(paramstvConditionPresent);
    }

    private void problemsUnused(int position) {
        String pos = spinConditionPresent.getItemAtPosition(position).toString();
        List<KeyPairBoolData> selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));
        int stringArray = R.array.blank;

        switch (pos) {
            case "FUNCTIONAL USED":
            default:
                multspinProblemsUnused.setVisibility(View.GONE);
                edtOtherProblems.setVisibility(View.GONE);
                tvImplementUnused.setVisibility(View.GONE);
                tvYearInoperable.setVisibility(View.GONE);
                spinYearInoperable.setVisibility(View.GONE);
                paramstvLocation.topToBottom = R.id.spinConditionPresentImp;
                break;
            case "FUNCTIONAL UNUSED":
                multspinProblemsUnused.setVisibility(View.VISIBLE);
                tvImplementUnused.setText("Why is this Implement unused?");
                tvImplementUnused.setVisibility(View.VISIBLE);
                tvYearInoperable.setVisibility(View.VISIBLE);
                spinYearInoperable.setVisibility(View.VISIBLE);
                stringArray = R.array.problems_unused_implement;
                paramstvImplementUnused.topToBottom = R.id.spinYearInoperableImp;
                paramstvLocation.topToBottom = R.id.multspinProblemsUnusedImp;
                break;
            case "NON-FUNCTIONAL":
                multspinProblemsUnused.setVisibility(View.VISIBLE);
                tvImplementUnused.setText("Why is this Implement non-functional?");
                tvYearInoperable.setVisibility(View.VISIBLE);
                spinYearInoperable.setVisibility(View.VISIBLE);
                tvImplementUnused.setVisibility(View.VISIBLE);
                stringArray = R.array.problems_nonfunctional;
                paramstvImplementUnused.topToBottom = R.id.spinYearInoperableImp;
                paramstvLocation.topToBottom = R.id.multspinProblemsUnusedImp;
                break;
        }


        selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(stringArray)));
        multspinProblemsUnused.setItems(selectedProb, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = selectedItems.get(i).getName() + " : " + pos;
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
                listOfProblems = pos;
                if (pos.contains("OTHERS")) {
                    edtOtherProblems.setVisibility(View.VISIBLE);
                    paramstvLocation.topToBottom = R.id.edtOtherProblemsImp;
                    hasOtherProblems = true;
                } else {
                    hasOtherProblems = false;
                    edtOtherProblems.setVisibility(View.GONE);
                    paramstvLocation.topToBottom = R.id.multspinProblemsUnusedImp;
                    edtOtherProblems.setText("");
                }

            }
        });
        paramstvLocation.topMargin = bigMargin;
        tvLocation.setLayoutParams(paramstvLocation);


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

    private void ownershipSelect(int position) {
        String pos = spinOwnership.getItemAtPosition(position).toString();
        List<String> stringListAgency = Arrays.asList(getResources().getStringArray(R.array.blank));

        Intent intent = intentFromDb;
        Log.d("GOT TO OWNERSHIP", pos);
        switch (pos) {

            case "PRIVATELY OWNED":
                if (intent == null || !intent.hasExtra(EXTRA_IMP_ID)) {
                    spinPurchGrantDono.setSelection(0);
                }
                spinPurchGrantDono.setVisibility(View.GONE);
                tvPurchGrantDono.setVisibility(View.VISIBLE);
                spinAgency.setVisibility(View.GONE);
                tvPurchGrantDono.setVisibility(View.GONE);
                tvAgency.setVisibility(View.GONE);
                paramsYearAcquired.topToBottom = R.id.spinOwnershipImp;
                stringListAgency = Arrays.asList(getResources().getStringArray(R.array.agency_loan));
                break;
            case "COOPERATIVE/ASSOCIATION":
            case "CUSTOM PROVIDER":
            case "LGU":
                spinPurchGrantDono.setVisibility(View.VISIBLE);
                tvPurchGrantDono.setVisibility(View.VISIBLE);
                paramsYearAcquired.topToBottom = R.id.spinPurchGrantDonoImp;
                stringListAgency = Arrays.asList(getResources().getStringArray(R.array.agency));
                break;
            default:
                if (intent == null || !intent.hasExtra(EXTRA_IMP_ID)) {
                    spinPurchGrantDono.setSelection(0);
                }
                spinPurchGrantDono.setVisibility(View.GONE);
                spinAgency.setVisibility(View.GONE);
                tvPurchGrantDono.setVisibility(View.GONE);
                tvAgency.setVisibility(View.GONE);
                paramsYearAcquired.topToBottom = R.id.spinOwnershipImp;
                break;
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListAgency);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAgency.setAdapter(dataAdapter);

        paramsYearAcquired.topMargin = bigMargin;
        tvYearAcquired.setLayoutParams(paramsYearAcquired);

        if (intent != null && intent.hasExtra(EXTRA_IMP_ID)) {
            String stringCompare = intent.getStringExtra(EXTRA_AGENCY);

            if (!isNullOrEmpty(stringCompare)) {
                position = dataAdapter.getPosition(stringCompare);
            }
            spinAgency.setSelection(position);
        }
    }

    private void purchGrantDonoSelect(int position) {
        String pos = spinPurchGrantDono.getItemAtPosition(position).toString();

        switch (pos) {
            case "PURCHASED":
            default:
                spinAgency.setSelection(0);
                Log.d("INSIDE PURCHASE", pos);
                purchGrantDono = "PURCHASED";
                spinAgency.setVisibility(View.GONE);
                paramsYearAcquired.topToBottom = R.id.spinPurchGrantDonoImp;
                tvAgency.setVisibility(View.GONE);
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
                paramsYearAcquired.topToBottom = R.id.spinAgencyImp;
                break;
        }

        paramsYearAcquired.topMargin = bigMargin;
        tvYearAcquired.setLayoutParams(paramsYearAcquired);
    }

    private void otherAgency(int position) {
        String pos = spinAgency.getItemAtPosition(position).toString();

        Log.d("POSITION OF AGENCY", pos);

        if ("OTHERS".equals(pos)) {
            paramsYearAcquired.topToBottom = R.id.edtOtherAgencyImp;
            edtOtherAgency.setVisibility(View.VISIBLE);
        } else {
            paramsYearAcquired.topToBottom = R.id.spinAgencyImp;
            edtOtherAgency.setVisibility(View.GONE);
        }
        paramsYearAcquired.topMargin = bigMargin;
        tvYearAcquired.setLayoutParams(paramsYearAcquired);
    }

    private void setInputFilters() {
        edtEffectiveAreaAccompMain.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtEffectiveAreaAccompPlant.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtEffectiveAreaAccompFert.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtEffectiveAreaAccompHarvest.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtEffectiveAreaAccompGrab.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        edtAveYieldHarvest.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        edtTimeUsedDuringOpMain.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        edtTimeUsedDuringOpPlant.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        edtTimeUsedDuringOpFert.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        edtTimeUsedDuringOpHarvest.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});
        edtTimeUsedDuringOpGrab.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3, 2)});

        edtDistanceofPlantMat.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});
        edtWeightOfFert.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});
        edtDepthOfCutDitch.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

    }

    private void clearCursor() {
        edtQRCode.setFocusable(false);
        edtEffectiveAreaAccompMain.setFocusable(false);
        edtTimeUsedDuringOpMain.setFocusable(false);
        edtNumberofRowsPlant.setFocusable(false);
        edtDistanceofPlantMat.setFocusable(false);
        edtEffectiveAreaAccompPlant.setFocusable(false);
        edtTimeUsedDuringOpPlant.setFocusable(false);
        edtEffectiveAreaAccompFert.setFocusable(false);
        edtTimeUsedDuringOpFert.setFocusable(false);
        edtWeightOfFert.setFocusable(false);
        edtEffectiveAreaAccompHarvest.setFocusable(false);
        edtTimeUsedDuringOpHarvest.setFocusable(false);
        edtAveYieldHarvest.setFocusable(false);
        edtEffectiveAreaAccompGrab.setFocusable(false);
        edtTimeUsedDuringOpGrab.setFocusable(false);
        edtLoadCapacityGrab.setFocusable(false);
        edtDepthOfCutDitch.setFocusable(false);
        edtOtherAgency.setFocusable(false);
        edtModifications.setFocusable(false);
        edtOtherProblems.setFocusable(false);
    }

    private void setOnTouchEditText() {
        edtQRCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtQRCode);
                return false;
            }
        });
        edtEffectiveAreaAccompMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtEffectiveAreaAccompMain);
                return false;
            }
        });
        edtTimeUsedDuringOpMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtTimeUsedDuringOpMain);
                return false;
            }
        });
        edtNumberofRowsPlant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtNumberofRowsPlant);
                return false;
            }
        });
        edtDistanceofPlantMat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtDistanceofPlantMat);
                return false;
            }
        });
        edtEffectiveAreaAccompPlant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtEffectiveAreaAccompPlant);
                return false;
            }
        });
        edtTimeUsedDuringOpPlant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtTimeUsedDuringOpPlant);
                return false;
            }
        });
        edtEffectiveAreaAccompFert.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtEffectiveAreaAccompFert);
                return false;
            }
        });
        edtEffectiveAreaAccompFertForWeight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtEffectiveAreaAccompFertForWeight);
                return false;
            }
        });
        edtTimeUsedDuringOpFert.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtTimeUsedDuringOpFert);
                return false;
            }
        });
        edtWeightOfFert.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtWeightOfFert);
                return false;
            }
        });
        edtEffectiveAreaAccompHarvest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtEffectiveAreaAccompHarvest);
                return false;
            }
        });
        edtTimeUsedDuringOpHarvest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtTimeUsedDuringOpHarvest);
                return false;
            }
        });
        edtAveYieldHarvest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtAveYieldHarvest);
                return false;
            }
        });
        edtEffectiveAreaAccompGrab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtEffectiveAreaAccompGrab);
                return false;
            }
        });
        edtTimeUsedDuringOpGrab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtTimeUsedDuringOpGrab);
                return false;
            }
        });
        edtLoadCapacityGrab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtLoadCapacityGrab);
                return false;
            }
        });
        edtDepthOfCutDitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtDepthOfCutDitch);
                return false;
            }
        });
        edtOtherAgency.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtOtherAgency);
                return false;
            }
        });
        edtModifications.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtModifications);
                return false;
            }
        });
        edtOtherProblems.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusOnEdt(edtOtherProblems);
                return false;
            }
        });
    }

    private void setFocusOnEdt(EditText focusThis) {
        focusThis.setFocusableInTouchMode(true);
    }

    private Bitmap scale(Bitmap bitmap) {
        // Determine the constrained dimension, which determines both dimensions.
        int width;
        int height;
        float widthRatio = (float) bitmap.getWidth() / 1440;
        float heightRatio = (float) bitmap.getHeight() / 2560;
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = 1440;
            height = (int) (((float) width / bitmap.getWidth()) * bitmap.getHeight());
        }
        // Height constrained.
        else {
            height = 2560;
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

//        public void askCameraPermission () {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
//            } else {
//            }
//
//        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_icon);
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
/*    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }*/
    }

    static class MachineClass {
        String code;
        String type;

        MachineClass(String code, String type) {
            this.type = type;
            this.code = code;
        }
    }
}
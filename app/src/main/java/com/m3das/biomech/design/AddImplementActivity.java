package com.m3das.biomech.design;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.m3das.biomech.design.machinedb.Machines;
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

public class AddImplementActivity extends AppCompatActivity {

    private Spinner spinImplementType, spinMachineUsing, spinTypeOfPlanter, spinYearAcquired, spinLocation, spinCondition;
    ImageButton camera, gallery, getLocation, btnScanQR;
    String currentPhotoPath, dateToStr, machineCode, machineComplete, landClear, prePlant, planting, fertilizer, pesticide, irrigationDrainage,
            cultivation, ratooning, harvest, postHarvest, hauling, encodedImage, machineQR, accuracy;
    ImageView selectedImage;
    Button btnSave;
    SingleSpinnerSearch singlespinProvinces, singlespinMunicipalities, singlespinBarangays;
    ConstraintLayout.LayoutParams paramsYearAcquired, paramsTSAMain, paramstvPlanter, paramsTSAFert, paramsTSAHarvest, paramsTSAGrab, paramsTSADitch;
    EditText edtQRCode, edtTotalServiceAreaMain, edtHoursPDayMain, edtDaysPSeasonMain, edtEffectiveAreaAccompMain, edtTimeUsedDuringOpMain,
            edtNumberofRowsPlant, edtDistanceofPlantMat, edtTotalServiceAreaPlant, edtHoursPDayPlant, edtDaysPSeasonPlant, edtEffectiveAreaAccompPlant, edtTimeUsedDuringOpPlant,
            edtTotalServiceAreaFert, edtHoursPDayFert, edtDaysPSeasonFert, edtEffectiveAreaAccompFert, edtTimeUsedDuringOpFert, edtWeightOfFert,
            edtTotalServiceAreaHarvest, edtHoursPDayHarvest, edtDaysPSeasonHarvest, edtEffectiveAreaAccompHarvest, edtTimeUsedDuringOpHarvest, edtAveYieldHarvest,
            edtTotalServiceAreaGrab, edtHoursPDayGrab, edtDaysPSeasonGrab, edtLoadCapacityGrab, edtNumberofLoadsGrab,
            edtTotalServiceAreaDitch, edtHoursPDayDitch, edtDaysPSeasonDitch, edtDepthOfCutDitch;

    TextView tvYearAcquired, tvLat, tvLong,
            tvHaMain, tvHoursPDayMain, tvDaysPSeasonMain, tvHaEAMain, tvHoursPDayOpMain, tvFieldCapacityMain, tvFieldCapacityResultMain,
            tvTypeofPlant, tvDistanceofPlant, tvHaPlant, tvHoursPDayPlant, tvDaysPSeasonPlant, tvHaEAPlant, tvHoursPDayOpPlant, tvFieldCapacityPlant, tvFieldCapacityResultPlant,
            tvHaFert, tvHoursPDayFert, tvDaysPSeasonfFert, tvHaEAFert, tvHoursPDayOpFert, tvFieldCapacityFert, tvFieldCapacityResultFert, tvWeightOfFert, tvDeliveryRateFert, tvDeliveryRateResultFert,
            tvHaHarvest, tvHoursPDayHarvest, tvDaysPSeasonHarvest, tvHaEAHarvest, tvHoursPDayOpHarvest, tvFieldCapacityHarvest, tvFieldCapacityResultHarvest, tvTonCannesPHaHarvest,
            tvHaGrab, tvHoursPDayGrab, tvDaysPSeasonGrab, tvLoadPHaGrab,
            tvHaDitch, tvHoursPDayDitch, tvDaysPSeasonDitch, tvDepthCutDitch;
    CheckBox cbLandClear, cbPrePlant, cbPlant, cbFert, cbPest, cbIrriDrain, cbCult, cbRatoon, cbHarvest, cbPostHarvest, cbHaul;
    int bigMargin, smallMargin;
    Double fieldCap = null;
    Double deliveryCap = null;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int LOCATION_REQUEST_CODE = 127;
    public static final int WRITE_PERM_CODE = 279;
    static Uri capturedImageUri = null;
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


    private String resLat, resLong;
    private MachineListViewModel machineListViewModel;

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

        paramsYearAcquired.topToBottom = R.id.cbHauling;
        paramsYearAcquired.topMargin = bigMargin;
        tvYearAcquired.setLayoutParams(paramsYearAcquired);


        spinYearsSetAdapter();

        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);
        machineListViewModel.getAllMachines().observe(this, new Observer<List<Machines>>() {
            @Override
            public void onChanged(List<Machines> machines) {
                ArrayList<String> stringArrayList = new ArrayList<>();
                for (int i = machines.size() - 1; i >= 0; i--) {
                    stringArrayList.add(machines.get(i).getId() + " " + machines.get(i).getMachine_qrcode() + " | " + machines.get(i).getMachine_type());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, stringArrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinMachineUsing.setAdapter(adapter);
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
                used = spinMachineUsing.getSelectedItem().toString();
                Log.d("Machine using", used.substring(0, 1) + used.substring(2, 14));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinImplementType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectImplement(spinImplementType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<KeyPairBoolData> allProvinces = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.provinces)));
        singlespinProvinces.setItems(allProvinces, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

                Log.d("Single Prov", selectedItem.getName());

            }

            @Override
            public void onClear() {

            }
        });

        List<KeyPairBoolData> allMunicipalities = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.municipalities)));
        singlespinMunicipalities.setItems(allMunicipalities, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

                Log.d("Single Mun", selectedItem.getName());

            }

            @Override
            public void onClear() {
            }
        });

        List<KeyPairBoolData> allBarangays = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.barangays)));
        singlespinBarangays.setItems(allBarangays, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {
                Log.d("Single Brgy", selectedItem.getName());
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
                    prePlant = "PRE PLANTING";
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
                    pesticide = "FERTILIZER APPLICATION";
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

        cbHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbHarvest.isChecked()) {
                    harvest = "HARVESTING";
                } else {
                    harvest = "";
                }

            }
        });

        cbPostHarvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbPostHarvest.isChecked()) {
                    postHarvest = "POST HARVEST";
                } else {
                    postHarvest = "";
                }

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

        edtTimeUsedDuringOpMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) && !isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())) {
                    tvFieldCapacityResultMain.setText(getFieldCapacity(edtEffectiveAreaAccompMain.getText().toString(),edtTimeUsedDuringOpMain.getText().toString()));
                }
                else if(isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())){
                    tvFieldCapacityResultMain.setText("Not yet acquired");
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
                    tvFieldCapacityResultMain.setText(getFieldCapacity(edtEffectiveAreaAccompMain.getText().toString(),edtTimeUsedDuringOpMain.getText().toString()));
                }
                else if(isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())){
                    tvFieldCapacityResultMain.setText("Not yet acquired");
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
                    tvFieldCapacityResultMain.setText(getFieldCapacity(edtEffectiveAreaAccompMain.getText().toString(),edtTimeUsedDuringOpMain.getText().toString()));
                }
                else if(isNullOrEmpty(edtEffectiveAreaAccompMain.getText().toString()) || isNullOrEmpty(edtTimeUsedDuringOpMain.getText().toString())){
                    tvFieldCapacityResultMain.setText("Not yet acquired");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


    }

    private void initTextViewData() {
        tvFieldCapacityResultMain.setText("Not yet Acquired");
        tvFieldCapacityResultPlant.setText("Not yet Acquired");
        tvFieldCapacityResultFert.setText("Not yet Acquired");
        tvDeliveryRateResultFert.setText("Not yet Acquired");
        tvFieldCapacityResultHarvest.setText("Not yet Acquired");
        tvLat.setText("Not yet Acquired");
        tvLong.setText("Not yet Acquired");
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

    private void initTextViewData() {
        tvFieldCapacityResultMain.setText("Not yet Acquired");
        tvFieldCapacityResultPlant.setText("Not yet Acquired");
        tvFieldCapacityResultFert.setText("Not yet Acquired");
        tvDeliveryRateResultFert.setText("Not yet Acquired");
        tvFieldCapacityResultHarvest.setText("Not yet Acquired");
        tvLat.setText("Not yet Acquired");
        tvLong.setText("Not yet Acquired");
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

    private void initTextViewData() {
        tvFieldCapacityResultMain.setText("Not yet Acquired");
        tvFieldCapacityResultPlant.setText("Not yet Acquired");
        tvFieldCapacityResultFert.setText("Not yet Acquired");
        tvDeliveryRateResultFert.setText("Not yet Acquired");
        tvFieldCapacityResultHarvest.setText("Not yet Acquired");
        tvLat.setText("Not yet Acquired");
        tvLong.setText("Not yet Acquired");
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

    private void initTextViewData() {
        tvFieldCapacityResultMain.setText("Not yet Acquired");
        tvFieldCapacityResultPlant.setText("Not yet Acquired");
        tvFieldCapacityResultFert.setText("Not yet Acquired");
        tvDeliveryRateResultFert.setText("Not yet Acquired");
        tvFieldCapacityResultHarvest.setText("Not yet Acquired");
        tvLat.setText("Not yet Acquired");
        tvLong.setText("Not yet Acquired");
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

            }
        });

        ArrayList<String> years = new ArrayList<String>();
        years.add("");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYearAcquired.setAdapter(adapter);
    }

    private void setMarginSize() {
        smallMargin = (int) pxFromDp(this, 8);
        bigMargin = (int) pxFromDp(this, 32);
    }

    private String getFieldCapacity(String area,String time){
        String fieldCapacity = "";
        fieldCapacity = String.valueOf(Double.parseDouble(area) / Double.parseDouble(time));
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
        Intent dataAddImplement = new Intent();

//        getComputations();

        dateToStr = new SimpleDateFormat("MM/dd/yy HH:mm:ss").format(new Date());

        String implementType = spinImplementType.getSelectedItem().toString();
        String implementQRCode = edtQRCode.getText().toString();

        if(isNullOrEmpty(encodedImage)){
            encodedImage = "Not yet Acquired";
        }

        if (isNullOrEmpty(accuracy)){
            accuracy = "Not yet Acquired";
        }

        String temp = spinMachineUsing.getSelectedItem().toString();
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
        dataAddImplement.putExtra(EXTRA_IMP_TYPE, spinImplementType.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_IMP_QR, edtQRCode.getText().toString());
        dataAddImplement.putExtra(EXTRA_DATE, dateToStr);
        dataAddImplement.putExtra(EXTRA_USED_ON, temp.substring(2, 14));
        dataAddImplement.putExtra(EXTRA_USED_COMPLETE, temp);
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
        dataAddImplement.putExtra(EXTRA_TSA_MAIN, edtTotalServiceAreaMain.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_MAIN, edtHoursPDayMain.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_MAIN, edtDaysPSeasonMain.getText().toString());
        dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_MAIN, edtEffectiveAreaAccompMain.getText().toString());
        dataAddImplement.putExtra(EXTRA_TIME_USED_OP_MAIN, edtTimeUsedDuringOpMain.getText().toString());
        dataAddImplement.putExtra(EXTRA_FIELD_CAP_MAIN, tvFieldCapacityResultMain.getText().toString());
        dataAddImplement.putExtra(EXTRA_TYPE_PLANT, spinTypeOfPlanter.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_NUM_ROWS_PLANT, edtNumberofRowsPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_DIST_MAT_PLANT, edtDistanceofPlantMat.getText().toString());
        dataAddImplement.putExtra(EXTRA_TSA_PLANT, edtTotalServiceAreaPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_PLANT, edtHoursPDayPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_PLANT, edtDaysPSeasonPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_PLANT, edtEffectiveAreaAccompPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_TIME_USED_OP_PLANT, edtTimeUsedDuringOpPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_FIELD_CAP_PLANT, tvFieldCapacityResultPlant.getText().toString());
        dataAddImplement.putExtra(EXTRA_TSA_FERT, edtTotalServiceAreaFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_FERT, edtHoursPDayFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_FERT, edtHoursPDayFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_FERT, edtEffectiveAreaAccompFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_TIME_USED_OP_FERT, edtTimeUsedDuringOpFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_FIELD_CAP_FERT, tvFieldCapacityResultFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_WEIGHT_FERT, edtWeightOfFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_DEL_RATE_FERT, tvDeliveryRateResultFert.getText().toString());
        dataAddImplement.putExtra(EXTRA_TSA_HARVEST, edtTotalServiceAreaHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_HARVEST, edtHoursPDayHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_HARVEST, edtDaysPSeasonHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_HARVEST, edtEffectiveAreaAccompHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_TIME_USED_OP_HARVEST, edtTimeUsedDuringOpHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_FIELD_CAP_HARVEST, tvFieldCapacityResultHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_YIELD_HARVEST, edtAveYieldHarvest.getText().toString());
        dataAddImplement.putExtra(EXTRA_TSA_GRAB, edtTotalServiceAreaGrab.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_GRAB, edtHoursPDayGrab.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_GRAB, edtDaysPSeasonGrab.getText().toString());
//        dataAddImplement.putExtra(EXTRA_EFF_AREA_ACC_GRAB,effe);
        dataAddImplement.putExtra(EXTRA_LOAD_CAP_GRAB, edtLoadCapacityGrab.getText().toString());
        dataAddImplement.putExtra(EXTRA_NUM_LOAD_GRAB, edtNumberofLoadsGrab.getText().toString());
        dataAddImplement.putExtra(EXTRA_TSA_DITCH, edtTotalServiceAreaDitch.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_HOURS_DITCH, edtHoursPDayDitch.getText().toString());
        dataAddImplement.putExtra(EXTRA_AVE_OP_DAYS_DITCH, edtDaysPSeasonDitch.getText().toString());
        dataAddImplement.putExtra(EXTRA_DEPTH_CUT_DITCH, edtDepthOfCutDitch.getText().toString());
        dataAddImplement.putExtra(EXTRA_YEAR_ACQUIRED, spinYearAcquired.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_CONDITION, spinCondition.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_LOCATION, spinLocation.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_PROVINCE, singlespinProvinces.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_MUNICIPALITY, singlespinMunicipalities.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_BARANGAY, singlespinBarangays.getSelectedItem().toString());
        dataAddImplement.putExtra(EXTRA_LATITUDE, tvLat.getText().toString());
        dataAddImplement.putExtra(EXTRA_LONGITUDE, tvLong.getText().toString());
        dataAddImplement.putExtra(EXTRA_ACCURACY, accuracy);
        Variable.setStringImage(encodedImage);

        setResult(RESULT_OK, dataAddImplement);
        finish();
//        }
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public List<KeyPairBoolData> pairingOfList(List<String> stringList) {
        final List<KeyPairBoolData> listArray1 = new ArrayList<>();
        List<String> list = stringList;
        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            listArray1.add(h);
        }
        return listArray1;
    }

            }
        });

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
                paramsTSAMain.topToBottom = R.id.cbHauling;
                paramsTSAMain.topMargin = bigMargin;
                edtTotalServiceAreaMain.setLayoutParams(paramsTSAMain);
                tvYearAcquired.setLayoutParams(paramsYearAcquired);
                paramsYearAcquired.topToBottom = R.id.tvFieldCapacityResultMain;
                break;
            case "MECHANICAL PLANTER":
                hideAll();
                showPlanter();
                paramstvPlanter.topToBottom = R.id.cbHauling;
                paramstvPlanter.topMargin = bigMargin;
                tvTypeofPlant.setLayoutParams(paramstvPlanter);
                paramsYearAcquired.topToBottom = R.id.tvFieldCapacityResultPlant;
                break;
            case "GRANULAR FERTILIZER APPLICATOR":
            case "FERTILIZER APPLICATOR WITH CUTAWAY":
                hideAll();
                showFert();
                paramsTSAFert.topToBottom = R.id.cbHauling;
                paramsTSAFert.topMargin = bigMargin;
                edtTotalServiceAreaFert.setLayoutParams(paramsTSAFert);
                paramsYearAcquired.topToBottom = R.id.tvDeliveryRateResultFert;
                break;
            case "MECHANICAL HARVESTER":
                hideAll();
                showHarvest();
                paramsTSAHarvest.topToBottom = R.id.cbHauling;
                paramsTSAHarvest.topMargin = bigMargin;
                edtTotalServiceAreaHarvest.setLayoutParams(paramsTSAHarvest);
                paramsYearAcquired.topToBottom = R.id.tvTonCannesPHaHarvest;
                break;
            case "CANE GRAB LOADERS":
                hideAll();
                showGrab();
                paramsTSAGrab.topToBottom = R.id.cbHauling;
                paramsTSAGrab.topMargin = bigMargin;
                edtTotalServiceAreaGrab.setLayoutParams(paramsTSAGrab);
                paramsYearAcquired.topToBottom = R.id.tvLoadPHaGrab;
                break;
            case "DITCHER":
                hideAll();
                showDitch();
                paramsTSADitch.topToBottom = R.id.cbHauling;
                paramsTSADitch.topMargin = bigMargin;
                edtTotalServiceAreaDitch.setLayoutParams(paramsTSADitch);
                paramsYearAcquired.topToBottom = R.id.tvDepthCutDitch;
                break;
            default:
                hideAll();
                paramsYearAcquired.topToBottom = R.id.cbHauling;
                break;
        }
        paramsYearAcquired.topMargin = bigMargin;
        tvYearAcquired.setLayoutParams(paramsYearAcquired);
    }

    private void showDitch() {
        edtTotalServiceAreaDitch.setVisibility(View.VISIBLE);
        edtHoursPDayDitch.setVisibility(View.VISIBLE);
        edtDaysPSeasonDitch.setVisibility(View.VISIBLE);
        edtDepthOfCutDitch.setVisibility(View.VISIBLE);

        tvHaDitch.setVisibility(View.VISIBLE);
        tvHoursPDayDitch.setVisibility(View.VISIBLE);
        tvDaysPSeasonDitch.setVisibility(View.VISIBLE);
        tvDepthCutDitch.setVisibility(View.VISIBLE);
    }

    private void showGrab() {
        edtTotalServiceAreaGrab.setVisibility(View.VISIBLE);
        edtHoursPDayGrab.setVisibility(View.VISIBLE);
        edtDaysPSeasonGrab.setVisibility(View.VISIBLE);
        edtLoadCapacityGrab.setVisibility(View.VISIBLE);
        edtNumberofLoadsGrab.setVisibility(View.VISIBLE);

        tvHaGrab.setVisibility(View.VISIBLE);
        tvHoursPDayGrab.setVisibility(View.VISIBLE);
        tvDaysPSeasonGrab.setVisibility(View.VISIBLE);
        tvLoadPHaGrab.setVisibility(View.VISIBLE);
    }

    private void showHarvest() {
        edtTotalServiceAreaHarvest.setVisibility(View.VISIBLE);
        edtHoursPDayHarvest.setVisibility(View.VISIBLE);
        edtDaysPSeasonHarvest.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompHarvest.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpHarvest.setVisibility(View.VISIBLE);
        edtAveYieldHarvest.setVisibility(View.VISIBLE);

        tvHaHarvest.setVisibility(View.VISIBLE);
        tvHoursPDayHarvest.setVisibility(View.VISIBLE);
        tvDaysPSeasonHarvest.setVisibility(View.VISIBLE);
        tvHaEAHarvest.setVisibility(View.VISIBLE);
        tvHoursPDayOpHarvest.setVisibility(View.VISIBLE);
        tvFieldCapacityHarvest.setVisibility(View.VISIBLE);
        tvFieldCapacityResultHarvest.setVisibility(View.VISIBLE);
        tvTonCannesPHaHarvest.setVisibility(View.VISIBLE);
    }

    private void showFert() {
        edtTotalServiceAreaFert.setVisibility(View.VISIBLE);
        edtHoursPDayFert.setVisibility(View.VISIBLE);
        edtDaysPSeasonFert.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompFert.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpFert.setVisibility(View.VISIBLE);
        edtWeightOfFert.setVisibility(View.VISIBLE);

        tvHaFert.setVisibility(View.VISIBLE);
        tvHoursPDayFert.setVisibility(View.VISIBLE);
        tvDaysPSeasonfFert.setVisibility(View.VISIBLE);
        tvHaEAFert.setVisibility(View.VISIBLE);
        tvHoursPDayOpFert.setVisibility(View.VISIBLE);
        tvFieldCapacityFert.setVisibility(View.VISIBLE);
        tvFieldCapacityResultFert.setVisibility(View.VISIBLE);
        tvWeightOfFert.setVisibility(View.VISIBLE);
        tvDeliveryRateFert.setVisibility(View.VISIBLE);
        tvDeliveryRateResultFert.setVisibility(View.VISIBLE);
    }

    private void showPlanter() {
        edtNumberofRowsPlant.setVisibility(View.VISIBLE);
        edtDistanceofPlantMat.setVisibility(View.VISIBLE);
        edtTotalServiceAreaPlant.setVisibility(View.VISIBLE);
        edtHoursPDayPlant.setVisibility(View.VISIBLE);
        edtDaysPSeasonPlant.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompPlant.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpPlant.setVisibility(View.VISIBLE);

        tvTypeofPlant.setVisibility(View.VISIBLE);
        tvDistanceofPlant.setVisibility(View.VISIBLE);
        tvHaPlant.setVisibility(View.VISIBLE);
        tvHoursPDayPlant.setVisibility(View.VISIBLE);
        tvDaysPSeasonPlant.setVisibility(View.VISIBLE);
        tvHaEAPlant.setVisibility(View.VISIBLE);
        tvHoursPDayOpPlant.setVisibility(View.VISIBLE);
        tvFieldCapacityPlant.setVisibility(View.VISIBLE);
        tvFieldCapacityResultPlant.setVisibility(View.VISIBLE);
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
        singlespinProvinces = findViewById(R.id.singlespinProvincesImp);
        singlespinMunicipalities = findViewById(R.id.singlespinMunicipalitiesImp);
        singlespinBarangays = findViewById(R.id.singlespinBarangaysImp);
        tvLat = findViewById(R.id.tvLatitudeImp);
        tvLong = findViewById(R.id.tvLongitudeImp);
        spinLocation = findViewById(R.id.spinLocationImp);
        spinCondition = findViewById(R.id.spinConditionImp);
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

        edtTotalServiceAreaMain = findViewById(R.id.edtTotalServiceAreaMain);
        edtHoursPDayMain = findViewById(R.id.edtHoursPDayMain);
        edtDaysPSeasonMain = findViewById(R.id.edtDaysPSeasonMain);
        edtEffectiveAreaAccompMain = findViewById(R.id.edtEffectiveAreaAccompMain);
        edtTimeUsedDuringOpMain = findViewById(R.id.edtTimeUsedDuringOpMain);
        tvHaMain = findViewById(R.id.tvHaMain);
        tvHoursPDayMain = findViewById(R.id.tvHoursPDayMain);
        tvDaysPSeasonMain = findViewById(R.id.tvDaysPSeasonMain);
        tvHaEAMain = findViewById(R.id.tvHaEAMain);
        tvHoursPDayOpMain = findViewById(R.id.tvHoursPDayOpMain);
        tvFieldCapacityMain = findViewById(R.id.tvFieldCapacityMain);
        tvFieldCapacityResultMain = findViewById(R.id.tvFieldCapacityResultMain);

        spinTypeOfPlanter = findViewById(R.id.spinTypeOfPlanterPlant);
        edtNumberofRowsPlant = findViewById(R.id.edtNumberofRowsPlant);
        edtDistanceofPlantMat = findViewById(R.id.edtDistanceOfPlantMatPlant);
        edtTotalServiceAreaPlant = findViewById(R.id.edtTotalServiceAreaPlant);
        edtHoursPDayPlant = findViewById(R.id.edtHoursPDayPlant);
        edtDaysPSeasonPlant = findViewById(R.id.edtDaysPSeasonPlant);
        edtEffectiveAreaAccompPlant = findViewById(R.id.edtEffectiveAreaAccompPlant);
        edtTimeUsedDuringOpPlant = findViewById(R.id.edtTimeUsedDuringOpPlant);
        tvTypeofPlant = findViewById(R.id.tvTypeofPlant);
        tvDistanceofPlant = findViewById(R.id.tvDistancePlant);
        tvHaPlant = findViewById(R.id.tvHaPlant);
        tvHoursPDayPlant = findViewById(R.id.tvHoursPDayPlant);
        tvDaysPSeasonPlant = findViewById(R.id.tvDaysPSeasonPlant);
        tvHaEAPlant = findViewById(R.id.tvHaEAPlant);
        tvHoursPDayOpPlant = findViewById(R.id.tvHoursPDayOpPlant);
        tvFieldCapacityPlant = findViewById(R.id.tvFieldCapacityPlant);
        tvFieldCapacityResultPlant = findViewById(R.id.tvFieldCapacityResultPlant);


        edtTotalServiceAreaFert = findViewById(R.id.edtTotalServiceAreaFert);
        edtHoursPDayFert = findViewById(R.id.edtHoursPDayFert);
        edtDaysPSeasonFert = findViewById(R.id.edtDaysPSeasonFert);
        edtEffectiveAreaAccompFert = findViewById(R.id.edtEffectiveAreaAccompFert);
        edtTimeUsedDuringOpFert = findViewById(R.id.edtTimeUsedDuringOpFert);
        edtWeightOfFert = findViewById(R.id.edtWeightOfFertFert);
        tvHaFert = findViewById(R.id.tvHaFert);
        tvHoursPDayFert = findViewById(R.id.tvHoursPDayFert);
        tvDaysPSeasonfFert = findViewById(R.id.tvDaysPSeasonFert);
        tvHaEAFert = findViewById(R.id.tvHaEAFert);
        tvHoursPDayOpFert = findViewById(R.id.tvHoursPDayOpFert);
        tvFieldCapacityFert = findViewById(R.id.tvFieldCapacityFert);
        tvFieldCapacityResultFert = findViewById(R.id.tvFieldCapacityResultFert);
        tvWeightOfFert = findViewById(R.id.tvWeightFert);
        tvDeliveryRateFert = findViewById(R.id.tvDeliveryRateFert);
        tvDeliveryRateResultFert = findViewById(R.id.tvDeliveryRateResultFert);

        edtTotalServiceAreaHarvest = findViewById(R.id.edtTotalServiceAreaHarvest);
        edtHoursPDayHarvest = findViewById(R.id.edtHoursPDayHarvest);
        edtDaysPSeasonHarvest = findViewById(R.id.edtDaysPSeasonHarvest);
        edtEffectiveAreaAccompHarvest = findViewById(R.id.edtEffectiveAreaAccompHarvest);
        edtTimeUsedDuringOpHarvest = findViewById(R.id.edtTimeUsedDuringOpHarvest);
        edtAveYieldHarvest = findViewById(R.id.edtAveYieldHarvest);
        tvHaHarvest = findViewById(R.id.tvHaHarvest);
        tvHoursPDayHarvest = findViewById(R.id.tvHoursPDayHarvest);
        tvDaysPSeasonHarvest = findViewById(R.id.tvDaysPSeasonHarvest);
        tvHaEAHarvest = findViewById(R.id.tvHaEAHarvest);
        tvHoursPDayOpHarvest = findViewById(R.id.tvHoursPDayOpHarvest);
        tvFieldCapacityHarvest = findViewById(R.id.tvFieldCapacityHarvest);
        tvFieldCapacityResultHarvest = findViewById(R.id.tvFieldCapacityResultHarvest);
        tvTonCannesPHaHarvest = findViewById(R.id.tvTonCannesPHaHarvest);

        edtTotalServiceAreaGrab = findViewById(R.id.edtTotalServiceAreaGrab);
        edtHoursPDayGrab = findViewById(R.id.edtHoursPDayGrab);
        edtDaysPSeasonGrab = findViewById(R.id.edtDaysPSeasonGrab);
        edtLoadCapacityGrab = findViewById(R.id.edtLoadCapacityGrab);
        edtNumberofLoadsGrab = findViewById(R.id.edtNumberofLoadsGrab);
        tvHaGrab = findViewById(R.id.tvHaGrab);
        tvHoursPDayGrab = findViewById(R.id.tvHoursPDayGrab);
        tvDaysPSeasonGrab = findViewById(R.id.tvDaysPSeasonGrab);
        tvLoadPHaGrab = findViewById(R.id.tvLoadPHaGrab);

        edtTotalServiceAreaDitch = findViewById(R.id.edtTotalServiceAreaDitch);
        edtHoursPDayDitch = findViewById(R.id.edtHoursPDayDitch);
        edtDaysPSeasonDitch = findViewById(R.id.edtDaysPSeasonDitch);
        edtDepthOfCutDitch = findViewById(R.id.edtDepthOfCutDitch);
        tvHaDitch = findViewById(R.id.tvHaDitch);
        tvHoursPDayDitch = findViewById(R.id.tvHoursPDayDitch);
        tvDaysPSeasonDitch = findViewById(R.id.tvDaysPSeasonDitch);
        tvDepthCutDitch = findViewById(R.id.tvDepthCutDitch);
    }

    private void showMainImplements() {
        edtTotalServiceAreaMain.setVisibility(View.VISIBLE);
        edtHoursPDayMain.setVisibility(View.VISIBLE);
        edtDaysPSeasonMain.setVisibility(View.VISIBLE);
        edtEffectiveAreaAccompMain.setVisibility(View.VISIBLE);
        edtTimeUsedDuringOpMain.setVisibility(View.VISIBLE);

        tvHaMain.setVisibility(View.VISIBLE);
        tvHoursPDayMain.setVisibility(View.VISIBLE);
        tvDaysPSeasonMain.setVisibility(View.VISIBLE);
        tvHaEAMain.setVisibility(View.VISIBLE);
        tvHoursPDayOpMain.setVisibility(View.VISIBLE);
        tvFieldCapacityMain.setVisibility(View.VISIBLE);
        tvFieldCapacityResultMain.setVisibility(View.VISIBLE);
    }

    private void hideAll() {

        edtTotalServiceAreaMain.setVisibility(View.GONE);
        edtHoursPDayMain.setVisibility(View.GONE);
        edtDaysPSeasonMain.setVisibility(View.GONE);
        edtEffectiveAreaAccompMain.setVisibility(View.GONE);
        edtTimeUsedDuringOpMain.setVisibility(View.GONE);

        edtNumberofRowsPlant.setVisibility(View.GONE);
        edtDistanceofPlantMat.setVisibility(View.GONE);
        edtTotalServiceAreaPlant.setVisibility(View.GONE);
        edtHoursPDayPlant.setVisibility(View.GONE);
        edtDaysPSeasonPlant.setVisibility(View.GONE);
        edtEffectiveAreaAccompPlant.setVisibility(View.GONE);
        edtTimeUsedDuringOpPlant.setVisibility(View.GONE);

        edtTotalServiceAreaFert.setVisibility(View.GONE);
        edtHoursPDayFert.setVisibility(View.GONE);
        edtDaysPSeasonFert.setVisibility(View.GONE);
        edtEffectiveAreaAccompFert.setVisibility(View.GONE);
        edtTimeUsedDuringOpFert.setVisibility(View.GONE);
        edtWeightOfFert.setVisibility(View.GONE);

        edtTotalServiceAreaHarvest.setVisibility(View.GONE);
        edtHoursPDayHarvest.setVisibility(View.GONE);
        edtDaysPSeasonHarvest.setVisibility(View.GONE);
        edtEffectiveAreaAccompHarvest.setVisibility(View.GONE);
        edtTimeUsedDuringOpHarvest.setVisibility(View.GONE);
        edtAveYieldHarvest.setVisibility(View.GONE);

        edtTotalServiceAreaGrab.setVisibility(View.GONE);
        edtHoursPDayGrab.setVisibility(View.GONE);
        edtDaysPSeasonGrab.setVisibility(View.GONE);
        edtLoadCapacityGrab.setVisibility(View.GONE);
        edtNumberofLoadsGrab.setVisibility(View.GONE);

        edtTotalServiceAreaDitch.setVisibility(View.GONE);
        edtHoursPDayDitch.setVisibility(View.GONE);
        edtDaysPSeasonDitch.setVisibility(View.GONE);
        edtDepthOfCutDitch.setVisibility(View.GONE);


        tvHaMain.setVisibility(View.GONE);
        tvHoursPDayMain.setVisibility(View.GONE);
        tvDaysPSeasonMain.setVisibility(View.GONE);
        tvHaEAMain.setVisibility(View.GONE);
        tvHoursPDayOpMain.setVisibility(View.GONE);
        tvFieldCapacityMain.setVisibility(View.GONE);
        tvFieldCapacityResultMain.setVisibility(View.GONE);

        tvTypeofPlant.setVisibility(View.GONE);
        tvDistanceofPlant.setVisibility(View.GONE);
        tvHaPlant.setVisibility(View.GONE);
        tvHoursPDayPlant.setVisibility(View.GONE);
        tvDaysPSeasonPlant.setVisibility(View.GONE);
        tvHaEAPlant.setVisibility(View.GONE);
        tvHoursPDayOpPlant.setVisibility(View.GONE);
        tvFieldCapacityPlant.setVisibility(View.GONE);
        tvFieldCapacityResultPlant.setVisibility(View.GONE);

        tvHaFert.setVisibility(View.GONE);
        tvHoursPDayFert.setVisibility(View.GONE);
        tvDaysPSeasonfFert.setVisibility(View.GONE);
        tvHaEAFert.setVisibility(View.GONE);
        tvHoursPDayOpFert.setVisibility(View.GONE);
        tvFieldCapacityFert.setVisibility(View.GONE);
        tvFieldCapacityResultFert.setVisibility(View.GONE);
        tvWeightOfFert.setVisibility(View.GONE);
        tvDeliveryRateFert.setVisibility(View.GONE);
        tvDeliveryRateResultFert.setVisibility(View.GONE);

        tvHaHarvest.setVisibility(View.GONE);
        tvHoursPDayHarvest.setVisibility(View.GONE);
        tvDaysPSeasonHarvest.setVisibility(View.GONE);
        tvHaEAHarvest.setVisibility(View.GONE);
        tvHoursPDayOpHarvest.setVisibility(View.GONE);
        tvFieldCapacityHarvest.setVisibility(View.GONE);
        tvFieldCapacityResultHarvest.setVisibility(View.GONE);
        tvTonCannesPHaHarvest.setVisibility(View.GONE);

        tvHaGrab.setVisibility(View.GONE);
        tvHoursPDayGrab.setVisibility(View.GONE);
        tvDaysPSeasonGrab.setVisibility(View.GONE);
        tvLoadPHaGrab.setVisibility(View.GONE);

        tvHaDitch.setVisibility(View.GONE);
        tvHoursPDayDitch.setVisibility(View.GONE);
        tvDaysPSeasonDitch.setVisibility(View.GONE);
        tvDepthCutDitch.setVisibility(View.GONE);
    }

    private void initAllLayoutParameters() {
        paramsYearAcquired = (ConstraintLayout.LayoutParams) tvYearAcquired.getLayoutParams();
        paramsTSAMain = (ConstraintLayout.LayoutParams) edtTotalServiceAreaMain.getLayoutParams();
        paramsTSAFert = (ConstraintLayout.LayoutParams) edtTotalServiceAreaFert.getLayoutParams();
        paramsTSAHarvest = (ConstraintLayout.LayoutParams) edtTotalServiceAreaHarvest.getLayoutParams();
        paramsTSAGrab = (ConstraintLayout.LayoutParams) edtTotalServiceAreaGrab.getLayoutParams();
        paramsTSADitch = (ConstraintLayout.LayoutParams) edtTotalServiceAreaDitch.getLayoutParams();
        paramstvPlanter = (ConstraintLayout.LayoutParams) tvTypeofPlant.getLayoutParams();
    }


    private Bitmap scale(Bitmap bitmap, int maxWidth, int maxHeight) {
        // Determine the constrained dimension, which determines both dimensions.
        int width;
        int height;
        float widthRatio = (float) bitmap.getWidth() / maxWidth;
        float heightRatio = (float) bitmap.getHeight() / maxHeight;
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = maxWidth;
            height = (int) (((float) width / bitmap.getWidth()) * bitmap.getHeight());
        }
        // Height constrained.
        else {
            height = maxHeight;
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
        } else {
        }

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
                bitmap = scale(bitmap, 1080, 1920);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgInByte = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(imgInByte, Base64.DEFAULT);
                selectedImage.setImageBitmap(bitmap);

            }


        }
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            tvLat.setText(data.getStringExtra("strLat"));
            tvLong.setText(data.getStringExtra("StrLong"));
            accuracy = data.getStringExtra("StrAcc");
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
                bitmap = scale(bitmap, 720, 1280);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imgInByte = byteArrayOutputStream.toByteArray();
                String encodedImg = Base64.encodeToString(imgInByte, Base64.DEFAULT);
                selectedImage.setImageBitmap(bitmap);
            }

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
                bitmap = scale(bitmap, 1080, 1920);
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
}
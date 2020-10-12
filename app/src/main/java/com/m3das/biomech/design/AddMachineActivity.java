package com.m3das.biomech.design;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddMachineActivity extends AppCompatActivity{
private ImageButton camera, gallery, getLocation, btnScanQR;
private String currentPhotoPath;
private ImageView selectedImage;
private EditText edtQRCode, edtCapacity, edtAveYield, edtNumLoads, edtRate, edtAveOpHours, edtAveOpDays;
private TextView tvLat, tvLong, tvTypeOfMill, tvBrand, tvOwnership, tvTypeOfTubewells;
private Spinner spinMachineType, spinRentProv, spinRentMun, spinTypeOfMill, spinTypeofTubeWells;
private DatePicker dateOfSurvey;
private String resLat, resLong;
private MultiSpinnerSearch multiSpinnerSearch, multspinRentBrgy;
private ConstraintLayout.LayoutParams paramstvBrand, paramstvOwnership, paramsedtCapacity, paramsedtNumLoads, paramsedtAveYield, paramsedtRate, paramstvTypeTubewells;
public static final int CAMERA_PERM_CODE = 101;
public static final int CAMERA_REQUEST_CODE = 102;
public static final int GALLERY_REQUEST_CODE = 105;
public static final int LOCATION_REQUEST_CODE = 127;
public static final String EXTRA_MACHINE_TYPE = "EXTRA_MACHINE_TYPE";
public static final String EXTRA_MACHINE_QRCODE = "EXTRA_MACHINE_QRCODE";
public static final String EXTRA_LAT = "EXTRA_LAT";
public static final String EXTRA_LONG = "EXTRA_LONG";
public static final String  EXTRA_ID = "EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_machine_activity);

        camera = findViewById(R.id.btnCamera);
        gallery = findViewById(R.id.btnGallery);
        selectedImage = findViewById(R.id.imgMachine);
        getLocation = findViewById(R.id.btnGetLocation);
        edtQRCode = findViewById(R.id.edtQRCode);
        btnScanQR = findViewById(R.id.btnScanQRCodeMach);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        spinMachineType = findViewById(R.id.spinMachineType);
        Button btnSave = findViewById(R.id.btnSaveNewMachine);
        multiSpinnerSearch = findViewById(R.id.multspinProblemsUnused);
        multspinRentBrgy = findViewById(R.id.multispinRentBrgy);
        spinRentMun = findViewById(R.id.spinRentMunicipality);
        spinRentProv = findViewById(R.id.spinRentProvince);
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

        edtAveOpHours.setFilters(new InputFilter[]{ new MinMaxFilter( "0" , "24" )});
        edtAveOpDays.setFilters(new InputFilter[]{ new MinMaxFilter("1", "31")});

        paramstvBrand = (ConstraintLayout.LayoutParams) tvBrand.getLayoutParams();


        tvTypeOfMill.setVisibility(View.INVISIBLE);
        spinTypeOfMill.setVisibility(View.INVISIBLE);
        tvTypeOfTubewells.setVisibility(View.INVISIBLE);
        spinTypeofTubeWells.setVisibility(View.INVISIBLE);

        paramstvBrand.topToBottom = R.id.edtQRCode;
        paramstvBrand.topMargin = (int) pxFromDp(this, 32);;
        tvBrand.setLayoutParams(paramstvBrand);




        spinRentProv.setPrompt("Select Province");
        spinRentMun.setPrompt("Select Municipality");
        multspinRentBrgy.setHintText("Select Barangays");
        multiSpinnerSearch.setHintText("Select Problems...");

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMachineActivity.this.askCameraPermission();
                Toast.makeText(AddMachineActivity.this, "Camera Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        gallery.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);

            Toast.makeText(AddMachineActivity.this, "Gallery Clicked", Toast.LENGTH_SHORT).show();
        });

        btnScanQR.setOnClickListener(view -> {
            AddMachineActivity.this.askCameraPermission();
            Intent intent = new Intent(AddMachineActivity.this, ScanBarcodeActivity.class);
            startActivityForResult(intent,0);
            Toast.makeText(AddMachineActivity.this, "Scanning QR", Toast.LENGTH_SHORT).show();
        });

        getLocation.setOnClickListener(view -> {
            Toast.makeText(AddMachineActivity.this, "Map Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), LocationMapsActivity.class);
            startActivityForResult(intent, LOCATION_REQUEST_CODE);
        });

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            int position = -1;
            setTitle("Editing Machine");
            String [] arr = getResources().getStringArray(R.array.machine_types);

            for (int i=0;i<arr.length;i++) {
                if (arr[i].equals(intent.getStringExtra(EXTRA_MACHINE_TYPE))) {
                    position = i;
                    break;
                }
            }

           Log.d("Position", "Position is: " +intent.getStringExtra(EXTRA_MACHINE_TYPE) + " " + position);
           spinMachineType.setSelection(position);
           edtQRCode.setText(intent.getStringExtra(EXTRA_MACHINE_QRCODE));
           tvLat.setText( intent.getStringExtra(EXTRA_LAT));
           tvLong.setText(intent.getStringExtra(EXTRA_LONG));

        }
        else {
            setTitle("Adding Machine");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
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

        multiSpinnerSearch.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.problems_unused))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
            }
        });

        multspinRentBrgy.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.aklan_all_brgy))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
            }
        });
    }
    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public List<KeyPairBoolData> pairingOfList(List<String> stringList){
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
    private void machineSelect(int position){
        String machineType = spinMachineType.getItemAtPosition(position).toString();


        switch (machineType){
            case "2 WHEEL TRACTOR":
            case "4 WHEEL TRACTOR":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
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
                edtCapacity.setVisibility(View.VISIBLE);
                edtCapacity.setHint("Tank Capacity (in L)");
                getParams(2);
                break;
            case "CANE GRAB LOADER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                edtNumLoads.setVisibility(View.VISIBLE);
                edtCapacity.setHint("Loading Capacity (in tons/load)");
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
                edtCapacity.setHint("Capacity (in ha/h)");
                edtAveYield.setHint("Average Yield (in ton cannes/ha)");
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
                edtCapacity.setHint("Capacity (in ha/h)");
                edtAveYield.setHint("Average Yield (in ton/ha)");
                getParams(4);
                break;
            case "DRYER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                edtRate.setVisibility(View.VISIBLE);
                edtCapacity.setHint("Loading Capacity (in kg/h)");
                edtRate.setHint("Drying Rate (in tons/h)");
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
                edtCapacity.setVisibility(View.VISIBLE);
                edtCapacity.setHint("Capacity (in tons/load)");
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
                edtCapacity.setHint("Capacity (in ha/h)");
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
                edtCapacity.setHint("Capacity (in kg)");
                edtRate.setHint("Milling Rate (in tons/h)");
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
                edtCapacity.setHint("Capacity (in kg)");
                edtRate.setHint("Shelling Rate (in tons/h)");
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
                edtCapacity.setHint("Capacity (in kg)");
                edtRate.setHint("Threshing Rate (in tons/h)");
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
                edtCapacity.setHint("Capacity (in L/s)");
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
                getParams(2);
                break;
        }
    }

    private void getParams(int input){
        int type = 0;
        int topMargin = (int) pxFromDp(this, 32);

        paramstvOwnership = (ConstraintLayout.LayoutParams) tvOwnership.getLayoutParams();
        paramsedtCapacity = (ConstraintLayout.LayoutParams) edtCapacity.getLayoutParams();
        paramsedtNumLoads = (ConstraintLayout.LayoutParams) edtNumLoads.getLayoutParams();
        paramsedtAveYield = (ConstraintLayout.LayoutParams) edtAveYield.getLayoutParams();
        paramsedtRate = (ConstraintLayout.LayoutParams) edtRate.getLayoutParams();
        paramstvTypeTubewells = (ConstraintLayout.LayoutParams) tvTypeOfTubewells.getLayoutParams();

        type = input;

        switch (type){
            case 1:
            default:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.tvSubInputDays;
                break;
            case 2:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtCapacity;
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                break;
            case 3:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtNumberOfLoads;
                paramsedtCapacity.topToBottom =R.id.tvSubInputDays;
                paramsedtNumLoads.topToBottom = R.id.edtCapacity;
                break;
            case 4:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtAveYield;
                paramsedtCapacity.topToBottom =R.id.tvSubInputDays;
                paramsedtAveYield.topToBottom = R.id.edtCapacity;
                break;
            case 5:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtRate;
                paramsedtCapacity.topToBottom =R.id.tvSubInputDays;
                paramsedtRate.topToBottom = R.id.edtCapacity;
                break;
            case 6 :
                paramstvTypeTubewells.topToBottom = R.id.edtQRCode;
                paramstvBrand.topToBottom = R.id.spinTypeOfTubewells;
                paramstvOwnership.topToBottom = R.id.edtCapacity;
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                break;
            case 7 :
                paramstvBrand.topToBottom = R.id.spinTypeMill;
                paramstvOwnership.topToBottom = R.id.edtRate;
                paramsedtCapacity.topToBottom =R.id.tvSubInputDays;
                paramsedtRate.topToBottom = R.id.edtCapacity;
                break;
        }

        paramstvBrand.topMargin = topMargin;
        paramstvOwnership.topMargin = topMargin;
        paramsedtCapacity.topMargin = topMargin;
        paramsedtNumLoads.topMargin = topMargin;
        paramsedtAveYield.topMargin = topMargin;
        paramsedtRate.topMargin = topMargin;

        tvBrand.setLayoutParams(paramstvBrand);
        tvOwnership.setLayoutParams(paramstvOwnership);
        edtCapacity.setLayoutParams(paramsedtCapacity);
        edtNumLoads.setLayoutParams(paramsedtNumLoads);
        edtAveYield.setLayoutParams(paramsedtAveYield);
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
        {
            return false;
        }
        return true;
    }
    private void saveNote() {
        int day = 0,month = 0 , year = 0;
        String machineType = spinMachineType.getSelectedItem().toString();
        String machineQRCode = edtQRCode.getText().toString();
//        day =  dateOfSurvey.getDayOfMonth();
//        month = dateOfSurvey.getMonth();
//        year = dateOfSurvey.getYear();
        String date = month + "/" + day + "/" + year;
        String latitude = tvLat.getText().toString();
        String longitude = tvLong.getText().toString();

        if (machineType.trim().isEmpty() ||
                machineQRCode.trim().isEmpty() ||
                isNullOrEmpty(latitude) ||
                isNullOrEmpty(longitude)) {
            Toast.makeText(this, "Incomplete Data", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Type: " + machineType +" QR Code: " + machineQRCode + " Latitude: " + latitude + " Longitude: "+longitude, Toast.LENGTH_LONG).show();
            Intent dataAddMachine = new Intent();
            dataAddMachine.putExtra(EXTRA_MACHINE_TYPE, machineType);
            dataAddMachine.putExtra(EXTRA_MACHINE_QRCODE, machineQRCode);
            dataAddMachine.putExtra(EXTRA_LAT, latitude);
            dataAddMachine.putExtra(EXTRA_LONG, longitude);

            int id = getIntent().getIntExtra(EXTRA_ID,-1);
            if(id != -1){
                dataAddMachine.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, dataAddMachine);
            finish();
        }

    }


        private Bitmap scale(Bitmap bitmap, int maxWidth, int maxHeight) {
        // Determine the constrained dimension, which determines both dimensions.
        int width;
        int height;
        float widthRatio = (float)bitmap.getWidth() / maxWidth;
        float heightRatio = (float)bitmap.getHeight() / maxHeight;
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = maxWidth;
            height = (int)(((float)width / bitmap.getWidth()) * bitmap.getHeight());
        }
        // Height constrained.
        else {
            height = maxHeight;
            width = (int)(((float)height / bitmap.getHeight()) * bitmap.getWidth());
        }
        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float ratioX = (float)width / bitmap.getWidth();
        float ratioY = (float)height / bitmap.getHeight();
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
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }
        else {
            dispatchTakePictureIntent();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            }

        }

        if(requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data !=null){
            resLat = data.getStringExtra("strLat");
            resLong = data.getStringExtra("StrLong");
            tvLat.setText(resLat);
            tvLong.setText(resLong);
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

        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null){
                    Log.e("Check", "Receiving data");
                    Barcode barcode = data.getParcelableExtra("barcode");
                    edtQRCode.setText(barcode.displayValue);
                }
                else {
                    edtQRCode.setHint("No Barcode found");
                }

            }
        }
        else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

/*    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }*/


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.biomech.design.",//TODO ADD AUTHORITY
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

}



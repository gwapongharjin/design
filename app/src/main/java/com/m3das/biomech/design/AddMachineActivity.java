package com.m3das.biomech.design;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.androidbuts.multispinnerfilter.SingleSpinnerListener;
import com.androidbuts.multispinnerfilter.SingleSpinnerSearch;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMachineActivity extends AppCompatActivity {
    private ImageButton camera, gallery, getLocation, btnScanQR;
    private Button btnSave;
    private String currentPhotoPath;
    private int bigMargin;
    private int smallMargin;
    private ImageView selectedImage;
    private EditText edtQRCode, edtCapacity, edtAveYield, edtNumLoads, edtRate, edtAveOpHours, edtAveOpDays, edtServiceArea, edtNameOfOwnerOrg, edtCustomRate, edtCustomRateUnit, edtOtherProblems, edtOtherAgency, edtOtherBrand, edtOtherModel, edtRatedPower;
    private TextView tvLat, tvLong, tvTypeOfMill, tvBrand, tvOwnership, tvTypeOfTubewells, tvMachineAvailability, tvConditionPresent, tvLocation, tvModel;
    private Spinner spinMachineType, spinRentProv, spinRentMun, spinTypeOfMill, spinRental, spinCustomUnit, spinAvailability, spinConditionPresent,
            spinTypeofTubeWells, spinOwnership, spinPurchGrantDono, spinAgency, spinProvince, spinMunicipality, spinBrand, spinModel, spinYearAcquired;
    private DatePicker dateOfSurvey;
    private String resLat, resLong, encodedImage, mCurrentPhotoPath;
    ;
    private MultiSpinnerSearch multspinProblemsUnused, multspinRentBrgy;
    private SingleSpinnerSearch singlespinBarangay, singlespinYearAcquired;
    private ConstraintLayout.LayoutParams paramstvBrand, paramstvOwnership, paramsedtCapacity, paramsedtNumLoads, paramstvConditionPresent, paramstvLocation, paramstvModel, paramsedtRatedPower,
            paramsedtAveYield, paramsedtRate, paramstvTypeTubewells, paramsedtNameOfOwnerOrg, paramstvMachineAvailability;
    private MachineListViewModel machineListViewModel;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int LOCATION_REQUEST_CODE = 127;
    public static final int WRITE_PERM_CODE = 279;
    public static final String EXTRA_MACHINE_TYPE = "EXTRA_MACHINE_TYPE";
    public static final String EXTRA_MACHINE_QRCODE = "EXTRA_MACHINE_QRCODE";
    public static final String EXTRA_LAT = "EXTRA_LAT";
    public static final String EXTRA_LONG = "EXTRA_LONG";
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_IMG = "EXTRA_IMG";
    public static final String EXTRA_MACHINE_DB = "EXTRA_MACHINR_DB";
    static Uri capturedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_machine_activity);

        askCameraPermission();

        if (checkPermission()) {
            // Code for above or equal 23 API Oriented Device
            // Your Permission granted already .Do next code
        } else {
            requestPermission(); // Code for permission
        }
        machineListViewModel = new ViewModelProvider(this).get(MachineListViewModel.class);

        smallMargin = (int) pxFromDp(this, 8);
        bigMargin = (int) pxFromDp(this, 32);

        initViews();
        hide();

        edtAveOpHours.setFilters(new InputFilter[]{new MinMaxFilter("0", "24")});
        edtAveOpDays.setFilters(new InputFilter[]{new MinMaxFilter("1", "31")});
        edtServiceArea.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        paramstvBrand = (ConstraintLayout.LayoutParams) tvBrand.getLayoutParams();
        paramstvMachineAvailability = (ConstraintLayout.LayoutParams) tvMachineAvailability.getLayoutParams();
        paramsedtNameOfOwnerOrg = (ConstraintLayout.LayoutParams) edtNameOfOwnerOrg.getLayoutParams();
        paramstvConditionPresent = (ConstraintLayout.LayoutParams) tvConditionPresent.getLayoutParams();
        paramstvLocation = (ConstraintLayout.LayoutParams) tvLocation.getLayoutParams();
        paramstvModel = (ConstraintLayout.LayoutParams) tvModel.getLayoutParams();
        paramsedtRatedPower = (ConstraintLayout.LayoutParams) edtRatedPower.getLayoutParams();

        paramstvBrand.topToBottom = R.id.edtQRCode;
        paramstvBrand.topMargin = bigMargin;
        tvBrand.setLayoutParams(paramstvBrand);

        paramstvMachineAvailability.topToBottom = R.id.spinRental;
        paramstvMachineAvailability.topMargin = bigMargin;
        tvMachineAvailability.setLayoutParams(paramstvMachineAvailability);

        paramsedtNameOfOwnerOrg.topToBottom = R.id.spinOwnership;
        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);

        ArrayList<String> years = new ArrayList<String>();
        years.add("");
        for (int i = 1960; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(Integer.toString(i));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spinYearAcquired.setAdapter(adapter);


        multspinRentBrgy.setHintText("Select Barangays");
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

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            int position = -1;
            String compareMachineType = intent.getStringExtra(EXTRA_MACHINE_TYPE);

            ArrayAdapter<CharSequence> adaptercompare = ArrayAdapter.createFromResource(this, R.array.machine_types_sugarcane, android.R.layout.simple_spinner_item);
            adaptercompare.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (compareMachineType != null) {
                position = adaptercompare.getPosition(compareMachineType);
            }

            Log.d("Position", "Position is: " + intent.getStringExtra(EXTRA_MACHINE_TYPE) + " " + position);
            spinMachineType.setSelection(position);
            edtQRCode.setText(intent.getStringExtra(EXTRA_MACHINE_QRCODE));
            tvLat.setText(intent.getStringExtra(EXTRA_LAT));
            tvLong.setText(intent.getStringExtra(EXTRA_LONG));

            Log.d("Extra IMG value", Variable.getStringImage());
            byte[] decodedString = Base64.decode(Variable.getStringImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            selectedImage.setImageBitmap(decodedByte);

        } else {
            setTitle("Adding Machine");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        singlespinBarangay.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank))), new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

            }

            @Override
            public void onClear() {

            }
        });

        spinBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandSelect(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelSelect(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                provMunSort(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        multspinRentBrgy.setItems(pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank))), new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
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

        spinMachineType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                machineSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        if (checkPermission()) {
            // Code for above or equal 23 API Oriented Device
            // Your Permission granted already .Do next code
        } else {
            requestPermission(); // Code for permission
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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

    private void brandSelect(int position) {
        String pos = spinBrand.getItemAtPosition(position).toString();
        String typeMachine = spinMachineType.getSelectedItem().toString();
        switch (pos) {
            case "OTHERS":
                edtOtherBrand.setVisibility(View.VISIBLE);
                paramstvModel.topToBottom = R.id.edtOtherBrand;
                paramstvModel.topToBottom = R.id.edtOtherBrand;
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield)));
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinModel.setAdapter(dataAdapter);
                break;
            default:
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
                break;
        }
        paramstvModel.topMargin = bigMargin;
        tvModel.setLayoutParams(paramstvModel);
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

    }

    private void sortingBrand2WheelTractor(String position) {
        List<String> stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.blank));

        switch (position) {
            case "ACT":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.act_models_2wheel_tractors));
                break;
            case "AIMS":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.aims_models_2wheel_tractors));
                break;
            case "APT":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.apt_models_2wheel_tractors));
                break;
            case "BOWA":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.bowa_models_2wheel_tractors));
                break;
            case "BUFFALO":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.buffalo_models_2wheel_tractors));
                break;
            case "D.U.A":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.dua_models_2wheel_tractors));
                break;
            case "FARM MASTER":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.farm_master_models_2wheel_tractors));
                break;
            case "KAPITAN":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kapitan_models_2wheel_tractors));
                break;
            case "KASAMA HARABAS":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kasama_harabas_models_2wheel_tractors));
                break;
            case "KATO":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kato_models_2wheel_tractors));
                break;
            case "KELLY":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kelly_models_2wheel_tractors));
                break;
            case "KULIGLIG":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.kuliglig_models_2wheel_tractors));
                break;
            case "LONG FOONG":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.long_foong_models_2wheel_tractors));
                break;
            case "MITSUBOMAR":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.mitsubomar_models_2wheel_tractors));
                break;
            case "NICHINO":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.nichino_models_2wheel_tractors));
                break;
            case "SUMO PLUS":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.sumo_plus_models_2wheel_tractors));
                break;
            case "SUPER":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.super_models_2wheel_tractors));
                break;
            case "TIBAY KULIGLIG":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.tibay_kuliglig_models_2wheel_tractors));
                break;
            case "TRIPLE J":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.triple_j_models_2wheel_tractors));
                break;
            case "WEST WIND":
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.west_wind_models_2wheel_tractors));
                break;
            default:
                stringListModel2WheelTractor = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
                break;

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListModel2WheelTractor);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinModel.setAdapter(dataAdapter);
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
    }

    private void modelSelect(int position) {
        String pos = spinModel.getItemAtPosition(position).toString();

        switch (pos) {
            case "OTHERS":
                edtOtherModel.setVisibility(View.VISIBLE);
                paramsedtRatedPower.topToBottom = R.id.edtOtherModel;
                break;
            default:
                edtOtherModel.setVisibility(View.INVISIBLE);
                paramsedtRatedPower.topToBottom = R.id.spinModel;
                break;
        }
        paramsedtRatedPower.topMargin = bigMargin;
        edtRatedPower.setLayoutParams(paramsedtRatedPower);
    }

    private void provMunSort(int position) {
        String pos = spinProvince.getItemAtPosition(position).toString();
        ArrayAdapter<String> adaptermun;
        List<KeyPairBoolData> selectedBrgy = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));

        switch (pos) {
            case "CAPIZ":
                adaptermun = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.capiz_mun));
                break;
            case "AKLAN":
                adaptermun = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.aklan_mun));
                selectedBrgy = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.aklan_all_brgy)));
                break;
            case "ILOILO":
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.iloilo_mun));
                break;
            case "ANTIQUE":
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.antique_mun));
                break;
            case "NEGROS OCCIDENTAL":
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.negros_occidental_mun));
                break;
            case "BATANGAS":
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.batangas_mun));
                break;
            case "NEGROS ORIENTAL":
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.negros_oriental_mun));
                break;
            case "BUKIDNON":
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bukidnon_mun));
                break;
            default:
                adaptermun = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.bukidnon_mun));
                selectedBrgy = selectedBrgy = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));
                break;
        }
        adaptermun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMunicipality.setAdapter(adaptermun);
        singlespinBarangay.setItems(selectedBrgy, new SingleSpinnerListener() {
            @Override
            public void onItemsSelected(KeyPairBoolData selectedItem) {

            }

            @Override
            public void onClear() {

            }
        });

    }

    private void problemsUnused(int position) {
        String pos = spinConditionPresent.getItemAtPosition(position).toString();
        List<KeyPairBoolData> selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.blank)));


        switch (pos) {
            case "FUNCTIONAL USED":
                multspinProblemsUnused.setVisibility(View.INVISIBLE);
                edtOtherProblems.setVisibility(View.INVISIBLE);
                paramstvLocation.topToBottom = R.id.spinConditionPresent;
                break;
            case "FUNCTIONAL UNUSED":
                multspinProblemsUnused.setVisibility(View.VISIBLE);
                selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.problems_unused)));
                paramstvLocation.topToBottom = R.id.multspinProblemsUnused;
                break;
            case "NON-FUNCTIONAL":
                multspinProblemsUnused.setVisibility(View.VISIBLE);
                selectedProb = pairingOfList(Arrays.asList(getResources().getStringArray(R.array.problems_nonfunctional)));
                paramstvLocation.topToBottom = R.id.multspinProblemsUnused;
                break;
        }

        multspinProblemsUnused.setItems(selectedProb, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                String pos = "";
                for (int i = 0; i < selectedItems.size(); i++) {
                    pos = pos + " " + selectedItems.get(i).getName();
                    Log.d("MULT SPIN", i + " : " + selectedItems.get(i).getName() + " : " + selectedItems.get(i).isSelected());
                }
                if (pos.contains("OTHERS")) {
                    edtOtherProblems.setVisibility(View.VISIBLE);
                    paramstvLocation.topToBottom = R.id.edtOtherProblems;
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
                spinRentProv.setVisibility(View.VISIBLE);
                spinRentMun.setVisibility(View.VISIBLE);
                multspinRentBrgy.setVisibility(View.VISIBLE);
                paramstvConditionPresent.topToBottom = R.id.multispinRentBrgy;
                break;
            case "WITHIN BARANGAY":
            default:
                spinRentProv.setVisibility(View.INVISIBLE);
                spinRentMun.setVisibility(View.INVISIBLE);
                multspinRentBrgy.setVisibility(View.INVISIBLE);
                paramstvConditionPresent.topToBottom = R.id.spinAvailability;
                break;
        }
        paramstvConditionPresent.topMargin = bigMargin;
        tvConditionPresent.setLayoutParams(paramstvConditionPresent);
    }

    private void customUnitSelect(int position) {
        String pos = spinCustomUnit.getItemAtPosition(position).toString();

        Log.d("UNIT SELECT", pos);

        switch (pos) {
            case "SPECIFY":
                edtCustomRateUnit.setVisibility(View.VISIBLE);
                paramstvMachineAvailability.topToBottom = R.id.edtCustomRateUnit;
                break;
            default:
                edtCustomRateUnit.setVisibility(View.INVISIBLE);
                paramstvMachineAvailability.topToBottom = R.id.edtCustomRate;
                break;
        }

        paramstvMachineAvailability.topMargin = bigMargin;
        tvMachineAvailability.setLayoutParams(paramstvMachineAvailability);

    }

    private void rentSelect(int position) {
        String pos = spinRental.getItemAtPosition(position).toString();

        switch (pos) {
            case "NO":
            default:
                edtCustomRateUnit.setVisibility(View.INVISIBLE);
                edtCustomRate.setVisibility(View.INVISIBLE);
                spinCustomUnit.setVisibility(View.INVISIBLE);
                paramstvMachineAvailability.topToBottom = R.id.spinRental;
                break;
            case "YES":
                edtCustomRate.setVisibility(View.VISIBLE);
                spinCustomUnit.setVisibility(View.VISIBLE);
                paramstvMachineAvailability.topToBottom = R.id.edtCustomRate;
                break;
        }
        paramstvMachineAvailability.topMargin = bigMargin;
        tvMachineAvailability.setLayoutParams(paramstvMachineAvailability);
    }

    private void machineSelect(int position) {
        String machineType = spinMachineType.getItemAtPosition(position).toString();
        List<String> stringListBrand = Arrays.asList(getResources().getStringArray(R.array.blank));


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
                getParams(1);
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.wheel2_tractor_brand));
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
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.wheel4_tractor_brand));
                getParams(1);
                break;
            case "BOOM SPRAYER":
                tvTypeOfMill.setVisibility(View.INVISIBLE);
                spinTypeOfMill.setVisibility(View.INVISIBLE);
                tvTypeOfTubewells.setVisibility(View.INVISIBLE);
                spinTypeofTubeWells.setVisibility(View.INVISIBLE);
                edtAveYield.setVisibility(View.INVISIBLE);
                edtNumLoads.setVisibility(View.INVISIBLE);
                edtRate.setVisibility(View.INVISIBLE);
                edtCapacity.setVisibility(View.VISIBLE);
                edtCapacity.setHint("Tank Capacity (in L)");
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
                getParams(2);
                break;
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
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
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
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.harvester_brands));
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
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
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
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.waterpump_brands));
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
                stringListBrand = Arrays.asList(getResources().getStringArray(R.array.specify_only_brand_boom_sprayer_cane_grab_infield));
                getParams(2);
                break;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringListBrand);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBrand.setAdapter(dataAdapter);
    }

    private void ownershipSelect(int position) {
        String pos = spinOwnership.getItemAtPosition(position).toString();

        Log.d("GOT TO OWNERSHIP", pos);
        switch (pos) {
            case "PRIVATELY OWNED":
            default:

                spinPurchGrantDono.setVisibility(View.INVISIBLE);
                spinAgency.setVisibility(View.INVISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinOwnership;
                Log.d("INSIDE PRIVATELY OWNED", pos + ":" + paramsedtNameOfOwnerOrg);
                break;
            case "COOPERATIVE/ASSOCIATION":
            case "CUSTOM PROVIDER":
            case "LGU":
                spinPurchGrantDono.setVisibility(View.VISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinPurchGrantDono;
                break;
        }
        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
    }

    private void purchGrantDonoSelect(int position) {
        String pos = spinPurchGrantDono.getItemAtPosition(position).toString();

        switch (pos) {
            case "PURCHASED":
            default:
                Log.d("INSIDE PURCHASE", pos);
                spinAgency.setVisibility(View.INVISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinPurchGrantDono;
                spinAgency.setSelection(0);
                break;
            case "GRANT":
            case "DONATION":
                spinAgency.setVisibility(View.VISIBLE);
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinAgency;
                break;
        }

        paramsedtNameOfOwnerOrg.topMargin = bigMargin;
        edtNameOfOwnerOrg.setLayoutParams(paramsedtNameOfOwnerOrg);
    }


    private void otherAgency(int position) {
        String pos = spinAgency.getItemAtPosition(position).toString();

        Log.d("POSITION OF AGENCY", pos);

        switch (pos) {
            case "OTHERS":
                paramsedtNameOfOwnerOrg.topToBottom = R.id.edtOtherAgency;
                edtOtherAgency.setVisibility(View.VISIBLE);
                break;
            default:
                paramsedtNameOfOwnerOrg.topToBottom = R.id.spinAgency;
                edtOtherAgency.setVisibility(View.INVISIBLE);
                break;
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
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                paramsedtNumLoads.topToBottom = R.id.edtCapacity;
                break;
            case 4:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtAveYield;
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                paramsedtAveYield.topToBottom = R.id.edtCapacity;
                break;
            case 5:
                paramstvBrand.topToBottom = R.id.edtQRCode;
                paramstvOwnership.topToBottom = R.id.edtRate;
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                paramsedtRate.topToBottom = R.id.edtCapacity;
                break;
            case 6:
                paramstvTypeTubewells.topToBottom = R.id.edtQRCode;
                paramstvBrand.topToBottom = R.id.spinTypeOfTubewells;
                paramstvOwnership.topToBottom = R.id.edtCapacity;
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                break;
            case 7:
                paramstvBrand.topToBottom = R.id.spinTypeMill;
                paramstvOwnership.topToBottom = R.id.edtRate;
                paramsedtCapacity.topToBottom = R.id.tvSubInputDays;
                paramsedtRate.topToBottom = R.id.edtCapacity;
                break;
        }

        paramstvBrand.topMargin = bigMargin;
        paramstvOwnership.topMargin = bigMargin;
        paramsedtCapacity.topMargin = bigMargin;
        paramsedtNumLoads.topMargin = bigMargin;
        paramsedtAveYield.topMargin = bigMargin;
        paramsedtRate.topMargin = bigMargin;

        tvBrand.setLayoutParams(paramstvBrand);
        tvOwnership.setLayoutParams(paramstvOwnership);
        edtCapacity.setLayoutParams(paramsedtCapacity);
        edtNumLoads.setLayoutParams(paramsedtNumLoads);
        edtAveYield.setLayoutParams(paramsedtAveYield);
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private void saveNote() {
        int day = 0, month = 0, year = 0;
        String machineType = spinMachineType.getSelectedItem().toString();
        String machineQRCode = edtQRCode.getText().toString();
        String date = month + "/" + day + "/" + year;
        String latitude = tvLat.getText().toString();
        String longitude = tvLong.getText().toString();
        String image = encodedImage;

        if (machineType.trim().isEmpty() ||
                machineQRCode.trim().isEmpty() ||
                isNullOrEmpty(latitude) ||
                isNullOrEmpty(longitude)) {
            Toast.makeText(this, "Incomplete Data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Type: " + machineType + " QR Code: " + machineQRCode + " Latitude: " + latitude + " Longitude: " + longitude, Toast.LENGTH_LONG).show();
            Intent dataAddMachine = new Intent();
//            dataAddMachine.putExtra(EXTRA_MACHINE_TYPE, machineType);
//            dataAddMachine.putExtra(EXTRA_MACHINE_QRCODE, machineQRCode);
//            dataAddMachine.putExtra(EXTRA_LAT, latitude);
//            dataAddMachine.putExtra(EXTRA_LONG, longitude);
//            dataAddMachine.putExtra(EXTRA_IMG, image);

            Machines machines = new Machines(machineQRCode, machineType, latitude, longitude, image);
            machineListViewModel.insert(machines);

            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                machines.setId(id);
                machineListViewModel.update(machines);
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
            //TODO ADD PICTURE INTENT
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO ADD PICTURE INTENT
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddMachineActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddMachineActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AddMachineActivity.this, "NECESITAMOS QUE NOS CONCEDAS LOS PERMISOS DE ALMACENAMIENTO PARA GUARDAR NOTICIAS O RADIOS COMO FAVORITOS.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddMachineActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERM_CODE);
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
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] imgInByte = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(imgInByte, Base64.DEFAULT);
                selectedImage.setImageBitmap(bitmap);
            }

        }

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
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
//                bitmap = scale(bitmap, 576, 1024);
                bitmap = scale(bitmap, 1080, 1920);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
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
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);
        spinMachineType = findViewById(R.id.spinMachineType);
        btnSave = findViewById(R.id.btnSaveNewMachine);
        multspinProblemsUnused = findViewById(R.id.multspinProblemsUnused);
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
        edtServiceArea = findViewById(R.id.edtServiceArea);
        spinOwnership = findViewById(R.id.spinOwnership);
        spinPurchGrantDono = findViewById(R.id.spinPurchGrantDono);
        spinAgency = findViewById(R.id.spinAgency);
        edtNameOfOwnerOrg = findViewById(R.id.edtNameOfOwnerOrOrganization);
        spinYearAcquired = findViewById(R.id.spinYearAcquired);
        spinRental = findViewById(R.id.spinRental);
        edtCustomRate = findViewById(R.id.edtCustomRate);
        spinCustomUnit = findViewById(R.id.spinCustomUnit);
        edtCustomRateUnit = findViewById(R.id.edtCustomRateUnit);
        tvMachineAvailability = findViewById(R.id.tvMachineAvailability);
        spinAvailability = findViewById(R.id.spinAvailability);
        tvConditionPresent = findViewById(R.id.tvConditionPresent);
        spinConditionPresent = findViewById(R.id.spinConditionPresent);
        multspinProblemsUnused = findViewById(R.id.multspinProblemsUnused);
        tvLocation = findViewById(R.id.tvLocationOfMachine);
        spinProvince = findViewById(R.id.spinProvince);
        spinMunicipality = findViewById(R.id.spinMunicipality);
        singlespinBarangay = findViewById(R.id.singlespinBrgy);
        edtOtherAgency = findViewById(R.id.edtOtherAgency);
        edtOtherBrand = findViewById(R.id.edtOtherBrand);
        edtOtherModel = findViewById(R.id.edtOtherModel);
        spinBrand = findViewById(R.id.spinBrand);
        spinModel = findViewById(R.id.spinModel);
        tvModel = findViewById(R.id.tvModel);
        edtRatedPower = findViewById(R.id.edtRatedPower);
        edtOtherProblems = findViewById(R.id.edtOtherProblems);
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
        edtCustomRateUnit.setVisibility(View.INVISIBLE);
        spinRentProv.setVisibility(View.INVISIBLE);
        spinRentMun.setVisibility(View.INVISIBLE);
        multspinRentBrgy.setVisibility(View.INVISIBLE);
        multspinProblemsUnused.setVisibility(View.INVISIBLE);
        edtOtherAgency.setVisibility(View.INVISIBLE);
        edtOtherModel.setVisibility(View.INVISIBLE);
        edtOtherBrand.setVisibility(View.INVISIBLE);
        edtOtherProblems.setVisibility(View.INVISIBLE);
    }
}




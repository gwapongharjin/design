package com.m3das.biomech.design;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.m3das.biomech.design.implementfragments.DitcherFragment;
import com.m3das.biomech.design.implementfragments.FertilizerFragment;
import com.m3das.biomech.design.implementfragments.GrabLoadersFragment;
import com.m3das.biomech.design.implementfragments.MainImplementFragment;
import com.m3das.biomech.design.implementfragments.MechHarvestFragment;
import com.m3das.biomech.design.implementfragments.MechPlanterFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddImplementActivity extends AppCompatActivity {

    private Spinner selectFrag;
    ImageButton camera, gallery, getLocation, btnScanQR;
    String currentPhotoPath;
    ImageView selectedImage;
    EditText edtQRCode;
    TextView tvCoordinates, tvMultSpin;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int LOCATION_REQUEST_CODE = 127;
    private String resLat, resLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_implement_activity);

        selectFrag = findViewById(R.id.spinImplementType);
        camera = findViewById(R.id.btnCameraImp);
        gallery = findViewById(R.id.btnGalleryImp);
        selectedImage = findViewById(R.id.imgMachineImp);
        getLocation = findViewById(R.id.btnGetLocationImp);
        edtQRCode = findViewById(R.id.edtQRCodeImp);
        btnScanQR = findViewById(R.id.btnScanQRCodeImp);
        tvCoordinates = findViewById(R.id.tvCoordinatesImp);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddImplementActivity.this.askCameraPermission();
                Toast.makeText(AddImplementActivity.this, "Camera Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        gallery.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);

            Toast.makeText(AddImplementActivity.this, "Gallery Clicked", Toast.LENGTH_SHORT).show();
        });

        btnScanQR.setOnClickListener(view -> {
            Intent intent = new Intent(AddImplementActivity.this, ScanBarcodeActivity.class);
            startActivityForResult(intent,0);
            Toast.makeText(AddImplementActivity.this, "Scanning QR", Toast.LENGTH_SHORT).show();
        });

        getLocation.setOnClickListener(view -> {
            Toast.makeText(AddImplementActivity.this, "Map Clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(view.getContext(), LocationMapsActivity.class);
            startActivityForResult(intent, LOCATION_REQUEST_CODE);
        });





        selectFrag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("Position:", String.valueOf( selectFrag.getSelectedItemPosition()));

                if(selectFrag.getSelectedItemPosition() == 0) {
                    BlankFragment blankFragment = new BlankFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, blankFragment, blankFragment.getTag())
                            .commit();


                }else if(selectFrag.getSelectedItemPosition() == 1){
                    GrabLoadersFragment grabLoadersFragment = new GrabLoadersFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, grabLoadersFragment, grabLoadersFragment.getTag())
                            .commit();



                }

                else if(selectFrag.getSelectedItemPosition() == 6){
                    DitcherFragment ditcherFragment = new DitcherFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, ditcherFragment, ditcherFragment.getTag())
                            .commit();

                }
                else if(selectFrag.getSelectedItemPosition() == 7 || selectFrag.getSelectedItemPosition() == 10){
                    FertilizerFragment fertilizerFragment = new FertilizerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, fertilizerFragment, fertilizerFragment.getTag())
                            .commit();

                }
                else if(selectFrag.getSelectedItemPosition() == 12){

                    MechHarvestFragment mechHarvestFragment = new MechHarvestFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, mechHarvestFragment, mechHarvestFragment.getTag())
                            .commit();

                }
                else if(selectFrag.getSelectedItemPosition() == 13){
                    MechPlanterFragment mechPlanterFragment = new MechPlanterFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, mechPlanterFragment, mechPlanterFragment.getTag())
                            .commit();
                }
                else{
                    MainImplementFragment mainImplementFragment = new MainImplementFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .replace(R.id.constraintLayoutImplementSpecs, mainImplementFragment, mainImplementFragment.getTag())
                            .commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
            tvCoordinates.setText("Lat: " + resLat + " Long: " + resLong);
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
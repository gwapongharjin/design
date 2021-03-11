
package com.m3das.biomech.design;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissionsRejected = new ArrayList<>();
    private final ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 27;
    private FloatingActionButton fabGetLoc, fabSaveLoc;
    private Double latitude, longitude, accuracy;
    private TextView tvAcc, tvLong, tvLat, tvDrawable;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
    LocationTrack locationTrack;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitByBackKey() {
        androidx.appcompat.app.AlertDialog alertbox = new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage("Do you want to stop getting your location?")
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
        setContentView(R.layout.location_maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);


        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.

        tvAcc = findViewById(R.id.tvAccMaps);
        tvLat = findViewById(R.id.tvLatMaps);
        tvLong = findViewById(R.id.tvLongMaps);
        tvDrawable = findViewById(R.id.tvDrawable);

//        final DialogGetLocation dialogGetLocation = new DialogGetLocation(this);

//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                dialogGetLocation.dismissDialog();
//
//
//            }
//        };
//
//        final Handler h = new Handler();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        fabGetLoc = findViewById(R.id.fabGetLocation);
        fabSaveLoc = findViewById(R.id.fabSaveLocation);

        longitude = Double.NaN;
        latitude = Double.NaN;
        accuracy = Double.NaN;

        fabGetLoc.setOnClickListener(view -> {
            getLocationInfo();
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        });

        fabSaveLoc.setOnClickListener(view -> {

            if (latitude.isNaN() && longitude.isNaN() && accuracy.isNaN()) {
                Toast.makeText(this, "Please get your location first before saving", Toast.LENGTH_SHORT).show();
            } else if (latitude == 0 && longitude == 0 && accuracy == 0) {
                Toast.makeText(this, "Please get your location first before saving", Toast.LENGTH_SHORT).show();
            } else if (accuracy >= 8) {
                Toast.makeText(this, "Unable to save. Please wait until the accuracy is below 8 or the indicator is yellow/green.", Toast.LENGTH_LONG).show();
            } else {
                String strLat = String.format("%.8f", latitude);
                String strLong = String.format("%.8f", longitude);
                String strAcc = String.format("%8f", accuracy);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("strLat", strLat);
                resultIntent.putExtra("StrLong", strLong);
                resultIntent.putExtra("StrAcc", strAcc);
                LocationMapsActivity.this.setResult(RESULT_OK, resultIntent);
                LocationMapsActivity.this.finish();
            }

        });
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                getLocationInfo();
//                Toast.makeText(getApplicationContext(), "Showing every second", Toast.LENGTH_SHORT).show();
            }
        }, delay);
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    private void getLocationInfo() {
        locationTrack = new LocationTrack(LocationMapsActivity.this);

        if (locationTrack.canGetLocation()) {
            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();
            accuracy = locationTrack.getAccuracy();

            int color;

            if (accuracy > 0 && accuracy <= 5) {
                color = Color.GREEN;
            } else if (accuracy > 5 && accuracy < 8) {
                color = Color.YELLOW;
            } else {
                color = Color.RED;
            }
//                Toast.makeText(getApplicationContext(), "Longitude:" + longitude + "\nLatitude:" + latitude + "\nAccuracy:" + accuracy, Toast.LENGTH_SHORT).show();

            tvLat.setText(String.valueOf(latitude));
            tvLong.setText(String.valueOf(longitude));
            tvAcc.setText(String.valueOf(accuracy));

            Drawable drawable = getResources().getDrawable(R.drawable.circle);
            drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            tvDrawable.setBackground(drawable);

            mMap.clear();
            LatLng loc = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(loc).title("My Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        } else {
            locationTrack.showSettingsAlert();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng loc = new LatLng(14.1676, 121.2435);
        mMap.addMarker(new MarkerOptions().position(loc).title("My Previous Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (isMarshmallow()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean isMarshmallow() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LocationMapsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}

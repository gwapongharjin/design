package com.m3das.biomech.design.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.maps.zzac;
import com.google.android.gms.internal.maps.zzk;
import com.google.android.gms.internal.maps.zzn;
import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.internal.maps.zzw;
import com.google.android.gms.internal.maps.zzz;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.ILocationSourceDelegate;
import com.google.android.gms.maps.internal.IProjectionDelegate;
import com.google.android.gms.maps.internal.IUiSettingsDelegate;
import com.google.android.gms.maps.internal.zzab;
import com.google.android.gms.maps.internal.zzad;
import com.google.android.gms.maps.internal.zzaf;
import com.google.android.gms.maps.internal.zzaj;
import com.google.android.gms.maps.internal.zzal;
import com.google.android.gms.maps.internal.zzan;
import com.google.android.gms.maps.internal.zzap;
import com.google.android.gms.maps.internal.zzar;
import com.google.android.gms.maps.internal.zzat;
import com.google.android.gms.maps.internal.zzav;
import com.google.android.gms.maps.internal.zzax;
import com.google.android.gms.maps.internal.zzaz;
import com.google.android.gms.maps.internal.zzbb;
import com.google.android.gms.maps.internal.zzbd;
import com.google.android.gms.maps.internal.zzbf;
import com.google.android.gms.maps.internal.zzbs;
import com.google.android.gms.maps.internal.zzc;
import com.google.android.gms.maps.internal.zzh;
import com.google.android.gms.maps.internal.zzl;
import com.google.android.gms.maps.internal.zzp;
import com.google.android.gms.maps.internal.zzr;
import com.google.android.gms.maps.internal.zzv;
import com.google.android.gms.maps.internal.zzx;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.m3das.biomech.design.LocationMapsActivity;
import com.m3das.biomech.design.LocationTrack;
import com.m3das.biomech.design.R;
import com.m3das.biomech.design.implementdb.Implements;
import com.m3das.biomech.design.machinedb.Machines;
import com.m3das.biomech.design.viewmodels.ImplementViewModel;
import com.m3das.biomech.design.viewmodels.MachineListViewModel;

import java.util.List;

public class MapsFragment extends Fragment {

    private ImplementViewModel implementViewModel;
    private MachineListViewModel machineListViewModel;
    LatLng locationImp;
    LatLng locationMach;
    FloatingActionButton fabRefresh;
    private GoogleMap gMap;
    private LocationTrack locationTrack;
    private LatLng loc;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.00, 122.00), 5));
        }
    };

    private void refreshMap() {
        locationMach = null;
        locationImp = null;
        implementViewModel.getAllImplements().observe(getActivity(), new Observer<List<Implements>>() {
            @Override
            public void onChanged(List<Implements> anImplements) {
                for (int i = 0; i < anImplements.size(); i++) {
                    if (anImplements.get(i).getLatitude().toLowerCase().equals("not yet acquired") || anImplements.get(i).getLongitude().toLowerCase().equals("not yet acquired")) {
                    } else {
                        locationImp = new LatLng(Double.parseDouble(anImplements.get(i).getLatitude()), Double.parseDouble(anImplements.get(i).getLongitude()));
                        gMap.addMarker(new MarkerOptions()
                                        .position(locationImp)
                                        .title("Implement Code: " + anImplements.get(i).getImplement_qrcode())
                                        .snippet("Attached to: " + anImplements.get(i).getUsed_on_machine())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.attachmentresized))
                        );
                        Log.d("LocImp", String.valueOf(locationImp));
                    }
                }
                if (locationImp != null) {
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationImp, 14));
                }
            }
        });

        machineListViewModel.getAllMachines().

                observe(getActivity(), new Observer<List<Machines>>() {
                    @Override
                    public void onChanged(List<Machines> machines) {
                        for (int i = 0; i < machines.size(); i++) {
                            if (machines.get(i).getMachine_latitude().toLowerCase().equals("not yet acquired") || machines.get(i).getMachine_longitude().toLowerCase().equals("not yet acquired") || machines.get(i).getMachine_longitude().isEmpty() || machines.get(i).getMachine_latitude().isEmpty()) {
                            } else {
                                locationMach = new LatLng(Double.parseDouble(machines.get(i).getMachine_latitude()), Double.parseDouble(machines.get(i).getMachine_longitude()));
                                gMap.addMarker(new MarkerOptions()
                                                .position(locationMach)
                                                .title("Machine Code: " + machines.get(i).getMachine_qrcode())
                                                .snippet("Respondent: " + machines.get(i).getResName())
                                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.machineresized))
                                );
                                Log.d("LocMach", String.valueOf(locationMach));
                            }
                        }
                        if (locationMach != null) {
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMach, 14));
                        }
                    }
                });

//        if (locationMach == null && locationImp == null) {
//            Toast.makeText(getContext(), "Please input data first", Toast.LENGTH_SHORT).show();
//        } else {
//            if (locationMach == null) {
//                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationImp, 14));
//            }
//            if (locationImp == null) {
//                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMach, 14));
//            }
//
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maps_fragment, container, false);
        fabRefresh = v.findViewById(R.id.fabRefreshMap);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//                    mapFragment.getMapAsync(callback);
//                    OnMapReadyCallback
                gMap.clear();
                refreshMap();
            }

        });

        machineListViewModel = new ViewModelProvider(getActivity()).get(MachineListViewModel.class);
        implementViewModel = new ViewModelProvider(getActivity()).get(ImplementViewModel.class);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
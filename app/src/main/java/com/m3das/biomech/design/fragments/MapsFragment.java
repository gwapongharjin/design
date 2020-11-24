package com.m3das.biomech.design.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

            machineListViewModel = new ViewModelProvider(getActivity()).get(MachineListViewModel.class);
            implementViewModel = new ViewModelProvider(getActivity()).get(ImplementViewModel.class);

            implementViewModel.getAllImplements().observe(getActivity(), new Observer<List<Implements>>() {
                @Override
                public void onChanged(List<Implements> anImplements) {
                    for (int i = 0; i < anImplements.size(); i++) {
                        locationImp = new LatLng(Double.parseDouble(anImplements.get(i).getLatitude()), Double.parseDouble(anImplements.get(i).getLongitude()));
                        googleMap.addMarker(new MarkerOptions()
                                .position(locationImp)
                                .title(anImplements.get(i).getImplement_qrcode())
                                .snippet(anImplements.get(i).getUsed_on_machine())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.attachmentresized))
                        );
                    }
                }
            });

            machineListViewModel.getAllMachines().observe(getActivity(), new Observer<List<Machines>>() {
                @Override
                public void onChanged(List<Machines> machines) {
                    for (int i = 0; i < machines.size(); i++) {
                        locationMach = new LatLng(Double.parseDouble(machines.get(i).getMachine_latitude()), Double.parseDouble(machines.get(i).getMachine_longitude()));
                        googleMap.addMarker(new MarkerOptions()
                                .position(locationMach)
                                .title(machines.get(i).getMachine_qrcode())
                                .snippet(machines.get(i).getResName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.machineresized))
                        );
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationMach, 15));
                    }
                }
            });



        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.maps_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
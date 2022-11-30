package com.location.geofencesapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.slider.Slider;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.location.geofencesapp.adapters.GeofencesAdapter;
import com.location.geofencesapp.databinding.FragmentGeofenceBinding;
import com.location.geofencesapp.helpers.GeofenceHelper;
import com.location.geofencesapp.room.GeofenceModel;

import java.util.List;

public class GeofenceHome extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "GeofenceHome";

    private FragmentGeofenceBinding binding;

    private GeofenceViewModel viewModel;

    private Dialog dialog;
    private EditText et_lat,et_long,et_name;
    private Slider area_slider;
    private Button btn_add;
    private TextView tv_radius;

    private GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGeofenceBinding.inflate(inflater,container,false);

        dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.custom_dialogue);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initViews();

        viewModel = new ViewModelProvider(this).get(GeofenceViewModel.class);

        area_slider.addOnChangeListener((slider, value, fromUser) -> tv_radius.setText((int) value+" m"));

        binding.fab.setOnClickListener(view -> dialog.show());
        btn_add.setOnClickListener(view -> getGeofenceFromUser());

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(requireActivity());
        geofenceHelper = new GeofenceHelper(requireActivity());

        getAllGeofences();


        return binding.getRoot();
    }

    private void getGeofenceFromUser() {
        String latitude = et_lat.getText().toString();
        String longitude = et_long.getText().toString();

        String name = et_name.getText().toString();
        if (name.isEmpty())
            name = "Geofence";

        float radius = area_slider.getValue();

        if (!latitude.isEmpty() && !longitude.isEmpty()) {

            viewModel.insertGeofence(new GeofenceModel(Double.parseDouble(latitude),Double.parseDouble(longitude),radius, name));
            dialog.dismiss();
            Toast.makeText(requireActivity(), "Inserted", Toast.LENGTH_SHORT).show();

        }else {
            et_lat.setError("Latitude required");
            et_long.setError("Longitude required");
            et_lat.requestFocus();
        }
    }

    private void initViews() {
        et_name = dialog.findViewById(R.id.et_GeofenceName);
        et_lat = dialog.findViewById(R.id.et_latitude);
        et_long = dialog.findViewById(R.id.et_longitude);
        area_slider = dialog.findViewById(R.id.area_slider);
        btn_add = dialog.findViewById(R.id.btn_save);
        tv_radius = dialog.findViewById(R.id.tv_area);

    }

    @SuppressLint("MissingPermission")
    private void getAllGeofences() {
        viewModel.getAllGeofences().observe(getViewLifecycleOwner(), geofenceModels -> {
            if(!geofenceModels.isEmpty()){
                for (GeofenceModel fences : geofenceModels) {

                    String id = String.valueOf(fences.getId());
                    String name = fences.getGeofenceName();
                    LatLng latLng = new LatLng(fences.getLatitude(),fences.getLongitude());
                    float radius = fences.getRadius();

                    Geofence geofence = geofenceHelper.getGeofence(id,latLng,radius,Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_DWELL|Geofence.GEOFENCE_TRANSITION_EXIT);
                    GeofencingRequest geofencingRequest = geofenceHelper.getGeofenceRequest(geofence);
                    PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

                    //add geofence
                    addGeofences(geofencingRequest,pendingIntent);
                    //add marker and circle around geofence
                    addMarkerOptions(latLng,name);
                    addCircleOptions(latLng,radius);

                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void addGeofences(GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
        geofencingClient.addGeofences(geofencingRequest,pendingIntent)
                .addOnSuccessListener(unused -> Log.i(TAG, "onSuccess: Geofence Added"))
                .addOnFailureListener(e -> {
                    String error = geofenceHelper.getError(e);
                    Log.i(TAG, "onFailure: "+error);

                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pakistan = new LatLng(30.7425, 73.3302);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pakistan,15));

        setUpPermissions();

    }

    private void addMarkerOptions(LatLng latLng,String name) {
        MarkerOptions markerOptions = new MarkerOptions()
                .title(name)
                .position(latLng);

        mMap.addMarker(markerOptions);
    }

    private void addCircleOptions(LatLng latLng,float radius) {

        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeWidth(4)
                .strokeColor(Color.argb(255,255,0,0))
                .fillColor(Color.argb(64,255,0,0))
                .visible(true);

        mMap.addCircle(circleOptions);

    }

    private void setUpPermissions() {

        if (Build.VERSION.SDK_INT >= 29) {
            Dexter.withContext(requireActivity())
                    .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    .withListener(new PermissionListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                            mMap.setMyLocationEnabled(true);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    }).check();
        }else {
            Dexter.withContext(requireActivity())
                    .withPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ).withListener(new MultiplePermissionsListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                mMap.setMyLocationEnabled(true);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            if (list.isEmpty())
                                permissionToken.cancelPermissionRequest();

                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }

    }

}
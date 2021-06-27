package com.example.finalapplication.google_map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.finalapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class google_map extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    EasyLocationProvider easyLocationProvider;
    Marker myMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(google_map.this)
                    .setMessage("GPS Service Not Enabled..")
                    .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                            mapFragment.getMapAsync(google_map.this);
                        }
                    })
                    .setNegativeButton("Cancel",null)
                    .show();
        }else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
//        LatLng lastMarker = new LatLng(33.596152, 73.000568);
//        Marker marker = googleMap.addMarker(new MarkerOptions().position(lastMarker).title("Rawalpindi"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(lastMarker));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1000);
        }else {


            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                Marker lastMarker;

                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    if (lastMarker != null)
                        lastMarker.remove();
                    lastMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title("Anonymous"));

                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            });

            easyLocationProvider = new EasyLocationProvider.Builder(google_map.this)
                    .setInterval(5000)
                    .setFastestInterval(2000)
                    //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setListener(new EasyLocationProvider.EasyLocationCallback() {
                        @Override
                        public void onGoogleAPIClient(GoogleApiClient googleApiClient, String message) {
                            Log.e("EasyLocationProvider","onGoogleAPIClient: "+message);
                        }

                        @Override
                        public void onLocationUpdated(double latitude, double longitude) {
                            Log.e("EasyLocationProvider","onLocationUpdated:: "+ "Latitude: "+latitude+" Longitude: "+longitude);
                            if (myMarker != null)
                                myMarker.remove();
                            LatLng latLong = new LatLng(latitude, longitude);
                            myMarker = googleMap.addMarker(new MarkerOptions().position(latLong).title("Current Location"));
                        }

                        @Override
                        public void onLocationUpdateRemoved() {
                            Log.e("EasyLocationProvider","onLocationUpdateRemoved");
                        }
                    }).build();

            getLifecycle().addObserver(easyLocationProvider);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted !",Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        easyLocationProvider.removeUpdates();
        getLifecycle().removeObserver(easyLocationProvider);
        super.onDestroy();
    }
}

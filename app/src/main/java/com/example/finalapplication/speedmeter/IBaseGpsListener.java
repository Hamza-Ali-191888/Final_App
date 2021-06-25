package com.example.finalapplication.speedmeter;

import android.Manifest;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import com.example.finalapplication.R;

import java.util.Formatter;
import java.util.Locale;

//MAIN FILE FOR SPEED METER

public class IBaseGpsListener extends AppCompatActivity implements LocationListener {

    SwitchCompat sw_metric;
    TextView tv_speed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedmeter);
        sw_metric = findViewById(R.id.sw_metric);
        tv_speed = findViewById(R.id.tv_speed);

        //check for gps permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start program if permission granted
            doStuff();
        }
        this.updateSpeed(null);
        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                IBaseGpsListener.this.updateSpeed(null);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            this.updateSpeed(location);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void doStuff() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this,"Waiting For GPS connection!",Toast.LENGTH_SHORT).show();
    }
    
    private void updateSpeed(Location location){
        float nCurrentSpeed=0;
        //tv_speed.setText(String.valueOf(location.getSpeed()));
//        if(location!=null){
//            location.setbUseMeterunits(this.useMetricUnits());
//            nCurrentSpeed=location.getSpeed();
//        }
//        Formatter fmt= new Formatter(new StringBuilder());
//        fmt.format(Locale.US, "%5.1f",nCurrentSpeed);
//        String strCurrentSpeed = fmt.toString();
//        strCurrentSpeed = strCurrentSpeed.replace("","0");
//
//
        if (location != null) {
            if (this.useMetricUnits()) {
                double speed_in_km_h = location.getSpeed() * 3.6;
                tv_speed.setText(speed_in_km_h + " km/h");
            } else {
                double speed_in_miles_h = location.getSpeed() * 2.236936;
                tv_speed.setText(speed_in_miles_h + " miles/h");
            }
        }

    }

    private  boolean useMetricUnits(){
        return  sw_metric.isChecked();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            } else {
                finish();
            }

        }
    }
}

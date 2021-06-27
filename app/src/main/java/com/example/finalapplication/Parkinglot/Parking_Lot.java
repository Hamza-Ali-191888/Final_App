package com.example.finalapplication.Parkinglot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.finalapplication.R;
import com.google.android.gms.common.wrappers.PackageManagerWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Parking_Lot extends AppCompatActivity {

    Spinner spType;
    Button btFind;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;
    double currentLat = 0, currentLong =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.parking_lot);


        spType = findViewById(R.id.sp_type);
        btFind = findViewById(R.id.bt_find);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //initinalizing array of place type

        String[] placeTypeList = {"Parking Lot"};

        //Initializing array of place name

        String[] placeNameList = {"PARKING LOT"};

        //Setting adapter on Spinner
        spType.setAdapter(new ArrayAdapter<>(Parking_Lot.this, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        //Initializing fused location provider client
        // to provide the location information that your app needs

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Check permission

        if (ActivityCompat.checkSelfPermission(Parking_Lot.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }
        else{

            //if permission rejected then
            ActivityCompat.requestPermissions(Parking_Lot.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 440);

        }

        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Selected position of spinner
                int i= spType.getSelectedItemPosition();

                //Initializing url

                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=" + currentLat + "," + currentLong +
                " &radius=5000" +
                "&types=" + placeTypeList[i] +
                "&sensor=true" +
                "&key=" + getResources().getString(R.string.google_places_key); //Google map key

                //Execute Place task method to downlod json data
                try {
                    new PlaceTask().execute(url);
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }




            }
        });


    }


    private void getCurrentLocation() {
        //initailizing task location

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    //getting current latitude
                    currentLat= location.getLatitude();

                    //getting current Longitude

                    currentLong = location.getLongitude();

                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            //when map ready
                            map = googleMap;
                            //zooming camera on current location
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong), 10
                            ));

                        }
                    });
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 440){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                //initalizing data
                 data = downloadUrl(strings[0]);
                 Log.d("Data: ", data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            //Execute parser task
            Log.d("S: ", s);
            new ParseTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        //initalizing URL
        URL url = new URL(string);
        //initalizing connection
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        //connecting it
        connection.connect();
        //initializing input stream
        InputStream stream = connection.getInputStream();
        //intializing buffer reader
        BufferedReader reader= new BufferedReader(new InputStreamReader(stream));
        //initalizing string builder
        StringBuilder builder = new StringBuilder();
        //initalizing string variable
        String line="";
        while ((line = reader.readLine())!=null){
            //append line
            builder.append(line);
        }
        //Get appended data
        String data= builder.toString();
        //Closing reader
        reader.close();

        return data;
    }

    private class ParseTask  extends  AsyncTask <String,Integer, List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //create json parser class
            JsonParser jsonParser = new JsonParser();
            //initalizing hashmap list
            List<HashMap<String,String>> mapList = null;
            JSONObject object= null;
            try {
                 object = new JSONObject((strings[0]));
                 //Parse json object
                mapList= jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            //Clear map
            map.clear();
            //use for loop
            for(int i=0;i<hashMaps.size(); i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);
                //Get latitude
                double lat= Double.parseDouble(hashMapList.get("lat"));
                //Get longitude
                double lng= Double.parseDouble(hashMapList.get("lng"));
                //Get name
                String name = hashMapList.get("name");
                //Concat latitude and longitude or joining both object to single
                LatLng latLng = new LatLng(lat,lng);
                //intializing marker optiop
                MarkerOptions options = new MarkerOptions();
                //set position
                options.position(latLng);
                //set title
                options.title(name);
                //Add marker on map
                map.addMarker(options);

            }

        }
    }
}

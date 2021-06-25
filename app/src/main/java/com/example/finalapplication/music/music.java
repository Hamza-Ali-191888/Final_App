package com.example.finalapplication.music;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalapplication.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class music extends AppCompatActivity {
    ListView myListViewForSongs;
    String[] item;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);
        context = getApplicationContext();

        myListViewForSongs = (ListView) findViewById(R.id.mySongListView);
        //runtimePermission();
        takePermission1();

    }

    public void takePermission1(){
        if(isPermissionGranted()){
            display();
        }
        else{
            takePermission();
        }
    }

    //Taking Permissions FOR ANDROID 11 and Above
    private boolean isPermissionGranted(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else{
            int readEnternalStoragePermissions = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            return readEnternalStoragePermissions == PackageManager.PERMISSION_GRANTED;
        }
    }


    private void takePermission(){
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
            try {
                Intent intent= new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent,100);
            }
            catch (Exception exception){
                Intent intent= new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent,100);
            }
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            if(requestCode==100){
                if(Build.VERSION.SDK_INT == Build.VERSION_CODES.R){
                    if(Environment.isExternalStorageManager()){
                        Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                        display();
                    }
                    else {
                        takePermission();
                    }
                }
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            if(requestCode==101){
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(readExternalStorage){
                    Toast.makeText(this,"Read Permission Is Granted",Toast.LENGTH_SHORT).show();
                }
                else {
                    takePermission();
                }
            }

        }

    }

    //old runtime permissions
    public void runtimePermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                display();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();

            }
        }).check();
    }

    //To Find Song In Phone
    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                    arrayList.add(singleFile);
                }
            }

        }
        return arrayList;
    }

    //For Storing songs In Array
    void display() {
        final ArrayList<File> mysongs = findSong(Environment.getExternalStorageDirectory());
        item = new String[mysongs.size()];

        for (int i = 0; i < mysongs.size(); i++) {
            item[i] = mysongs.get(i).getName().toString().replace(".mp3", "").replace("wav", "");

        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.list_items, item);
        myListViewForSongs.setAdapter(myAdapter);

        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String songName=myListViewForSongs.getItemAtPosition(i).toString();
                startActivity(new Intent(context, musicPlayer.class).putExtra("songs",mysongs).putExtra("songname",songName).putExtra("pos",i));
            }
        });

    }
}

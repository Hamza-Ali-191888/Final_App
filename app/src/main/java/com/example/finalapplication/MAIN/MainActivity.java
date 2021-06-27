package com.example.finalapplication.MAIN;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapplication.Parkinglot.Parking_Lot;
import com.example.finalapplication.R;
import com.example.finalapplication.google_map.google_map;
import com.example.finalapplication.music.music;
import com.example.finalapplication.phonebook.ContactList;
import com.example.finalapplication.speedmeter.IBaseGpsListener;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity  {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    ImageView mic;
    ImageView powerof;
    ImageView musicPlayer;
    ImageView speed;
    ImageView phone;
    ImageView messsage;
    ImageView map;
    ImageView parking;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.finalapplication.R.layout.activity_main);
        mic=findViewById(com.example.finalapplication.R.id.mic1);
        //button click to show text dialouge
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });

        //music player buttons
        musicPlayer= (ImageView) findViewById(R.id.music_player);
        musicPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, music.class);
                startActivity(intent);
            }
        });

        //Google Map
        map=(ImageView) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, google_map.class);
                startActivity(intent);
            }
        });

        //Parking
        parking=(ImageView) findViewById(R.id.parking);
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Parking_Lot.class);
                startActivity(intent);
            }
        });

        // MESSAGE
        messsage=(ImageView) findViewById(R.id.message);
        messsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getPackageManager().getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(MainActivity.this));
                startActivity(intent);

            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},1000);
        }


        //speedmeter

        speed =(ImageView) findViewById(R.id.speed);
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, IBaseGpsListener.class);
                startActivity(intent);
            }
        });

        //For PhoneBook
        phone=(ImageView) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ContactList.class);
                startActivity(intent);
            }
        });



        //power off button
        powerof=findViewById(R.id.power_off);
        powerof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });

    }

    //Receiving Message
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

    //Mic Settings
    private void speak() {
        //intent to show text dialouge
        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hi Im Speaking");
        //start intent
        try {
            //no error
            //show dialog
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);

        }
        catch (Exception e){
            //if there was any error
            //get message or show error
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    //receive voice input and handle it


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode==RESULT_OK && null !=data){
                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("Result", result.get(0));
                    if (result.get(0).contains("music") || result.get(0).contains("Music"))
                    {
                        Intent intent = new Intent(MainActivity.this, music.class);
                        startActivity(intent);
                    }else{
                        if (result.get(0).contains("speed") || result.get(0).contains("Speed"))
                        {
                            Intent intent= new Intent(MainActivity.this, IBaseGpsListener.class);
                            startActivity(intent);
                        }else
                        {
                            if (result.get(0).contains("phone") || result.get(0).contains("Phone") || result.get(0).contains("PhoneBook") || result.get(0).contains("Phonebook") || result.get(0).contains("phonebook"))
                            {
                                Intent intent=new Intent(MainActivity.this, ContactList.class);
                                startActivity(intent);
                            }else
                            {
                                if (result.get(0).contains("message") || result.get(0).contains("messages") || result.get(0).contains("Message") || result.get(0).contains("Messages"))
                                {
                                    Intent intent=getPackageManager().getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(MainActivity.this));
                                    startActivity(intent);
                                }else
                                {
                                    if (result.get(0).contains("google map") || result.get(0).contains("Google Map"))
                                    {
                                        Intent intent =new Intent(MainActivity.this, google_map.class);
                                        startActivity(intent);
                                    }else
                                    {
                                        if (result.get(0).contains("parking") || result.get(0).contains("Parking"))
                                        {
                                            Intent intent =new Intent(MainActivity.this, Parking_Lot.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                break;
            }
        }
    }
    


}
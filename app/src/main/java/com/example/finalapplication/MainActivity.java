package com.example.finalapplication;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

                }
                break;
            }
        }
    }


}
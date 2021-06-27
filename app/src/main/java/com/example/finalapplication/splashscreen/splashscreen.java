package com.example.finalapplication.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalapplication.MAIN.MainActivity;
import com.example.finalapplication.R;


public class splashscreen extends AppCompatActivity {
    private Handler waitHandler = new Handler();
    @Override
    protected void onCreate(@Nullable  Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysplash);
        ImageView image = (ImageView)findViewById(R.id.image);
        TextView text = findViewById(R.id.splashtext);

        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        image.startAnimation(animation1);
        text.startAnimation(animation1);

        waitHandler.postDelayed(new Runnable() {
           @Override
           public void run() {
               try {
                   Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                   startActivity(intent);

                   finish();
               }
               catch (Exception ignored){
                   ignored.printStackTrace();
               }
           }
       },2000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        //Remove all the callbacks otherwise navigation will execute even after activity is killed or closed.
        waitHandler.removeCallbacksAndMessages(null);
    }
}

package com.example.finalapplication.music;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class musicPlayer extends AppCompatActivity {
    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;
    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    String sname;
    Thread updateseekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        btn_next=(Button) findViewById(R.id.next);
        btn_previous=(Button) findViewById(R.id.previous);
        btn_pause=(Button) findViewById(R.id.pause);
        songTextLabel= (TextView) findViewById(R.id.songLabel);
        songSeekbar= (SeekBar) findViewById(R.id.seekBar);

        //to keep updating seekbar regularly
        updateseekBar= new Thread(){
            int done;

            @Override
            public void run() {
                int totalDuration= myMediaPlayer.getDuration();
                int currentPosition=0;
                done = 3;
                while (currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                        break;
                    }
                }
            }

            public void setRun()
            {
                done = 4;
            }
        };
        if(myMediaPlayer!= null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle=i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

        sname=mySongs.get(position).getName().toString();

        String songName = i.getStringExtra("songname");

        songTextLabel.setText(songName);
        //
        songTextLabel.setSelected(true);

        position= bundle.getInt("pos",0);

        Uri u= Uri.parse(mySongs.get(position).toString());

        myMediaPlayer= MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        // to sync seekbar with music
        songSeekbar.setMax(myMediaPlayer.getDuration());
        updateseekBar.start();

        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                updateseekBar.interrupt();
                songSeekbar.setProgress(0);
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
                songSeekbar.setMax(myMediaPlayer.getDuration());
                updateseekBar = new Thread(){
                    @Override
                    public void run() {
                        int totalDuration= myMediaPlayer.getDuration();
                        int currentPosition=0;
                        while (currentPosition < totalDuration){
                            try {
                                sleep(500);
                                currentPosition=myMediaPlayer.getCurrentPosition();
                                songSeekbar.setProgress(currentPosition);
                            }
                            catch (InterruptedException e){
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                };
                updateseekBar.start();
            }
        });

        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                songSeekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                songSeekbar.getThumb().setColorFilter(color,PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration());

                if(myMediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                    myMediaPlayer.pause();
                }
                else {
                    btn_pause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    myMediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateseekBar.interrupt();
                songSeekbar.setProgress(0);
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u=Uri.parse(mySongs.get(position).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
                songSeekbar.setMax(myMediaPlayer.getDuration());
                updateseekBar.start();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();
                position=((position-1)<0) ? (mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();

            }
        });




    }
}

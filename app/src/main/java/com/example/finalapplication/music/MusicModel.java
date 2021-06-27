package com.example.finalapplication.music;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.finalapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class MusicModel {
    ArrayList<File> mySongs;
    CustomThread thread;
    public static MediaPlayer myMediaPlayer;
    SeekBar songSeekbar;
    int songNumber;
    TextView songNameTextView;
    Context context;
    boolean isPaused;

    MusicModel (ArrayList<File> mySongs, int songNumber, TextView songNameTextView, Context context, SeekBar songSeekbar)
    {
        this.mySongs = mySongs;
        this.thread = createThread();
        this.songNumber = songNumber;
        this.songNameTextView = songNameTextView;
        this.songSeekbar = songSeekbar;
        int color = Color.argb(255, 255, 255, 255);
        songSeekbar.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        this.context = context;
        startSong();
        thread.start();
    }

    public void setMySongs(ArrayList<File> mySongs) {
        this.mySongs = mySongs;
    }

    public static void setMyMediaPlayer(MediaPlayer myMediaPlayer) {
        MusicModel.myMediaPlayer = myMediaPlayer;
    }

    public CustomThread createThread()
    {
        return new CustomThread(){
            @Override
            public void run() {
                while (currentPosition < totalDuration){
                    try {
                        sleep(1000);
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
    }

    public void startSong()
    {
        commonSongPlayer();



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
    }

    public void playNextSong()
    {
        songNumber +=1;
        if(!isPaused)
            commonSongPlayer();
        else
        {
            songSeekbar.setProgress(0);
            Uri u= Uri.parse(mySongs.get(songNumber).toString());
            myMediaPlayer= MediaPlayer.create(context,u);

            // to sync seekbar with music
            songSeekbar.setMax(myMediaPlayer.getDuration());
            songNameTextView.setText(mySongs.get(songNumber).getName());

        }
    }
    public void playPreviousSong()
    {
        songNumber -=1;
        if(!isPaused)
            commonSongPlayer();
        else
        {
            songSeekbar.setProgress(0);
            Uri u= Uri.parse(mySongs.get(songNumber).toString());
            myMediaPlayer= MediaPlayer.create(context,u);

            // to sync seekbar with music
            songSeekbar.setMax(myMediaPlayer.getDuration());
            songNameTextView.setText(mySongs.get(songNumber).getName());

        }
    }

    public void commonSongPlayer()
    {
        if(myMediaPlayer!= null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        songSeekbar.setProgress(0);

        Uri u= Uri.parse(mySongs.get(songNumber).toString());
        myMediaPlayer= MediaPlayer.create(context,u);

        // to sync seekbar with music
        songSeekbar.setMax(myMediaPlayer.getDuration());
        songNameTextView.setText(mySongs.get(songNumber).getName());

        thread.currentPosition = 0;
        thread.setTotalDuration(myMediaPlayer.getDuration());
        myMediaPlayer.setLooping(false);

        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });

        myMediaPlayer.start();

    }

    public void pauseSong(Button btn_pause)
    {

        if(myMediaPlayer.isPlaying()){
            btn_pause.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
            myMediaPlayer.pause();
            isPaused=true;
        }
        else {
            btn_pause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
            myMediaPlayer.start();
            isPaused=false;
        }
    }


}

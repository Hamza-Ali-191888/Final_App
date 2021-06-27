package com.example.finalapplication.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalapplication.R;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayerFixed extends AppCompatActivity {
    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;
    MusicModel musicModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_player);

        btn_next=(Button) findViewById(R.id.next);
        btn_previous=(Button) findViewById(R.id.previous);
        btn_pause=(Button) findViewById(R.id.pause);
        songTextLabel= (TextView) findViewById(R.id.songLabel);
        songSeekbar= (SeekBar) findViewById(R.id.seekBar);

        songTextLabel.setSelected(true);



        StartMusicPlayer();

        //Set Listeners
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicModel.playNextSong();
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicModel.playPreviousSong();
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicModel.pauseSong(btn_pause);
            }
        });
    }

    void StartMusicPlayer()
    {
        Intent i = getIntent();
        Bundle bundle=i.getExtras();
        ArrayList<File> arrayList = (ArrayList) bundle.getParcelableArrayList("songs");
        if (arrayList != null)
            musicModel = new MusicModel(arrayList, bundle.getInt("pos",0), songTextLabel, getApplicationContext(), songSeekbar);
    }

    @Override
    protected void onDestroy() {
        this.musicModel.thread.interrupt();
        super.onDestroy();
    }
}

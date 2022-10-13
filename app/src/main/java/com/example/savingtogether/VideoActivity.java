package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    TextView videoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        System.out.println("I HAVE REACHED");
        videoTitle = findViewById(R.id.videoTitle);

        // Retrieve topic title
        Intent intent = getIntent();
        String title = intent.getStringExtra("TopicName");
        videoTitle.setText(title);
        System.out.println("hello" + title);

        // Prevent auto video playback to let user play the video themselves to see the controls.
        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.demo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
//        videoView.start()
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

    }
}
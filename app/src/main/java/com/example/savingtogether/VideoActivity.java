package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;

public class VideoActivity extends AppCompatActivity {

    Button backBtn, menuBtn;
    TextView videoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        System.out.println("I HAVE REACHED");

        // Retrieve topic title
        videoTitle = findViewById(R.id.videoTitle);
        Intent intent = getIntent();
        String title = intent.getStringExtra("TopicName");
        videoTitle.setText(title);

        // Prevent auto video playback to let user play the video themselves to see the controls.
        VideoView videoView = findViewById(R.id.videoView);
        String getVideoTitle = title.replaceAll("\\s+","").toLowerCase(); // lowercase and trim whitespace to match path
        System.out.println(getVideoTitle);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + "/raw/" + getVideoTitle);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

        // Back button
        backBtn = findViewById(R.id.videoBackButton);
        backBtn.setOnClickListener(view -> {
            clickSound.start();
            onBackPressed();
        });

        // Back to menu button
        menuBtn = findViewById(R.id.videoMenuButton);
        menuBtn.setOnClickListener(view -> {
            clickSound.start();
            openMenu();
        });

    }

    // Open screen for topics menu page
    private void openMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}
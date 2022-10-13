package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenuActivity extends AppCompatActivity {
    Button introBtn, howToUseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        introBtn =findViewById(R.id.learningTopicsButton);
        howToUseBtn = findViewById(R.id.forumButton);
        ImageView artView = findViewById(R.id.artView);
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);
        introBtn.setOnClickListener(view -> {
            clickSound.start();
            openIntro();
        });

//        howToUseBtn.setOnClickListener(view -> {
//            clickSound.start();
//            openTutorial();
//        });

        artView.animate().rotation(1450).alpha(1).setDuration(2000);

    }

    // Open screen for intro page
    private void openIntro() {
        Intent intent = new Intent(this, TopicsActivity.class);
        startActivity(intent);
    }

//    // Open screen for how to use tutorial
//    private void openTutorial() {
//        Intent intent = new Intent(this, Tutorial.class);
//        startActivity(intent);
//    }

}
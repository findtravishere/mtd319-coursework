package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class MainMenuActivity extends AppCompatActivity {
    Button topicsBtn, forumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        topicsBtn =findViewById(R.id.learningTopicsButton);
        forumBtn = findViewById(R.id.forumButton);
        ImageView artView = findViewById(R.id.artView);
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

        // Open Topics Menu
        topicsBtn.setOnClickListener(view -> {
            clickSound.start();
            openTopics();
        });

        forumBtn.setOnClickListener(view -> {
            clickSound.start();
            openForum();
        });

        artView.animate().rotation(1450).alpha(1).setDuration(2000);

    }

    // Open screen for topics page
    private void openTopics() {
        Intent intent = new Intent(this, TopicsActivity.class);
        startActivity(intent);
    }

    // Open screen for community forum page
    private void openForum() {
        Intent intent = new Intent(this, ForumActivity.class);
        startActivity(intent);
    }

}
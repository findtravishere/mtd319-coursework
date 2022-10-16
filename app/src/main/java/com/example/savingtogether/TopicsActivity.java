package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class TopicsActivity extends AppCompatActivity {

    //    SearchView searchView;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    ArrayList<String> topics = new ArrayList<>();
    Button menuButton, topicsForumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Example topics for the app
        topics.add("How To Save Money");
        topics.add("How To Cook At Home");
        topics.add("How To Eat Healthy");
        topics.add("Finance Tips For Beginners");
        topics.add("Inflation advice");
        topics.add("Cheap Groceries");
        topics.add("Learning Investing");
        topics.add("ETFs Basics");
        topics.add("CPF Overview");
        topics.add("Best Credit Cards");
        topics.add("Take Public Transport");
        topics.add("How Medisave Helps Finances");
        topics.add("How To BTO");
        topics.add("Singapore Savings Bonds");
        topics.add("Career Advice");
        topics.add("Side Hustles");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        // Ids for components
        listView = findViewById(R.id.listViewSearch);
        menuButton = findViewById(R.id.menuButton);
        topicsForumButton = findViewById(R.id.topicsForumButton);

        // Sort topics alphabetically
        Collections.sort(topics);

        // Setup
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);
        listView.setAdapter(arrayAdapter);
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);


        // Dynamically switch to selected topic
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickSound.start();
                Intent intent = new Intent(TopicsActivity.this, IndividualMenuActivity.class);
                int titleId = (int) listView.getItemIdAtPosition(i);
                intent.putExtra("TopicName", topics.get(titleId));
                startActivity(intent);
            }
        });

        // Back to menu button
        menuButton.setOnClickListener(view -> {
            clickSound.start();
            openMenu();
        });

        // Go to forum button
        topicsForumButton.setOnClickListener(view -> {
            clickSound.start();
            openForum();
        });

    }

    // Open screen for intro page
    private void openMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    // Open screen for forum page
    private void openForum() {
        Intent intent = new Intent(this, ForumActivity.class);
        startActivity(intent);
    }

}
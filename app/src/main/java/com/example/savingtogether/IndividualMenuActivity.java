package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class IndividualMenuActivity extends AppCompatActivity {

    TextView topicText;
    Button startVideoButton, commentButton, topicsMenuButton;
    SharedPreferences sharedPreferences;
    static ArrayList<String> comments = new ArrayList<>();
    ArrayList<String> truncatedComments = new ArrayList<>();
    static ArrayList<String> usernames = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_menu);

        // Retrieve data from the list
        topicText = findViewById(R.id.topicTextView);
        Intent intent = getIntent();
        String title = intent.getStringExtra("TopicName");
        topicText.setText(title);

        // Sound setup
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

        // Store listview comments in persistent storage setup
        ListView listView = findViewById(R.id.listCommentsView);
        String getTitleSQL = title.replaceAll("\\s+","").toLowerCase(); // lowercase and trim whitespace to match path

        // SQLite setup

        SQLiteDatabase myDatabase = this.openOrCreateDatabase(getTitleSQL, MODE_PRIVATE, null );

        // run this for testing for clearing data
//        myDatabase.execSQL("DROP TABLE IF EXISTS '" + getTitleSQL + "'");


        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + getTitleSQL + " (comment VARCHAR, username VARCHAR)");
//        myDatabase.execSQL("INSERT INTO " + getTitleSQL + " (comment) VALUES " + "('test" + getTitleSQL + "')");
//        myDatabase.execSQL("INSERT INTO " + getTitleSQL + " (comment) VALUES " + "('test 2" + getTitleSQL + "')");
//        myDatabase.execSQL("INSERT INTO " + getTitleSQL + " (comment) VALUES " + "('test 3" + getTitleSQL + "')");

        // Retrieve data
        Cursor c = myDatabase.rawQuery("SELECT * FROM " + getTitleSQL, null);
        int commentIndex = c.getColumnIndex("comment");
        int usernameIndex = c.getColumnIndex("username");


        if (c != null ) {
            comments.clear();
            usernames.clear();
            if (c.moveToFirst()) {
                do {
                    String comments = c.getString(commentIndex);
                    IndividualMenuActivity.comments.add(comments);
                    String usernames = c.getString(usernameIndex);
                    IndividualMenuActivity.usernames.add(usernames);
                }while (c.moveToNext());
            }
        }

        Collections.reverse(comments);
        Collections.reverse(usernames);

        // Truncate comments to be displayed before expanding by clicking
        truncatedComments = (ArrayList) comments.clone();
        for (int i = 0; i < truncatedComments.size(); i++) {
            String retrievedComment = truncatedComments.get(i);
            if (retrievedComment.length() > 33) {
                truncatedComments.set(i, retrievedComment.substring(0,34) + " [read more ...]");
            }
        }


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, truncatedComments);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickSound.start();
                Intent intent = new Intent(getApplicationContext(),CommentActivity.class);
                intent.putExtra("commentId",i);
                startActivity(intent);
            }
        });


        // Handle video button
        startVideoButton = findViewById(R.id.startVideoButton);
        startVideoButton.setOnClickListener(view -> {
            clickSound.start();
            openVideo();
        });

        // Handle comments button
        commentButton = findViewById(R.id.commentsButton);
        commentButton.setOnClickListener(view -> {
            clickSound.start();
            openComments();
        });

        // Handle back to topics button
        topicsMenuButton = findViewById(R.id.topicsMenuButton);
        topicsMenuButton.setOnClickListener(view -> {
            clickSound.start();
            onBackPressed();
        });


    }
    private void openVideo() {
        Intent intent = new Intent(this, VideoActivity.class);
        String title = (String) topicText.getText();
        intent.putExtra("TopicName", title);
        startActivity(intent);
    }

    private void openComments() {
        Intent intent = new Intent(this, CommentActivity.class);
        String title = (String) topicText.getText();
        intent.putExtra("TopicName", title);
        startActivity(intent);
    }


    // Refresh data on submit when returning back to this page
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}

// ARCHIVED
//        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.savingtogether", Context.MODE_PRIVATE);
//        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet(spTitle, null);
//        if (set == null) {
//        } else {
//            comments = new ArrayList(set);
//        }
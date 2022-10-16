package com.example.savingtogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class ForumActivity extends AppCompatActivity {

    ArrayAdapter arrayAdapter;
    static ArrayList<String> titles = new ArrayList<>();
    static ArrayList<String> posts = new ArrayList<>();
    Button backButton, topicsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        // Sound setup
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

        // SQLite setup, do not match with other db in case post title matches
        ListView listView = findViewById(R.id.forumListView);
        SQLiteDatabase forumDatabase = this.openOrCreateDatabase("Forum", MODE_PRIVATE, null );

        // Run this for testing for clearing data
//        forumDatabase.execSQL("DROP TABLE IF EXISTS 'forum'");

        forumDatabase.execSQL("CREATE TABLE IF NOT EXISTS forum (title VARCHAR, post VARCHAR)");

        // Retrieve data
        Cursor c = forumDatabase.rawQuery("SELECT * FROM forum" , null);
        int titleIndex = c.getColumnIndex("title");
        int postIndex = c.getColumnIndex("post");
        int imageIndex = c.getColumnIndex("image");


        if (c != null ) {
            titles.clear();
            posts.clear();
            if (c.moveToFirst()) {
                do {
                    String titles = c.getString(titleIndex);
                    ForumActivity.titles.add(titles);
                    String posts = c.getString(postIndex);
                    ForumActivity.posts.add(posts);
                }while (c.moveToNext());
            }
        }

        // Sort by latest
        Collections.reverse(titles);
        Collections.reverse(posts);

        // Dynamically switch to selected topic
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickSound.start();
                Intent intent = new Intent(ForumActivity.this, IndividualPostActivity.class);
                // Get the item which the user clicks on in the list
                int titleId = (int) listView.getItemIdAtPosition(i);
                intent.putExtra("ForumTitle", titles.get(titleId));
                intent.putExtra("ForumPost", posts.get(titleId));
                startActivity(intent);
            }
        });

        // Back and topics button
        backButton = findViewById(R.id.backForumButton);
        backButton.setOnClickListener(view -> {
            clickSound.start();
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        });

        topicsButton = findViewById(R.id.forumTopicsButton);
        topicsButton.setOnClickListener(view -> {
            clickSound.start();
            Intent intent = new Intent(this, TopicsActivity.class);
            startActivity(intent);
        });
    }


    // Forum add post menu selector
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Individual add post
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_post) {
            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
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
package com.example.savingtogether;

import static com.example.savingtogether.IndividualMenuActivity.usernames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashSet;

public class CommentActivity extends AppCompatActivity {

    int commentId;
    Button submitButton, backCommentButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Sound setup
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);

        // Edit username and comment handling
        EditText commentField = findViewById(R.id.editComment);
        EditText usernameField = findViewById(R.id.commentName);
        Intent intent = getIntent();
        commentId = intent.getIntExtra("commentId",-1);
        String title = intent.getStringExtra("TopicName");
        submitButton = findViewById(R.id.submitCommentButton);
        backCommentButton = findViewById(R.id.backCommentButton);

        // Show popup expansion of comment if clicked (disable and hide everything)
        if (commentId != -1) {
            commentField.setText(IndividualMenuActivity.comments.get(commentId));
            usernameField.setText(usernames.get(commentId) + "'s comment");

            // disable editting and submit button
            commentField.setEnabled(false);

            submitButton.setAlpha(0);
            submitButton.setEnabled(false);

            backCommentButton.setAlpha(1);
            backCommentButton.setOnClickListener(view -> {
                clickSound.start();
                onBackPressed();
            });

            // Handle new comment submission
        } else {
            IndividualMenuActivity.comments.add("");
            commentId = IndividualMenuActivity.comments.size() - 1;
            backCommentButton.setEnabled(false);
        }

        // Handle submit comment button
        submitButton.setOnClickListener(view -> {
            String getTitleSQL = title.replaceAll("\\s+","").toLowerCase(); // lowercase and trim whitespace to match path
            SQLiteDatabase myDatabase = this.openOrCreateDatabase(getTitleSQL, MODE_PRIVATE, null );
            String commentToSubmit = commentField.getText().toString();
            String usernameToSubmit = usernameField.getText().toString();

            // Error handling for blank submission and username max limit
            if (usernameToSubmit.length() == 0) {
                Toast.makeText(this, "Username should not be blank", Toast.LENGTH_LONG).show();
                return;
            } else if (usernameToSubmit.length() > 20) {
                Toast.makeText(this, "Username length should be less than 20", Toast.LENGTH_LONG).show();
                return;
            }

            if (usernames.contains(usernameToSubmit)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
                return;
            }

            if (commentToSubmit.length() == 0) {
                Toast.makeText(this, "Comment should not be blank", Toast.LENGTH_LONG).show();
                return;
            }

            submitButton.setEnabled(true);
            myDatabase.execSQL("INSERT INTO " + getTitleSQL + " (comment, username) VALUES " + "('" + commentToSubmit + "'" + ", " + "'" + usernameToSubmit + "')");
            clickSound.start();
            onBackPressed();
        });

    }

}



// ARCHIVED for editing existing comments
// Handle submit comment button
//        submitButton.setOnClickListener(view -> {
//            clickSound.start();
//            String commentToSubmit = commentField.getText().toString();
//            IndividualMenuActivity.comments.set(commentId, commentToSubmit);
//            IndividualMenuActivity.arrayAdapter.notifyDataSetChanged();
//
//            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.zappycode.notes", Context.MODE_PRIVATE);
//            HashSet<String> set = new HashSet<>(IndividualMenuActivity.comments);
//            sharedPreferences.edit().putStringSet("comments", set).apply();
//            onBackPressed();
//        });
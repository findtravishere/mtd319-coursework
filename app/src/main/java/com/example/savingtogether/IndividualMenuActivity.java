package com.example.savingtogether;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IndividualMenuActivity extends AppCompatActivity {

    TextView topicText;

    Button startVideoButton, discussionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_menu);
        topicText = findViewById(R.id.topicTextView);
        startVideoButton = findViewById(R.id.startVideoButton);
        discussionButton = findViewById(R.id.discussionButton);

        // Retrieve data from the list
        Intent intent = getIntent();
        String title = intent.getStringExtra("TopicName");
        topicText.setText(title);
        System.out.println("hello" + title);

        // Handle video button
        startVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });


    }
    private void openActivity() {
        Intent intent = new Intent(this, VideoActivity.class);
        String title = (String) topicText.getText();
        intent.putExtra("TopicName", title);
        startActivity(intent);
        System.out.println("asaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + title);
    }
}
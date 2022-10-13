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
    Button menuButton;
//    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Store in persistent storage
//        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.savingtogether", Context.MODE_PRIVATE);
//        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("topics", null);
//        if (set == null) {
//
//        } else {
//            topics = new ArrayList(set);
//        }

        // Example topics for the app
        topics.add("How to save money");
        topics.add("How to cook at home");
        topics.add("How to eat healthy");
        topics.add("Finance tips for beginners");
        topics.add("Inflation advice");
        topics.add("Cheap groceries");
        topics.add("Learning investing");
        topics.add("ETFs 101");
        topics.add("CPF Overview");
        topics.add("Best credit cards");
        topics.add("Take public transport");
        topics.add("How Medisave will help you");
        topics.add("How to BTO");
        topics.add("Ride sharing recommendations");
        topics.add("Singapore Savings Bonds");
        topics.add("Career advice");
        topics.add("University subsidies");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        // Ids for components
        listView = findViewById(R.id.listViewSearch);
        menuButton = findViewById(R.id.menuButton);

        // Sort topics alphabetically
        Collections.sort(topics);

        // Setup
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);
        listView.setAdapter(arrayAdapter);
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);


        // Dynmically switch to selected topic
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

    }

    // Open screen for intro page
    private void openMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    // Menu add topic

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.add_topic, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        super.onOptionsItemSelected(item);
//        if (item.getItemId() == R.id.add_topic) {
//            Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        return false;
//    }
}
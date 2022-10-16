package com.example.savingtogether;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class IndividualPostActivity extends AppCompatActivity {

    TextView individualPostTitle;
    EditText individualPost;
    ImageView readPhoto;
    Button backIndividualPostButton;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_post);

        // Retrieve data from the list
        individualPostTitle = findViewById(R.id.individualPostTitle);
        individualPost = findViewById(R.id.individualPost);
        Intent intent = getIntent();
        String title = intent.getStringExtra("ForumTitle");
        String post = intent.getStringExtra("ForumPost");
        individualPostTitle.setText(title);
        individualPost.setText(post);

        // Read image from firebase
        readPhoto = findViewById(R.id.readPhoto);
        String photoTitle = title;
        String getPhotoTitle = photoTitle.replaceAll("\\s+","").toLowerCase();
        System.out.println("The photo title is" + getPhotoTitle);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + getPhotoTitle + ".jpg");
        System.out.println(storageReference);
        try {
            File localFile = File.createTempFile(getPhotoTitle, ".jpg");
            System.out.println("local file outside function" + localFile);
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("local file in function" + localFile);
                            System.out.println("i worked");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            readPhoto.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(IndividualPostActivity.this, "Failed to retrieve", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Back to all posts
        backIndividualPostButton = findViewById(R.id.backIndividualPostButton);
        backIndividualPostButton.setOnClickListener(view -> {
            onBackPressed();
        });

    }
}
package com.example.savingtogether;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 101;
    EditText postTitle, editPost;
    Button submitForumButton, cameraButton, galleryButton;
    ImageView imageView;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> uploadPhoto;
    Uri imageURI;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Sound setup
        MediaPlayer clickSound = MediaPlayer.create(this, R.raw.click);
        postTitle = findViewById(R.id.forumPostTitle);
        editPost = findViewById(R.id.forumPost);

        // Take camera image setup
        imageView = findViewById(R.id.imageView);
        // Compare imageView with tag
        imageView.setTag("0");
        cameraButton = findViewById(R.id.cameraButton);
        galleryButton = findViewById(R.id.galleryButton);

        cameraButton.setOnClickListener(view -> {
            askCameraPermission();
        });

        // upload photo immediately after taking an image
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK && result.getData() != null) {
            Bundle bundle = result.getData().getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "val", null);
            imageURI = Uri.parse(path);
            imageView.setImageURI(imageURI);
            imageView.setImageBitmap(bitmap);
            imageView.setTag("1");
        }
        }
        });


        // Upload image from gallery setup
        uploadPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    System.out.println("THE IMAGE URI:" + result);
                    imageURI = result;
                    imageView.setImageURI(imageURI);
                    imageView.setTag("1");
                } else {
                    if (imageURI != null) {
                        System.out.println("no photo selected");
                        System.out.println("existing URI: " + imageURI);
                        imageView.setImageURI(imageURI);
                        imageView.setTag("1");
                    } else {
                        System.out.println("no photo selected");
                        System.out.println("existing URI: " + imageURI);
                    }

                }

            }
        });

        galleryButton.setOnClickListener(view -> {
            uploadPhoto.launch("image/*");
        });

        // Upload image to firebase
        storage =  FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Handle submit comment button
        submitForumButton = findViewById(R.id.submitForumButton);
        submitForumButton.setOnClickListener(view -> {
            SQLiteDatabase forumDatabase = this.openOrCreateDatabase("Forum", MODE_PRIVATE, null );
            String titleToSubmit = postTitle.getText().toString();
            String postToSubmit = editPost.getText().toString();

            // Error handling for blank submission and title max limit
            if (titleToSubmit.length() == 0) {
                Toast.makeText(this, "Title should not be blank", Toast.LENGTH_LONG).show();
                return;
            } else if (titleToSubmit.length() > 30) {
                Toast.makeText(this, "Title length should be less than 30", Toast.LENGTH_LONG).show();
                return;
            }

            if (postToSubmit.length() == 0) {
                Toast.makeText(this, "Post should not be blank", Toast.LENGTH_LONG).show();
                return;
            }

            // Error handling for empty photo
            if (imageView.getTag() == "0") {
                System.out.println("HAHAHAHAHAHAHAHAH");
                Toast.makeText(this, "Please upload a photo", Toast.LENGTH_LONG).show();
                return;
            }

            // Handle image type for upload
            submitForumButton.setEnabled(true);

            // for sqlite
            forumDatabase.execSQL("INSERT INTO forum" + " (title, post) VALUES " + "('" + titleToSubmit + "'" + ", " + "'" + postToSubmit + "')");

            // for firebase
            uploadPicture();

            // end
            clickSound.start();
            onBackPressed();
        });
    }


    // Check camera permissions
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle open camera using non-deprecated activityResult
    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activityResultLauncher.launch(camera);
    }

    private void uploadPicture() {
        String photoTitle = postTitle.getText().toString();
        String getPhotoTitle = photoTitle.replaceAll("\\s+","").toLowerCase(); // lowercase and trim whitespace to match path
        StorageReference riversRef = storageReference.child("images/" + getPhotoTitle + ".jpg");
        riversRef.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                        System.out.println("success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                        System.out.println("fail");
                    }
                });
    }
}


// ARCHIVED



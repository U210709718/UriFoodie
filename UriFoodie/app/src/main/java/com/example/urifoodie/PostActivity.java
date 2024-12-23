package com.example.urifoodie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

public class PostActivity extends AppCompatActivity {

    private EditText postTextInput, recipeTextInput;
//    private ImageView capturedImageView;
    private Button submitPostButton, captureImageButton;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Initialize UI components
        postTextInput = findViewById(R.id.postTextInput);
//        capturedImageView = findViewById(R.id.capturedImageView);
        recipeTextInput = findViewById(R.id.recipie);
        submitPostButton = findViewById(R.id.submitPostButton);
        captureImageButton = findViewById(R.id.captureImageButton);
        submitPostButton.setOnClickListener(v -> submitPost());


        // Set up listeners
//        captureImageButton.setOnClickListener(v -> dispatchTakePictureIntent());
//        submitPostButton.setOnClickListener(v -> submitPost());
    }

    private void submitPost() {
        String postText = postTextInput.getText().toString().trim();
        String recipeText = recipeTextInput.getText().toString().trim();
        if (postText.isEmpty()) {
            Toast.makeText(this, "Post text cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        savePostToFirebase(postText, recipeText);
    }
    private void savePostToFirebase(String postText, String recipeText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Use the correct constructor
            Post newPost = new Post(
                    user.getDisplayName(), // username
                    postText,              // postText
                    recipeText,            // recipeText
                    Timestamp.now()        // current timestamp
            );

            db.collection("Users").document(user.getUid()).collection("Posts").add(newPost)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Return to the previous activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to submit post.", Toast.LENGTH_SHORT).show();
                        Log.e("PostActivity", "Error submitting post", e);
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } else {
//            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            capturedImageView.setImageBitmap(imageBitmap);
//        }
//    }

//    private void submitPost() {
//        String postText = postTextInput.getText().toString().trim();
//        if (postText.isEmpty()) {
//            Toast.makeText(this, "Post text cannot be empty.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String encodedImage = encodeImage();
//        savePostToFirebase(postText, encodedImage);
//    }

//    private String encodeImage() {
//        if (capturedImageView.getDrawable() instanceof BitmapDrawable) {
//            Bitmap bitmap = ((BitmapDrawable) capturedImageView.getDrawable()).getBitmap();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            return Base64.encodeToString(byteArray, Base64.DEFAULT);
//        }
//        return "";
//    }

//    private void savePostToFirebase(String postText, String encodedImage) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null) {
//            Post newPost = new Post(
//                    user.getDisplayName(), // username
//                    postText,              // postText
//                    recipeTextInput.getText().toString(), // recipeText
//                    encodedImage,          // imageUrl
//                    Timestamp.now(),       // timestamp
//                    user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "" // userProfilePicUrl
//            );
//
//            db.collection("Users").document(user.getUid()).collection("Posts").add(newPost)
//                    .addOnSuccessListener(documentReference -> {
//                        Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
//                        finish(); // Return to the previous activity
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, "Failed to submit post.", Toast.LENGTH_SHORT).show();
//                        Log.e("PostActivity", "Error submitting post", e);
//                    });
//        } else {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
//        }
//    }
}
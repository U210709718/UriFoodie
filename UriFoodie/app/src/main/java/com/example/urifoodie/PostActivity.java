package com.example.urifoodie;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private EditText postTextInput, recipeTextInput;
    private Button submitPostButton, captureImageButton;


    private Uri photoUri; // Store the URI of the photo
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap capturedImageBitmap; // Store the captured photo
    private ImageView capturedImageView; // To display the image




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Request camera permission at runtime if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }

        // Initialize UI components
        postTextInput = findViewById(R.id.postTextInput);
        recipeTextInput = findViewById(R.id.recipie);

        capturedImageView = findViewById(R.id.capturedImageView); // Add this line
        submitPostButton = findViewById(R.id.submitPostButton);
        captureImageButton = findViewById(R.id.captureImageButton);

        captureImageButton.setOnClickListener(v -> dispatchTakePictureIntent());
        submitPostButton.setOnClickListener(v -> submitPost());


        // Set up listeners
//        captureImageButton.setOnClickListener(v -> dispatchTakePictureIntent());
//        submitPostButton.setOnClickListener(v -> submitPost());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(); // Create a temporary file for the image
            } catch (IOException ex) {
                Log.e("PostActivity", "Error creating image file: ", ex);
                Toast.makeText(this, "Error creating image file!", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "com.example.urifoodie.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this, "Could not create file for the image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No camera app available!", Toast.LENGTH_SHORT).show();
        }
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call the superclass implementation

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied. Cannot open camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Display the photo in the ImageView using the Uri
            capturedImageView.setImageURI(photoUri);
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
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
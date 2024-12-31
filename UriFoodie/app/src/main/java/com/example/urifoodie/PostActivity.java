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
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;

import okhttp3.Request;
import okhttp3.RequestBody;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;


public class PostActivity extends AppCompatActivity {

    private EditText postTextInput, recipeTextInput;
    private Button submitPostButton, captureImageButton;


    private Uri photoUri; // Store the URI of the photo
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap capturedImageBitmap; // Store the captured photo
    private ImageView capturedImageView; // To display the image

    private File currentImageFile; // Added this var as a member variable



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
        recipeTextInput = findViewById(R.id.recipe);

        capturedImageView = findViewById(R.id.capturedImageView); // Add this line
        submitPostButton = findViewById(R.id.submitPostButton);
        captureImageButton = findViewById(R.id.captureImageButton);

        captureImageButton.setOnClickListener(v -> dispatchTakePictureIntent());
        submitPostButton.setOnClickListener(v -> submitPost());

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        File photoFile = null;
            try{
                currentImageFile = createImageFile(); // Create a temporary file for the image
                if (currentImageFile != null) {
                    photoUri = FileProvider.getUriForFile(this, "com.example.urifoodie.fileprovider", currentImageFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(this, "Could not create file for the image.", Toast.LENGTH_SHORT).show();
                }
            }catch (IOException ex) {
                Log.e("PostActivity", "Error creating image file: ", ex);
                Toast.makeText(this, "Error creating image file!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No camera app available!", Toast.LENGTH_SHORT).show();
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Get the directory for the app's private pictures directory.
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create an image file in the specified directory
        File imageFile = new File(storageDir, imageFileName + ".jpg");

        // Create the image file. If the file already exists, this method will not override it.
        if (!imageFile.exists()) {
            boolean created = imageFile.createNewFile();
            if (!created) {
                Log.e("Imgur Upload", "Failed to create the file: " + imageFile.getAbsolutePath());
                throw new IOException("Failed to create file: " + imageFile.getAbsolutePath());
            }
        }

        return imageFile;
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
            capturedImageView.setImageURI(photoUri);
            Log.d("UploadImage", "Attempting to upload image to Imgur");

            if (currentImageFile != null) {
                // Provide the ImageUploadCallback to handle the image URL after upload
                uploadImageToImgur(currentImageFile, imageUrl -> {
                    Log.d("Imgur Upload", "Image URL: " + imageUrl);
                    Toast.makeText(PostActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.e("Imgur Upload", "Current image file is null.");
            }
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();

        }
    }

    private void submitPost() {
        String postText = postTextInput.getText().toString().trim();
        String recipeText = recipeTextInput.getText().toString().trim();

        if (postText.isEmpty() || recipeText.isEmpty()) {
            Toast.makeText(this, "Post text and recipe text cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentImageFile == null) {
            Toast.makeText(this, "Please capture an image before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Imgur and then save the post to Firebase
        uploadImageToImgur(currentImageFile, imageUrl -> savePostToFirebase(postText, recipeText, imageUrl));
    }

    private void savePostToFirebase(String postText, String recipeText, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();


            db.collection("Users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");
                            if (username == null || username.isEmpty()) {
                                username = "Anonymous";
                            }


                            Post newPost = new Post(
                                    username,
                                    postText,
                                    recipeText,
                                    Timestamp.now(),
                                    imageUrl
                            );

                            db.collection("Users").document(userId).collection("Posts").add(newPost)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(PostActivity.this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(PostActivity.this, "Failed to submit post.", Toast.LENGTH_SHORT).show();
                                        Log.e("Firebase Upload", "Error submitting post", e);
                                    });

                        } else {
                            Toast.makeText(this, "User document not found!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to fetch username from Firestore.", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore Error", "Error fetching user document", e);
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadImageToImgur(File imageFile, ImageUploadCallback callback) {
        if (!imageFile.exists()) {
            Log.e("Imgur Upload", "Image file does not exist: " + imageFile.getAbsolutePath());
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
                .writeTimeout(30, TimeUnit.SECONDS)  // Write timeout
                .readTimeout(30, TimeUnit.SECONDS)   // Read timeout
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(), RequestBody.create(imageFile, okhttp3.MediaType.parse("image/jpeg")))
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/upload")
                .header("Authorization", "Client-ID " + "5f518c75ddb3422") // Replace with your actual Client ID
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Imgur Upload", "Failed to upload image: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(PostActivity.this, "Image upload failed!", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String imageUrl = jsonObject.getJSONObject("data").getString("link");

                        Log.d("Imgur Upload", "Image uploaded successfully: " + imageUrl);

                        // Pass the imageUrl to the callback
                        runOnUiThread(() -> callback.onSuccess(imageUrl));
                    } catch (JSONException e) {
                        Log.e("Imgur Upload", "JSON parsing error: " + e.getMessage());
                        runOnUiThread(() -> Toast.makeText(PostActivity.this, "Error parsing Imgur response.", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Log.e("Imgur Upload", "Server responded with: " + response.code());
                    runOnUiThread(() -> Toast.makeText(PostActivity.this, "Image upload failed: " + response.code(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }



    /* OLD METHOD
    private void savePostWithImageUrl(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String postText = postTextInput.getText().toString().trim();
        String recipeText = recipeTextInput.getText().toString().trim();

        if (user != null) {
            Post newPost = new Post(
                    user.getDisplayName(),
                    postText,
                    recipeText,
                    Timestamp.now(), imageUrl
            );

            db.collection("Users").document(user.getUid()).collection("Posts").add(newPost)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(PostActivity.this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PostActivity.this, "Post upload failed.", Toast.LENGTH_SHORT).show();
                    });
        }
    }*/

    // interface for image upload success
    interface ImageUploadCallback {
        void onSuccess(String imageUrl);
    }


    /*private void savePostToFirebase(String postText, String recipeText) {
=======
    private void submitPost() {
        String postText = postTextInput.getText().toString().trim();
        String recipeText = recipeTextInput.getText().toString().trim();

        if (postText.isEmpty() || recipeText.isEmpty()) {
            Toast.makeText(this, "Post text and recipe text cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentImageFile == null) {
            Toast.makeText(this, "Please capture an image before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Imgur and then save the post to Firebase
        uploadImageToImgur(currentImageFile, imageUrl -> savePostToFirebase(postText, recipeText, imageUrl));
    }

    private void savePostToFirebase(String postText, String recipeText, String imageUrl) {
>>>>>>> main
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Post newPost = new Post(
                    user.getDisplayName(),
                    postText,
                    recipeText,
                    Timestamp.now(),
                    imageUrl
            );

            db.collection("Users").document(user.getUid()).collection("Posts").add(newPost)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(PostActivity.this, "Post submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Return to Home page
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PostActivity.this, "Failed to submit post.", Toast.LENGTH_SHORT).show();
                        Log.e("Firebase Upload", "Error submitting post", e);
                    });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
<<<<<<< HEAD
    } Old version of this method!!*/

}
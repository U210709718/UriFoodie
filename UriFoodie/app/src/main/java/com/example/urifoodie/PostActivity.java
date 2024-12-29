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
<<<<<<< HEAD
import androidx.core.content.ContextCompat;
=======
>>>>>>> main
import androidx.core.content.FileProvider;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import android.Manifest;

<<<<<<< HEAD
=======

import org.json.JSONException;
>>>>>>> main
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

<<<<<<< HEAD
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
=======
import okhttp3.MultipartBody;

import okhttp3.Request;
import okhttp3.RequestBody;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
>>>>>>> main

public class PostActivity extends AppCompatActivity {

    private EditText postTextInput, recipeTextInput;
    private Button submitPostButton, captureImageButton;
<<<<<<< HEAD
    private ImageView capturedImageView;

    private Uri photoUri; //Store the URI of the photo
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap capturedImageBitmap;
=======


    private Uri photoUri; // Store the URI of the photo
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap capturedImageBitmap; // Store the captured photo
    private ImageView capturedImageView; // To display the image

    private File currentImageFile; // Added this var as a member variable
>>>>>>> main


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

<<<<<<< HEAD
       /* // Request camera permission at runtime if not granted
=======
        // Request camera permission at runtime if not granted
>>>>>>> main
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
<<<<<<< HEAD
        }*/

        initializeUIComponents();
        requestCameraPermission();
    }

    private void initializeUIComponents(){
        postTextInput = findViewById(R.id.postTextInput);
        recipeTextInput = findViewById(R.id.recipie);
        capturedImageView = findViewById(R.id.capturedImageView);
=======
        }

        // Initialize UI components
        postTextInput = findViewById(R.id.postTextInput);
        recipeTextInput = findViewById(R.id.recipie);

        capturedImageView = findViewById(R.id.capturedImageView); // Add this line
>>>>>>> main
        submitPostButton = findViewById(R.id.submitPostButton);
        captureImageButton = findViewById(R.id.captureImageButton);

        captureImageButton.setOnClickListener(v -> dispatchTakePictureIntent());
        submitPostButton.setOnClickListener(v -> submitPost());
<<<<<<< HEAD
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

=======

    }

>>>>>>> main
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
<<<<<<< HEAD
            //File photoFile = null; --old code...
            try {
                File photoFile = createImageFile();
                //photoFile = createImageFile(); // Create a temporary file for the image--OLD CODE!!
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, "com.example.urifoodie.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                showToast("Error creating image file!");
                Log.e("PostActivity", "Error creating image file: ", ex);
                //Toast.makeText(this, "Error creating image file!", Toast.LENGTH_SHORT).show(); --OLD CODE!!--
            }

            /*if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, "com.example.urifoodie.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Toast.makeText(this, "Could not create file for the image.", Toast.LENGTH_SHORT).show();
            } OLD CODE!!!*/
        } else {
            showToast("No camera app available!");
            //Toast.makeText(this, "No camera app available!", Toast.LENGTH_SHORT).show(); -- OLD CODE!!!--
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


=======
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

>>>>>>> main
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
<<<<<<< HEAD
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

=======

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
>>>>>>> main



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call the superclass implementation

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
<<<<<<< HEAD
                showToast("Camera permission granted!");
            } else {
                showToast("Camera permission denied. Cannot open camera.");
=======
                Toast.makeText(this, "Camera permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied. Cannot open camera.", Toast.LENGTH_SHORT).show();
>>>>>>> main
            }
        }
    }

<<<<<<< HEAD

=======
>>>>>>> main
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
<<<<<<< HEAD
            // Display the photo in the ImageView using the Uri
            capturedImageView.setImageURI(photoUri);
        } else {
            showToast("Picture wasn't taken!");
=======
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
>>>>>>> main
        }
    }


<<<<<<< HEAD
    //retrofit setup
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", "Client-ID {5f518c75ddb3422}")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            })
            .build();


    /*OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Client-ID {5f518c75ddb3422}")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            })
            .build();  OLD VERSION OF THIS METHOD!!!*/

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.imgur.com/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Define an interface for your API calls
    public interface ImgurAPI {
        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
    }

    private void uploadImageToImgur(File imageFile, String postText, String recipeText) {
        ImgurAPI api = retrofit.create(ImgurAPI.class);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        Call<ResponseBody> call = api.uploadImage(body);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String imgurUrl = new JSONObject(response.body().string()).getJSONObject("data").getString("link");
                        savePostToFirebase(postText, recipeText, imgurUrl); // store the url inside firebase firestore
                    } catch (Exception e) {
                        Log.e("ImgurAPI", "Error parsing response", e);
                        showToast("Error parsing Imgur response!...");
                    }
                } else {
                    showToast("Image upload failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Image upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void submitPost() {
        String postText = postTextInput.getText().toString().trim();
        String recipeText = recipeTextInput.getText().toString().trim();
        if (postText.isEmpty()) {
            showToast("Post text cannot be empty.");
            return;
        }
        if (photoUri != null) {
            File imageFile = new File(photoUri.getPath());
            uploadImageToImgur(imageFile, postText, recipeText); // start to uploading on Imgur
        } else {
            showToast("Please capture an image before submitting the post.");
        }
    }


    private void savePostToFirebase(String postText, String recipeText, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Post newPost = new Post(
                    user.getDisplayName(),
                    postText,
                    recipeText,
                    imageUrl,
                    Timestamp.now(),
                    user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "" //user profile pic URL
            );

            db.collection("Users").document(user.getUid()).collection("Posts").add(newPost)
                    .addOnSuccessListener(documentReference -> {
                        showToast("Post submitted successfully!");
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        showToast("Failed to submit post.");
                        Log.e("PostActivity", "Error submitting post", e);
                    });
        } else {
            showToast("User not logged in!");
        }
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
=======
    }
    private void uploadImageToImgur(File imageFile, ImageUploadCallback callback) {
        if (!imageFile.exists()) {
            Log.e("Imgur Upload", "Image file does not exist: " + imageFile.getAbsolutePath());
            return;
        }

        OkHttpClient client = new OkHttpClient();
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
>>>>>>> main




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
    }

    // interface for image upload success
    interface ImageUploadCallback {
        void onSuccess(String imageUrl);
    }


}
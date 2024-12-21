package com.example.urifoodie;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText newPostInput;
    private ImageView submitPostButton, cancelPostButton;
    private ImageView capturedImageView; // Reference to an ImageView where the captured image will be displayed
    RecyclerView recyclerView;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 101; // Unique identifier for the camera permission request
    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Initialize RecyclerView and other UI components here...
        recyclerView = view.findViewById(R.id.postList); // Make sure you have a RecyclerView with this ID in your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Find the icons
        ImageView notificationsIcon = view.findViewById(R.id.notificationsIcon);
        ImageView favoritesIcon = view.findViewById(R.id.favoritesIcon);
        ImageView messagesIcon = view.findViewById(R.id.messagesIcon);
        ImageView menuIcon = view.findViewById(R.id.menuIcon);

        // Set onClick listeners for icons
        notificationsIcon.setOnClickListener(this::handleNotificationsClick);
        favoritesIcon.setOnClickListener(this::handleFavoritesClick);
        messagesIcon.setOnClickListener(this::handleMessagesClick);
        menuIcon.setOnClickListener(this::handleMenuClick);

        newPostInput = view.findViewById(R.id.newPostInput);
        submitPostButton = view.findViewById(R.id.submitPostButton);
        cancelPostButton = view.findViewById(R.id.cancelPostButton);

        // Listener for submit button
        submitPostButton.setOnClickListener(v -> submitPost());

        // Listener for cancel button
        cancelPostButton.setOnClickListener(v -> clearPostInput());

        ImageView cameraButton = view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        capturedImageView = view.findViewById(R.id.capturedImageView); // Ensure you have this ImageView in your layout


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchPosts(); // Call this here to load posts as soon as the view is created
    }

    private void handleMenuClick(View view) {
        openFragment(new TripleLinePageFragment());
    }

    private void handleMessagesClick(View view) {
        openFragment(new MessengerPageFragment());
    }

    private void handleFavoritesClick(View view) {
        openFragment(new FavoritePageFragment());
    }

    private void handleNotificationsClick(View view) {
        openFragment(new NotificationsPageFragment());
    }

    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)  // Allows navigation back to HomePage
                .commit();
    }
    private void checkAndUpdateUsername(FirebaseUser user) {
        if (user != null && (user.getDisplayName() == null || user.getDisplayName().isEmpty())) {
            // Prompt user to input their username or set it programmatically
            String newUsername = "seham22"; // Example, or fetch this from user input
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();

            user.updateProfile(profileUpdate)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ProfileUpdate", "User profile updated.");
                            Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("ProfileUpdate", "Failed to update profile.");
                            Toast.makeText(getContext(), "Failed to update profile.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateProfile(String newUsername) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("ProfileUpdate", "User profile updated.");
                        }
                    });
        }
    }

    private void submitPost() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("SubmitPost", "Username: " + user.getDisplayName()); // Log to check username
            String postText = newPostInput.getText().toString().trim();
            if (postText.isEmpty()) {
                Toast.makeText(getContext(), "Post text cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }
            String imageBase64 = getCurrentEncodedImage();
            createPost(user.getUid(), user.getDisplayName(), postText, imageBase64);
            clearPostInput();
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }



    private void createPost(String userId, String username, String postText, String imageBase64) {
        Post newPost = new Post(userId, username, postText, imageBase64);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).collection("Posts").add(newPost)
                .addOnSuccessListener(documentReference -> Log.d("PostCreation", "Post added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("PostCreation", "Error adding post", e));
    }



    /*private void continuePostSubmission(FirebaseUser user) {
        String userId = user.getUid();
        String username = user.getDisplayName();
        String postText = newPostInput.getText().toString().trim();
        if (postText.isEmpty()) {
            Toast.makeText(getContext(), "Post text cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        createPost(userId, username, postText);
        clearPostInput();
    }*/



    private void clearPostInput() {
        newPostInput.setText("");  // Clear the post input field
    }
    private void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();
        db.collectionGroup("Posts") // This fetches all documents inside collections named "Posts" across all users
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            posts.add(document.toObject(Post.class));
                        }
                        updateRecyclerView(posts);
                    } else {
                        Log.w("HomePageFragment", "Error getting posts.", task.getException());
                    }
                });
    }

    private void updateRecyclerView(List<Post> posts) {
        if (recyclerView != null) {
            PostAdapter adapter = new PostAdapter(posts);
            recyclerView.setAdapter(adapter);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(getContext(), "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImageView.setImageBitmap(imageBitmap);
            // Log or toast message to confirm image capture
            Log.d("Camera", "Image captured and set to view");
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getContext(), "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Now you can store this in your Post object
        // For example: newPost.setImageBase64(encodedImage);
    }
    private String getCurrentEncodedImage() {
        if (capturedImageView == null || capturedImageView.getDrawable() == null) {
            return ""; // Return an empty string if there is no image set
        }

        // Get the bitmap from the ImageView
        capturedImageView.setDrawingCacheEnabled(true);
        capturedImageView.buildDrawingCache();
        Bitmap bitmap = capturedImageView.getDrawingCache();

        // Convert the Bitmap to a Base64 string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


}
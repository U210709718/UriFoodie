package com.example.urifoodie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ProfilePageFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private String username;

    TextView userNameTextView , postsCountTextView;;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    RecyclerView profileGallery;
    ImageAdapter imageAdapter;
    ArrayList<String> imageUrls = new ArrayList<>();


    public ProfilePageFragment() {
        // Required empty public constructor
    }
    public static ProfilePageFragment newInstance(String username) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        userNameTextView = view.findViewById(R.id.userName);
        postsCountTextView = view.findViewById(R.id.postsCount);
        profileGallery = view.findViewById(R.id.profileGallery);

        profileGallery.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Grid for images
        imageAdapter = new ImageAdapter(imageUrls);
        profileGallery.setAdapter(imageAdapter);

        if (currentUser != null) {
            String userId = currentUser.getUid();
            getUsernameFromFirestore(userId);
            getPostsCountFromFirestore(userId);
            loadImagesFromFirestore(userId);
        }
        return view;
    }
    private void loadImagesFromFirestore(String userId) {
        firestore.collection("Users").document(userId).collection("Posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    imageUrls.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String imageUrl = document.getString("imageUrl");
                        if (imageUrl != null) {
                            imageUrls.add(imageUrl);
                        }
                    }

                    imageAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("TAG", "Error loading images: " + e.getMessage()));
    }

    public void getUsernameFromFirestore(String userId) {
        firestore.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        userNameTextView.setText(username);
                    } else {
                        Log.w("TAG", "User data not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error retrieving username: " + e.getMessage());
                });
    }
    public void getPostsCountFromFirestore(String userId) {
        firestore.collection("Users").document(userId).collection("Posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int postsCount = queryDocumentSnapshots.size(); // Count the number of documents
                    postsCountTextView.setText(String.valueOf(postsCount));
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "Error retrieving posts count: " + e.getMessage());
                });
    }


}
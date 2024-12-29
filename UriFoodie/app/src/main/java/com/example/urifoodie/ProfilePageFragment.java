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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePageFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private String username;

    TextView userNameTextView , postsCountTextView;;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();


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
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        userNameTextView = view.findViewById(R.id.userName);
        postsCountTextView = view.findViewById(R.id.postsCount);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            getUsernameFromFirestore(userId);
            getPostsCountFromFirestore(userId);
        }
        return view;
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
package com.example.urifoodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageFragment extends Fragment {

    private FirebaseFirestore firestore;
    private RecyclerView postList;
    private Button submitPostButton;
    private List<Post> posts = new ArrayList<>();
    private ImageView notificationsIcon, favoritesIcon, messagesIcon, menuIcon;
    private PostListRecyclerViewAdapter postAdapter;


    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance(String param1, String param2) {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Initialize UI components
        postList = view.findViewById(R.id.postList);
        postList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up adapter
        postAdapter = new PostListRecyclerViewAdapter(posts);
        postList.setAdapter(postAdapter);

        // Set up other UI components
        setupUI(view);
        // Set up listeners
        setupListeners();



        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchPosts(); // Load posts when the fragment view is created
    }
    // Helper method to open a new fragment
    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    /**
     * Sets up click listeners for UI components.
     */
    private void setupListeners() {
        submitPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        });

        notificationsIcon.setOnClickListener(v -> openFragment(new NotificationsPageFragment()));
        favoritesIcon.setOnClickListener(v -> openFragment(new FavoritePageFragment()));
        messagesIcon.setOnClickListener(v -> openFragment(new MessengerPageFragment()));
        menuIcon.setOnClickListener(v -> openFragment(new TripleLinePageFragment()));
    }
    private void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Post> posts = new ArrayList<>();

        db.collectionGroup("Posts") // Fetch all documents in "Posts" collections
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);
                            if (post != null) {
                                posts.add(post);
                            }
                        }
                        updateRecyclerView(posts);
                    } else {
                        Log.w("HomePageFragment", "Error getting posts.", task.getException());
                        Toast.makeText(getContext(), "Failed to load posts.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Updates the RecyclerView with the list of posts.
     *
     * @param posts List of posts to display.
     */
    private void updateRecyclerView(List<Post> posts) {
        PostListRecyclerViewAdapter adapter = new PostListRecyclerViewAdapter(posts);
        postList.setAdapter(adapter);
    }

    private void setupUI(View view) {
        // Set click listeners for the icons
        notificationsIcon = view.findViewById(R.id.notificationsIcon);
        favoritesIcon = view.findViewById(R.id.favoritesIcon);
        messagesIcon = view.findViewById(R.id.messagesIcon);
        menuIcon = view.findViewById(R.id.menuIcon);

        notificationsIcon.setOnClickListener(v -> openFragment(new NotificationsPageFragment()));
        favoritesIcon.setOnClickListener(v -> openFragment(new FavoritePageFragment()));
        messagesIcon.setOnClickListener(v -> openFragment(new MessengerPageFragment()));
        menuIcon.setOnClickListener(v -> openFragment(new TripleLinePageFragment()));

    }


}

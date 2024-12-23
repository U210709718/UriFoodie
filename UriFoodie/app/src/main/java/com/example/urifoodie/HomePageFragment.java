package com.example.urifoodie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button submitPostButton;
    private ImageView notificationsIcon, favoritesIcon, messagesIcon, menuIcon;

    public HomePageFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Initialize UI components
        recyclerView = view.findViewById(R.id.postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        submitPostButton = view.findViewById(R.id.btnPost);
        notificationsIcon = view.findViewById(R.id.notificationsIcon);
        favoritesIcon = view.findViewById(R.id.favoritesIcon);
        messagesIcon = view.findViewById(R.id.messagesIcon);
        menuIcon = view.findViewById(R.id.menuIcon);

        // Set up listeners
        setupListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchPosts(); // Load posts when the fragment view is created
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

    /**
     * Opens a new fragment.
     *
     * @param fragment The fragment to open.
     */
    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Fetches posts from Firestore and updates the RecyclerView.
     */
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
        PostAdapter adapter = new PostAdapter(posts);
        recyclerView.setAdapter(adapter);
    }
}

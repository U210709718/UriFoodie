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

        // Set up other UI components
        setupUI(view);
        // Set up listeners
        setupListeners();

        // Initialize UI components
        postList = view.findViewById(R.id.postList);
        postList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up adapter
        postAdapter = new PostListRecyclerViewAdapter(posts);
        postList.setAdapter(postAdapter);


        submitPostButton = view.findViewById(R.id.btnPost);
        submitPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);

            }
        });
        
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchPosts(); // Load posts when the fragment view is created
    }
    // Helper method to open a new fragment
    private void openFragment(Fragment fragment) {
        Log.d("HomePageFragment", "Opening fragment: " + fragment.getClass().getSimpleName());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Sets up click listeners for UI components.
     */
    private void setupListeners() {

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
        if (menuIcon == null) {
            Log.e("HomePageFragment", "menuIcon is null. Check your layout file.");
        }


        notificationsIcon.setOnClickListener(v -> openFragment(new NotificationsPageFragment()));
        favoritesIcon.setOnClickListener(v -> openFragment(new FavoritePageFragment()));
        messagesIcon.setOnClickListener(v -> openFragment(new MessengerPageFragment()));
        menuIcon.setOnClickListener(v -> openFragment(new TripleLinePageFragment()));

    }


}

/*  @Override      onCreateView  OLD
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Set up other UI components
        setupUI(view);

        // Find the UI elements
        ImageView cameraButton = view.findViewById(R.id.cameraButton);
        EditText urlInput = view.findViewById(R.id.urlInput);
        ImageView submitPostButton = view.findViewById(R.id.submitPostButton);
        EditText newPostInput = view.findViewById(R.id.newPostInput);

        postList = view.findViewById(R.id.postList);
        postList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up adapter
        postAdapter = new PostListRecyclerViewAdapter(posts);
        postList.setAdapter(postAdapter);

        // Set the click listener for the camera button
        cameraButton.setOnClickListener(v -> {
            // Show the URL input field
            urlInput.setVisibility(View.VISIBLE);
        });

        // Set the click listener for the submit button
        submitPostButton.setOnClickListener(v -> {
            String postText = newPostInput.getText().toString();
            String imageUrl = urlInput.getText().toString(); // Get the URL from the input field

            if (postText.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a context!", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userId);

            userRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String username = documentSnapshot.getString("username");

                            CollectionReference postsRef = userRef.collection("posts");

                            Map<String, Object> post = new HashMap<>();
                            post.put("username", username);
                            post.put("text", postText);
                            post.put("imageUrl", imageUrl);
                            post.put("timestamp", FieldValue.serverTimestamp());

                            postsRef.add(post)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(getContext(), "The post successfully added!", Toast.LENGTH_SHORT).show();
                                        fetchPostsFromFirestore();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Post", "Error adding post", e);
                                    });
                        } else {
                            Log.d("Firestore", "User document does not exist!");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error getting user document", e);
                    });
        });

        // Fetch posts from Firestore
        fetchPostsFromFirestore();

        return view;
    }
    */

/*

 OLD fetchPostsFromFirestore
private void fetchPostsFromFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        CollectionReference postsRef = firestore.collection("Users").document(userId).collection("posts");

        postsRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    posts.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }

                    postAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error getting documents", e);
                });

    }  */

/*  /*private void loadPosts() {
        firestore.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING) // Sort posts by timestamp
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    postList = new ArrayList<>();
                    for (QuerySnapshot document : queryDocumentSnapshots) {
                        postList.add(document.getData());
                    }

                    // Set up adapter with fetched data
                    postAdapter = new PostRecyclerViewAdapter(getContext(), postList);
                    postListRecyclerView.setAdapter(postAdapter);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error loading posts: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }*/

/*private void setupUI(View view) {
    // Set click listeners for the icons
    ImageView notificationsIcon = view.findViewById(R.id.notificationsIcon);
    ImageView favoritesIcon = view.findViewById(R.id.favoritesIcon);
    ImageView messagesIcon = view.findViewById(R.id.messagesIcon);
    ImageView menuIcon = view.findViewById(R.id.menuIcon);

    notificationsIcon.setOnClickListener(v -> openFragment(new NotificationsPageFragment()));
    favoritesIcon.setOnClickListener(v -> openFragment(new FavoritePageFragment()));
    messagesIcon.setOnClickListener(v -> openFragment(new MessengerPageFragment()));
    menuIcon.setOnClickListener(v -> openFragment(new TripleLinePageFragment()));

}

// Helper method to open a new fragment
private void openFragment(Fragment fragment) {
    requireActivity().getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null) // Allows navigation back to HomePage
            .commit();
}  */
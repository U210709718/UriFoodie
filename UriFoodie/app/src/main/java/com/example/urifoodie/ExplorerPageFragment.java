package com.example.urifoodie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExplorerPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplorerPageFragment extends Fragment {

    private RecyclerView explorerGrid;
    private ExplorerAdapter explorerAdapter;
    private List<Post> postList;
    private String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExplorerPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExplorerPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExplorerPageFragment newInstance(String param1, String param2) {
        ExplorerPageFragment fragment = new ExplorerPageFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer_page, container, false);

        explorerGrid = view.findViewById(R.id.explorerGrid);
        explorerGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));

        postList = new ArrayList<>();
        explorerAdapter = new ExplorerAdapter(postList);
        explorerGrid.setAdapter(explorerAdapter);

        loadPosts();

        return view;
    }


    private void loadPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postList.clear(); // Clear existing posts
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageUrl = document.getString("imageUrl");
                            String username = document.getString("username");

                            // Ensure required fields are non-null
                            if (imageUrl != null && username != null) {
                                postList.add(new Post(username, null, null, null, imageUrl));
                            }
                        }

                        // Notify adapter about data change
                        explorerAdapter.notifyDataSetChanged();

                        Log.d("Firestore", "Posts loaded: " + postList.size());
                    } else {
                        Log.e("Firestore", "Error fetching posts", task.getException());
                    }
                });
    }







}
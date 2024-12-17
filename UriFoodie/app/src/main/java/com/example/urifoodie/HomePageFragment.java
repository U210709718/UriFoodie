package com.example.urifoodie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

        // Find the icons
        ImageView notificationsIcon = view.findViewById(R.id.notificationsIcon);
        ImageView favoritesIcon = view.findViewById(R.id.favoritesIcon);
        ImageView messagesIcon = view.findViewById(R.id.messagesIcon);
        ImageView menuIcon = view.findViewById(R.id.menuIcon);

        // Set click listener for Notifications Page
        notificationsIcon.setOnClickListener(v -> openFragment(new NotificationsPageFragment()));

        // Set click listener for Favorites Page
        favoritesIcon.setOnClickListener(v -> openFragment(new FavoritePageFragment()));

        // Set click listener for Messenger Page
        messagesIcon.setOnClickListener(v -> openFragment(new MessengerPageFragment()));

        // Set click listener for Triple Line Page (Menu)
        menuIcon.setOnClickListener(v -> openFragment(new TripleLinePageFragment()));

        return view;
    }

    // Helper method to open a new fragment
    private void openFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null) // Allows navigation back to HomePage
                .commit();
    }
}

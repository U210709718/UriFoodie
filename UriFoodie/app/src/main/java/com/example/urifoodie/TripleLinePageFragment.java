package com.example.urifoodie;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripleLinePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripleLinePageFragment extends Fragment {
    private Button btnLogout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TripleLinePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TripleLinePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripleLinePageFragment newInstance(String param1, String param2) {
        TripleLinePageFragment fragment = new TripleLinePageFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_triple_line_page, container, false);

        // Initialize the Log Out button
        btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout == null) {
            Log.e("TripleLinePageFragment", "btnLogout is null. Check your layout file.");
        }

        // Log Out Button Click Listener
        btnLogout.setOnClickListener(v -> {
            try {
                FirebaseAuth.getInstance().signOut(); // Logs out the user
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish(); // Closes the current activity
                } else {
                    Log.e("TripleLinePageFragment", "Activity is null.");
                }
            } catch (Exception e) {
                Log.e("TripleLinePageFragment", "Error during log out", e);
            }
        });

        return view;
    }

}
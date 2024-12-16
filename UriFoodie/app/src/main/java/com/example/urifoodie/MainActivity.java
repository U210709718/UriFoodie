package com.example.urifoodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView txtView;
    Button btnLogout;
    FirebaseAuth mAuth;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        Log.d("Firebase", "Firebase Initialized");


        //Handling LOGIN and LOGOUT button
        txtView = findViewById(R.id.user_details);
        btnLogout = findViewById(R.id.logOut);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            txtView.setText(user.getEmail());
        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                //it will sign out the user from firebase !
                Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //___________________________________________________________________________________

        //INSIDE APPLICATION!!

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomePageFragment();
            } else if (item.getItemId() == R.id.nav_explorer) {
                selectedFragment = new ExplorerPageFragment();
            } else if (item.getItemId() == R.id.nav_my_recipes) {
                selectedFragment = new MyRecipiesPageFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfilePageFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();
            }
            return true;
        });


        // Load the default Fragment (HomePageFragment)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new HomePageFragment())
                    .commit();
        }



    }
}
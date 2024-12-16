package com.example.urifoodie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check login status
            boolean isLoggedIn = checkLoginStatus();

            if (isLoggedIn) {
                // Go to MainActivity
                Intent intent = new Intent(StartPageActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                // Go to LoginActivity
                Intent intent = new Intent(StartPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            finish(); // Close StartPageActivity
        }, 2000); // Delay for 2 seconds
    }

    private boolean checkLoginStatus() {
        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false); // Default is false
    }

}

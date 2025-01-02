package com.example.urifoodie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    TextInputEditText EditTextEmail, EditTextPassword, EditTextUsername;
    Button btnReg;
    TextView textView;


    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore firestore;
    public static String  uname;

    @Override
    public void onStart() {
        super.onStart();
        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);
        EditTextEmail = findViewById(R.id.email);
        EditTextPassword = findViewById(R.id.password);
        EditTextUsername = findViewById(R.id.userName);
        textView = findViewById(R.id.loginNow);
        btnReg = findViewById(R.id.btn_register);

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnReg.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String email = String.valueOf(EditTextEmail.getText()).trim();
            String password = String.valueOf(EditTextPassword.getText()).trim();
            String username = String.valueOf(EditTextUsername.getText()).trim();


            // Validate input
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(RegistrationActivity.this, "Please fill email field", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(RegistrationActivity.this, "Please fill password field", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(RegistrationActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(RegistrationActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (!PasswordValidator.isPasswordStrong(password)) {
                Toast.makeText(RegistrationActivity.this, "Password must be at least 8 characters long and include uppercase, lowercase, digits, and special characters.", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Create a new user in Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserNameToFirestore(user.getUid(), username);
                    }

                    Toast.makeText(RegistrationActivity.this, "Account created successfully. Please login.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred";
                    Log.e("RegisterError", errorMessage);
                    Toast.makeText(RegistrationActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    public void saveUserNameToFirestore(String userId, String username) {
        DocumentReference docRef = firestore.collection("Users").document(userId);
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);

        docRef.set(userData).addOnSuccessListener(unused -> {
            Log.d("Firestore", "Username saved successfully");
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error saving username: ", e);
            Toast.makeText(RegistrationActivity.this, "Failed to save username to Firestore", Toast.LENGTH_SHORT).show();
        });
        uname =username;
    }
}

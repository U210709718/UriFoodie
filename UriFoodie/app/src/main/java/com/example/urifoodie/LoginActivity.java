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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText EditTextEmail, EditTextPassword;
    Button btnLogin;
    TextView textViewRegister;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        EditTextEmail = findViewById(R.id.email);
        EditTextPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        textViewRegister = findViewById(R.id.registerNow);
        progressBar = findViewById(R.id.progressBar);

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
            finish();
        });

        btnLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = String.valueOf(EditTextEmail.getText());
            String password = String.valueOf(EditTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // Login successful
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Login failed
                    String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred";
                    Log.e("LoginError", errorMessage);
                    Toast.makeText(LoginActivity.this, "Authentication Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}

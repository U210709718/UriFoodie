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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationActivity extends AppCompatActivity {

    TextInputEditText EditTextEmail, EditTextPassword;
    Button btnReg;
    TextView textView;

    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already log in! if so it will go to main activity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(RegistrationActivity.this , MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        EditTextEmail= findViewById(R.id.email);
        EditTextPassword= findViewById(R.id.password);
        textView = findViewById(R.id.loginNow);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create intent to go to login activity
                Intent intent = new Intent(RegistrationActivity.this , LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });


        btnReg = findViewById(R.id.btn_register);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //read text from edit text!
                String email,password;
                email =  String.valueOf(EditTextEmail.getText());
                password = String.valueOf(EditTextPassword.getText());

                //both fields can't be empty!

                if(TextUtils.isEmpty(email) ){
                    Toast.makeText(RegistrationActivity.this , "Please fill email field" , Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this , "Please fill password field" , Toast.LENGTH_SHORT).show();
                    return;

                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegistrationActivity.this, "Account created Successfully. Please Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationActivity.this , LoginActivity.class); //login
                            startActivity(intent);
                            finish();

                        }else{
                            // If sign in fails, display a message to the user.
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error occurred";
                            Log.e("RegisterError", errorMessage);
                            Toast.makeText(RegistrationActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();

                        }if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(RegistrationActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });



            }
        });




    }
}
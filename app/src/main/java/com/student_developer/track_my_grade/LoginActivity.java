package com.student_developer.track_my_grade;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private Button btnSubmitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge mode
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);

        // Initialize UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);
        btnSubmitLogin = findViewById(R.id.btnLoginSubmit);

        // Handle login button click
        btnSubmitLogin.setOnClickListener(v -> loginUser());

        // Handle registration prompt click
        TextView registerPrompt = findViewById(R.id.signUpPrompt);
        registerPrompt.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Basic validation
        if (!validateInputs(email, password)) {
            return;
        }

        // Disable login button and show progress bar
        progressBar.setVisibility(View.VISIBLE);
        btnSubmitLogin.setEnabled(false);

        // Simulate login process with a delay
        new Handler().postDelayed(() -> {
            // Simulate a successful login
            boolean loginSuccess = true;  // Change this for simulating failure

            if (loginSuccess) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish(); // Close LoginActivity
            } else {
                // Simulate login failure
                Toast.makeText(LoginActivity.this, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show();
            }

            // Hide progress bar and enable login button
            progressBar.setVisibility(View.GONE);
            btnSubmitLogin.setEnabled(true);

        }, 2000); // Simulate 2 seconds delay for login
    }

    // Input validation method
    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        // Email validation
        if (TextUtils.isEmpty(email)) {
            setError(etEmail, "Email is required.");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(etEmail, "Enter a valid email address.");
            isValid = false;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            setError(etPassword, "Password is required.");
            isValid = false;
        } else if (password.length() <= 6) {
            setError(etPassword, "Password length should be greater than 6.");
            isValid = false;
        }

        // Reset backgrounds if inputs are valid
        if (isValid) {
            resetBackgrounds();
        }

        return isValid;
    }

    // Set error method
    private void setError(EditText editText, String message) {
        editText.setError(message);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_round_corner));
        editText.requestFocus();
    }

    // Method to reset EditText backgrounds
    private void resetBackgrounds() {
        etEmail.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
        etPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
    }
}

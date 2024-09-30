package com.student_developer.track_my_grade;

import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText rollno, etEmail, etPassword, etConfirmPassword;
    private ProgressBar progressBar;
    private Button btnRegisterSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge mode (if supported in your project)
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);

        // Initialize UI elements
        initUI();

        // Handle register button click
        btnRegisterSubmit.setOnClickListener(v -> registerUser());

        // Handle login prompt click
        TextView loginPrompt = findViewById(R.id.loginPrompt);
        loginPrompt.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    // Method to initialize UI elements
    private void initUI() {
        rollno = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        progressBar = findViewById(R.id.progressBar);

        // Set default background for EditText fields
        setEditTextBackgrounds();
    }

    // Method to set EditText backgrounds
    private void setEditTextBackgrounds() {
        rollno.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
        etEmail.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
        etPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
        etConfirmPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
    }

    // Registration validation and process
    private void registerUser() {
        String roll_no = rollno.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation checks
        if (!validateInput(roll_no, email, password, confirmPassword)) {
            return;  // Stop further execution if validation fails
        }

        // Show progress bar and disable the button to prevent multiple submissions
        progressBar.setVisibility(View.VISIBLE);
        btnRegisterSubmit.setEnabled(false);

        // Simulate successful registration (Navigate to login page)
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

        // Hide progress bar and enable the button again after navigating
        progressBar.setVisibility(View.GONE);
        btnRegisterSubmit.setEnabled(true);
    }

    // Validation method for user input
    private boolean validateInput(String roll_no, String email, String password, String confirmPassword) {
        boolean valid = true;

        if (TextUtils.isEmpty(roll_no)) {
            setError(rollno, "Roll No is required.");
            valid = false;
        } else if (roll_no.length() < 7) {
            setError(rollno, "Enter a valid Roll No.");
            valid = false;
        }

        if (TextUtils.isEmpty(email)) {
            setError(etEmail, "Email is required.");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(etEmail, "Enter a valid email.");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            setError(etPassword, "Password is required.");
            valid = false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            setError(etConfirmPassword, "Confirm Password is required.");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            setError(etConfirmPassword, "Passwords do not match.");
            valid = false;
        }

        // Reset background for all fields if validation passes
        if (valid) {
            setEditTextBackgrounds();
        }

        return valid;
    }

    // Method to set error and change background for invalid inputs
    private void setError(EditText editText, String message) {
        editText.setError(message);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_round_corner));
        editText.requestFocus();
    }
}

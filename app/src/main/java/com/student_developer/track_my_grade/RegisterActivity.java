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

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText rollno, etEmail, etPassword, etConfirmPassword;
    private ProgressBar progressBar;
    private Button btnRegisterSubmit, btn_mv_tosignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        initUI();

        //btn_mv_tosignup.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        btnRegisterSubmit.setOnClickListener(v -> registerUser());

        TextView loginPrompt = findViewById(R.id.loginPrompt);
        loginPrompt.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void initUI() {
        rollno = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegisterSubmit = findViewById(R.id.btnRegisterSubmit);
        progressBar = findViewById(R.id.progressBar);
        btn_mv_tosignup = findViewById(R.id.btnLogin);
        setEditTextBackgrounds();
    }

    private void setEditTextBackgrounds() {
        int backgroundResource = R.drawable.edittext_backgrouond;
        rollno.setBackground(ContextCompat.getDrawable(this, backgroundResource));
        etEmail.setBackground(ContextCompat.getDrawable(this, backgroundResource));
        etPassword.setBackground(ContextCompat.getDrawable(this, backgroundResource));
        etConfirmPassword.setBackground(ContextCompat.getDrawable(this, backgroundResource));
    }

    private void registerUser() {
        String roll_no = rollno.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (validateInput(roll_no, email, password, confirmPassword)) {
            showProgressBar(true);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> handleRegistrationResult(task, roll_no, email));
        }
    }

    private void handleRegistrationResult(Task<AuthResult> task, String roll_no, String email) {
        if (task.isSuccessful()) {
            saveUserData(roll_no, email);
        } else {
            handleError(task.getException());
            clearPasswordFields();
        }
        showProgressBar(false);
    }

    private void saveUserData(String roll_no, String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> userData = new HashMap<>();
        userData.put("roll_no", roll_no);
        userData.put("email", email);

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Registration successful and data saved!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show());
    }

    private void handleError(Exception exception) {
        String errorMessage = "Registration failed. Please try again.";
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            errorMessage = "Weak password! Please choose a stronger password.";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            errorMessage = "Invalid email! Please provide a valid email address.";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            errorMessage = "Email already in use! Please use a different email.";
        } else if (exception instanceof FirebaseNetworkException) {
            errorMessage = "Network error. Please check your internet connection.";
        }
        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void clearPasswordFields() {
        etPassword.getText().clear();
        etConfirmPassword.getText().clear();
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegisterSubmit.setEnabled(!show);
        btnRegisterSubmit.setText(show ? "Registering..." : "Register");
    }

    private boolean validateInput(String roll_no, String email, String password, String confirmPassword) {
        boolean valid = true;

        if (TextUtils.isEmpty(roll_no) || roll_no.length() < 7) {
            setError(rollno, "Enter a valid Roll No (minimum 7 characters).");
            valid = false;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(etEmail, "Enter a valid email.");
            valid = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            setError(etPassword, "Password should be at least 6 characters long.");
            valid = false;
        }

        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)) {
            setError(etConfirmPassword, "Passwords do not match.");
            valid = false;
        }

        if (valid) {
            setEditTextBackgrounds();
        }
        return valid;
    }

    private void setError(EditText editText, String message) {
        editText.setError(message);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_round_corner));
        editText.requestFocus();
    }
}

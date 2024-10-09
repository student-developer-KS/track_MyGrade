package com.student_developer.track_my_grade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.MeshSpecification;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final int MIN_PASSWORD_LENGTH = 8;

    // Error messages as constants
    private static final String ERROR_NO_INTERNET = "No Internet Connection.Make sure you are connected to Internet .";
    private static final String ERROR_EMAIL_REQUIRED = "Email is required.";
    private static final String ERROR_INVALID_EMAIL = "Enter a valid email address.";
    private static final String ERROR_PASSWORD_REQUIRED = "Password is required.";
    private static final String ERROR_PASSWORD_MIN_LENGTH = "Password must be a minimum of 8 characters.";
    private static final String ERROR_LOGIN_FAILED = "Login failed. Please try again.";
    private static final String ERROR_USER_NOT_REGISTERED = "This email address is not registered.";
    private static final String ERROR_INVALID_PASSWORD = "Invalid email Id or Password. Please try again.";

    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private Button btnSubmitLogin, btnMvToLogin;
    private FirebaseAuth authLogin;
    private ImageView ivTogglePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initializeUIElements();
        setOnClickListeners();
        etEmail.requestFocus();
    }

    private void initializeUIElements() {

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);
        btnSubmitLogin = findViewById(R.id.btnLoginSubmit);
        btnMvToLogin = findViewById(R.id.btnSignUp);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);
        authLogin = FirebaseAuth.getInstance();

        if (!isNetworkConnected()) {
            showErrorMessage(ERROR_NO_INTERNET);
            btnSubmitLogin.setEnabled(true);
            return;
        }

        setContentDescriptions();
    }

    private void setContentDescriptions() {

        etEmail.setContentDescription("Email input field");
        etPassword.setContentDescription("Password input field");
        btnSubmitLogin.setContentDescription("Login button");
        btnMvToLogin.setContentDescription("Navigate to register");
    }

    private void setOnClickListeners() {
        btnMvToLogin.setOnClickListener(v -> {hideKeyboard(btnSubmitLogin);navigateTo(RegisterActivity.class);});
        btnSubmitLogin.setOnClickListener(v -> {
            hideKeyboard(btnSubmitLogin);
            btnSubmitLogin.setEnabled(false);
            loginUser();
        });
        ivTogglePassword.setOnClickListener(v -> {hideKeyboard(btnSubmitLogin);togglePasswordVisibility();});
        findViewById(R.id.signUpPrompt).setOnClickListener(v ->{hideKeyboard(btnSubmitLogin); navigateTo(RegisterActivity.class);});
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(LoginActivity.this, targetActivity));
        finish();
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(email, password)) {
            btnSubmitLogin.setEnabled(true);
            return;
        }


        showProgressBar(true);
        authLogin.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this::handleLoginResponse);
    }

    private void handleLoginResponse(Task<AuthResult> task) {
        showProgressBar(false);
        if (task.isSuccessful()) {


            FirebaseUser firebaseuser = authLogin.getCurrentUser();
            if(firebaseuser.isEmailVerified()){

                navigateTo(MainActivity.class);
                showToast("Login successful!");

            }else{
                firebaseuser.sendEmailVerification();
                authLogin.signOut();
                showAlertDialog();
            }

        } else {
            handleLoginError(task);
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Verify your Email now. You can't login without verification");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleLoginError(Task<AuthResult> task) {
        Exception exception = task.getException();
        String errorMessage = ERROR_LOGIN_FAILED;

        if (exception instanceof FirebaseAuthInvalidUserException) {
            clearEmailField();
            clearPasswordField();
            errorMessage = ERROR_USER_NOT_REGISTERED;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            clearPasswordField();
            errorMessage = ERROR_INVALID_PASSWORD;
        }

        showErrorMessage(errorMessage);
        Log.e("LoginActivity", errorMessage, exception);
    }

    private void clearInputFields() {
        etEmail.setText("");
        etPassword.setText("");
    }

    private void clearEmailField() {
        clearField(etEmail);
    }

    private void clearPasswordField() {
        clearField(etPassword);
    }

    private void clearField(EditText editText) {
        editText.setText("");
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_round_corner));
        editText.requestFocus();
    }

    private boolean validateInputs(String email, String password) {
        boolean isValid = true;

        if (TextUtils.isEmpty(email)) {
            setError(etEmail, ERROR_EMAIL_REQUIRED);
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(etEmail, ERROR_INVALID_EMAIL);
            isValid = false;
        }

        if (TextUtils.isEmpty(password)) {
            setError(etPassword, ERROR_PASSWORD_REQUIRED);
            isValid = false;
        } else if (password.length() < MIN_PASSWORD_LENGTH) {
            setError(etPassword, ERROR_PASSWORD_MIN_LENGTH);
            isValid = false;
        }

        else{
            resetBackgrounds();
        }

        return isValid;
    }

    private void setError(EditText editText, String message) {
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_round_corner));
        editText.setError(message);
        editText.requestFocus();
    }

    private void resetBackgrounds() {
        etEmail.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
        etPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edittext_backgrouond));
    }

    private void showProgressBar(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        btnSubmitLogin.setEnabled(!isVisible);
        btnSubmitLogin.setText(isVisible ? "Logging in..." : "Login");
    }

    private void showErrorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void showToast(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show(); // Change to Toast if preferred
    }

    private void togglePasswordVisibility() {
        boolean isPasswordVisible = etPassword.getTransformationMethod() instanceof PasswordTransformationMethod;
        etPassword.setTransformationMethod(isPasswordVisible ? null : new PasswordTransformationMethod());
        ivTogglePassword.setImageResource(isPasswordVisible ? R.drawable.ic_visibility : R.drawable.ic_visibility_off);
        etPassword.setSelection(etPassword.length());

        // Show Snackbar if the password field is not empty
        if (!TextUtils.isEmpty(etPassword.getText())) {
            showToast(isPasswordVisible ? "Password visible" : "Password hidden");
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




//    @Override
//    protected void onStart(){
//        super.onStart();
//        if(authLogin.getCurrentUser()!=null){
//            Snackbar.make(LoginActivity.this.getCurrentFocus(),"Already Logged In!", Snackbar.LENGTH_SHORT).show();
//            navigateTo(MainActivity.class);
//            finish();
//        }else{
//            Snackbar.make(LoginActivity.this.getCurrentFocus(),"You Can Login Now!", Snackbar.LENGTH_SHORT).show();
//        }
//    }
}

package com.student_developer.track_my_grade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // Initial delay for the splash screen
    private static final int NETWORK_CHECK_INTERVAL = 2000; // Interval to check for network connectivity (2 seconds)

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable networkCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Initialize views
        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.titleTextView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Start animations
        animateSplashScreen(logo, title, progressBar);

        // Start network check after splash display length
        handler.postDelayed(() -> checkNetworkAndProceed(), SPLASH_DISPLAY_LENGTH);
    }

    private void checkNetworkAndProceed() {
        // Runnable to check network connectivity periodically
        networkCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected()) {
                    // If connected, proceed to LoginActivity and finish SplashActivity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    // Show a message about no internet and check again after a delay
                    Snackbar.make(findViewById(android.R.id.content), "No Internet. Waiting for connection...", Snackbar.LENGTH_LONG).show();
                    handler.postDelayed(this, NETWORK_CHECK_INTERVAL); // Recheck after a delay
                }
            }
        };

        // Start the first check immediately
        handler.post(networkCheckRunnable);
    }

    private void animateSplashScreen(ImageView logo, TextView title, ProgressBar progressBar) {
        logo.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        // Logo animation
        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(500)
                .withStartAction(() -> logo.setVisibility(View.VISIBLE))
                .start();

        // Title animation
        title.setTranslationY(100f);
        title.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(1000)
                .withStartAction(() -> title.setVisibility(View.VISIBLE))
                .start();

        // Progress bar animation
        progressBar.setAlpha(0f); // Start with alpha 0
        progressBar.setVisibility(View.VISIBLE); // Make it visible before animating
        progressBar.animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(1500)
                .start();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the network check runnable when activity is destroyed to avoid memory leaks
        if (handler != null && networkCheckRunnable != null) {
            handler.removeCallbacks(networkCheckRunnable);
        }
    }
}

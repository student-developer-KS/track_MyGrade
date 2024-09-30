package com.student_developer.track_my_grade;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.student_developer.track_my_grade.LoginActivity;
import com.student_developer.track_my_grade.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Initialize views
        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.titleTextView);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Load the fade_in animation from res/anim
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logo.startAnimation(fadeInAnimation);
        title.startAnimation(fadeInAnimation);
        progressBar.startAnimation(fadeInAnimation);

        // Set initial visibility for animations
        logo.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        // Animate logo and title with fade-in effect
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

        title.setTranslationY(100f);
        title.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(1500)
                .setStartDelay(1000)
                .withStartAction(() -> title.setVisibility(View.VISIBLE))
                .start();

        // Progress bar animation with delay
        progressBar.animate()
                .alpha(1f)
                .setDuration(800)
                .setStartDelay(1500) // Reduced delay for a more immediate feel
                .withStartAction(() -> progressBar.setVisibility(View.VISIBLE))
                .start();

        // Delayed transition to next activity
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000); // Shortened delay for better UX
    }
}

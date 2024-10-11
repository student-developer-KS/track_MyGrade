package com.student_developer.track_my_grade;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        loadFragment(new CalculatorFragment());

        // Find the buttons in the layout
        ImageView btnProfile = findViewById(R.id.btn_profile);
        ImageView btnCalculator = findViewById(R.id.btn_calculator);
        ImageView btnGraph = findViewById(R.id.btn_graph);

        // Set onClickListeners for each button
        btnProfile.setOnClickListener(v -> {
            // Load the Home Fragment when Home button is clicked
            loadFragment(new ProfileFragment());
        });

        btnCalculator.setOnClickListener(v -> {
            // Load the CGPA Calculator Fragment when CGPA Calculator button is clicked
            loadFragment(new CalculatorFragment());
        });

        btnGraph.setOnClickListener(v -> {
            // Load the Profile Fragment when Profile button is clicked
            loadFragment(new GraphFragment());
        });
    }

    // Method to load fragments into the FrameLayout
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            // Get the FragmentManager to manage the fragments
            FragmentManager fragmentManager = getSupportFragmentManager();
            // Begin a transaction to replace the current fragment
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Replace the fragment in the container (R.id.fragment_container should be the ID of your FrameLayout in XML)
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            // Commit the transaction to finalize the change
            fragmentTransaction.commit();
        }
    }
}

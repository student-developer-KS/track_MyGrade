package com.student_developer.track_my_grade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;




public class ProfileFragment extends Fragment {

    TextView tvpro1,tvpro2,tvpro3,tvpro4,tvpro5,tvpro6,tvpro7,tvpro8,tvCGPATotal;
    Button btnLogOut;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment (fragment_profile.xml)
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        String rollNO = sharedPref.getString("roll_no", null);

        btnLogOut =view.findViewById(R.id.btn_logOut);
        db = FirebaseFirestore.getInstance();
        String rollNo = rollNO;
        DocumentReference docRef = db.collection("GPA").document(rollNo);

        tvpro1 = view.findViewById((R.id.tv_pro1));
        tvpro2 = view.findViewById((R.id.tv_pro2));
        tvpro3 = view.findViewById((R.id.tv_pro3));
        tvpro4 = view.findViewById((R.id.tv_pro4));
        tvpro5 = view.findViewById((R.id.tv_pro5));
        tvpro6 = view.findViewById((R.id.tv_pro6));
        tvpro7 = view.findViewById((R.id.tv_pro7));
        tvpro8 = view.findViewById((R.id.tv_pro8));
        tvCGPATotal = view.findViewById((R.id.tvCgpaTotal));

        // Assuming you have already initialized your TextViews
        TextView[] textViews = {tvpro1, tvpro2, tvpro3, tvpro4, tvpro5, tvpro6, tvpro7, tvpro8};

        for (TextView textView : textViews) {
            textView.setOnClickListener(view1 -> navigateToCalculatorFragment());
        }
        btnLogOut.setOnClickListener(view1 -> {
            // Firebase sign out
            FirebaseAuth.getInstance().signOut();

            // Navigate back to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);

            // Optionally finish the current activity to prevent the user from returning to it using the back button
            getActivity().finish();
        });


        // Access the TextView from the activity (activity_calculator.xml)
        if (getActivity() != null) {
            // Accessing TextViews and Views in the activity layout
            TextView tvActivityProfile = getActivity().findViewById(R.id.tv_profile);
            TextView tvCgpa = getActivity().findViewById(R.id.tv_cgpa);
            TextView tvGraph = getActivity().findViewById(R.id.tv_graph);
            View vProfile = getActivity().findViewById(R.id.v_profile);
            View vCgpa = getActivity().findViewById(R.id.v_cgpa);
            View vGraph = getActivity().findViewById(R.id.v_graph);

            // Change the text color of the TextViews in the activity
            if (tvActivityProfile != null) {
                tvActivityProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
            }
            if (tvCgpa != null) {
                tvCgpa.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
            }
            if (tvGraph != null) {
                tvGraph.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
            }

            // Set visibility for specific views
            if (vProfile != null) {
                vProfile.setVisibility(View.VISIBLE); // Show profile view
            }
            if (vCgpa != null) {
                vCgpa.setVisibility(View.GONE); // Hide CGPA view
            }
            if (vGraph != null) {
                vGraph.setVisibility(View.GONE); // Hide graph view
            }
        }


// Retrieve the document
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Retrieve GPA values safely
                Double sem1GPA = documentSnapshot.getDouble("Sem 1");
                Float gpa1 = (sem1GPA != null) ? sem1GPA.floatValue() : null;

                Double sem2GPA = documentSnapshot.getDouble("Sem 2");
                Float gpa2 = (sem2GPA != null) ? sem2GPA.floatValue() : null;

                Double sem3GPA = documentSnapshot.getDouble("Sem 3");
                Float gpa3 = (sem3GPA != null) ? sem3GPA.floatValue() : null;

                Double sem4GPA = documentSnapshot.getDouble("Sem 4");
                Float gpa4 = (sem4GPA != null) ? sem4GPA.floatValue() : null;

                Double sem5GPA = documentSnapshot.getDouble("Sem 5");
                Float gpa5 = (sem5GPA != null) ? sem5GPA.floatValue() : null;

                Double sem6GPA = documentSnapshot.getDouble("Sem 6");
                Float gpa6 = (sem6GPA != null) ? sem6GPA.floatValue() : null;

                Double sem7GPA = documentSnapshot.getDouble("Sem 7");
                Float gpa7 = (sem7GPA != null) ? sem7GPA.floatValue() : null;

                Double sem8GPA = documentSnapshot.getDouble("Sem 8");
                Float gpa8 = (sem8GPA != null) ? sem8GPA.floatValue() : null;

                // Set the retrieved values to the corresponding TextViews
                tvpro1.setText(gpa1 != null && gpa1 > 0.0f ? String.valueOf(gpa1) : "N/A");
                tvpro2.setText(gpa2 != null && gpa2 > 0.0f ? String.valueOf(gpa2) : "N/A");
                tvpro3.setText(gpa3 != null && gpa3 > 0.0f ? String.valueOf(gpa3) : "N/A");
                tvpro4.setText(gpa4 != null && gpa4 > 0.0f ? String.valueOf(gpa4) : "N/A");
                tvpro5.setText(gpa5 != null && gpa5 > 0.0f ? String.valueOf(gpa5) : "N/A");
                tvpro6.setText(gpa6 != null && gpa6 > 0.0f ? String.valueOf(gpa6) : "N/A");
                tvpro7.setText(gpa7 != null && gpa7 > 0.0f ? String.valueOf(gpa7) : "N/A");
                tvpro8.setText(gpa8 != null && gpa8 > 0.0f ? String.valueOf(gpa8) : "N/A");

                Float[] gpas = {gpa1, gpa2, gpa3, gpa4, gpa5, gpa6, gpa7, gpa8};

                // Set the retrieved values to the corresponding TextViews
                setGPAColorAndText(tvpro1, gpa1);
                setGPAColorAndText(tvpro2, gpa2);
                setGPAColorAndText(tvpro3, gpa3);
                setGPAColorAndText(tvpro4, gpa4);
                setGPAColorAndText(tvpro5, gpa5);
                setGPAColorAndText(tvpro6, gpa6);
                setGPAColorAndText(tvpro7, gpa7);
                setGPAColorAndText(tvpro8, gpa8);

// Helper method to set text and color based on GPA value
                // Calculate CGPA
                double sum = 0.0;
                int count = 0;
                for (Float gpa : gpas) {
                    if (gpa != null && gpa > 0.0 && gpa <= 10.0) {
                        sum += gpa;
                        count++;
                    }
                }

                // Calculate the mean GPA
                Double meanGPA = (count > 0) ? (sum / count) : null;
                Float mean = meanGPA != null ? meanGPA.floatValue() : null;

                // Display the CGPA
                if (meanGPA != null) {
                    tvCGPATotal.setText("Your CGPA for " + count + " Semester(s): " + String.format("%.2f", mean));
                } else {
                    tvCGPATotal.setText("N/A");
                }

            } else {
                // If document doesn't exist
                clearTextViews();
            }
        }).addOnFailureListener(e -> {
            // Handle error
            clearTextViews();
        });

        // Return the fragment view
        return view;
    }

    private void setGPAColorAndText(TextView textView, Float gpa) {
        if (gpa != null && gpa > 0.0f) {
            // Set GPA value and color to green
            textView.setText(String.valueOf(gpa));
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        } else {
            // Set "N/A" and color to red
            textView.setText("N/A");
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        }
    }
    private void navigateToCalculatorFragment() {
        // Create an instance of the CalculatorFragment
        CalculatorFragment calculatorFragment = new CalculatorFragment();

        // Use the FragmentManager to replace the current fragment with the CalculatorFragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, calculatorFragment); // Change fragment_container to your actual container ID
        transaction.addToBackStack(null); // Optional: Add to back stack if you want to navigate back
        transaction.commit();
    }

    private void clearTextViews() {
        tvpro1.setText("");
        tvpro2.setText("");
        tvpro3.setText("");
        tvpro4.setText("");
        tvpro5.setText("");
        tvpro6.setText("");
        tvpro7.setText("");
        tvpro8.setText("");
    }
}

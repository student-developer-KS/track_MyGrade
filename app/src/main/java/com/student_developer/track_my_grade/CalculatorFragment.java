package com.student_developer.track_my_grade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class CalculatorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment (fragment_profile.xml)
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

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
                tvActivityProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
            }
            if (tvCgpa != null) {
                tvCgpa.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
            }
            if (tvGraph != null) {
                tvGraph.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
            }

            // Set visibility for specific views
            if (vProfile != null) {
                vProfile.setVisibility(View.GONE); // Show profile view
            }
            if (vCgpa != null) {
                vCgpa.setVisibility(View.VISIBLE); // Hide CGPA view
            }
            if (vGraph != null) {
                vGraph.setVisibility(View.GONE); // Hide graph view
            }
        }

        // Return the fragment view
        return view;
    }
}

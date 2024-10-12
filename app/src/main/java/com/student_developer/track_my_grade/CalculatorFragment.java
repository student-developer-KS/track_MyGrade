package com.student_developer.track_my_grade;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class CalculatorFragment extends Fragment {

    // Existing member variables
    EditText etNoOfSubs;
    Button btnGenerateSubs;
    LinearLayout ll_no_of_sub;
    ScrollView sv_containers;
    LinearLayout ll_subjects_container; // New container for subject marks

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

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


        // Initialize UI components
        etNoOfSubs = view.findViewById(R.id.et_no_of_subjects);
        etNoOfSubs.requestFocus();
        btnGenerateSubs = view.findViewById(R.id.btn_generate_subjects);
        ll_no_of_sub = view.findViewById(R.id.ll_no_of_subs);
        ll_subjects_container = view.findViewById(R.id.ll_subjects_container);
        ll_subjects_container.setVisibility(View.GONE);
        sv_containers = view.findViewById(R.id.sv_container);

        btnGenerateSubs.setOnClickListener(v -> {
            hideKeyboard(v);
            String noOfSubjects = etNoOfSubs.getText().toString().trim();

            if (TextUtils.isEmpty(noOfSubjects)) {
                etNoOfSubs.setError("Please enter the number of subjects");
                etNoOfSubs.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                return;
            }

            try {
                int numberOfSubjects = Integer.parseInt(noOfSubjects);
                if (numberOfSubjects <= 0 || numberOfSubjects > 11) {
                    etNoOfSubs.setError("Your input is not valid");
                    etNoOfSubs.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                    return;
                }

                // Remove any previously generated views
                ll_subjects_container.removeAllViews();
                ll_no_of_sub.setVisibility(View.GONE);
                ll_subjects_container.setVisibility(View.VISIBLE);
                sv_containers.setVisibility(View.VISIBLE);

                // Add a row with the labels Subject Name, CR, GP
                addSubjectLabels();

                // Dynamically create 'n' subject details
                for (int i = 0; i < numberOfSubjects; i++) {
                    createSubjectDetailView(i + 1);
                }

                // Add a button at the bottom to process the inputs
                addCalculateButton();

            } catch (NumberFormatException e) {
                etNoOfSubs.setError("Please enter a valid number");
                etNoOfSubs.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
            }
        });

        return view;
    }

    private void createSubjectDetailView(int subjectNumber) {
        // Create a new LinearLayout for subject details
        LinearLayout llSubjectDetail = new LinearLayout(requireContext());
        llSubjectDetail.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, WRAP_CONTENT));
        llSubjectDetail.setOrientation(LinearLayout.HORIZONTAL);

        // Subject Name EditText
        EditText etSubjectName = new EditText(requireContext());


        etSubjectName.setId(View.generateViewId());
        etSubjectName.setTag("sub" + subjectNumber);

        LinearLayout.LayoutParams subjectNameParams = new LinearLayout.LayoutParams(
                WRAP_CONTENT, WRAP_CONTENT);
        subjectNameParams.setMargins(0, convertDpToPx(10), convertDpToPx(30), 0); // marginTop 10dp and marginEnd 30dp
        etSubjectName.setLayoutParams(subjectNameParams);
        etSubjectName.setHint("Subject " + subjectNumber);
        etSubjectName.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
        etSubjectName.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edittext_backgrouond));
        etSubjectName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etSubjectName.setPadding(convertDpToPx(10), convertDpToPx(10), convertDpToPx(10), convertDpToPx(10)); // Padding 10dp
        etSubjectName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Text alignment center

        // CR EditText
        EditText etCr = new EditText(requireContext());


        etCr.setId(View.generateViewId());
        etCr.setTag("cr" + subjectNumber);

        LinearLayout.LayoutParams crParams = new LinearLayout.LayoutParams(
                WRAP_CONTENT, WRAP_CONTENT);
        crParams.setMargins(0, convertDpToPx(10), 0, 0); // marginTop 10dp
        etCr.setLayoutParams(crParams);
        etCr.setHint("Enter CR");
        etCr.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
        etCr.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edittext_backgrouond));
        etCr.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCr.setPadding(convertDpToPx(10), convertDpToPx(10), convertDpToPx(10), convertDpToPx(10)); // Padding 10dp
        etCr.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Text alignment center

        // GP EditText
        EditText etGp = new EditText(requireContext());


        etGp.setId(View.generateViewId());
        etGp.setTag("gp" + subjectNumber);

        LinearLayout.LayoutParams gpParams = new LinearLayout.LayoutParams(
                WRAP_CONTENT, WRAP_CONTENT);
        gpParams.setMargins(convertDpToPx(30), convertDpToPx(10), 0, 0); // marginTop 10dp and marginStart 30dp
        etGp.setLayoutParams(gpParams);
        etGp.setHint("Enter GP");
        etGp.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
        etGp.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edittext_backgrouond));
        etGp.setInputType(InputType.TYPE_CLASS_NUMBER);
        etGp.setPadding(convertDpToPx(10), convertDpToPx(10), convertDpToPx(10), convertDpToPx(10)); // Padding 10dp
        etGp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Text alignment center

        // Add EditTexts to the LinearLayout
        llSubjectDetail.addView(etSubjectName);
        llSubjectDetail.addView(etCr);
        llSubjectDetail.addView(etGp);

        // Add the subject detail layout to the container
        ll_subjects_container.addView(llSubjectDetail);
    }

    private void addSubjectLabels() {
        LinearLayout llLabels = new LinearLayout(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 20); // Set margin bottom to 20dp
        llLabels.setLayoutParams(layoutParams);
        llLabels.setOrientation(LinearLayout.HORIZONTAL);
        llLabels.setGravity(Gravity.CENTER); // Center the content horizontally


        // Subject Name Label
        TextView tvSubjectLabel = new TextView(requireContext());
        LinearLayout.LayoutParams subjectLabelParams = new LinearLayout.LayoutParams(
                0, WRAP_CONTENT, 1f);
        subjectLabelParams.setMargins(convertDpToPx(15), convertDpToPx(16), convertDpToPx(20), convertDpToPx(16));
        tvSubjectLabel.setLayoutParams(subjectLabelParams);
        tvSubjectLabel.setText("Subject Name");
        tvSubjectLabel.setTextSize(18);
        tvSubjectLabel.setTypeface(null, Typeface.BOLD);  // Set text style to bold
        tvSubjectLabel.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));  // Set text color to black

        // CR Label
        TextView tvCrLabel = new TextView(requireContext());
        LinearLayout.LayoutParams crLabelParams = new LinearLayout.LayoutParams(
                0, WRAP_CONTENT, 1f);
        crLabelParams.setMargins(convertDpToPx(40), convertDpToPx(16), convertDpToPx(0), convertDpToPx(16));
        tvCrLabel.setLayoutParams(crLabelParams);
        tvCrLabel.setText("CR");
        tvCrLabel.setTextSize(18);
        tvCrLabel.setTypeface(null, Typeface.BOLD);
        tvCrLabel.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

        // GP Label
        TextView tvGpLabel = new TextView(requireContext());
        LinearLayout.LayoutParams gpLabelParams = new LinearLayout.LayoutParams(
                0, WRAP_CONTENT, 1f);
        gpLabelParams.setMargins(convertDpToPx(30), convertDpToPx(16), convertDpToPx(0), convertDpToPx(16));
        tvGpLabel.setLayoutParams(gpLabelParams);
        tvGpLabel.setText("GP");
        tvGpLabel.setTextSize(18);
        tvGpLabel.setTypeface(null, Typeface.BOLD);
        tvGpLabel.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black));

        // Add Labels to the LinearLayout
        llLabels.addView(tvSubjectLabel);
        llLabels.addView(tvCrLabel);
        llLabels.addView(tvGpLabel);

        // Add the labels layout to the container
        ll_subjects_container.addView(llLabels);
    }

    private void addCalculateButton() {
        Button btnCalculate = new Button(requireContext());
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, WRAP_CONTENT);
        buttonLayoutParams.setMargins(0, convertDpToPx(20), 0, 0); // Set margin top as 20dp
        btnCalculate.setLayoutParams(buttonLayoutParams);
        btnCalculate.setText("Calculate GPA");
        btnCalculate.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.blue_600));
        btnCalculate.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        btnCalculate.setPadding(20, 20, 20, 20);
        btnCalculate.setGravity(Gravity.CENTER); // Center the text in the button


        // Add a click listener for the button to process the inputs
        btnCalculate.setOnClickListener(v -> {
            if (isInputValid()) {// Check if all inputs are valid
                hideKeyboard(v);
                calculate();
                // If valid, perform the calculation
            } else {
            }
        });


        // Add the button to the container
        ll_subjects_container.addView(btnCalculate);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (requireContext().getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private boolean isInputValid() {
        for (int i = 0; i < ll_subjects_container.getChildCount(); i++) {
            View child = ll_subjects_container.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout subjectLayout = (LinearLayout) child;
                for (int j = 0; j < subjectLayout.getChildCount(); j++) {
                    View inputView = subjectLayout.getChildAt(j);
                    if (inputView instanceof EditText) {
                        EditText editText = (EditText) inputView;

                        // Check if the input is empty
                        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                            editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                            editText.requestFocus();
                            return false; // Return false if any EditText is empty
                        } else {
                            editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edittext_backgrouond));
                        }

                        // Validate GP (should be 10 or less)
                        if (editText.getTag().toString().startsWith("gp")) {
                            try {
                                float gpValue = Float.parseFloat(editText.getText().toString().trim());
                                if (gpValue > 10) {
                                    Toast.makeText(requireContext(), "GP should be 10 or less", Toast.LENGTH_SHORT).show();
                                    editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                                    editText.requestFocus();
                                    return false; // Return false if GP is more than 10
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(requireContext(), "Please enter a valid number for GP", Toast.LENGTH_SHORT).show();
                                editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                                editText.requestFocus();
                                return false; // Return false if GP input is not a number
                            }
                        }

                        // Validate CR (should be 10 or less)
                        if (editText.getTag().toString().startsWith("cr")) {
                            try {
                                int crValue = Integer.parseInt(editText.getText().toString().trim());
                                if (crValue > 10) {
                                    Toast.makeText(requireContext(), "CR should be 10 or less", Toast.LENGTH_SHORT).show();
                                    editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                                    editText.requestFocus();
                                    return false; // Return false if CR is more than 10
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(requireContext(), "Please enter a valid number for CR", Toast.LENGTH_SHORT).show();
                                editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edit_text_round_corner));
                                editText.requestFocus();
                                return false; // Return false if CR input is not a number
                            }
                        }
                    }
                }
            }
        }
        return true; // All inputs are valid
    }


    private void calculate() {
        // Variables to hold the subject names, credit hours, and grade points
        String[] subjectNames = new String[ll_subjects_container.getChildCount()];
        int[] creditHours = new int[ll_subjects_container.getChildCount()];
        float[] gradePoints = new float[ll_subjects_container.getChildCount()];

        for (int i = 0; i < ll_subjects_container.getChildCount(); i++) {
            View child = ll_subjects_container.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout subjectLayout = (LinearLayout) child;
                for (int j = 0; j < subjectLayout.getChildCount(); j++) {
                    View inputView = subjectLayout.getChildAt(j);
                    if (inputView instanceof EditText) {
                        EditText editText = (EditText) inputView;
                        if (j == 0) {
                            subjectNames[i] = editText.getText().toString().trim(); // Store subject name
                        } else if (j == 1) {
                            creditHours[i] = Integer.parseInt(editText.getText().toString().trim()); // Store CR
                        } else if (j == 2) {
                            gradePoints[i] = Float.parseFloat(editText.getText().toString().trim()); // Store GP
                        }
                    }
                }
            }
        }

        // Now you can use subjectNames, creditHours, and gradePoints for your CGPA calculation logic
        // For example:
        float cgpa = calculateCGPA(creditHours, gradePoints);
        Toast.makeText(requireContext(), "CGPA: " + cgpa , Toast.LENGTH_SHORT).show();

    }
    private float calculateCGPA(int[] creditHours, float[] gradePoints) {
        int totalCreditHours = 0;
        float totalGradePoints = 0;

        for (int i = 0; i < creditHours.length; i++) {
            totalCreditHours += creditHours[i];
            totalGradePoints += creditHours[i] * gradePoints[i];
        }


        return totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0; // Avoid division by zero
    }


//    private void printSubjectDetails() {
//        StringBuilder details = new StringBuilder("Subject Details:\n");
//
//        // Start checking from index 1 to skip the label row
//        for (int i = 1; i < ll_subjects_container.getChildCount(); i++) {
//            View view = ll_subjects_container.getChildAt(i);
//
//            // Check if the child is a LinearLayout (which contains EditTexts)
//            if (view instanceof LinearLayout) {
//                LinearLayout subjectDetailLayout = (LinearLayout) view;
//
//                // Get the EditTexts for Subject Name, CR, and GP
//                String subjectName = ((EditText) subjectDetailLayout.getChildAt(0)).getText().toString().trim();
//                String cr = ((EditText) subjectDetailLayout.getChildAt(1)).getText().toString().trim();
//                String gp = ((EditText) subjectDetailLayout.getChildAt(2)).getText().toString().trim();
//
//                // Append the details to the StringBuilder
//                details.append("Subject Name: ").append(subjectName)
//                        .append(", CR: ").append(cr)
//                        .append(", GP: ").append(gp).append("\n");
//            }
//        }
//
//        // Print the collected details using Snackbar
//        View rootView = requireActivity().findViewById(android.R.id.content); // Get the root view
//        Snackbar.make(rootView, details.toString(), Snackbar.LENGTH_LONG)
//                .setAction("Action", null) // Optional action button
//                .show(); // Show the Snackbar
//    }





}

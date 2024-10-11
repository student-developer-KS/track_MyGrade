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
        btnCalculate.setGravity(View.TEXT_ALIGNMENT_CENTER);
        btnCalculate.setGravity(Gravity.CENTER); // Center the text in the button


        // Add a click listener for the button to process the inputs
        btnCalculate.setOnClickListener(v -> {
            // Your CGPA calculation logic goes here
            Toast.makeText(requireContext(), "Calculate CGPA button clicked", Toast.LENGTH_SHORT).show();
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
}

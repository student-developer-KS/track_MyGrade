package com.student_developer.track_my_grade;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {
    private FirebaseFirestore db;
    private LineChart lineChart;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        lineChart = view.findViewById(R.id.chart);
        progressBar = view.findViewById(R.id.progress_bar);

        // Get the shared preferences for roll number
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        String rollNO = sharedPref.getString("roll_no", null);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        progressBar.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.GONE); // Hide the chart initially

        // Set up TextViews in the activity (activity_calculator.xml)
        setupActivityViews();

        // Fetch data and set up charts
        if (rollNO != null) {
            fetchGPAData(rollNO);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        return view;
    }


    // Method to set up activity views
    private void setupActivityViews() {
        if (getActivity() != null) {
            TextView tvActivityProfile = getActivity().findViewById(R.id.tv_profile);
            TextView tvCgpa = getActivity().findViewById(R.id.tv_cgpa);
            TextView tvGraph = getActivity().findViewById(R.id.tv_graph);
            View vProfile = getActivity().findViewById(R.id.v_profile);
            View vCgpa = getActivity().findViewById(R.id.v_cgpa);
            View vGraph = getActivity().findViewById(R.id.v_graph);

            if (tvActivityProfile != null) {
                tvActivityProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
            }
            if (tvCgpa != null) {
                tvCgpa.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
            }
            if (tvGraph != null) {
                tvGraph.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
            }

            if (vProfile != null) {
                vProfile.setVisibility(View.GONE);
            }
            if (vCgpa != null) {
                vCgpa.setVisibility(View.GONE);
            }
            if (vGraph != null) {
                vGraph.setVisibility(View.VISIBLE);
            }
        }
    }

    // Fetch GPA data from Firestore and setup charts
    private void fetchGPAData(String rollNO) {
        DocumentReference docRef = db.collection("GPA").document(rollNO);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Float[] gpas = new Float[8];
                for (int i = 1; i <= 8; i++) {
                    Double semGPA = documentSnapshot.getDouble("Sem " + i);
                    gpas[i - 1] = (semGPA != null) ? semGPA.floatValue() : null;
                }

                if (isAdded()) {
                    setupCharts(gpas);
                }

                progressBar.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);

            } else {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Setup charts with GPA data
    private void setupCharts(Float[] gpas) {
        if (!isAdded()) {
            return;  // Ensure fragment is attached
        }

        // Setup the LineChart data dynamically based on GPA
        LineDataSet lineDataSet = new LineDataSet(getLineChartData(gpas), "SEMESTER");
        customizeLineDataSet(lineDataSet);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        // Setup line chart customizations
        setupLineChart(lineChart);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
        lineChart.invalidate();  // Refresh the LineChart
    }

    private void customizeLineDataSet(LineDataSet lineDataSet) {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLineWidth(3f);
        lineDataSet.setCircleRadius(7f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleHoleRadius(2.0f);
        lineDataSet.setValueTextSize(15f);
        lineDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.blue_600));
        lineDataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(requireContext(), R.color.white));
        lineDataSet.setDrawFilled(false);  // Drawing filled is handled in setupCharts
        lineDataSet.setFillColor(ContextCompat.getColor(requireContext(), R.color.green));
        lineDataSet.setFillAlpha(40);
    }

    private List<Entry> getLineChartData(Float[] gpas) {
        ArrayList<Entry> dataValue = new ArrayList<>();
        for (int i = 0; i < gpas.length; i++) {
            if (gpas[i] != null) {
                dataValue.add(new Entry(i + 1, gpas[i]));  // i + 1 to represent Semester numbers
            }
        }
        return dataValue;
    }

    private void setupLineChart(LineChart lineChart) {
        lineChart.getDescription().setText("");
        lineChart.getDescription().setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
        lineChart.getDescription().setTextSize(12f);
        lineChart.animateX(1500);
        lineChart.animateY(1500);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        xAxis.setTextSize(16f);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD); // Set X-axis labels to bold

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawLabels(false); // This will hide the labels
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(0f);
        leftAxis.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        leftAxis.setTextSize(16f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setTypeface(Typeface.DEFAULT_BOLD); // Set Y-axis labels to bold

        lineChart.getAxisRight().setEnabled(false);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
        legend.setTextSize(22f);
        legend.setTypeface(Typeface.DEFAULT_BOLD); // Set legend labels to bold
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setForm(Legend.LegendForm.NONE);
    }

}

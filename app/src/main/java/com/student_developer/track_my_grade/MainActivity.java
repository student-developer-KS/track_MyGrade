package com.student_developer.track_my_grade;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ListView listView;
    private CustomAdapter adapter; // Use CustomAdapter
    private List<String> gpaDataList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        gpaDataList = new ArrayList<>();
        adapter = new CustomAdapter(this, gpaDataList); // Use CustomAdapter here
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadGPAData();
    }

    private void loadGPAData() {
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Assuming GPAData is a class that holds GPA info
                    String Email = document.getString("Email");
                    String RollNo = document.getString("Roll No");
                    String Password = document.getString("Password");

                    if (Email != null && RollNo != null && Password !=null) {
                        String Users =("Email : "+ Email + "\nRoll No : " + RollNo+ "\nPassword : " +Password);
                        gpaDataList.add(Users);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error getting documents: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        showExitConfirmationDialog(); // Call the method to show the dialog
    }
}

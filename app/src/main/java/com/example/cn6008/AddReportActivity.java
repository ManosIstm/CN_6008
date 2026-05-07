package com.example.cn6008;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cn6008.network.Report;
import com.example.cn6008.network.SupabaseClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReportActivity extends AppCompatActivity {

    private TextInputEditText etTitle;
    private TextInputEditText etDescription;
    private Spinner spinnerCategory;
    private Button btnSubmit;
    
    private double currentLat = 0.0;
    private double currentLng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);

        etTitle = findViewById(R.id.et_report_title);
        etDescription = findViewById(R.id.et_description);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnSubmit = findViewById(R.id.btn_submit_report);

        // Get location from intent
        if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
            currentLat = getIntent().getDoubleExtra("lat", 0.0);
            currentLng = getIntent().getDoubleExtra("lng", 0.0);
        }

        // Setup Spinner
        String[] categories = {"Pothole", "Streetlight", "Flooding", "Vandalism", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String cat = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                submitReport(title, desc, cat, currentLat, currentLng);
            }
        });
    }

    private void submitReport(String title, String desc, String category, double lat, double lng) {
        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", "");
        String authToken = "Bearer " + token;

        Report report = new Report(title, desc, category, lat, lng);

        SupabaseClient.getApi().submitReport(BuildConfig.SUPABASE_KEY, authToken, report).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("SUBMIT");

                if (response.isSuccessful()) {
                    Toast.makeText(AddReportActivity.this, "Report submitted!", Toast.LENGTH_LONG).show();
                    finish(); // Return to map
                } else {
                    Toast.makeText(AddReportActivity.this, "Failed to submit: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("SUBMIT");
                Toast.makeText(AddReportActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

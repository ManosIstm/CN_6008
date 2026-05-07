package com.example.cn6008;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn6008.network.Report;
import com.example.cn6008.network.SupabaseClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerNearby;
    private double currentLat = 0.0;
    private double currentLng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerNearby = findViewById(R.id.recycler_nearby_reports);
        recyclerNearby.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra("lat") && getIntent().hasExtra("lng")) {
            currentLat = getIntent().getDoubleExtra("lat", 0.0);
            currentLng = getIntent().getDoubleExtra("lng", 0.0);
        }

        fetchNearbyReports();
    }

    private void fetchNearbyReports() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", "");
        String authToken = "Bearer " + token;

        SupabaseClient.getApi().getReports(BuildConfig.SUPABASE_KEY, authToken, "*").enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Report> allReports = response.body();
                    
                    // Calculate distance for each report
                    float[] results = new float[1];
                    for (Report r : allReports) {
                        Location.distanceBetween(currentLat, currentLng, r.getLatitude(), r.getLongitude(), results);
                        r.setDistanceToUser(results[0]);
                    }

                    // Sort by distance (ascending)
                    Collections.sort(allReports, (r1, r2) -> Double.compare(r1.getDistanceToUser(), r2.getDistanceToUser()));

                    NearbyReportAdapter adapter = new NearbyReportAdapter(allReports);
                    recyclerNearby.setAdapter(adapter);
                } else {
                    Toast.makeText(ListActivity.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Toast.makeText(ListActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.cn6008;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.cn6008.network.Report;
import com.example.cn6008.network.SupabaseClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerReports;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerReports = findViewById(R.id.recycler_user_reports);
        recyclerReports.setLayoutManager(new LinearLayoutManager(this));

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profile);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_map) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (itemId == R.id.nav_list) {
                Intent intent = new Intent(ProfileActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });

        fetchUserReports();
    }

    private void fetchUserReports() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", "");
        String userId = prefs.getString("user_id", "");
        String authToken = "Bearer " + token;

        if (userId.isEmpty()) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String userIdQuery = "eq." + userId;

        SupabaseClient.getApi().getUserReports(BuildConfig.SUPABASE_KEY, authToken, userIdQuery, "*").enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Report> reports = response.body();
                    if (reports.isEmpty()) {
                        Toast.makeText(ProfileActivity.this, "No reports found for you.", Toast.LENGTH_SHORT).show();
                    }
                    ReportAdapter adapter = new ReportAdapter(reports);
                    recyclerReports.setAdapter(adapter);
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

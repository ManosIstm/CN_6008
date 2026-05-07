package com.example.cn6008;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cn6008.network.Report;

import java.util.List;
import java.util.Locale;

public class NearbyReportAdapter extends RecyclerView.Adapter<NearbyReportAdapter.NearbyViewHolder> {

    private List<Report> reports;

    public NearbyReportAdapter(List<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_nearby, parent, false);
        return new NearbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        Report report = reports.get(position);
        holder.tvTitle.setText(report.getTitle());
        holder.tvCategory.setText(report.getCategory());
        holder.tvDesc.setText(report.getDescription());
        
        // Color coding for categories
        int bgColor = Color.parseColor("#757575"); // Default Grey
        String cat = report.getCategory();
        if (cat != null) {
            switch (cat.toLowerCase()) {
                case "pothole": bgColor = Color.parseColor("#FF9800"); break; // Orange
                case "streetlight": bgColor = Color.parseColor("#FFC107"); break; // Yellow/Amber
                case "flooding": bgColor = Color.parseColor("#2196F3"); break; // Blue
                case "vandalism": bgColor = Color.parseColor("#F44336"); break; // Red
            }
        }
        
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(bgColor);
        gd.setCornerRadius(16f);
        holder.tvCategory.setBackground(gd);
        
        double dist = report.getDistanceToUser();
        if (dist > 1000) {
            holder.tvDistance.setText(String.format(Locale.getDefault(), "%.1f km", dist / 1000.0));
        } else {
            holder.tvDistance.setText(String.format(Locale.getDefault(), "%.0f m", dist));
        }
    }

    @Override
    public int getItemCount() {
        return reports == null ? 0 : reports.size();
    }

    static class NearbyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDesc, tvDistance;

        public NearbyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_nearby_title);
            tvCategory = itemView.findViewById(R.id.tv_nearby_category);
            tvDesc = itemView.findViewById(R.id.tv_nearby_desc);
            tvDistance = itemView.findViewById(R.id.tv_nearby_distance);
        }
    }
}

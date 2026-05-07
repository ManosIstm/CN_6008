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

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reports;

    public ReportAdapter(List<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return reports == null ? 0 : reports.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDesc;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvCategory = itemView.findViewById(R.id.tv_item_category);
            tvDesc = itemView.findViewById(R.id.tv_item_desc);
        }
    }
}

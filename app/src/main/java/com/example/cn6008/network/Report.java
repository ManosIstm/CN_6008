package com.example.cn6008.network;

public class Report {
    private String title;
    private String description;
    private String category;
    private double latitude;
    private double longitude;

    public Report(String title, String description, String category, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}

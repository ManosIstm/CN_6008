package com.example.cn6008.network;

public class Report {
    private String title;
    private String description;
    private String category;
    private double latitude;
    private double longitude;
    private String user_id;
    private transient double distanceToUser;

    public Report(String title, String description, String category, double latitude, double longitude, String user_id) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getUserId() { return user_id; }
    
    public double getDistanceToUser() { return distanceToUser; }
    public void setDistanceToUser(double distance) { this.distanceToUser = distance; }
}

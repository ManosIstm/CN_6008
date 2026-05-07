package com.example.cn6008.network;

public class LoginResponse {
    private String access_token;
    private User user;

    public String getAccessToken() {
        return access_token;
    }

    public User getUser() {
        return user;
    }

    public static class User {
        private String id;
        private String email;

        public String getId() { return id; }
        public String getEmail() { return email; }
    }
}

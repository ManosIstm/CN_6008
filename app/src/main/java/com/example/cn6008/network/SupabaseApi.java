package com.example.cn6008.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SupabaseApi {
    @POST("auth/v1/token")
    Call<LoginResponse> login(
            @Query("grant_type") String grantType,
            @Header("apikey") String apiKey,
            @Body LoginRequest request
    );

    @POST("auth/v1/signup")
    Call<LoginResponse> register(
            @Header("apikey") String apiKey,
            @Body LoginRequest request
    );

    @POST("rest/v1/reports")
    Call<Void> submitReport(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Body Report report
    );
}

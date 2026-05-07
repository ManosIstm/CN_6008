package com.example.cn6008.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import java.util.List;

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

    @GET("rest/v1/reports")
    Call<List<Report>> getReports(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Query("select") String select
    );

    @GET("rest/v1/reports")
    Call<List<Report>> getUserReports(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Query("user_id") String userIdQuery,
            @Query("select") String select
    );
}

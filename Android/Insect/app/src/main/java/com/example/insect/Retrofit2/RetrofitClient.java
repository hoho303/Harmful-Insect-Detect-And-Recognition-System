package com.example.insect.Retrofit2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static Retrofit getClient(String baseUrl)
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .readTimeout(2, TimeUnit.MINUTES)
                                        .connectTimeout(3,TimeUnit.MINUTES)
                                        .writeTimeout(2,TimeUnit.MINUTES)
                                        .retryOnConnectionFailure(true)
                                        .build();
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .client(okHttpClient)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .build();
        return retrofit;
    }
}

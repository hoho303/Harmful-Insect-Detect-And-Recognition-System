package com.example.insect.Retrofit2;

public class APIUtils {
    public static final String baseUrl = "http://115.79.118.196:5000/";

    public static DataClient getData()
    {
        return RetrofitClient.getClient(baseUrl).create(DataClient.class);
    }
}

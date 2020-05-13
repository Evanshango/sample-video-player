package com.evans.sampleimpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static final String BASE_URL = "http://139.59.155.134:8080/";
    private static Retrofit general = null;
    private static OkHttpClient sOkHttpClient;

    private static Retrofit getGeneral(){
        if (general == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            showLogs(logging);

            general = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(sOkHttpClient)
//                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return general;
    }

    private static void showLogs(HttpLoggingInterceptor logging) {
        sOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }

    public static Api getApiClient() {
        return getGeneral().create(Api.class);
    }
}

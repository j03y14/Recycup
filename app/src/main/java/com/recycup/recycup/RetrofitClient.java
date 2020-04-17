package com.recycup.recycup;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private RetrofitBaseApiService apiService;
    private static final String URL = RetrofitBaseApiService.Base_Url;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {

        return SingletonHolder.INSTANCE;
    }


    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        createBaseApi();
    }

    public void createBaseApi(){
        apiService = create(RetrofitBaseApiService.class);
    }
    public static <S> S create(Class<S> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public void createAccount(String phoneNumber, String name, String password, final RetroCallback callback){
        apiService.createAccount(phoneNumber, name, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void signIn(String phoneNumber, String cryptoPW, final RetroCallback callback){
        apiService.signIn(phoneNumber,cryptoPW).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
    public void duplicateCheck(String phoneNumber, final RetroCallback callback){
        apiService.duplicateCheck(phoneNumber).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
    public void getCupInfo(String phoneNumber, final RetroCallback callback){
        apiService.getCupInfo(phoneNumber).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}

package com.recycup.recycup_cafe;

import android.util.Log;

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
        private static RetrofitClient KAKAOPAY_INSTANCE = new RetrofitClient();
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


    public void registerCafe(String cafeId, String cafePassword, String headName, String cafeName, double latitude, double longitude, final RetroCallback callback){
        apiService.registerCafe(cafeId, cafePassword,headName, cafeName, latitude, longitude).enqueue(new Callback<JsonObject>() {
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



    public void cafeSignIn(String cafeId, String cafePassword, final RetroCallback callback){
        apiService.cafeSignIn(cafeId,cafePassword).enqueue(new Callback<JsonObject>() {
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
    public void signIn(String phoneNumber, String password, final RetroCallback callback){
        apiService.signIn(phoneNumber,password).enqueue(new Callback<JsonObject>() {
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


    public void duplicateCheck(String cafeId, final RetroCallback callback){
        apiService.duplicateCheck(cafeId).enqueue(new Callback<JsonObject>() {
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

    public void sales(String phoneNumber, String headName, String date, int amount, final RetroCallback callback){
        apiService.sales(phoneNumber,headName,date,amount).enqueue(new Callback<JsonObject>() {
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

    public void returnCup(String phoneNumber, String headName, final RetroCallback callback){
        apiService.returnCup(phoneNumber,headName).enqueue(new Callback<JsonObject>() {
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


}

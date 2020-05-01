package com.recycup.recycup;

import android.content.Context;
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
    private KakaoApiService kakaoApiService;
    private static final String URL = RetrofitBaseApiService.Base_Url;
    private static final String KAKAO_URL = KakaoApiService.Base_Url;
    private static Retrofit retrofit;
    private static Retrofit kakaoRetrofit;

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
        private static RetrofitClient KAKAOPAY_INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {

        return SingletonHolder.INSTANCE;
    }

    public static RetrofitClient getKakaoInstance() {

        return SingletonHolder.KAKAOPAY_INSTANCE;
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

        kakaoRetrofit = new Retrofit.Builder()
                .baseUrl(KAKAO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        createKakaoApi();
    }

    public void createBaseApi(){
        apiService = create(RetrofitBaseApiService.class);
    }
    public void createKakaoApi(){
        kakaoApiService = createKakao(KakaoApiService.class);
    }
    public static <S> S create(Class<S> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
    public static <S> S createKakao(Class<S> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return kakaoRetrofit.create(service);
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

    public void paymentReady( String cid, String partner_order_id, String partner_user_id, String item_name, String quantity,
                              String total_amount, String tax_free_amount, String approval_url,
                              String cancel_url, String fail_url, final RetroCallback callback){
        kakaoApiService.paymentReady(cid,partner_order_id,partner_user_id,item_name,quantity,total_amount,tax_free_amount,approval_url,cancel_url,fail_url).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                    Log.e("response body", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("paymentReady", t.toString());
            }
        });
    }

    public void paymentApprove(String cid, String tid, String partner_order_id, String partner_user_id, String pg_token, final RetroCallback callback){
        kakaoApiService.paymentApprove(cid, tid, partner_order_id, partner_user_id, pg_token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                    Log.e("response body", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("paymentApprove", t.toString());
            }
        });
    }

    public void getPoint(String phoneNumber, final RetroCallback callback){
        apiService.getPoint(phoneNumber).enqueue(new Callback<JsonObject>() {
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

    public void getStatistics(String phoneNumber, final RetroCallback callback){
        apiService.getStatistics(phoneNumber).enqueue(new Callback<JsonObject>() {
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

    public void addPoint(String phoneNumber, int amount,final RetroCallback callback){
        apiService.addPoint(phoneNumber, amount).enqueue(new Callback<JsonObject>() {
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

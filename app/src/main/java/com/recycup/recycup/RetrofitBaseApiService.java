package com.recycup.recycup;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface RetrofitBaseApiService {
    final String Base_Url = "https://localhost";

    //회원가입
    @FormUrlEncoded
    @POST("/signUp/")
    Call<JsonObject> createAccount(@Field("phoneNumber") String phoneNumber, @Field("password") String password, @Field("name") String name);

    //로그인
    @FormUrlEncoded
    @POST("/signIn/")
    Call<JsonObject> signIn(@Field("phoneNumber") String phoneNumber, @Field("password") String password);

    @FormUrlEncoded
    @POST("/duplicateCheck/")
    Call<JsonObject> duplicateCheck(@Field("phoneNumber") String phoneNumber);

}

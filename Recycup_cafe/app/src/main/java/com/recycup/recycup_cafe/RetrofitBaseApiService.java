package com.recycup.recycup_cafe;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface RetrofitBaseApiService {
    final String Base_Url = "http://59.187.219.187:22406";

    //카페 지점 등록
    //카페 지점이 있으면 그 카페의 정보를 가져오고 카페 지점이 없으면 등록 후 정보를 가져온다.
    //input : headName(String), cafeName (String) , latitude, longitude (double)
    //return : success(bool), headName(String), cafeName(String), material(String)
    @FormUrlEncoded
    @POST("cafe/cafeInfo/head/register")
    Call<JsonObject> registerCafe(@Field("headName") String headName, @Field("cafeName") String cafeName, @Field("latitude") double latitude, @Field("longitude") double longitude);


    //로그인
    //input : phoneNumber, password (string)
    //return : phoneNumber, name, password (string), point(int)
    @FormUrlEncoded
    @POST("/customer/signIn")
    Call<JsonObject> signIn(@Field("phoneNumber") String phoneNumber, @Field("password") String password);

    //가입되어있는 전화번호인지 확인
    //input : phoneNumber

}

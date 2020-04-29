package com.recycup.recycup;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface RetrofitBaseApiService {
    final String Base_Url = "http://35.229.219.32:8888";

    //회원가입
    //input : phoneNumber, password, name (string)
    //return : phoneNumber, password, name (string)
    @FormUrlEncoded
    @POST("/signUp")
    Call<JsonObject> createAccount(@Field("phoneNumber") String phoneNumber, @Field("name") String name, @Field("password") String password);

    //로그인
    //input : phoneNumber, password (string)
    //return : success(boolean형)
    @FormUrlEncoded
    @POST("/signIn")
    Call<JsonObject> signIn(@Field("phoneNumber") String phoneNumber, @Field("password") String password);

    //회원가입 할 때 핸드폰 번호 중복 체크
    //input : phoneNumber (string)
    //return : duplicate (boolean)
    @FormUrlEncoded
    @POST("/duplicateCheck")
    Call<JsonObject> duplicateCheck(@Field("phoneNumber") String phoneNumber);

    //회원가입 할 때 핸드폰 번호 중복 체크
    //input : phoneNumber (string)
    //return : ['cafeId'= (int), 'cafeName'= (String), 'cupNumber'=(int),'cupMeterial'=(String), 'cafeLogo'=(string)] (JsonArray)
    //cafeId는 cafe의 primarykey, cafeName은 카페 이름, cupNumber는 컵 개수, cupMeterial은 컵의 소재(pet, pp, ps 등),cafeLogo는 카페의 이미지 url
    @FormUrlEncoded
    @POST("/cupInfo/get")
    Call<JsonArray> getCupInfo(@Field("phoneNumber") String phoneNumber);
}

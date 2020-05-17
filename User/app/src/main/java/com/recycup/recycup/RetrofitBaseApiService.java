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
    @POST("/customer/signUp")
    Call<JsonObject> createAccount(@Field("phoneNumber") String phoneNumber, @Field("name") String name, @Field("password") String password);

    //로그인
    //input : phoneNumber, password (string)
    //return : phoneNumber, name, password (string)
    @FormUrlEncoded
    @POST("/customer/signIn")
    Call<JsonObject> signIn(@Field("phoneNumber") String phoneNumber, @Field("password") String password);

    //회원가입 할 때 핸드폰 번호 중복 체크
    //input : phoneNumber (string)
    //return : duplicate (boolean)
    @FormUrlEncoded
    @POST("/customer/duplicateCheck")
    Call<JsonObject> duplicateCheck(@Field("phoneNumber") String phoneNumber);

    //카페 정보를 가져온다.
    //return : ['headName'= (String),'cupMeterial'=(String), 'cafeLogo'=(string)] (JsonArray)
    //cafeName은 카페 이름, cupNumber는 컵 개수, cupMeterial은 컵의 소재(pet, pp, ps 등),cafeLogo는 카페의 이미지 url
    @POST("/cafe/headInfo/get")
    Call<JsonArray> getCupInfo();


    //해당 회원의 이번 달 구매 반납 통계를 가져온다.
    //input : phoneNumber (string)
    //return : takeOutNumber (int) 는 이번 달 구매 컵 갯수, returnNumber (int) 는 이번 달 반납 컵 갯수, point (int)
    @FormUrlEncoded
    @POST("/customer/customerInfo/get")
    Call<JsonObject> getStatistics(@Field("phoneNumber") String phoneNumber);

    //해당 회원의 포인트를 amount만큼 충전한다.
    //input : phoneNumber (string), amount (int) : 충전할 포인트 양
    //return : success (boolean)
    @FormUrlEncoded
    @POST("/customer/charge")
    Call<JsonObject> addPoint(@Field("phoneNumber") String phoneNumber,@Field("amount") int amount);

    //정해진 위치 주변의 카페나 쓰레기통의 위치를 가져온다. (카페,위도, 경도를 넘겨서 그 위치로부터 일정 거리 안에 있는
    //input : cafeName (string), latitude (double), longitude (double)
    //return : ['latitude'=(double), 'longitude'=(double), 'cafeLogo'=(string)] (JsonArray)
    //예를들어 '스타벅스', 35, 36 을 넘기면, 위도 35, 경도 36 주변 일정 거리 안에 있는 스타벅스들의 위도 경도와 로고 url을 넘긴다.
    @FormUrlEncoded
    @POST("/cafe/cafeInfo/get")
    Call<JsonArray> getLocationsOf(@Field("cafeName") String cafeName,@Field("latitude") double latitude,@Field("longitude") double longitude);

}

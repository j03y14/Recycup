package com.recycup.recycup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface KakaoApiService {
    final String Base_Url = "https://kapi.kakao.com";

    @Headers({"Authorization: KakaoAK 17023469abbce4ce2480e82906db58b8"})
    @FormUrlEncoded
    @POST("/v1/payment/ready")
    Call<JsonObject> paymentReady(@Field("cid") String cid, @Field("partner_order_id") String partner_order_id,@Field("partner_user_id") String partner_user_id, @Field("item_name") String item_name, @Field("quantity") String quantity,
                                  @Field("total_amount") String total_amount, @Field("tax_free_amount") String tax_free_amount, @Field("approval_url") String approval_url,
                                  @Field("cancel_url") String cancel_url, @Field("fail_url") String fail_url);

    @Headers({"Authorization: KakaoAK 17023469abbce4ce2480e82906db58b8"})
    @FormUrlEncoded
    @POST("/v1/payment/approve")
    Call<JsonObject> paymentApprove(@Field("cid") String cid, @Field("tid") String tid, @Field("partner_order_id") String partner_order_id, @Field("partner_user_id") String partner_user_id, @Field("pg_token") String pg_token);
}

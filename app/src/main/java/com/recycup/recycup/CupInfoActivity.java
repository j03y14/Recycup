package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CupInfoActivity extends AppCompatActivity {
    RetrofitClient retrofitClient;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup_info);

        retrofitClient = RetrofitClient.getInstance();
        user = User.getInstance();
    }

    public void getCupInfo(){
        retrofitClient.getCupInfo(user.getPhoneNumber(), new RetroCallback<JsonArray>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonArray receivedData) {
                for(int i=0; i<receivedData.size(); i++){
                    JsonObject data = (JsonObject) receivedData.get(i);
                    int cafeId = data.get("cafeId").getAsInt();
                    String cafeName = data.get("cafeName").getAsString();
                    int cupNumber = data.get("cupNumber").getAsInt();
                    String cupMeterial = data.get("cupMeterial").getAsString();
                    String cafeLogo = data.get("cafeLogo").getAsString();
                }
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }
}

package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CupInfoActivity extends AppCompatActivity {
    RetrofitClient retrofitClient;
    User user;
    Toolbar toolbar;
    RecyclerView recyclerView;

    CupInfoItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cup_info);

        retrofitClient = RetrofitClient.getInstance();
        user = User.getInstance();

        toolbar = (Toolbar) findViewById(R.id.cupInfoToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CupInfoItemAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.cupInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getTempCupInfo();
    }

    private void getTempCupInfo() {
        for(int i=0; i< 20; i++){
            String cafeLogo = "https://upload.wikimedia.org/wikipedia/ko/a/aa/%ED%83%90%EC%95%A4%ED%83%90%EC%8A%A4_%EB%A1%9C%EA%B3%A0.png";
            String cupMaterial = "pp";
            int cupNumber = 10;
            String cafeName = "탐앤탐스";
            adapter.addItem(new CupInfo(1, cafeName,cupNumber,cupMaterial,cafeLogo));
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

        adapter = new CupInfoItemAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.cupInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getCupInfo();
        //getTempCupInfo();
    }

    private void getTempCupInfo() {

        String cafeLogo = "https://upload.wikimedia.org/wikipedia/ko/a/aa/%ED%83%90%EC%95%A4%ED%83%90%EC%8A%A4_%EB%A1%9C%EA%B3%A0.png";
        String cupMaterial = "pp";
        String cafeName = "탐앤탐스";
        adapter.addItem(new CupInfo(cafeName,cupMaterial,cafeLogo));

        cafeLogo = "http://pimage.design.co.kr/cms/contents/direct/info_id/55928/1305623912181.jpg";
        cupMaterial = "pet";
        cafeName = "스타벅스";
        adapter.addItem(new CupInfo( cafeName,cupMaterial,cafeLogo));


    }

    public void getCupInfo(){
        retrofitClient.getCupInfo( new RetroCallback<JsonArray>() {
            @Override
            public void onError(Throwable t) {

                Log.e("error", t.toString());
            }

            @Override
            public void onSuccess(int code, JsonArray receivedData) {
                for(int i=0; i<receivedData.size(); i++){
                    JsonObject data = (JsonObject) receivedData.get(i);

                    String headName = data.get("headName").getAsString();
                    String cupMeterial = data.get("type").getAsString();
                    String cafeLogo = data.get("logoPath").getAsString();

                    adapter.addItem(new CupInfo( headName, cupMeterial, cafeLogo));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
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

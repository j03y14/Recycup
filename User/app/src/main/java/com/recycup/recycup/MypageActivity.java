package com.recycup.recycup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class MypageActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView nameTextView;
    TextView pointTextView;
    Button chargePointButton;
    ProgressBar statisticsBar;
    TextView takeOutCupNumber;
    TextView returnCupNumber;

    User user;
    RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        toolbar = findViewById(R.id.mypageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        chargePointButton = (Button) findViewById(R.id.chargePointButton);
        statisticsBar = (ProgressBar) findViewById(R.id.statisticsBar);
        takeOutCupNumber = (TextView) findViewById(R.id.takeOutCupNumber);
        returnCupNumber = (TextView) findViewById(R.id.returnCupNumber);

        user = User.getInstance();

        //getPoint(user.phoneNumber);
        //getStatistics(user.phoneNumber);

        chargePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChargePointActivity.class);
                startActivityForResult(intent, 2001);
            }
        });

        retrofitClient = RetrofitClient.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2001 && resultCode ==1){
            getPoint(user.phoneNumber);
            getStatistics(user.phoneNumber);
        }
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

    public void getPoint(String phoneNumber){
        retrofitClient.getPoint(phoneNumber, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                int point = receivedData.get("point").getAsInt();
                pointTextView.setText(String.valueOf(point) + "p");

            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

    public void getStatistics(String phoneNumber){
        retrofitClient.getStatistics(phoneNumber, new RetroCallback<JsonObject>(){

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                int takeOutNumer = receivedData.get("takeOutNumer").getAsInt();
                int returnNumber = receivedData.get("returnNumber").getAsInt();

                int percent = takeOutNumer / returnNumber * 100;

                takeOutCupNumber.setText(String.valueOf(takeOutNumer));
                returnCupNumber.setText(String.valueOf(returnNumber));

                statisticsBar.setProgress(percent);
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }
}

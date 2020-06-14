package com.recycup.recycup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    Button logoutButton;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    User user;
    RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mypage);



        retrofitClient = RetrofitClient.getInstance();
        toolbar = findViewById(R.id.mypageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        chargePointButton = (Button) findViewById(R.id.chargePointButton);
        statisticsBar = (ProgressBar) findViewById(R.id.statisticsBar);
        statisticsBar.setMax(100);
        takeOutCupNumber = (TextView) findViewById(R.id.takeOutCupNumber);
        returnCupNumber = (TextView) findViewById(R.id.returnCupNumber);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        sp = getSharedPreferences("sp", Activity.MODE_PRIVATE);
        editor = sp.edit();
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("phoneNumber");
                editor.remove("password");

                editor.commit();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        user = User.getInstance();

        nameTextView.setText(user.name);

        getStatistics(user.phoneNumber);

        chargePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChargePointActivity.class);
                startActivityForResult(intent, 2001);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2001 && resultCode ==1){


        }
//        nameTextView.setText(user.name);
//        getStatistics(user.phoneNumber);
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

    @Override
    protected void onResume() {
        super.onResume();
        nameTextView.setText(user.name);

        getStatistics(user.phoneNumber);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        nameTextView.setText(user.name);

        getStatistics(user.phoneNumber);
    }

    public void getStatistics(String phoneNumber){
        retrofitClient.getStatistics(phoneNumber, new RetroCallback<JsonObject>(){

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                int takeOutNumer = receivedData.get("sales").getAsInt();
                int returnNumber = receivedData.get("return").getAsInt();
                int point = receivedData.get("point").getAsInt();
                int percent = (int) (100.0 * returnNumber / takeOutNumer);

                takeOutCupNumber.setText(String.valueOf(takeOutNumer));
                returnCupNumber.setText(String.valueOf(returnNumber));
                pointTextView.setText(String.valueOf(point));
                statisticsBar.setProgress(percent);
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }
}

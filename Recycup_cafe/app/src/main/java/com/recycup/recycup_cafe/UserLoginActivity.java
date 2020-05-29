package com.recycup.recycup_cafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.StringTokenizer;

public class UserLoginActivity extends AppCompatActivity {

    ConstraintLayout phoneNumberLogin;
    ConstraintLayout QRLogin;
    Activity activity;
    RetrofitClient retrofitClient;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        Intent intent = getIntent();

        if(intent.hasExtra("from")){
            from = intent.getStringExtra("from");

        }


        retrofitClient = RetrofitClient.getInstance();
        activity = this;
        phoneNumberLogin = findViewById(R.id.returnContainer);
        phoneNumberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneNumberLoginActivity.class);
                intent.putExtra("from", from);
                startActivity(intent);
            }
        });
        QRLogin = findViewById(R.id.saleContainer);
        QRLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
                intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리
                intentIntegrator.setCameraId(1);  // Use a specific camera of the device

                intentIntegrator.initiateScan();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                StringTokenizer tokens = new StringTokenizer(result.getContents());

                String phoneNumber = tokens.nextToken(",");
                String cryptoPassword = tokens.nextToken(",");
                signIn(phoneNumber,cryptoPassword);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void signIn(String phoneNumber, String cryptoPW) {
        retrofitClient.signIn(phoneNumber, cryptoPW, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", t.toString());
            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                User user = User.getInstance();
                String phone = receivedData.get("phoneNumber").getAsString();
                String name = receivedData.get("name").getAsString();
                String cryptoPW = receivedData.get("password").getAsString();
                int point = receivedData.get("point").getAsInt();

                user.setName(name);
                user.setPassword(cryptoPW);
                user.setPhoneNumber(phone);
                user.setPoint(point);

                Intent intent;
                if(from.equals("return")){
                    intent = new Intent(getApplicationContext(), ReturnCupActivity.class);
                    startActivity(intent);
                }else if(from.equals("sale")){
                    intent = new Intent(getApplicationContext(), SaleActivity.class);
                    startActivity(intent);
                }




            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
            }
        });
    }
}


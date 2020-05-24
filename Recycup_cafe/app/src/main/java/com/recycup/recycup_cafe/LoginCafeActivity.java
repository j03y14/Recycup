package com.recycup.recycup_cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;

public class LoginCafeActivity extends AppCompatActivity {

    EditText cafeIdEditText;
    EditText cafePasswordEditText;
    Button submitButton;
    Button registerButtton;

    RetrofitClient retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cafe);

        retrofitClient = RetrofitClient.getInstance();


        cafeIdEditText = findViewById(R.id.cafeIdEditText);
        cafePasswordEditText = findViewById(R.id.cafePasswordEditText);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cafeId = cafeIdEditText.getText().toString();
                String cafePassword = cafePasswordEditText.getText().toString();
                String cryptoPW;



                try {
                    cryptoPW = Crypto.sha256(cafePassword);
                    //서버로 보내는 부분
                    cafeSignIn(cafeId, cryptoPW);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        registerButtton = findViewById(R.id.registerButton);
        registerButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterCafeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void cafeSignIn(String cafeId, String cafePassword){
        retrofitClient.cafeSignIn(cafeId, cafePassword, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                if(receivedData.get("success").getAsBoolean()){
                    Cafe.getInstance().cafeName = receivedData.get("cafeName").getAsString();
                    Cafe.getInstance().headName = receivedData.get("headName").getAsString();
                    Cafe.getInstance().material = receivedData.get("material").getAsString();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginCafeActivity.this, receivedData.get("error").getAsString(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
            }
        });
    }


}

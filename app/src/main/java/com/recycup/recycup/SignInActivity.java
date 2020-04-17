package com.recycup.recycup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;

public class SignInActivity extends AppCompatActivity {

    RetrofitClient retroClient;
    EditText phoneNumberEditText;
    EditText passwordEditText;

    //로그인 버튼
    Button signInButton;
    //회원가입 버튼
    Button signUpButton;

    SharedPreferences sp;

    int SIGN_UP_ACTIVITY_START = 1001;
    int FROM_SIGN_UP = 2001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sp = getSharedPreferences("sp", Activity.MODE_PRIVATE); //(저장될 키, 값)
        String loginID = sp.getString("phoneNumber", ""); // 처음엔 값이 없으므로 ""
        String loginPW = sp.getString("password","");

        //이전에 로그인 한 정보가 있으면 자동 로그인
        if(loginID != "" && loginPW != "") {
            signIn(loginID,loginPW);
        }


        retroClient = RetrofitClient.getInstance();

        phoneNumberEditText = (EditText) findViewById(R.id.phoneInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);

        signUpButton = (Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent , SIGN_UP_ACTIVITY_START);
            }
        });

        signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String cryptoPW;



                try {
                    cryptoPW = Crypto.sha256(password);
                    //서버로 보내는 부분
                    signIn(phoneNumber, cryptoPW);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void signIn(String phoneNumber, String cryptoPW){
        retroClient.signIn(phoneNumber, cryptoPW, new RetroCallback<JsonObject>() {
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

                user.setName(name);
                user.setPassword(cryptoPW);
                user.setPhoneNumber(phone);

                SharedPreferences.Editor editor = sp.edit(); //로그인 정보 저장
                editor.putString("phoneNumber", user.getPhoneNumber());
                editor.putString("password", user.getPassword());

                editor.commit();
                //메인 액티비티로

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();

            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_UP_ACTIVITY_START){
            if(resultCode == FROM_SIGN_UP){
                String phoneNumber = data.getStringExtra("phoneNumber");
                phoneNumberEditText.setText(phoneNumber);
            }
        }
    }
}

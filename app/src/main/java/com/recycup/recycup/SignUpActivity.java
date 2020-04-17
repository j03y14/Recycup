package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

    RetrofitClient retroClient;
    EditText phoneEditText;
    EditText nameEditText;
    EditText passwordEditText;

    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        retroClient = RetrofitClient.getInstance();

        phoneEditText = (EditText) findViewById(R.id.phoneInput);
        nameEditText = (EditText) findViewById(R.id.nameInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);

        signUpButton =(Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phoneEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String cryptoPW;
                try {
                    cryptoPW = Crypto.sha256(password);
                    //서버로 보내는 부분
                    createAccount(phoneNumber,name, cryptoPW);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void createAccount(String phoneNumber, String name, String password){
        retroClient.createAccount(phoneNumber, name, password, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                //로그인 액티비티로
            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
            }
        });
    }
}

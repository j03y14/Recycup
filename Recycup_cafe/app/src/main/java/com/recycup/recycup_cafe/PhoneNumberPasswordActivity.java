package com.recycup.recycup_cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;

public class PhoneNumberPasswordActivity extends AppCompatActivity {
    TextView passwordTextView;
    NumberKeyboard numberKeyboard;
    Button nextButton;
    Button previousButton;
    String phoneNumber;
    String from;

    RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_password);

        Intent intent = getIntent();

        if(intent.hasExtra("phoneNumber")){
            phoneNumber = intent.getStringExtra("phoneNumber");
        }
        if(intent.hasExtra("from")){
            from = intent.getStringExtra("from");
        }

        retrofitClient = RetrofitClient.getInstance();

        passwordTextView = findViewById(R.id.passwordTextView);
        numberKeyboard = findViewById(R.id.numberKeyBoard);
        numberKeyboard.setListener(new NumberKeyboardListener() {
            @Override
            public void onNumberClicked(int i) {
                passwordTextView.append(String.valueOf(i));
            }

            @Override
            public void onLeftAuxButtonClicked() {

            }

            @Override
            public void onRightAuxButtonClicked() {
                String string = passwordTextView.getText().toString().substring(0, passwordTextView.getText().toString().length()-1);

                passwordTextView.setText(string);
            }
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cryptoPW = null;
                try {
                    cryptoPW = Crypto.sha256(passwordTextView.getText().toString());
                    signIn(phoneNumber, cryptoPW);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        });
        previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

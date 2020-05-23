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

public class PhoneNumberPasswordActivity extends AppCompatActivity {
    TextView passwordTextView;
    NumberKeyboard numberKeyboard;
    Button nextButton;
    Button previousButton;
    String phoneNumber;

    RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_password);

        Intent intent = getIntent();

        if(intent.hasExtra("phoneNumber")){
            phoneNumber = intent.getStringExtra("phoneNumber");
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
                signIn(phoneNumber, passwordTextView.getText().toString());
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

                user.setName(name);
                user.setPassword(cryptoPW);
                user.setPhoneNumber(phone);


                Intent intent = new Intent(getApplicationContext(), ReturnCupActivity.class);
                intent.putExtra("phoneNumber", passwordTextView.getText().toString());

                startActivity(intent);

            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
            }
        });
    }
}

package com.recycup.recycup_cafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends AppCompatActivity {

    ConstraintLayout phoneNumberLogin;
    ConstraintLayout QRLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNumberLogin = findViewById(R.id.returnContainer);
        phoneNumberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PhoneNumberLoginActivity.class);
                startActivity(intent);
            }
        });
        QRLogin = findViewById(R.id.saleContainer);
        QRLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), QRLoginActivity.class);
//                startActivity(intent);
            }
        });


    }
}


package com.recycup.recycup_cafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davidmiguel.numberkeyboard.NumberKeyboard;
import com.davidmiguel.numberkeyboard.NumberKeyboardListener;
import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleActivity extends AppCompatActivity {
    TextView salesNumberTextView;
    TextView nameTextView;
    TextView pointTextView;
    NumberKeyboard numberKeyboard;
    Button nextButton;
    Button previousButton;

    ConstraintLayout successContainer;
    Button successButton;
    int price;

    RetrofitClient retrofitClient;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        retrofitClient = RetrofitClient.getInstance();
        user = User.getInstance();


        //컵 가격을 300원이라고 가정
        price = 500;

        nameTextView = findViewById(R.id.name);
        nameTextView.setText(user.getName());
        pointTextView = findViewById(R.id.point);
        pointTextView.setText(String.valueOf(user.getPoint()));

        salesNumberTextView = findViewById(R.id.salesNumberTextView);
        numberKeyboard = findViewById(R.id.numberKeyBoard);
        numberKeyboard.setListener(new NumberKeyboardListener() {
            @Override
            public void onNumberClicked(int i) {
                if(salesNumberTextView.getText().toString().equals("") && i==0){
                    return;
                }
                salesNumberTextView.append(String.valueOf(i));

                if(Integer.parseInt(salesNumberTextView.getText().toString())* price > user.point){
                    nextButton.setEnabled(false);
                    nextButton.setTextColor(Color.LTGRAY);
                }else{
                    nextButton.setEnabled(true);
                    nextButton.setTextColor(Color.parseColor("#1A701A"));                }
            }

            @Override
            public void onLeftAuxButtonClicked() {

            }

            @Override
            public void onRightAuxButtonClicked() {
                if(salesNumberTextView.getText().toString().length() == 0){
                    return;
                }
                String string = salesNumberTextView.getText().toString().substring(0, salesNumberTextView.getText().toString().length()-1);

                salesNumberTextView.setText(string);
                if(salesNumberTextView.getText().toString().equals("")){
                    nextButton.setEnabled(false);
                    nextButton.setTextColor(Color.LTGRAY);
                    return;
                }
                if(Integer.parseInt(salesNumberTextView.getText().toString()) * price > user.point){
                    nextButton.setEnabled(false);
                    nextButton.setTextColor(Color.LTGRAY);
                }else{
                    nextButton.setEnabled(true);
                    nextButton.setTextColor(Color.parseColor("#1A701A"));

                }
            }
        });

        nextButton = findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
        nextButton.setTextColor(Color.LTGRAY);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
                String formatDate = sdfNow.format(date);


                sales(user.phoneNumber, Cafe.getInstance().headName,formatDate, Integer.parseInt(salesNumberTextView.getText().toString()));

            }
        });
        previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                user.clear();
                startActivity(intent);
            }
        });

        successContainer = findViewById(R.id.successContainer);
        successButton = findViewById(R.id.successButton);
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                user.clear();
                startActivity(intent);
            }
        });
    }

    public void sales(String phoneNumber, String headName, String date, int amount){
        retrofitClient.sales(phoneNumber, headName, date, amount, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                if(receivedData.get("success").getAsBoolean()){
                    successContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }
}

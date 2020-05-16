package com.recycup.recycup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;


public class ChargePointActivity extends AppCompatActivity {

    Toolbar chargeToolbar;
    EditText chargeAmount;
    Button minusButton;
    Button plusButton;
    Button chargeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_point);

        chargeToolbar = (Toolbar) findViewById(R.id.chargeToolbar);
        setSupportActionBar(chargeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chargeAmount = (EditText) findViewById(R.id.chargeAmount);
        minusButton = (Button) findViewById(R.id.minusButton);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount;
                if(chargeAmount.getText().toString().equals("")){
                    amount = 100;
                }else{
                    amount = Integer.parseInt(chargeAmount.getText().toString()) - 100;
                    if(amount < 0){
                        amount = 0;
                    }
                }

                chargeAmount.setText(Integer.toString(amount));
            }
        });
        plusButton = (Button) findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount;
                if(chargeAmount.getText().toString().equals("")){
                    amount = 100;
                }else{
                    amount = Integer.parseInt(chargeAmount.getText().toString()) + 100;
                }
                if(amount >=0){
                    chargeAmount.setText(Integer.toString(amount));
                }

            }
        });
        chargeButton = (Button) findViewById(R.id.chargeButton);
        chargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = chargeAmount.getText().toString();
                Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
                intent.putExtra("amount", amount);
                startActivityForResult(intent, 2001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 2001 && resultCode == 1){
            setResult(1);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                setResult(1);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}


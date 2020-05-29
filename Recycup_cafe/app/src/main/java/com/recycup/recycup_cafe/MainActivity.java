package com.recycup.recycup_cafe;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout saleContainer;
    ConstraintLayout returnContainer;
    TextView headNameTextView;
    TextView cafeNameTextView;
    Cafe cafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cafe = Cafe.getInstance();
        headNameTextView = findViewById(R.id.headName);
        cafeNameTextView = findViewById(R.id.cafeName);

        if(Cafe.getInstance().cafeName == null){
            Intent intent = new Intent(getApplicationContext(), LoginCafeActivity.class );

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        headNameTextView.setText(cafe.headName);
        cafeNameTextView.setText(cafe.cafeName);
        returnContainer = findViewById(R.id.returnContainer);
        returnContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                intent.putExtra("from", "return");
                startActivity(intent);
            }
        });
        saleContainer = findViewById(R.id.saleContainer);
        saleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                intent.putExtra("from", "sale");
                startActivity(intent);
            }
        });


    }
}


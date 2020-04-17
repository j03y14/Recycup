package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

   MainCardView cupInfoCardView;
   MainCardView returnCupCardView;
   MainCardView locationCardView;
   MainCardView myPageCardview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cupInfoCardView = (MainCardView)findViewById(R.id.cupCardView);
        cupInfoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CupInfoActivity.class);
                startActivity(intent);
            }
        });


        returnCupCardView = (MainCardView)findViewById(R.id.returnCardView);
        locationCardView = (MainCardView)findViewById(R.id.mapCardView);
        myPageCardview = (MainCardView)findViewById(R.id.myPageCardView);

    }
}

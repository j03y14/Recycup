package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MypageActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView nameTextView;
    TextView pointTextView;
    Button chargePointButton;
    ProgressBar statisticsBar;
    TextView takeOutCupNumber;
    TextView returnCupNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        toolbar = findViewById(R.id.mypageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        chargePointButton = (Button) findViewById(R.id.chargePointButton);
        statisticsBar = (ProgressBar) findViewById(R.id.statisticsBar);
        takeOutCupNumber = (TextView) findViewById(R.id.takeOutCupNumber);
        returnCupNumber = (TextView) findViewById(R.id.returnCupNumber);

        statisticsBar.setProgress(50);


    }
}

package com.example.recycup_cafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterCafeActivity extends AppCompatActivity {

    Spinner cafeSpiner;
    Button searchLocationButton;

    public String headName;

    Button submitButton;
    double latitude;
    double longitude;
    EditText cafenameEditText;
    TextView addressTextView;


    private static int SEARCH_LOCATION = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cafe);
        headName = null;

        cafenameEditText = findViewById(R.id.cafeEditText);
        latitude =0;
        longitude = 0;

        addressTextView = findViewById(R.id.cafeLocationTextView);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headName = cafeSpiner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), headName, Toast.LENGTH_LONG).show();
            }
        });

        searchLocationButton = findViewById(R.id.cafeLocationButton);
        searchLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchLocationActivity.class);

                startActivityForResult(intent, SEARCH_LOCATION);
            }
        });

        cafeSpiner = findViewById(R.id.cafeSpinner);
        String[] superHero = new String[]{"빽다방", "스타벅스", "이디야커피", "할리스커피", "탐앤탐스"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.cafe_spinner_list, superHero);
        arrayAdapter.setDropDownViewResource(R.layout.cafe_spinner_list);
        cafeSpiner.setAdapter(arrayAdapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==SEARCH_LOCATION ){
            if(resultCode == RESULT_OK){
                Intent intent = data;

                String lat = intent.getStringExtra("latitude");
                String lon = intent.getStringExtra("longitude");
                latitude = Double.parseDouble(intent.getStringExtra("latitude"));
                longitude = Double.parseDouble(intent.getStringExtra("longitude"));
                String address = intent.getStringExtra("address");

                addressTextView.setText(address);
            }
        }
    }
}

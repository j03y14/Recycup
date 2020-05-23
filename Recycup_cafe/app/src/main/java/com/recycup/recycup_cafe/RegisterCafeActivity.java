package com.recycup.recycup_cafe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;

public class RegisterCafeActivity extends AppCompatActivity {

    Spinner cafeSpinner;
    Button searchLocationButton;

    public String headName;

    Button submitButton;
    double latitude;
    double longitude;
    EditText cafenameEditText;
    TextView addressTextView;
    RetrofitClient retrofitClient;

    private static int SEARCH_LOCATION = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cafe);

        retrofitClient = RetrofitClient.getInstance();

        headName = null;

        cafenameEditText = findViewById(R.id.cafeEditText);
        latitude =0;
        longitude = 0;

        addressTextView = findViewById(R.id.cafeLocationTextView);

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headName = cafeSpinner.getSelectedItem().toString();
                String cafeName = cafenameEditText.getText().toString();
                registerCafe(headName,cafeName,latitude,longitude);
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

        cafeSpinner = findViewById(R.id.cafeSpinner);
        String[] superHero = new String[]{"빽다방", "스타벅스", "이디야커피", "할리스커피", "탐앤탐스"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.cafe_spinner_list, superHero);
        arrayAdapter.setDropDownViewResource(R.layout.cafe_spinner_list);
        cafeSpinner.setAdapter(arrayAdapter);

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


    public void registerCafe(String headName, String cafeName, double latitude, double longitude){
        retrofitClient.registerCafe(headName, cafeName, latitude, longitude, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                if(receivedData.get("success").getAsBoolean()){

                    String headName = receivedData.get("headName").getAsString();
                    String cafeName = receivedData.get("cafeName").getAsString();
                    String material = receivedData.get("material").getAsString();

                    Cafe.getInstance().setHeadName(headName);
                    Cafe.getInstance().setHeadName(cafeName);
                    Cafe.getInstance().setHeadName(material);


                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

}

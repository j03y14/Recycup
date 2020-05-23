package com.example.recycup_cafe;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;         // 핵심 모듈

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    Spinner cafeSpiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cafeSpiner = findViewById(R.id.cafeSpinner);
        String[] superHero = new String[]{"빽다방", "스타벅스", "이디야커피", "할리스커피", "탐앤탐스"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.cafe_spinner_list, superHero);
        arrayAdapter.setDropDownViewResource(R.layout.cafe_spinner_list);
        cafeSpiner.setAdapter(arrayAdapter);
    }
}


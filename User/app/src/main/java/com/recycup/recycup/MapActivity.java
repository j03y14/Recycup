package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity {

    Toolbar toolbar;
    ConstraintLayout mapViewContainer;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        toolbar = (Toolbar) findViewById(R.id.mapToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapViewContainer = (ConstraintLayout) findViewById(R.id.mapViewContainer);

        mapView = new MapView(this);

        mapView.setOpenAPIKeyAuthenticationResultListener(new MapView.OpenAPIKeyAuthenticationResultListener() {
            @Override
            public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {
                Log.d("map key authentication result", Integer.toString(i) + "," + s);
            }
        });

        mapViewContainer.addView(mapView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}

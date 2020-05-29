package com.recycup.recycup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener{

    Toolbar toolbar;
    ConstraintLayout mapViewContainer;
    MapView mapView;
    Button gpsButton;

    MapViewItemAdapter adapter;
    RecyclerView recyclerView;
    LocationManager locationManager;
    Location location;

    RetrofitClient retrofitClient;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        retrofitClient = RetrofitClient.getInstance();





        //permission check
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        } else {

            checkRuntimePermission();
        }

        //set toolbar
        toolbar = (Toolbar) findViewById(R.id.mapToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set gps button click event listener
        gpsButton = (Button) findViewById(R.id.gpsButton);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkLocationServicesStatus()) {

                    showDialogForLocationServiceSetting();
                } else {

                    checkRuntimePermission();
                }

                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location ==null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if(location != null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                }

            }
        });

        //instantiate mapView
        mapViewContainer = (ConstraintLayout) findViewById(R.id.mapViewContainer);
        mapView = new MapView(this);
        mapView.setMapViewEventListener(this);
        tempGetLocationOf();

        mapViewContainer.addView(mapView);

        adapter = new MapViewItemAdapter(mapView, getApplicationContext(), this);
        recyclerView = findViewById(R.id.cafeListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

//        //쓰레기통 추가
//        String trashCanLogo_pp = getURLForResource(R.drawable.baseline_delete_black_18dp_pp);
//        String trashCanName_pp = "pp 반납";
//        String tarshCanMeterial_pp = "pp";
//        adapter.addItem(new CupInfo(trashCanName_pp,tarshCanMeterial_pp,trashCanLogo_pp));
//        //쓰레기통 추가
//        String trashCanLogo_pet = getURLForResource(R.drawable.baseline_delete_black_18dp_pet);
//        String trashCanName_pet = "pet 반납";
//        String tarshCanMeterial_pet = "pet";
//        adapter.addItem(new CupInfo(trashCanName_pet,tarshCanMeterial_pet,trashCanLogo_pet));
//        //쓰레기통 추가
//        String trashCanLogo_ps = getURLForResource(R.drawable.baseline_delete_black_18dp_ps);
//        String trashCanName_ps = "ps 반납";
//        String tarshCanMeterial_ps = "ps";
//        adapter.addItem(new CupInfo(trashCanName_ps,tarshCanMeterial_ps,trashCanLogo_ps));


        getCupInfo();


        //get mapView's authentication result
//        mapView.setOpenAPIKeyAuthenticationResultListener(new MapView.OpenAPIKeyAuthenticationResultListener() {
//            @Override
//            public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {
//                Log.d("map key authentication result", Integer.toString(i) + "," + s);
//            }
//        });
    }

    public void getCupInfo(){
        retrofitClient.getCupInfo( new RetroCallback<JsonArray>() {
            @Override
            public void onError(Throwable t) {

                Log.e("error", t.toString());
            }

            @Override
            public void onSuccess(int code, JsonArray receivedData) {
                Intent intent = getIntent(); /*데이터 수신*/
                String name = null;
                String material;
                if(intent.hasExtra("cafeName")){
                    name = intent.getExtras().getString("cafeName");
                    material = intent.getExtras().getString("material");
                    String logo = getURLForResource(getLogo(name));


                }
                for(int i=0; i<receivedData.size(); i++){
                    JsonObject data = (JsonObject) receivedData.get(i);

                    String headName = data.get("headName").getAsString();
                    String cupMeterial = data.get("type").getAsString();
                    String cafeLogo = data.get("logoPath").getAsString();

                    CupInfo cupInfo =new CupInfo( headName, cupMeterial, cafeLogo);
                    adapter.addItem(cupInfo);
                    if(headName.equals(name)){
                        adapter.selectedList.add(cupInfo);
                        getLocationsOf(name, 0,0);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                locationManager.removeUpdates(mLocationListener);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(this, "gps 권한이 거부되었습니다. gps 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(this, "gps 권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }


    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("쓰레기통 찾기를 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void checkRuntimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음

            //get current location and change center point of the map

            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location ==null){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);





        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getApplicationContext(), "이 기능을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
    }

    public void getLocationsOf(String cafeName, double latitude, double longitude ){
        retrofitClient.getLocationsOf(cafeName, latitude, longitude, new RetroCallback<JsonArray>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonArray receivedData) {

                for(int i=0; i<receivedData.size(); i++){
                    JsonObject spot = receivedData.get(i).getAsJsonObject();
                    Log.d("spot", String.valueOf(receivedData));

                    String headName = spot.get("headName").getAsString();
                    String cafeName = spot.get("cafeName").getAsString();
                    double latitude = spot.get("latitude").getAsDouble();
                    double longitude = spot.get("longitude").getAsDouble();
                    MapPOIItem mapPOIItem = new MapPOIItem();
                    mapPOIItem.setTag(0);
                    mapPOIItem.setItemName(headName);
                    //마커 위치 설정
                    mapPOIItem.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
                    //마커 로고 설정
                    mapPOIItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    int drawable = getLogo(headName);
                    mapPOIItem.setCustomImageResourceId(drawable); // 마커 이미지.
                    mapPOIItem.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
                    mapPOIItem.setCustomImageAnchor(0.5f, 0.5f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

                    mapView.addPOIItem(mapPOIItem);
                }


            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

    public void tempGetLocationOf(){
        //지도에 있는 모든 마커들을 없앤다.
        mapView.removeAllPOIItems();



        String logoUrl = "https://upload.wikimedia.org/wikipedia/ko/a/aa/%ED%83%90%EC%95%A4%ED%83%90%EC%8A%A4_%EB%A1%9C%EA%B3%A0.png";
        double latitude = 37.519375;
        double longitude = 127.016533;
        MapPOIItem mapPOIItem = new MapPOIItem();
        mapPOIItem.setTag(0);
        mapPOIItem.setItemName("빽다방");
        //마커 위치 설정
        mapPOIItem.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        //마커 로고 설정
        mapPOIItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);

        mapPOIItem.setShowDisclosureButtonOnCalloutBalloon(false);

        mapPOIItem.setCustomImageResourceId(R.drawable.ic_trash_can); // 마커 이미지.
        mapPOIItem.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        mapPOIItem.setCustomImageAnchor(0.5f, 0.5f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        mapView.addPOIItem(mapPOIItem);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.519375, 127.016533), false);

    }


    private final LocationListener mLocationListener = new LocationListener() {

        public void onLocationChanged(Location location) {

            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.



            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.

            }else {

                //Network 위치제공자에 의한 위치변화


            }


        }

        public void onProviderDisabled(String provider) {

        }



        public void onProviderEnabled(String provider) {

        }



        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };


    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    private String getURLForResource(int resId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }

    int getLogo(String name){
        if(name.equals("빽다방")){
            return R.drawable.paiks;
        }else if(name.equals("스타벅스")){
            return R.drawable.starbucks;
        }else if(name.equals("셀프레소")){
            return R.drawable.salpresso;
        }else if(name.equals("이디야커피")){
            return R.drawable.ediyacoffee;
        }else if(name.equals("커피에반하다")){
            return R.drawable.coban;
        }else if(name.equals("커피온니")){
            return R.drawable.coffeeonly;
        }else if(name.equals("탐앤탐스")){
            return R.drawable.tomandtoms;
        }else if(name.equals("할리스커피")){
            return R.drawable.hollyscoffe;
        }

        return 0;
    }

}

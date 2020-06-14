package com.recycup.recycup_cafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class ReturnCupActivity extends AppCompatActivity {

    WebView mWebView;
    WebSettings mWebSettings;
    private Handler handler;

    static int INITIAL =0;
    static int READY = 1;

    int state;

    Button cancelButton;

    ConstraintLayout checkBackground;
    TextView indicator;
    Button checkButton;
    Button retryButton;
    EditText phoneNumberEditText;

    RetrofitClient retrofitClient;

    public String headName;


    private static Context context;

    int permissionCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_cup);
        context = this;
        retrofitClient = RetrofitClient.getInstance();
        mWebView = findViewById(R.id.webView);

        // 카메라 permission check
        permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if( permissionCheck == PackageManager.PERMISSION_DENIED){
            //권한 없음
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }else{
            init_webView();
        }

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });

        checkBackground = findViewById(R.id.checkBackground);
        indicator = findViewById(R.id.indicator);
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!headName.equals("")){
                    returnCup(phoneNumberEditText.getText().toString(),Cafe.getInstance().headName, headName);

                }

            }
        });
        retryButton = findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCheck();
            }
        });

        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        initCheck();



        handler = new Handler();

    }


    private void init_webView(){
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
        });

        mWebView.addJavascriptInterface(new AndroidBridge(), "Recognition");
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        mWebSettings.setMediaPlaybackRequiresUserGesture(false);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);










        mWebView.loadUrl("https://hardcore-newton-e59e78.netlify.app");// 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void recognition(String recogHeadName) {
            handler.post(new Runnable() {
                @Override
                public void run() {


                    // 인식된 것이 라벨없음이면 무조건 state initial
                    if(recogHeadName.equals("라벨없음")){
                        initCheck();
                        return;
                    }

                    // state가 INITIAL 일 때
                    if(state==INITIAL){
                        // headName이 빈공간이면 state를 변경한다.
                        if(recogHeadName.equals("빈공간")){
                           return;
                        }else {


                            // 현재 앱이 쓰레기통이면 recogReady 보여줌.
                            String appHeadName = Cafe.getInstance().headName;
                            if (appHeadName.equals("pet쓰레기통")) {
                                recogReady(recogHeadName);


                            } else {
                                // 앱이 카페면 비교해서 맞는거만 recogReady
                                if (appHeadName.equals(recogHeadName)) {
                                    Log.d("인식:",recogHeadName);
                                    recogReady(recogHeadName);


                                } else {
                                    recogFail(recogHeadName);
                                }
                            }
                        }

                    }else if(state == READY){
                        // READY일 때는 빈공간만 인식한다.
                        if(recogHeadName.equals("빈공간")){
                            Log.d("인식:","recogsuccess");
                            recogSuccess();
                            return;
                        }
                    }



                    mWebView.setVisibility(View.GONE);

                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==0){
            if(grantResults[0] ==0){
                //카메라 권한 승인
                init_webView();
            }else{
                Toast.makeText(getApplicationContext(), "카메라 권한을 설정해주세요.", Toast.LENGTH_SHORT);
                finish();
            }
        }
    }

    private void initCheck(){
        checkBackground.setVisibility(View.GONE);
        checkButton.setVisibility(View.GONE);
        indicator.setText("nothing");
        phoneNumberEditText.setText("");
        mWebView.setVisibility(View.VISIBLE);

        headName="";
        state = INITIAL;

    }

    private void recogReady(String headName){
        state = READY;
        this.headName = headName;
        checkBackground.setVisibility(View.VISIBLE);
        //checkButton.setVisibility(View.VISIBLE);
        indicator.setText(headName+"컵 인식\n판을 꺼내 컵을 떨어뜨리세요.");
        //phoneNumberEditText.setVisibility(View.VISIBLE);

        mWebView.setVisibility(View.GONE);
    }

    private void recogSuccess(){

        checkBackground.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        indicator.setText(headName+"컵 반납\n전화번호를 입력해 반납을 완료하세요.");
        phoneNumberEditText.setVisibility(View.VISIBLE);

        mWebView.setVisibility(View.GONE);


    }
    private void recogFail(String headName){
        checkBackground.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.GONE);
        indicator.setText(headName+"컵은 이곳에서 반납할 수 없습니다.");
        phoneNumberEditText.setVisibility(View.GONE);

        mWebView.setVisibility(View.GONE);
    }

    private void end(){
        User.clear();
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//
//        startActivity(intent);

        finish();

    }

    public void returnCup(String phoneNumber, String headName,String cupHeadName){
        retrofitClient.returnCup(phoneNumber, headName,cupHeadName, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {
                Toast.makeText(getApplicationContext(),"on error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                boolean success = receivedData.get("success").getAsBoolean();
                if(success){
                    initCheck();
                }else{
                    Toast.makeText(getApplicationContext(),receivedData.get("msg").getAsString(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(getApplicationContext(),"on failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        end();
    }
}

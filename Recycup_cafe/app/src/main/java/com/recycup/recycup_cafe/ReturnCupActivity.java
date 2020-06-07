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
import android.provider.MediaStore;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class ReturnCupActivity extends AppCompatActivity {

    WebView mWebView;
    WebSettings mWebSettings;
    private Handler handler;

    Button cancelButton;

    ConstraintLayout checkBackground;
    TextView indicator;
    Button checkButton;
    Button retryButton;
    EditText phoneNumberEditText;

    RetrofitClient retrofitClient;

    String headName;

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
                    returnCup(phoneNumberEditText.getText().toString(), headName);
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


        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부


        mWebView.loadUrl("https://hardcore-newton-e59e78.netlify.app");// 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void recognition(String headName) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String appHeadName = Cafe.getInstance().headName;
                    if(appHeadName.equals("pet쓰레기통") || appHeadName.equals("pet쓰레기통") || appHeadName.equals("pet쓰레기통")){
                        recogSuccess(headName);
                    }else{
                        if(appHeadName.equals(headName)){
                            recogSuccess(headName);
                        }else{
                            recogFail(headName);
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

    }

    private void recogSuccess(String headName){
        this.headName = headName;
        checkBackground.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        indicator.setText(headName+"컵을 반납할 수 있습니다.");

        mWebView.setVisibility(View.GONE);


    }
    private void recogFail(String headName){
        checkBackground.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.GONE);
        indicator.setText(headName+"컵은 이곳에서 반납할 수 없습니다.");

        mWebView.setVisibility(View.GONE);
    }

    private void end(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        User.clear();
        startActivity(intent);

    }

    public void returnCup(String phoneNumber, String headName){
        retrofitClient.returnCup(phoneNumber, headName, new RetroCallback<JsonObject>() {
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

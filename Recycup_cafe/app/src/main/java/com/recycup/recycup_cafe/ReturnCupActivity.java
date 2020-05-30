package com.recycup.recycup_cafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.Toast;

public class ReturnCupActivity extends AppCompatActivity {

    WebView mWebView;
    WebSettings mWebSettings;
    private Handler handler;

    int permissionCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_cup);

        mWebView = findViewById(R.id.webView);
        // 카메라 permission check
        permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if( permissionCheck == PackageManager.PERMISSION_DENIED){
            //권한 없음
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }else{
            init_webView();
        }

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
                    Toast.makeText(getApplicationContext(),headName,Toast.LENGTH_SHORT);

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
}

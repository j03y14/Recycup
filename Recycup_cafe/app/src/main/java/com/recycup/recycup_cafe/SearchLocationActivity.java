package com.recycup.recycup_cafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SearchLocationActivity extends AppCompatActivity {

    WebView mWebView;
    WebSettings mWebSettings;
    private Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        mWebView = findViewById(R.id.webView);

        init_webView();

        handler = new Handler();
    }

    private void init_webView(){
        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        mWebView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        mWebSettings = mWebView.getSettings(); //세부 세팅 등록
        mWebSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부

        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부

        //mWebView.loadUrl("http://web-inf.tistory.com"); // 웹뷰에 표시할 웹사이트 주소, 웹뷰 시작

        mWebView.loadUrl("http://59.187.219.187:22406/kakaoLocation/");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String latitude, final String longitude, final String address) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),latitude+"," +longitude +","+address,Toast.LENGTH_LONG).show();

                    String lat = latitude;
                    String lon = longitude;

                    Intent intent = new Intent();
                    intent.putExtra("longitude", lat );
                    intent.putExtra("latitude", lon );
                    intent.putExtra("address", address );

                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}

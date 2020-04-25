package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;


public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    RetrofitClient retrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                return handleUri(uri);

            }
            private boolean handleUri(final Uri uri) {
                Log.i("TAG", "Uri =" + uri);
                final String host = uri.getHost();
                final String scheme = uri.getScheme();

                Log.i("host", host);
                Log.i("scheme", scheme);

                if(scheme.equals("intent")){
                    final Intent intent;
                    try {
                        intent = Intent.parseUri(uri.toString(),Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null){
                            startActivity(intent);
                        }

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }else if(scheme.equals("http")){
                    String token = uri.getQueryParameter("pg_token");
                    Log.i("token", token);

                    webView.loadUrl(uri.toString());
                }


                return true;
            }

        });

        retrofitClient = RetrofitClient.getKakaoInstance();
        paymentReady();
    }

    //cid,partner_order_id,item_name,quantity,total_amount,tax_free_amount,approval_url,cancel_url,fail_url
    public void paymentReady(){
        retrofitClient.paymentReady("TC0ONETIME", "1","1" ,"point", "1", "3000",
                "0", "http://35.229.219.32:8888/kakaoPay/success", "http://35.229.219.32:8888/kakaoPay/success", "http://35.229.219.32:8888/kakaoPay/success", new RetroCallback<JsonObject>() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d("paymentReady", "onError");
                    }

                    @Override
                    public void onSuccess(int code, JsonObject receivedData) {
                        String next_redirect_app_url = receivedData.get("next_redirect_app_url").getAsString();
                        String android_app_scheme = receivedData.get("android_app_scheme").getAsString();
                        Log.d("next_redirect_app_url", next_redirect_app_url);
                        Log.d("android_app_scheme", android_app_scheme);
                        webView.loadUrl(next_redirect_app_url);
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d("paymentReady", String.valueOf(code));
                    }
                });
    }


}

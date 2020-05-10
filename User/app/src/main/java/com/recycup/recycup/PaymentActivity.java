package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;


public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    WebView webView;
    ConstraintLayout constraintLayout;
    Button returnButton;
    TextView completeIndicator;

    RetrofitClient retrofitClient;


    User user;

    String cid;
    String tid;
    String partner_order_id;
    String partner_user_id;
    String item_name;
    String quantity;
    String total_amount;
    String tax_free_amount;
    String approval_url;
    String cancel_url;
    String fail_url;
    String pg_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        user = User.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        constraintLayout = (ConstraintLayout) findViewById(R.id.successContainer);
        returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(1);
                finish();
            }
        });
        completeIndicator = (TextView) findViewById(R.id.completeIndicator);

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
                    pg_token = uri.getQueryParameter("pg_token");
                    Log.i("token", pg_token);

                    webView.loadUrl(uri.toString());

                    paymentApprove();
                }


                return true;
            }

        });


        cid = "TC0ONETIME";
        partner_order_id = "1";
        partner_user_id = "1";
        item_name = "point";
        quantity = "1";
        total_amount = getIntent().getStringExtra("amount");
        tax_free_amount = "0";
        approval_url = "http://35.229.219.32:8888/kakaoPay/success";
        cancel_url = "http://35.229.219.32:8888/kakaoPay/success";
        fail_url = "http://35.229.219.32:8888/kakaoPay/success";
        retrofitClient = RetrofitClient.getKakaoInstance();
        paymentReady();
    }

    //cid,partner_order_id,item_name,quantity,total_amount,tax_free_amount,approval_url,cancel_url,fail_url
    public void paymentReady(){
        retrofitClient.paymentReady(cid, partner_order_id,partner_user_id ,item_name, quantity, total_amount,
                tax_free_amount, approval_url, cancel_url, fail_url, new RetroCallback<JsonObject>() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d("paymentReady", "onError");
                    }

                    @Override
                    public void onSuccess(int code, JsonObject receivedData) {
                        String next_redirect_app_url = receivedData.get("next_redirect_app_url").getAsString();
                        String android_app_scheme = receivedData.get("android_app_scheme").getAsString();
                        tid = receivedData.get("tid").getAsString();
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


    public void paymentApprove(){
        retrofitClient.paymentApprove(cid, tid, partner_order_id, partner_user_id, pg_token, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {
                Log.d("paymentApprove", "onError");
            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                addPoint(user.phoneNumber, Integer.parseInt(total_amount));
            }

            @Override
            public void onFailure(int code) {
                Log.d("paymentApprove", "onFailure");
            }
        });
    }

    public void addPoint(String phoneNumber, int amount){
        retrofitClient.addPoint(phoneNumber, amount, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                boolean success = receivedData.get("success").getAsBoolean();
                if(success){
                    Log.d("paymentApprove", "onSuccess");
                    completeIndicator.setText("결제가 완료되었습니다.");

                }else{
                    completeIndicator.setText("결제 시 오류가 발생했습니다.");
                }
                constraintLayout.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }
}

package com.recycup.recycup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;

public class SignUpActivity extends AppCompatActivity {

    RetrofitClient retroClient;
    EditText phoneEditText;
    EditText nameEditText;
    EditText passwordEditText;
    EditText passwordCheckEditText;

    ImageView passwordCheckImageView;

    String phoneNumber;
    String name;
    String password;
    String cryptoPW;

    int FROM_SIGN_UP = 2001;


    //이미 가입된 전화번호인지 확인
    private boolean phoneNumberCheck = false;
    boolean passwordOK;
    boolean passwordCheckOK;

    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        retroClient = RetrofitClient.getInstance();

        phoneEditText = (EditText) findViewById(R.id.phoneInput);
        nameEditText = (EditText) findViewById(R.id.nameInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        passwordCheckEditText = (EditText) findViewById(R.id.passwordCheckInput);
        passwordCheckImageView = (ImageView) findViewById(R.id.passwordCheckImage);

        passwordCheckEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==6){
                    passwordOK = true;
                }else{
                    passwordOK = false;
                }
            }
        });
        passwordCheckEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(passwordEditText.getText().toString())){
                    passwordCheckImageView.setImageResource(R.drawable.ic_ok_24px);
                    ImageViewCompat.setImageTintList(passwordCheckImageView, ColorStateList.valueOf(Color.GREEN));
                    passwordCheckOK = true;
                }else{
                    passwordCheckImageView.setImageResource(R.drawable.ic_no_24px);
                    ImageViewCompat.setImageTintList(passwordCheckImageView, ColorStateList.valueOf(Color.RED));
                    passwordCheckOK = false;
                }
            }
        });

        signUpButton =(Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = phoneEditText.getText().toString();
                name = nameEditText.getText().toString();
                password = passwordEditText.getText().toString();


                if(phoneNumber.equals("") || name.equals("")){
                    startToast("모든 항목을 입력해주세요.");
                    return;
                }

                if(!passwordOK){
                    startToast("비밀번호를 6자리로 설정해주세요.");
                    return;
                }
                if(!passwordCheckOK){
                    startToast("비밀번호와 비밀번호 설정이 다릅니다.");
                    return;
                }

                try {
                    cryptoPW = Crypto.sha256(password);
                    //아이디 중복 체크
                    duplicateCheck(phoneNumber);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void duplicateCheck(String phoneNumber){
        retroClient.duplicateCheck(phoneNumber,new RetroCallback<JsonObject>(){

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                boolean isDuplicate = receivedData.get("duplicate").getAsBoolean();


                if(isDuplicate){
                    //중복이면
                    Log.i("아이디 중복","중복임");
                    Toast.makeText(SignUpActivity.this, "이미 가입된 번호입니다.", Toast.LENGTH_SHORT).show();
                    phoneNumberCheck = false;


                }else{
                    //중복이 아니면
                    phoneNumberCheck = true;
                    createAccount(phoneNumber,name,cryptoPW);
                }
            }

            @Override
            public void onFailure(int code) {

            }
        });
    }

    public void createAccount(String phoneNumber, String name, String password){
        retroClient.createAccount(phoneNumber, name, password, new RetroCallback<JsonObject>() {
            @Override
            public void onError(Throwable t) {
                Log.e("error", t.toString());
            }

            @Override
            public void onSuccess(int code, JsonObject receivedData) {
                boolean success = receivedData.get("success").getAsBoolean();
                //로그인 액티비티로
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                setResult(FROM_SIGN_UP);
                finish();
            }

            @Override
            public void onFailure(int code) {
                Log.e("error", "onFailure");
            }
        });
    }

    public void startToast(String string){
        Toast toast= Toast.makeText(this, string, Toast.LENGTH_LONG);
        toast.show();
    }

}

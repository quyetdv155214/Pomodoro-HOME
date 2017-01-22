package com.example.quyet.podomoro.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quyet.podomoro.R;
import com.example.quyet.podomoro.networks.jsonmodel.LoginBodyJson;
import com.example.quyet.podomoro.networks.jsonmodel.LoginResponseJson;
import com.example.quyet.podomoro.networks.jsonmodel.RegisterBodyJson;
import com.example.quyet.podomoro.networks.jsonmodel.RegisterResponseJson;
import com.example.quyet.podomoro.networks.services.LoginService;
import com.example.quyet.podomoro.networks.services.RegisterService;
import com.example.quyet.podomoro.settings.LoginCredentials;
import com.example.quyet.podomoro.settings.Sharepref;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.toString();
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;
    private Button btRegister;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) this.findViewById(R.id.et_username); 
        etPassword = (EditText) this.findViewById(R.id.et_password);
        btLogin = (Button) this.findViewById(R.id.bt_login);
        btRegister = (Button) this.findViewById(R.id.bt_register);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAction();
            }
        });

        Sharepref.init(this);
//        skipLoginIfPosible();
        Sharepref.getInstance().put(new LoginCredentials("hieu", "xxx"));
        Log.d(TAG, String.format("onCreate: %s", Sharepref.getInstance().getLoginCredentials().toString()));
// retrofit

    }
    private void onLoginSuccess(){
        gotoTaskActivity();
    }
    private void sendRegister(String username, String password){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://a-task.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterService registerService = retrofit.create(RegisterService.class);
        MediaType jsonType = MediaType.parse("application/json");
        String registerJson = (new Gson().toJson(new RegisterBodyJson(username,password)));
        final RequestBody registerBody = RequestBody.create(jsonType,registerJson);
        registerService.register(registerBody).enqueue(new Callback<RegisterResponseJson>() {
            @Override
            public void onResponse(Call<RegisterResponseJson> call, Response<RegisterResponseJson> response) {
             RegisterResponseJson rrj = response.body();
                Toast.makeText(LoginActivity.this , String.format("onResponse code : %s, message : %s",
                        response.code(), response.message()), Toast.LENGTH_LONG).show();
                if (response.code() == 200){
                    Log.d(TAG, String.format(String.format("onResponse,  register success code %s", response.code()) ));

                }if (response.code() == 400){
                    Log.d(TAG, String.format(String.format("onResponse,  register fail, username " +
                            "already used , code %s", response.code())));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseJson> call, Throwable t) {
                Toast.makeText(LoginActivity.this , String.format("onFailure cause : %s, message : %s",
                        t.getCause(), t.getMessage()), Toast.LENGTH_LONG).show();
                Log.d(TAG, String.format(String.format("onFailure,  register fail " +
                        "message : %s ", t.getMessage())));
            }
        });
    }
    private void sendLogin(String username, String password)
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://a-task.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService loginService = retrofit.create(LoginService.class);
        //data & format
        //format --> mediatype
        //date --. json

        MediaType jsonType = MediaType.parse("application/json");
        String loginJson = (new Gson().toJson(new LoginBodyJson(username, password)));
        final RequestBody loginBody= RequestBody.create(jsonType, loginJson);
        loginService.login(loginBody).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(Call<LoginResponseJson> call, Response<LoginResponseJson> response) {
                LoginResponseJson loginResponseJson = response.body();
                Toast.makeText(LoginActivity.this , String.format("onResponse code : %s, message : %s",
                        response.code(), loginResponseJson.getMessage()), Toast.LENGTH_SHORT).show();
                if(loginResponseJson != null){
                    Log.d(TAG, String.format("onResponse, oh yeah: %s", loginResponseJson));
                    if (response.code() == 200) {
                        onLoginSuccess();
                    }

                }else{
                    Log.d(TAG, "onResponse: Could not parse body");
                }

            }

            @Override
            public void onFailure(Call<LoginResponseJson> call, Throwable t) {
                Log.d(TAG, String.format("onFailure: %s", t));
            }
        });
    }



    private void skipLoginIfPosible() {
        if (Sharepref.getInstance().getLoginCredentials() != null){
            gotoTaskActivity();
        }


    }

    private void registerAction() {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();


        if (username.equals("admin"))   {
            Toast.makeText(this, "username already exist, chosse other !", Toast.LENGTH_SHORT).show();
        }
        else if(username.equals("") || username == null){
            Toast.makeText(this, "username cannot be null", Toast.LENGTH_SHORT).show();
        }else if(password.equals("") || password == null)
        {
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
        }else{
            sendRegister(username, password);
        }



    }

    private void attemptLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
//        if (username.equals("admin") && password.equals("admin")) {
            //notification
        sendLogin(username, password);


//        } else {
//            Toast.makeText(this, "Wrong Username or password", Toast.LENGTH_SHORT).show();
//        }

    }
    private void gotoTaskActivity(){
        Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,TaskActivity.class);
        startActivity(intent);
    }
}

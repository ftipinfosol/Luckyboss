package com.ftipinfosol.luckyboss;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ftipinfosol.luckyboss.Helpers.db;


import com.ftipinfosol.luckyboss.Helpers.HttpCallBack;
import com.ftipinfosol.luckyboss.Helpers.Utli;
import com.ftipinfosol.luckyboss.Helpers.apiURL;
import com.ftipinfosol.luckyboss.Helpers.apiURL;
import com.ftipinfosol.luckyboss.Helpers.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.INTERNET;

public class loginActivity extends AppCompatActivity {

    private EditText input_username, input_password;
    private Button btnLogin;
    private RequestParams params = new RequestParams();
    private AsyncHttpClient client = new AsyncHttpClient();
    private boolean boolReturn=false;
    Utility oUtility;

    SharedPreferences sp;
    db odb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sp = getSharedPreferences("API", Context.MODE_PRIVATE);

        odb = new db();

        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    login();
                } catch (Exception ex) {
                    Log.e("loginlogEx", ex.getMessage());
                }

            }
        });

    }



    public void login() {

        try {

            if (input_username.getText().toString().trim().isEmpty()  || input_username.getText().toString().equals("0")) {
                input_username.setError("Please fill Username");
                input_username.requestFocus();
                return;
            } else {
                input_username.setError(null);
            }

            if (!Utli.isValidEmail( input_username.getText().toString().trim())) {
                input_username.setError("Please fill Username in EMail format");
                input_username.requestFocus();
                return;
            } else {
                input_username.setError(null);
            }

            if (input_password.getText().toString().trim().isEmpty()  || input_password.getText().toString().equals("0")) {
                input_password.setError("Please fill Password");
                input_password.requestFocus();
                return;
            } else {
                input_password.setError(null);
            }


            if(Utli.isNetworkConnected(getApplicationContext())) {
                params.put("email", input_username.getText().toString());
                params.put("password", input_password.getText().toString());
                client.addHeader("Accept", "application/json");

                Log.e("loginlog", "out");

                client.post(apiURL.LOGIN, params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Log.d("loginlogsuccess", response.toString());
                        Log.e("firststart", response.toString());
                        sp.edit().putString("TOKEN", String.valueOf(response)).apply();

//                        if(odb.update_sign_in_detail(String.valueOf(response))) {

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
//                        }
//                        else {
//                            Utli.message(getApplicationContext(),"Sign-in Yes issue");
//                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                        //Utli.message(getApplicationContext(), e.getMessage());
                        Log.e("loginlogfailureE", e.getMessage());
                        Log.e("loginlogfailureRes", response.toString());

                        Utli.message(getApplicationContext(), "Unauthorized login details");


                        if (statusCode == 422) {
                            //Utli.message(getApplicationContext(), "402");
//                        try {
////                            JSONObject errors = response.getJSONObject("errors");
////                            if (errors.has("user_code")) {
//////                            input_usercode.setError(errors.getJSONArray("user_code").getString(0));
//////                            input_usercode.requestFocus();
////                            }
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
                        } else if (statusCode == 401) {
                            //Utli.message(getApplicationContext(), "401");
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class).putExtra("logout", "logout");
//                    startActivity(i);
                        }

                    }

                });
            }
            else {
                Utli.message(getApplicationContext(), "No internet connection");
            }

        }catch (Exception e){
            Log.e("loginactivity",e.getMessage());
        }
    }



}

package com.ftipinfosol.luckyboss;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ftipinfosol.luckyboss.Helpers.HttpCallBack;
import com.ftipinfosol.luckyboss.Helpers.db;
import com.ftipinfosol.luckyboss.Helpers.Utli;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static  android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    public static JSONObject AUTH_USER;
    public static String AUTH_TOKEN;

    db odb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        odb = new db();

//        startActivity(new Intent(getApplicationContext(),homeActivity.class));
//        finish();

        try {
            Log.e("firststart", "in try");
            final SharedPreferences sp = getSharedPreferences("API", Context.MODE_PRIVATE);
            Intent intent = getIntent();
            if (intent.hasExtra("logout")) {
                logout(sp);
            }
            if (sp.contains("TOKEN")) {
                try {
                    Log.e("firststart", "in token");
                    AUTH_USER = new JSONObject(sp.getString("TOKEN", ""));

                    Log.e("firststart", AUTH_USER.toString());

                    AUTH_TOKEN = "Bearer "+AUTH_USER.getString("token");
                    //Check sign in granted or not
                    odb.getSignDetail(new HttpCallBack() {
                        @Override
                        public void onJSONResponse(boolean success, JSONObject response) {
                            try {

                                //Log.d("getSignDetailtry", response.getString("sign_in"));
                                Log.d("getSignDetailtry", response.toString());

                                if(response.has("message") ) {

                                    if (response.getString("message").equals("Unauthenticated.")) {
                                        Utli.message(getApplicationContext(), "Re-login requested from Admin");
                                        startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                        finish();
                                    }
                                }
                                if(response.has("sign_in") ) {
                                    if (response.getString("sign_in").equals("Yes")) {
                                        //Utli.message(getApplicationContext(), "Accepted");
                                        //Log.e("getSignDetail", "Accepted");


                                        startActivity(new Intent(getApplicationContext(), homeActivity.class));
                                        finish();

                                    } else {
                                        Utli.message(getApplicationContext(), "Re-login requested from Admin");
                                        startActivity(new Intent(getApplicationContext(), loginActivity.class));
                                        finish();
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("getSignDetailcatch", e.getMessage());
                            }
                        }

                        @Override
                        public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
                            Log.d("getSignDetailempty", response.toString());
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else{
                startActivity(new Intent(getApplicationContext(),loginActivity.class));
                finish();
            }

        }
        catch (Exception ex){
            Log.e("start:",ex.getMessage());
        }
    }

    public void logout(final SharedPreferences sp) {

        //Check sign in granted or not
        odb.updateSignDetail(new HttpCallBack() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                try {

                    Log.e("login",response.toString());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.e("inloguttry", e.getMessage());
                }
            }

            @Override
            public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
                Log.d("inlogutempty", response.toString());
            }
        });


    }

    private boolean askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(ACCESS_NETWORK_STATE);
        permissions.add(INTERNET);
        permissions.add(ACCESS_WIFI_STATE);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);


        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        return true;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}

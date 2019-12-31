package com.ftipinfosol.luckyboss.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utli extends AppCompatActivity {

    public static void message(Context context, String strMessage){
        //Toast.makeText(context,strMessage,Toast.LENGTH_LONG).show();
        Toast toast= Toast.makeText(context,strMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.YELLOW);
        toast.show();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean isStringNull(String str){
        if(str == null || (TextUtils.equals(str ,"null")) || (TextUtils.isEmpty(str)))
            return true;
        return false;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    static public  boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
//
//    static public boolean isURLReachable(){
//        try {
//            URL url = new URL(apiURL.BASE);   // Change to "http://google.com" for www  test.
//            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
//            urlc.setConnectTimeout(10 * 1000);          // 10 s.
//            urlc.connect();
//            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
//                Log.wtf("Connection", "Success !");
//                return true;
//            } else {
//                return false;
//            }
//        } catch (MalformedURLException e1) {
//            return false;
//        } catch (IOException e) {
//            return false;
//        }
//    }


}

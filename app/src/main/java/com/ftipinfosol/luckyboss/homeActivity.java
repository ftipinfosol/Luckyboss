package com.ftipinfosol.luckyboss;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class homeActivity extends AppCompatActivity {

    JSONObject AUTH_USER=null;
    String user_name;
    TextView tvWelcome;

    @Override
    protected  void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.home_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Lucky Boss");
        setSupportActionBar(toolbar);

        tvWelcome = findViewById(R.id.welcome);

        tvWelcome.setText("Hi");

        AUTH_USER = new JSONObject();

        AUTH_USER = MainActivity.AUTH_USER;

        try {
            //Log.e("authuser", AUTH_USER.toString());

            user_name = "Welcome, " + AUTH_USER.getString("name")+" - "+ BuildConfig.VERSION_NAME+"(v)";
            tvWelcome.setText(user_name);
        } catch (Exception e) {
            Log.e("homescreen", e.getMessage());
        }
    }

    public void logout(View view) {

//        new AlertDialog.Builder(this)
//                .setTitle("Confirmation")
//                .setMessage("Do you really want to Logout?")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        Intent i = new Intent(getApplicationContext(), MainActivity.class).putExtra("logout", "logout");
//                        startActivity(i);
//
//                    }})
//                .setNegativeButton(android.R.string.no, null).show();

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        try {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class).putExtra("logout", "logout");
                            startActivity(i);
                        }catch (Exception e){
                            Log.e("printticket", e.getMessage());
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(homeActivity.this, R.style.myDialog));
        builder.setMessage("Are you sure want to print?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void new_ticket(View view) {
        try {

            startActivity(new Intent(getApplicationContext(), newTicketActivity.class));
        }
        catch (Exception e){
            Log.e("newticket",e.getMessage());
        }
    }

    public void view_tickets(View view) {

        try{
            startActivity(new Intent(getApplicationContext(), ticketsActivity.class));
        }
        catch (Exception e){
            Log.e("newticket",e.getMessage());
        }
    }

    public void view_result_history(View view) {
        try {


        startActivity(new Intent(getApplicationContext(), resultHistoryActivity.class));
        }
        catch (Exception e){
            Log.e("newticket",e.getMessage());
        }
    }

}

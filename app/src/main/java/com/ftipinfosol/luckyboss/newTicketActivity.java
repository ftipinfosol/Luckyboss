package com.ftipinfosol.luckyboss;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ftipinfosol.luckyboss.Helpers.Hashids;
import com.ftipinfosol.luckyboss.Helpers.HttpCallBack;

import com.ftipinfosol.luckyboss.Helpers.HttpCallBackJsonArray;
import com.ftipinfosol.luckyboss.Helpers.Utility;
import com.ftipinfosol.luckyboss.Helpers.Utli;
import com.ftipinfosol.luckyboss.Helpers.db;
import com.ftipinfosol.luckyboss.Helpers.apiURL;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import cz.msebera.android.httpclient.Header;

import com.softland.palmtecandro.palmtecandro;




public class newTicketActivity extends AppCompatActivity {

    Button btnCreateTicket, btnViewTickets, btnNewTicket, btnPrintTicket;
    db odb;
    RadioButton rb12, rb3, rb7, rbBoard, rbPair, rb3D, rb4D, rbBox, rdbtnA, rdbtnB, rdbtnC, rdbtnAB, rdbtnBC, rdbtnAC, rdbtnR1, rdbtnR2, rdbtnR3;
    RadioGroup rgTimeActivity, rgTicketActivity, rgBoradActivity, rgPairActivity, rgRateAcivity, rgBoard, rgpair, rgRate;
    EditText etRate, etNumber, etQuantity, etTotal;
    String strTicketType, strRateType;
    Utility oUtility;
    Boolean boolSignIn, boolTimeRestriction = false;
    String tickets_ID;
    int ticket_count;

    @Override
    protected void onStart() {
        super.onStart();
        //palmtecandro.jnidevOpen(115200);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //palmtecandro.jnidevClose();
    }

//    private void Print(String input) {
//        int iLen = 0;
//        int iCount = 0;
//        int iPos = 0;
//
//        byte[] _data = input.getBytes();
//        iLen = _data.length;
//        iLen += 4;
//        final int[] dataArr = new int[iLen];
//        dataArr[0] = (byte) 0x1B;
//        dataArr[1] = (byte) 0x21;
//        dataArr[2] = (byte) 0x00;
//        iCount = 3;
//
//        for (; iCount < iLen - 1; iCount++, iPos++) {
//
//            dataArr[iCount] = _data[iPos];
//        }
//        dataArr[iCount] = (byte) 0x0A;
//
//        palmtecandro.jnidevDataWrite(dataArr, iLen);
//    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.new_ticket_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("New Ticket");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tickets_ID=generateID();
        ticket_count = 0;

        odb = new db();
        oUtility = new Utility();

        boolSignIn=false;

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
                            Log.e("getSignDetail", "Accepted");
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


        rgBoard = new RadioGroup(getApplicationContext());
        rgpair = new RadioGroup(getApplicationContext());
        rgRate = new RadioGroup(getApplicationContext());
        rgBoard.setOrientation(LinearLayout.HORIZONTAL);
        rgpair.setOrientation(LinearLayout.HORIZONTAL);
        rgRate.setOrientation(LinearLayout.HORIZONTAL);

        rb12 = findViewById(R.id.radioTwelve);
        rb3 = findViewById(R.id.radioThree);
        rb7 = findViewById(R.id.radioSeven);
        rbBoard = findViewById(R.id.radioBoard);
        rbPair = findViewById(R.id.radioPair);
        rb3D = findViewById(R.id.radio3D);
        rb4D = findViewById(R.id.radio4D);
        rbBox = findViewById(R.id.radioBox);



        etRate = findViewById(R.id.input_rate);
        etNumber = findViewById(R.id.input_number);
        etQuantity = findViewById(R.id.input_quantity);
        etTotal = findViewById(R.id.input_total);

        //rbBoard.setEnabled(false);
        rgTimeActivity = findViewById(R.id.radioGroupTime);
        rgTicketActivity = findViewById(R.id.radioGroupTicket);
        rgBoradActivity = findViewById(R.id.radioGroupBoardSerial);
        rgPairActivity = findViewById(R.id.radioGroupPairSerial);
        rgRateAcivity = findViewById(R.id.radioGroupRate);
        generateRadioButtonBoard();
        generateRadioButtonPair();
        generateRadioButtonRate(3);

        rgBoradActivity.setVisibility(View.GONE);
        rgPairActivity.setVisibility(View.GONE);
        rgRateAcivity.setVisibility(View.GONE);

        strTicketType="Board";
        strRateType="Rate-1";
        etRate.setEnabled(false);
        etTotal.setEnabled(false);


        btnViewTickets = findViewById(R.id.btn_view_new_ticket);
        btnNewTicket = findViewById(R.id.btn_add_new_ticket);
        btnPrintTicket = findViewById(R.id.btn_print_new_ticket);
        btnCreateTicket = findViewById(R.id.btn_create_new_ticket);

        try {

            btnViewTickets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        startActivity(new Intent(getApplicationContext(), ticketsActivity.class));
                    }
                    catch (Exception e){
                        Log.e("newticket",e.getMessage());
                    }
                }
            });

            btnNewTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        tickets_ID=generateID();
                        ticket_count=0;

                        String strPrint = "Print";
                        btnPrintTicket.setText(strPrint);
                    }
                    catch (Exception e){
                        Log.e("createnewticket", e.getMessage());
                    }
                }
            });

            btnPrintTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    try {
//                        if(ticket_count >= 1) {
//                            //Print(tickets_ID);
//                            Log.e("getrate","printIN - "+ tickets_ID);
//                            odb.getTicketsByTransID(tickets_ID, new HttpCallBackJsonArray() {
//                                @Override
//                                public void onJSONResponse(boolean success, JSONArray response) {
//                                    //Utli.message(getApplicationContext(), "cool");
//                                    int quantity = 0;
//                                    double total = 0;
//                                    boolean printHeaderOnce = true;
//
//                                    for(int i=0; i<= response.length(); i++){
//                                        try {
//                                            JSONObject jsonObject = new JSONObject();
//                                            jsonObject = response.getJSONObject(i);
//
//                                            if(printHeaderOnce){
//
//                                                    Print("          Lucky Boss          ");
//                                                    Print("------------------------------");
//                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                                                    SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yy", Locale.ENGLISH);
//                                                    Date date_val = format.parse(jsonObject.getString("date"));
//                                                    String date = "D:"+date_format.format( date_val);
//                                                    String timeSlot = jsonObject.getString("time_type") + "pm";
//                                                    String billNo = jsonObject.getString("bill_no");
//                                                    Print(date+"|TS:"+ timeSlot+"|BN:"+billNo);
//                                                    Print("------------------------------");
//                                                    String strPrintHeader = "Ticket" + "    " + "Num" + "    " + "Qty" + "   " + "Total";
//                                                    Print(strPrintHeader);
//                                                    Print("------------------------------");
//                                                    printHeaderOnce = false;
//                                            }
//
//                                            String ticket_type;
//                                            String ticketSerial = jsonObject.getString("ticket_serial");
//                                            if(Utli.isStringNull(ticketSerial))
//                                                ticket_type = String.format("%-7s", jsonObject.getString("ticket_type"));
//                                            else
//                                                ticket_type = String.format("%-7s", jsonObject.getString("ticket_type")+"-"+ticketSerial);
//
//
//                                            String number =String.format("%-4s", jsonObject.getString("ticket_number"));
//                                            String strQuantity = String.format("%-3s",jsonObject.getString("quantity"));
//                                            String strTotal = String.format("%-9s",jsonObject.getString("total"));
//                                            quantity = Integer.parseInt(jsonObject.getString("quantity")) + quantity;
//                                            total =  Double.parseDouble(jsonObject.getString("total")) + total;
//                                            String strPrint = ticket_type +"   "+number+"   "+strQuantity+"   "+strTotal;
//                                            Print(strPrint);
//                                        }catch (Exception e){
//                                            Log.e("getprint", e.getMessage());
//                                        }
//                                    }
//
//                                    Print("------------------------------");
//                                    total = Utli.round(total,2);
//                                    String strTotal = "Qty:"+String.valueOf(quantity)+"         Total:"+ String.valueOf(total);
//                                    Print(strTotal);
//                                    Print("");
//                                    Print("");
//                                    Print("");
//                                    Print("");
//                                    Print("");
//
//                                    tickets_ID = generateID();
//                                    ticket_count = 0;
//
//                                    String strPrint = "Print";
//                                    btnPrintTicket.setText(strPrint);
//                                }
//
//                                @Override
//                                public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
//                                    Utli.message(getApplicationContext(), "try again");
//                                }
//                            });
//
//
//                        }
//                        else {
//                            Utli.message(getApplicationContext(), "Please create tickets");
//                        }
//                    }catch (Exception e){
//                        Log.e("createnewticket", e.getMessage());
//                    }
                }
            });


            rbBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rgBoradActivity.setVisibility(View.VISIBLE);
                    rgPairActivity.setVisibility(View.GONE);
                    generateRadioButtonRate(1);
                    rgRateAcivity.setVisibility(View.VISIBLE);

                }
            });

            rbPair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rgBoradActivity.setVisibility(View.GONE);
                    rgPairActivity.setVisibility(View.VISIBLE);
                    generateRadioButtonRate(1);
                    rgRateAcivity.setVisibility(View.VISIBLE);
                }
            });

            rb3D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        rgPairActivity.setVisibility(View.GONE);
                        rgBoradActivity.setVisibility(View.GONE);
                        generateRadioButtonRate(3);
                        rgRateAcivity.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Log.e("createticket", e.getMessage());
                    }
                }
            });

            rb4D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rgPairActivity.setVisibility(View.GONE);
                    rgBoradActivity.setVisibility(View.GONE);
                    generateRadioButtonRate(2);
                    rgRateAcivity.setVisibility(View.VISIBLE);
                }
            });

            rbBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rgPairActivity.setVisibility(View.GONE);
                    rgBoradActivity.setVisibility(View.GONE);
                    generateRadioButtonRate(1);
                    rgRateAcivity.setVisibility(View.VISIBLE);
                }
            });



            etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {
                    try{
                        if(s.length() != 0){

                            if (s.length() == 1 && s.toString().equals("0"))
                                etQuantity.setText("");

                            String rate = etRate.getText().toString().trim();
                            double dobRate;

                            if(TextUtils.isEmpty(rate))
                                dobRate =0;
                            else
                                dobRate = Double.valueOf(rate);


                            int intQuantity = Integer.valueOf(s.toString());

                            double dobTotal = intQuantity * dobRate;
                            etTotal.setText(String.format("%.2f",dobTotal));
                            //etTotal.setText(String.valueOf(dobTotal));
                        }} catch (Exception e){
                        Log.e("printtotal",e.getMessage());
                    }

                }
            });

            etRate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() != 0) {



                        String quantity = etQuantity.getText().toString().trim();
                        int intQuantity;

                        if (TextUtils.isEmpty(quantity))
                            intQuantity = 0;
                        else
                            intQuantity = Integer.valueOf(quantity);

                        double dobRate = Double.valueOf(s.toString());

                        double dobTotal = intQuantity * dobRate;
                        etTotal.setText(String.format("%.2f",dobTotal));
                        //etTotal.setText(String.valueOf(dobTotal));

                    }

                }
            });


            rgTicketActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for(int i=0; i<rgTicketActivity.getChildCount(); i++){
                        RadioButton rdBtn = (RadioButton) rgTicketActivity.getChildAt(i);
                        if(rdBtn.getId() == checkedId){
                            //etRate.setText("0");
                            strTicketType = String.valueOf( rdBtn.getText());

                        }
                    }
                }
            });

            rgRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    for(int i=0; i<rgRate.getChildCount(); i++){
                        RadioButton rdBtn = (RadioButton) rgRate.getChildAt(i);
                        if(rdBtn.getId() == checkedId){
                            strRateType = String.valueOf( rdBtn.getText());

                            if(isNetworkConnected())
                                getRate();
                            else
                                Utli.message(getApplicationContext(),"No internet connection");

                        }
                    }
                }
            });




            btnCreateTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(isNetworkConnected()) {
                            createTicket();


                        }

                        else
                            Utli.message(getApplicationContext(),"No internet connection");
                    }
                    catch (Exception e){
                        Log.e("createticket",e.getMessage());
                    }

                }
            });

            etQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ( actionId == EditorInfo.IME_ACTION_GO) {
                        //createTicket();
                    }
                    return false;
                }
            });

        }
        catch (Exception e){
            Log.e("createnewticket", e.getMessage());
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private String generateID(){
        String returnID;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Hashids hashids = new Hashids("this is my salt");
        returnID = hashids.encrypt(timestamp.getTime());

        return  returnID;
    }


    private void createTicket() {

        try {


            //Validations
            if (etNumber.getText().toString().trim().isEmpty() || etNumber.getText().toString().equals("0")) {
                etNumber.setError("Please fill Number");
                etNumber.requestFocus();
                return;
            } else {
                etNumber.setError(null);
            }

            if (etQuantity.getText().toString().trim().isEmpty() || etQuantity.getText().toString().equals("0")) {
                etQuantity.setError("Please fill Quantity");
                etQuantity.requestFocus();
                return;
            } else {
                etQuantity.setError(null);
            }

            if (etRate.getText().toString().trim().isEmpty() || etRate.getText().toString().equals("0")) {
                Utli.message(getApplicationContext(), "Please select Rate");
                return;
            } else {
                etRate.setError(null);
            }

            if (rgRate.getCheckedRadioButtonId() == -1) {
                Utli.message(getApplicationContext(), "Please select Rate");
                return;
            }

            final JSONObject parameters = new JSONObject();
            parameters.put("trans_id", tickets_ID);
            final String strTimeType = getRadioButtonName(rgTimeActivity.getCheckedRadioButtonId());
            parameters.put("time_type", strTimeType);
            String ticketType = getRadioButtonName(rgTicketActivity.getCheckedRadioButtonId());
            parameters.put("ticket_type", ticketType);
            if (ticketType.equals("Board")) {
                if (rgBoard.getCheckedRadioButtonId() == -1) {
                    Utli.message(getApplicationContext(), "Please select Ticket Serial - A, B or C");
                    return;
                }
                parameters.put("ticket_serial", getRadioButtonName(rgBoard.getCheckedRadioButtonId()));
            } else if (ticketType.equals("Pair")) {
                if (rgpair.getCheckedRadioButtonId() == -1) {
                    Utli.message(getApplicationContext(), "Please select Ticket Serial - AB, BC or AC");
                    return;
                }
                parameters.put("ticket_serial", getRadioButtonName(rgpair.getCheckedRadioButtonId()));
            } else
                parameters.put("ticket_serial", "");

            if (rgRate.getCheckedRadioButtonId() == -1) {
                Utli.message(getApplicationContext(), "Please select Rate");
                return;
            }
            parameters.put("rate_type", getRadioButtonName(rgRate.getCheckedRadioButtonId()));
            parameters.put("ticket_number", etNumber.getText());
            parameters.put("quantity", etQuantity.getText());
            parameters.put("rate", etRate.getText());
            parameters.put("total", etTotal.getText());

            int numberCount = etNumber.length();

            if(ticketType.equals("Board")){
                if(numberCount != 1){
                    etNumber.setError("Please fill 1 digit Number for Board");
                    etNumber.requestFocus();
                    return;
                }
                else {
                    etNumber.setError(null);
                }
            }

            if(ticketType.equals("Pair")){
                if(numberCount != 2){
                    etNumber.setError("Please fill 2 digit Number for Pair");
                    etNumber.requestFocus();
                    return;
                }
                else {
                    etNumber.setError(null);
                }
            }

            if(ticketType.equals("3D")){
                if(numberCount != 3){
                    etNumber.setError("Please fill 3 digit Number for 3D");
                    etNumber.requestFocus();
                    return;
                }
                else {
                    etNumber.setError(null);
                }
            }

            if(ticketType.equals("4D")){
                if(numberCount != 4){
                    etNumber.setError("Please fill 4 digit Number  for 4D");
                    etNumber.requestFocus();
                    return;
                }
                else {
                    etNumber.setError(null);
                }
            }

            if(ticketType.equals("Box")){
                if(numberCount != 3){
                    etNumber.setError("Please fill 3 digit Number for Box");
                    etNumber.requestFocus();
                    return;
                }
                else {
                    etNumber.setError(null);
                }
            }

            //Check sign in granted or not
            odb.getSignDetail(new HttpCallBack() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    try {

                        //Log.d("getratesigninvalue", response.getString("sign_in"));

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
                                //Valid time restriction 12, 3, 7
                                odb.getTimeRestriction(strTimeType, new HttpCallBack() {
                                    @Override
                                    public void onJSONResponse(boolean success, JSONObject response) {
                                        try {

                                            Log.d("gettime", response.getString("returnvalue"));

                                            if (response.getString("returnvalue").equals("success")) {
                                                //Create new ticket
                                                odb.createNewTicket(MainActivity.AUTH_TOKEN, parameters, new HttpCallBack() {
                                                    @Override
                                                    public void onJSONResponse(boolean success, JSONObject response) {
                                                        Utli.message(getApplicationContext(), "Ticket Created Successfully!!");
                                                        etNumber.setText("");
                                                        etQuantity.setText("");
                                                        etRate.setText("");
                                                        etTotal.setText("");
                                                        etNumber.requestFocus();

                                                        ticket_count = ticket_count + 1;
//                                                    if(ticket_count == 1)
//                                                        tickets_ID = generateID();

                                                        String printText = "Print(" + ticket_count + ")";
                                                        btnPrintTicket.setText(printText);
                                                    }

                                                    @Override
                                                    public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
                                                        Utli.message(getApplicationContext(), "Something went wrong..");
                                                    }
                                                });

                                            } else {
                                                if (strTimeType.equals("12"))
                                                    Utli.message(getApplicationContext(), "Entry time is 6am to 11.55pm for 12pm slot");
                                                if (strTimeType.equals("3"))
                                                    Utli.message(getApplicationContext(), "Entry time is 6am to 2.58pm for 3pm slot");
                                                if (strTimeType.equals("7"))
                                                    Utli.message(getApplicationContext(), "Entry time is 6am to 6.55pm for 7pm slot");

                                                boolTimeRestriction = false;

                                            }
                                        } catch (Exception e) {
                                            Log.e("gettime", e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
                                        Log.d("gettime", response.toString());
                                    }
                                });
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


//            if(!boolTimeRestriction) {
//                finish();
//            }


        } catch (Exception e) {
            Log.e("createnewticket", e.getMessage());
        }
    }

    private String getRadioButtonName(int id){
        RadioButton rdbtn = findViewById(id);
        return  rdbtn.getText().toString().trim();
    }

    private void getRate(){

        Log.d("getrate", strTicketType +" - "+strRateType);

            odb.getRate(strTicketType, strRateType, new HttpCallBack() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    try {

                        Log.d("getrate", response.getString("rate"));

                        etRate.setText(response.getString("rate"));

                    } catch (Exception e) {
                        Log.e("getrate", e.getMessage());
                    }

                }

                @Override
                public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
                    Log.d("getrate", response.toString());
                }
            });

    }

//    private void getSigninDetail(){
//
//        //Log.d("getrate", strTicketType +" - "+strRateType);
//
//        odb.getSignDetail(new HttpCallBack() {
//            @Override
//            public void onJSONResponse(boolean success, JSONObject response) {
//                try {
//
//                    Log.d("getrate", response.getString("rate"));
//
//                    //etRate.setText(response.getString("sign_in"));
//                    Utli.message(getApplicationContext(), response.getString("sign_in"));
//
//                } catch (Exception e) {
//                    Log.e("getrate", e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
//                Log.d("getrate", response.toString());
//            }
//        });
//
//    }


    private void generateRadioButtonBoard(){
        rgBoard.addView(prepareRadioButton(rdbtnA, View.generateViewId(),"A"));
        rgBoard.addView(prepareRadioButton(rdbtnB, View.generateViewId(),"B"));
        rgBoard.addView(prepareRadioButton(rdbtnC, View.generateViewId(),"C"));
        rgBoradActivity.addView(rgBoard);
    }

    private void generateRadioButtonPair(){
        rgpair.addView(prepareRadioButton(rdbtnAB, View.generateViewId(),"AB"));
        rgpair.addView(prepareRadioButton(rdbtnBC, View.generateViewId(),"BC"));
        rgpair.addView(prepareRadioButton(rdbtnAC, View.generateViewId(),"AC"));
        rgPairActivity.addView(rgpair);
    }

    private void generateRadioButtonRate(int limit){
        if(rgRate.getChildCount()>0)
            rgRate.removeAllViews();
        if(rgRateAcivity.getChildCount()>0)
            rgRateAcivity.removeAllViews();

        for(int i=0; i<limit; i++){
            rgRate.addView(prepareRadioButton(rdbtnR1, View.generateViewId(),"Rate-"+String.valueOf(i+1)));
        }

        rgRateAcivity.addView(rgRate);
    }

    private RadioButton prepareRadioButton(RadioButton rdbtn, int id, String name){
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);

        rdbtn = new RadioButton(getApplicationContext());
        rdbtn.setId(id);
        rdbtn.setText(name);
        rdbtn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.radio_button_background));
        rdbtn.setButtonDrawable(android.R.color.transparent);
        rdbtn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rdbtn.setLayoutParams(layoutParams);

        return rdbtn;
    }




}

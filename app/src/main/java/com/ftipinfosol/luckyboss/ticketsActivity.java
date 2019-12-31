package com.ftipinfosol.luckyboss;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.ftipinfosol.luckyboss.Adapters.ticketsAdapter;
import com.ftipinfosol.luckyboss.Helpers.ClickListener;
import com.ftipinfosol.luckyboss.Helpers.HttpCallBack;
import com.ftipinfosol.luckyboss.Helpers.HttpCallBackJsonArray;
import com.ftipinfosol.luckyboss.Helpers.Utli;
import com.ftipinfosol.luckyboss.Helpers.db;
import com.softland.palmtecandro.palmtecandro;


public class ticketsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<JSONObject> ticket_list = new ArrayList<>();
    ticketsAdapter adapter;
    private SearchView searchView;
    db odb;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.tickets_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Tickets History");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        odb = new db();
        //add new line test gitHub
        //add new line test gitHub from FTip-3
        //add new line test gitHub from FTip-5

        if (Utli.isNetworkConnected(getApplicationContext())) {

            //Check sign in granted or not
            odb.getSignDetail(new HttpCallBack() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    try {

                        Log.d("getratesigninvalue", response.getString("sign_in"));

                        if(response.getString("sign_in").equals("Yes") ) {
                            //Utli.message(getApplicationContext(), "Accepted");;
                            Log.e("test","cool");

                        }
                        else {
                            Utli.message(getApplicationContext(), "Re-login requested from Admin");
                            startActivity(new Intent(getApplicationContext(),loginActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e("getrate", e.getMessage());
                    }
                }

                @Override
                public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
                    Log.d("getrate", response.toString());
                }
            });


            //Get all tickets
            odb.get_tickts(new HttpCallBackJsonArray() {
                @Override
                public void onJSONResponse(boolean success, JSONArray response) {
                    try {


                        for (int i = 0; i < response.length(); i++) {


                            ticket_list.add(response.getJSONObject(i));

                            //Log.d("getticketsone", ticket_list.get(i).toString());
                        }
                        Log.e("getticketin", ticket_list.toString());

                        adapter = new ticketsAdapter(ticket_list, new ClickListener() {
                            @Override
                            public void onItemClick(final JSONObject response) {

//                                new AlertDialog.Builder(getApplicationContext())
//                                        .setTitle("Confirmation")
//                                        .setMessage("Do you really want to Print?")
//                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                                try {
//                                                    String billNo = response.getString("bill_no");
//                                                    printTicket(billNo);
//                                                }catch (Exception e){
//                                                    Log.e("printticket", e.getMessage());
//                                                }
//
//                                            }})
//                                        .setNegativeButton(android.R.string.no, null).show();

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                //Yes button clicked
                                                try {
                                                    String billNo = response.getString("bill_no");
                                                    printTicket(billNo);
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

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ticketsActivity.this, R.style.myDialog));
                                builder.setMessage("Are you sure want to print?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

                            }
                        });

                        recyclerView = findViewById(R.id.recycler_list_tickets);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                        Log.e("gettickets", e.getMessage());
                    }

                }

                @Override
                public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {

                }
            });
        } else {
            Utli.message(getApplicationContext(), "No internet connection");
        }
    }

    private void printTicket(String billNo){
//        try {
//            //Utli.message(getApplicationContext(), "I am on tap - " + response.getString("bill_no"));
//
//
//
//            odb.getTicketsByBillNo(billNo, new HttpCallBackJsonArray() {
//                @Override
//                public void onJSONResponse(boolean success, JSONArray response) {
//                    Log.e("printticket",response.toString());
//
//                    int quantity = 0;
//                    double total = 0;
//                    boolean printHeaderOnce = true;
//
//                    for(int i=0; i<= response.length(); i++){
//                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject = response.getJSONObject(i);
//                            Log.e("printticket",jsonObject.toString());
//
//                            if(printHeaderOnce){
//
//                                Print("          Lucky Boss          ");
//                                Print("------------------------------");
//                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                                SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yy", Locale.ENGLISH);
//                                Date date_val = format.parse(jsonObject.getString("date"));
//                                String date = "D:"+date_format.format( date_val);
//                                String timeSlot = jsonObject.getString("time_type") + "pm";
//                                String billNo = jsonObject.getString("bill_no");
//                                Print(date+"|TS:"+ timeSlot+"|BN:"+billNo);
//                                Print("------------------------------");
//                                String strPrintHeader = "Ticket" + "    " + "Num" + "    " + "Qty" + "   " + "Total";
//                                Print(strPrintHeader);
//                                Print("------------------------------");
//                                printHeaderOnce = false;
//                            }
//
//                            String ticket_type;
//                            String ticketSerial = jsonObject.getString("ticket_serial");
//                            if(Utli.isStringNull(ticketSerial))
//                                ticket_type = String.format("%-7s", jsonObject.getString("ticket_type"));
//                            else
//                                ticket_type = String.format("%-7s", jsonObject.getString("ticket_type")+"-"+ticketSerial);
//
//
//                            String number =String.format("%-4s", jsonObject.getString("ticket_number"));
//                            String strQuantity = String.format("%-3s",jsonObject.getString("quantity"));
//                            String strTotal = String.format("%-9s",jsonObject.getString("total"));
//                            quantity = Integer.parseInt(jsonObject.getString("quantity")) + quantity;
//                            total =  Double.parseDouble(jsonObject.getString("total")) + total;
//                            String strPrint = ticket_type +"   "+number+"   "+strQuantity+"   "+strTotal;
//                            Print(strPrint);
//                        }catch (Exception e){
//                            Log.e("getprint", e.getMessage());
//                        }
//                    }
//
//                    Print("------------------------------");
//                    total = Utli.round(total,2);
//                    String strTotal = "Qty:"+String.valueOf(quantity)+"         Total:"+ String.valueOf(total);
//                    Print(strTotal);
//                    Print("");
//                    Print("");
//                    Print("");
//                    Print("");
//                    Print("");
//
//                }
//
//                @Override
//                public void onEmptyResponse(boolean success, int statusCode, JSONObject response) {
//                    Utli.message(getApplicationContext(), "try again");
//                }
//            });
//
//        }catch (Exception e){
//            Log.e("printticket", e.getMessage());
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticket_search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        else if (id== android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
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
    protected void onStart() {
        super.onStart();
        //palmtecandro.jnidevOpen(115200);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //palmtecandro.jnidevClose();
    }
}

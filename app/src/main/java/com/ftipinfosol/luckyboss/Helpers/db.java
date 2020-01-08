package com.ftipinfosol.luckyboss.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.ftipinfosol.luckyboss.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;



public class db {

    public void createNewTicket(String AUTH_TOKEN, JSONObject getparams, final HttpCallBack callback){
        try {

            RequestParams params = new RequestParams();
            AsyncHttpClient client = new AsyncHttpClient();

            params.put("trans_id", getparams.getString("trans_id"));
            params.put("time_type", getparams.getString("time_type"));
            params.put("ticket_type", getparams.getString("ticket_type"));
            params.put("ticket_serial", getparams.getString("ticket_serial"));
            params.put("rate_type", getparams.getString("rate_type"));
            params.put("ticket_number", getparams.getString("ticket_number"));
            params.put("quantity", getparams.getString("quantity"));
            params.put("rate", getparams.getString("rate"));
            params.put("total", getparams.getString("total"));


            client.addHeader("Authorization", AUTH_TOKEN);
            client.addHeader("Accept", "application/json");

            Log.e("createnewticket", "in");
            Log.e("createnewticket", apiURL.NEW_TICKET);

            client.post(apiURL.NEW_TICKET, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e("createnewticket", "onsucess");
                    Log.e("createnewticket", response.toString());


                    callback.onJSONResponse(true, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                    e.printStackTrace();
                    Log.e("createnewticket", "onfailure");
                    Log.e("createnewticket", e.getMessage());

                }

            });
        }catch (Exception e){
            Log.e("createnewticket",e.getMessage());
        }

    }

    public void getTicketsByTransID( String transID, final HttpCallBackJsonArray callBackJsonArray){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        params.put("trans_id", transID);

        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        Log.e("getTicketsByTransID", MainActivity.AUTH_TOKEN);

        client.post(apiURL.GET_TICKETS_BY_TRANSID, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.e("getTicketsByTransID","onsucess");
                Log.e("getTicketsByTransID",jsonArray.toString());


                callBackJsonArray.onJSONResponse(true, jsonArray);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
                Log.e("getTicketsByTransID","onfailure");
                Log.e("getTicketsByTransID",e.getMessage());

            }

        });

    }

    public void getTicketsByBillNo( String billNo, final HttpCallBackJsonArray callBackJsonArray){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        params.put("bill_no", billNo);

        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        Log.e("getTicketsByBillNo", MainActivity.AUTH_TOKEN);

        client.post(apiURL.GET_TICKETS_BY_BILLNO, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.e("getTicketsByBillNo","onsucess");
                Log.e("getTicketsByBillNo",jsonArray.toString());


                callBackJsonArray.onJSONResponse(true, jsonArray);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
                Log.e("getTicketsByBillNo","onfailure");
                Log.e("getTicketsByBillNo",e.getMessage());

            }

        });

    }

    public void getRate( String ticketType, String rateType, final HttpCallBack callback){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        params.put("ticket_type", ticketType);
        params.put("rate_type", rateType);

        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        Log.e("getsignrate", MainActivity.AUTH_TOKEN);

        client.post(apiURL.GET_RATE, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("getrate","onsucess");
                Log.e("getrate",response.toString());


                callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
                Log.e("getrate","onfailure");
                Log.e("getrate",e.getMessage());

            }

        });

    }

    public void getSignDetail( final HttpCallBack callback){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();



        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        Log.e("getSignDetaildb", MainActivity.AUTH_TOKEN);


        client.post(apiURL.GET_SIGN_IN_DETAIL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("getSignDetaildbsuccess","onsucess");
                Log.e("getSignDetaildbsuccess",response.toString());


                callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e("getSignDetaildbfailure","onfailure");
                Log.e("getSignDetaildbfailure",e.getMessage());
                Log.e("getSignDetaildbfailure",response.toString());
                callback.onJSONResponse(true, response);

            }

        });

    }

    public void updateSignDetail( final HttpCallBack callback){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();



        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        Log.e("updateSignDetailindb", MainActivity.AUTH_TOKEN);


        client.post(apiURL.UPDATE_SIGN_IN_DETAIL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("updateSignDetail","onsucess");
                Log.e("updateSignDetail",response.toString());


                callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e("updateSignDetail","onfailure");
                Log.e("updateSignDetail",e.getMessage());

            }

        });

    }

    public void getTimeRestriction(String time_type, final HttpCallBack callback){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        params.put("time", time_type);

        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        Log.e("gettime", MainActivity.AUTH_TOKEN);


        client.post(apiURL.GET_TIME_RESTRICTION, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("gettime","onsucess");
                Log.e("gettime",response.toString());


                callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {

                Log.e("gettime","onfailure");
                Log.e("gettime",e.getMessage());

            }

        });

    }




    public void get_tickts(final HttpCallBackJsonArray callBackJsonArray){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<JSONObject> returnList = new ArrayList<>();

//        params.put("ticket_type", ticketType);
//        params.put("rate_type", rateType);

        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        client.post(apiURL.GET_TICKETS, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.e("gettickets","onsucess");
                //Log.e("gettickets",response.toString());
                //returnList = jsonArray;
                try {

                    //Log.e("getticketsdbarray", jsonArray.toString());


//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject ticket = new JSONObject();
//                        //ticket.put("time_type", jsonArray.getJSONObject(i))
//
//                        returnList.add(jsonArray.getJSONObject(i));
//
//                        //Check if it is added to the list
//                        Log.d("getticketsone", returnList.get(i).toString());
//                    }
                    callBackJsonArray.onJSONResponse(true, jsonArray);
                }catch (Exception e){
                    Log.e("gettickets",e.getMessage());
                }


                //callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
                Log.e("gettickets","onfailure");
                Log.e("gettickets",e.getMessage());

            }

        });
    }

    public void get_results_history(final HttpCallBackJsonArray callBackJsonArray){
        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        final ArrayList<JSONObject> returnList = new ArrayList<>();

//        params.put("ticket_type", ticketType);
//        params.put("rate_type", rateType);

        client.addHeader("Authorization", MainActivity.AUTH_TOKEN);
        client.addHeader("Accept", "application/json");

        client.post(apiURL.GET_RESULTS_HISTORY, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.e("getresults","onsucess");
                //Log.e("gettickets",response.toString());
                //returnList = jsonArray;
                try {

                    //Log.e("getticketsdbarray", jsonArray.toString());


//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject ticket = new JSONObject();
//                        //ticket.put("time_type", jsonArray.getJSONObject(i))
//
//                        returnList.add(jsonArray.getJSONObject(i));
//
//                        //Check if it is added to the list
//                        Log.d("getticketsone", returnList.get(i).toString());
//                    }
                    callBackJsonArray.onJSONResponse(true, jsonArray);
                }catch (Exception e){
                    Log.e("getresults",e.getMessage());
                }


                //callback.onJSONResponse(true, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                e.printStackTrace();
                Log.e("getresults","onfailure");
                Log.e("getresults",e.getMessage());

            }

        });
    }



}

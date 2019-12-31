package com.ftipinfosol.luckyboss;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ftipinfosol.luckyboss.Adapters.resultHistoryAdapter;
import com.ftipinfosol.luckyboss.Adapters.ticketsAdapter;
import com.ftipinfosol.luckyboss.Helpers.ClickListener;
import com.ftipinfosol.luckyboss.Helpers.HttpCallBack;
import com.ftipinfosol.luckyboss.Helpers.HttpCallBackJsonArray;
import com.ftipinfosol.luckyboss.Helpers.Utli;
import com.ftipinfosol.luckyboss.Helpers.db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class resultHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<JSONObject> ticket_list = new ArrayList<>();
    resultHistoryAdapter adapter;
    private SearchView searchView;
    db odb;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.result_history_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Results History");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        odb = new db();



        if (Utli.isNetworkConnected(getApplicationContext())) {

            //Check sign in granted or not
            odb.getSignDetail(new HttpCallBack() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    try {

                        Log.d("getratesigninvalue", response.getString("sign_in"));

                        if(response.getString("sign_in").equals("Yes") ) {
                            //Utli.message(getApplicationContext(), "Accepted");
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

            //Get all results
            odb.get_results_history(new HttpCallBackJsonArray() {
                @Override
                public void onJSONResponse(boolean success, JSONArray response) {
                    try {


                        for (int i = 0; i < response.length(); i++) {


                            ticket_list.add(response.getJSONObject(i));

                            //Log.d("getticketsone", ticket_list.get(i).toString());
                        }
                        Log.e("getresult", ticket_list.toString());

                        adapter = new resultHistoryAdapter(ticket_list, new ClickListener() {
                            @Override
                            public void onItemClick(JSONObject response) {

                            }
                        });

                        recyclerView = findViewById(R.id.recycler_list_result_history);
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


}

package com.ftipinfosol.luckyboss.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ftipinfosol.luckyboss.Helpers.ClickListener;
import com.ftipinfosol.luckyboss.Helpers.Utli;
import com.ftipinfosol.luckyboss.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class resultHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<JSONObject> ticket;
    private List<JSONObject> filtered;
    private final ClickListener listener;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filtered = ticket;
                    //Log.e("printticket", filtered.toString());
                } else {
                    List<JSONObject> filteredList = new ArrayList<>();

                    for (JSONObject row : ticket) {
                        try {
                            if (row.getString("date").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("total_quantity").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("total_rate").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("total_prize").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("balance").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("balance").toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    filtered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //Added
                //filtered.clear();

                filtered = (ArrayList<JSONObject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public resultHistoryAdapter(List<JSONObject> ticket, ClickListener listener) {
        this.ticket = ticket;
        this.filtered = ticket;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date, quantity, rate, prize, balance, excess;

        MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.result_history_card_date);
            quantity = view.findViewById(R.id.result_history_card_quantity);
            rate = view.findViewById(R.id.result_history_card_rate);
            prize = view.findViewById(R.id.result_history_card_prize);
            balance = view.findViewById(R.id.result_history_card_balance);
            excess= view.findViewById(R.id.result_history_card_excess);
        }

        void bind(final JSONObject tic) {
            try {

                Date date_val = format.parse(tic.getString("date"));

                double balanced = Double.parseDouble(tic.getString("balance"));
                if(balanced < 0)
                    balanced = 0;

                double excessD = Double.parseDouble(tic.getString("excess"));
                if(excessD < 0)
                    excessD = 0;


                date.setText("Date: "+ date_format.format(date_val));
                quantity.setText("Quantity: "+tic.getString("total_quantity"));
                rate.setText("Rate: "+tic.getString("total_rate"));
                prize.setText("Prize: "+tic.getString("total_prize"));
                balance.setText("Balance: "+String.valueOf(balanced));
                excess.setText("Excess: "+String.valueOf(excessD));
//                quantity.setText("Quantity: ");
//                rate.setText("Rate: ");
//                prize.setText("Prize: ");
//                balance.setText("Balance: ");
//                excess.setText("Quantity: ");

            }catch (Exception e) {
                Log.e("resultprint", e.getMessage());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(tic);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_history_card, null);
        return new resultHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        resultHistoryAdapter.MyViewHolder holder = (resultHistoryAdapter.MyViewHolder) viewHolder;
        holder.bind(filtered.get(i));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }
}

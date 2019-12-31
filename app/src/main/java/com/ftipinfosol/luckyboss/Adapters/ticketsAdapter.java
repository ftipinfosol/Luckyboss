package com.ftipinfosol.luckyboss.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.ftipinfosol.luckyboss.Helpers.ClickListener;
import com.ftipinfosol.luckyboss.Helpers.Utli;
import com.ftipinfosol.luckyboss.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ticketsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
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
                            if (row.getString("time_type").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("ticket_type").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("ticket_serial").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("ticket_number").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("date").toLowerCase().contains(charString.toLowerCase()) ||
                                    row.getString("quantity").toLowerCase().contains(charString.toLowerCase())||
                                    row.getString("total").toLowerCase().contains(charString.toLowerCase())) {
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

    public ticketsAdapter(List<JSONObject> ticket, ClickListener listener) {
        this.ticket = ticket;
        this.filtered = ticket;
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time_type, ticket_type,    ticket_serial, ticket_number, bill_no;
        Button btnPrint;

        MyViewHolder(View view) {
            super(view);
            time_type=view.findViewById(R.id.tickets_card_time_type);
            ticket_type=view.findViewById(R.id.tickets_card_ticket_type);
            ticket_serial=view.findViewById(R.id.tickets_card_ticket_serial);
            ticket_number=view.findViewById(R.id.tickets_card_ticket_number);
            bill_no= view.findViewById(R.id.tickets_card_bill_no);
            btnPrint = view.findViewById(R.id.btn_print_from_card);
        }

        void bind(final JSONObject tic) {
            try {
                String ticketSerial = tic.getString("ticket_serial");
                if(Utli.isStringNull(ticketSerial))
                    ticketSerial = "XX";

                time_type.setText(tic.getString("time_type") +" - "+tic.getString("ticket_type")+" - "+ticketSerial+" - "+tic.getString("ticket_number"));

                Date date_val = format.parse(tic.getString("date"));
                ticket_type.setText("Date: "+date_format.format( date_val));

                ticket_number.setText("Total: "+tic.getString("total"));
                ticket_serial.setText("Quantity: "+ tic.getString("quantity"));
                bill_no.setText("Bill No: "+tic.getString("bill_no"));
            }catch (Exception e) {
                Log.e("ticketprint", e.getMessage());
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
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tickets_card, null);
        return new ticketsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        ticketsAdapter.MyViewHolder holder = (ticketsAdapter.MyViewHolder) viewHolder;
        holder.bind(filtered.get(i));

        ((MyViewHolder) viewHolder).btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("printticket", "I am print - "+ ((MyViewHolder) viewHolder).bill_no.getText().toString().substring(9));
            }
        });
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }




}

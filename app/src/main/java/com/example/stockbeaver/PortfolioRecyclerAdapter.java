package com.example.stockbeaver;

import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class PortfolioRecyclerAdapter  extends RecyclerView.Adapter<PortfolioRecyclerAdapter.MyViewHolder> {

    private final Map<String, Object> stockMap;
    private ArrayList<String> stockNames;
    private PortfolioRecyclerAdapter.RecyclerViewClickListener listener;

    /**
     * initializes habitNames and listener
     *
     * @param stockNames
     * @param hashMap
     * @param listener
     */
    public PortfolioRecyclerAdapter(ArrayList<String> stockNames, Map<String, Object> hashMap, RecyclerViewClickListener listener) {
        this.stockNames = stockNames;
        this.stockMap = hashMap;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView ticker;
        private TextView position;
        private TextView value;
        private TextView cost;
        private TextView dailyPrice;
        private TextView dailyPerc;
        private TextView totalGain;
        private TextView totalPerc;


        /**
         * @param view
         */
        public MyViewHolder(final View view) {
            super(view);
            ticker = view.findViewById(R.id.company_ticker);
            position = view.findViewById(R.id.position);
            value = view.findViewById(R.id.value_portfolio);
            cost = view.findViewById(R.id.cost_portfolio);
            dailyPrice = view.findViewById(R.id.dailyPrice_portfolio);
            dailyPerc = view.findViewById(R.id.dailyPerc_portfolio);
            totalGain = view.findViewById(R.id.totalPrice_portfolio);
            totalPerc = view.findViewById(R.id.totalPerc_portfolio);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public PortfolioRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_content, parent, false);
        return new PortfolioRecyclerAdapter.MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull PortfolioRecyclerAdapter.MyViewHolder holder, int position) {
        String symbol = stockNames.get(position);
        holder.ticker.setText(symbol);

        // float formatter
        NumberFormat formatter = new DecimalFormat("0.00");

        // get values
        Map map = (Map) stockMap.get(symbol);
        String size = (String) map.get("size");
        String price = (String) map.get("price");
        holder.position.setText(String.format("%s shares @ %s", size, price));

        int total_cost = Integer.parseInt(size) * Integer.parseInt(price);
        holder.cost.setText(String.valueOf(total_cost));



        // set rest of portfolio data
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Stock stock = YahooFinance.get(symbol);

            // change
            BigDecimal change = stock.getQuote().getChangeInPercent();
            if (change.compareTo(BigDecimal.ZERO) < 0){
                holder.dailyPerc.setTextColor(Color.RED);
            }
            else {
                holder.dailyPerc.setTextColor(Color.rgb(0,128,0));
            }
            String temp = change.toString().concat("%");
            holder.dailyPerc.setText(String.format("  %s  ", temp));

            // price
            BigDecimal dPrice = stock.getQuote(true).getPrice();
            holder.dailyPrice.setText(dPrice.toString());

            // value
            float total_value = dPrice.floatValue() * Integer.parseInt(size);
            String temp3 = formatter.format(total_value);
            holder.value.setText(temp3);

            // total price
            float total_gain = total_value - total_cost;
            String temp2 = formatter.format(total_gain);
            holder.totalGain.setText(temp2);

            // total percentage
            float total_per = (total_gain / total_cost) * 100;
            if (total_per < 0){
                holder.totalPerc.setTextColor(Color.RED);
            }
            else {
                holder.totalPerc.setTextColor(Color.rgb(0,128,0));
            }
            String temp1 = formatter.format(total_per).concat("%");
            holder.totalPerc.setText(String.format("  %s  ", temp1));


        } catch (Exception e) {
            e.printStackTrace();
            holder.totalPerc.setText("Error");
        }


    }

    @Override
    public int getItemCount() {
        return stockNames.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
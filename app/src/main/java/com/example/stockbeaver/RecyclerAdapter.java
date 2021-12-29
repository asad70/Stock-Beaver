package com.example.stockbeaver;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import java.math.BigDecimal;
import java.util.ArrayList;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;


/**
 * RecyclerAdaptor deals with watchlist data
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private ArrayList<String> stockNames;
    private RecyclerViewClickListener listener;

    /**
     * initializes habitNames and listener
     * @param stockNames
     * @param listener
     */
    public  RecyclerAdapter(ArrayList<String> stockNames, RecyclerViewClickListener listener){
        this.stockNames = stockNames;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView ticker;
        private TextView name;
        private TextView price;
        private TextView perc;

        /**
         *
         * @param view
         */
        public MyViewHolder(final View view){
            super(view);
            ticker = view.findViewById(R.id.sector_name);
            name = view.findViewById(R.id.stockName_watchlist);
            price = view.findViewById(R.id.price_watchlist);
            perc = view.findViewById(R.id.dailyPerc_watchlist);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String symbol = stockNames.get(position);
        holder.ticker.setText(symbol);

        // set company data
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            // name
            Stock stock = YahooFinance.get(symbol);
            String compName = stock.getName();
            holder.name.setText(compName);

            // change
            BigDecimal change = stock.getQuote().getChangeInPercent();
            holder.perc.setText(change.toString());

            // price
            BigDecimal price = stock.getQuote(true).getPrice();
            holder.price.setText(price.toString());

        } catch (Exception e) {
            e.printStackTrace();
            holder.name.setText("Couldn't retrieve data; internet");
            Log.d("Tag", String.valueOf(e));
        }

        // set company price
        
    }

    @Override
    public int getItemCount() {
        return stockNames.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
}
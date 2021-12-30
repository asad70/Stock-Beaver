package com.example.stockbeaver;



import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.mainstringargs.alphavantagescraper.AlphaVantageConnector;
import io.github.mainstringargs.alphavantagescraper.TimeSeries;
import io.github.mainstringargs.alphavantagescraper.input.timeseries.OutputSize;
import io.github.mainstringargs.alphavantagescraper.output.AlphaVantageException;
import io.github.mainstringargs.alphavantagescraper.output.timeseries.Daily;
import io.github.mainstringargs.alphavantagescraper.output.timeseries.data.StockData;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link currentInfoFrag#} factory method to
 * create an instance of this fragment.
 */
public class currentInfoFrag extends Fragment {
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_info, container, false);

        context = container.getContext();

        //Intent intent = getIntent();
        //clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE+"1");

        String clickedStock = "PLTR";
        Log.d("Tagclicked", clickedStock);

        // set the app bar title to name of company else to symbol
        Stock stocks = null;
        String compName = clickedStock;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            stocks = YahooFinance.get(clickedStock);
            compName = stocks.getName();

        } catch (IOException e) {
            //setTitle(clickedStock);
            e.printStackTrace();
        }

        // detail list
        ArrayList<String> arrayOfNames = new ArrayList<String>(Arrays.asList("Date", "Open", "High", "Low", "Close", "Volume"));
        ArrayList<String> arrayOfNameData = new ArrayList<String>();

        // get the stock daily data.
        String apiKey = "B0LKVW4P80ZWF54E";
        int timeout = 10000;
        AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
        TimeSeries stockTimeSeries = new TimeSeries(apiConnector);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Daily response = stockTimeSeries.daily(clickedStock, OutputSize.COMPACT);
            Map<String, String> metaData = response.getMetaData();
            List<StockData> stockData = response.getStockData();
            stockData.forEach(stock -> {
                if (arrayOfNameData.size() < 5){
                    arrayOfNameData.add(String.valueOf(stock.getDateTime()).substring(0,10));
                    arrayOfNameData.add(String.valueOf(stock.getOpen()));
                    arrayOfNameData.add(String.valueOf(stock.getHigh()));
                    arrayOfNameData.add(String.valueOf(stock.getLow()));
                    arrayOfNameData.add(String.valueOf(stock.getClose()));
                    arrayOfNameData.add(String.valueOf(stock.getVolume()));
                }
            });
        } catch (AlphaVantageException e) {
            System.out.println("something went wrong");
        }


        // Create the adapter to convert the array to views
        CustomStockDetailAdapter adapter = new CustomStockDetailAdapter(context, arrayOfNames, arrayOfNameData);
        // Attach the adapter to a ListView
        ListView listView = v.findViewById(R.id.stock_detail_listview);
        listView.setAdapter(adapter);

        return v;
    }

}

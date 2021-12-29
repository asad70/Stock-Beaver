package com.example.stockbeaver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.mainstringargs.alphavantagescraper.AlphaVantageConnector;
import io.github.mainstringargs.alphavantagescraper.SectorPerformances;
import io.github.mainstringargs.alphavantagescraper.TimeSeries;
import io.github.mainstringargs.alphavantagescraper.input.timeseries.Interval;
import io.github.mainstringargs.alphavantagescraper.input.timeseries.OutputSize;
import io.github.mainstringargs.alphavantagescraper.output.AlphaVantageException;
import io.github.mainstringargs.alphavantagescraper.output.timeseries.Daily;
import io.github.mainstringargs.alphavantagescraper.output.timeseries.IntraDay;
import io.github.mainstringargs.alphavantagescraper.output.timeseries.data.StockData;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class ViewStockDetail extends AppCompatActivity {
    public static String clickedStock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_stock_detail);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE+"1");

        // set the app bar title to name of company else to symbol
        Stock stocks = null;
        String compName = clickedStock;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            stocks = YahooFinance.get(clickedStock);
            compName = stocks.getName();

            setTitle(compName);
        } catch (IOException e) {
            setTitle(clickedStock);
            e.printStackTrace();
        }

        // detail list
        ArrayList<String> arrayOfNames = new ArrayList<String>(Arrays.asList("Date", "Open", "High", "Low", "Close", "Volume"));
        ArrayList<String> arrayOfNameData = new ArrayList<String>();

        // get the stock daily data.
        String apiKey = "B0LKVW4P80ZWF54E";
        int timeout = 3000;
        AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
        TimeSeries stockTimeSeries = new TimeSeries(apiConnector);
        try {
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
        CustomStockDetailAdapter adapter = new CustomStockDetailAdapter(this, arrayOfNames, arrayOfNameData);
        // Attach the adapter to a ListView
        ListView listView = findViewById(R.id.stock_detail_listview);
        listView.setAdapter(adapter);
    }

    // show back menu
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}

package com.example.stockbeaver;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.mainstringargs.alphavantagescraper.AlphaVantageConnector;
import io.github.mainstringargs.alphavantagescraper.StockQuotes;
import io.github.mainstringargs.alphavantagescraper.output.AlphaVantageException;
import io.github.mainstringargs.alphavantagescraper.output.quote.StockQuotesResponse;
import io.github.mainstringargs.alphavantagescraper.output.quote.data.StockQuote;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link currentInfoFrag#} factory method to
 * create an instance of this fragment.
 */
public class currentInfoFrag extends Fragment {
    Context context;
    Button detailedChart;
    Button companyPro;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_info, container, false);

        context = container.getContext();

        Intent intent = getActivity().getIntent();
        String clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE + "1");

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
        ArrayList<String> arrayOfNames = new ArrayList<String>(Arrays.asList("Latest Trading Day", "Open", "Low/High", "Change/Change %" , "Previous Close", "Volume"));
        ArrayList<String> arrayOfNameData = new ArrayList<String>();

        // get the stock daily data.
        String apiKey = "B0LKVW4P80ZWF54E";
        int timeout = 10000;
        AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
        StockQuotes stockQuotes = new StockQuotes(apiConnector);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            StockQuotesResponse response = stockQuotes.quote(clickedStock);
            StockQuote stock = response.getStockQuote();
            arrayOfNameData.add(String.valueOf(stock.getLatestTradingDay()));
            arrayOfNameData.add(String.valueOf(stock.getOpen()));
            String high = String.valueOf(stock.getHigh());
            String low = String.valueOf(stock.getLow());
            String highLow = low + "/" + String.format("%.2f", Float.parseFloat(high));
            arrayOfNameData.add(highLow);
            String change = String.valueOf(stock.getChange());
            String change_perc = String.valueOf(stock.getChangePercent());
            String change_per_both = change + "/" + String.format("%.2f", Float.parseFloat(change_perc)) + "%";
            arrayOfNameData.add(change_per_both);
            arrayOfNameData.add(String.valueOf(stock.getPreviousClose()));
            arrayOfNameData.add(String.valueOf(stock.getVolume()));
        } catch (AlphaVantageException e) {
            System.out.println("something went wrong");
        } catch (Exception e){
            Toast.makeText(getContext(), "Invalid Ticker",
                    Toast.LENGTH_LONG).show();
        }


        // set the webview
        int size = 0;
        Stock stock = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            stock = YahooFinance.get(clickedStock);
            String exchange = stock.getStockExchange();

            if (exchange.startsWith("Nasdaq")){
                exchange = "nasdaq";
            }

            String ex_sym = exchange.toUpperCase() + ":" + clickedStock.toUpperCase();

            // chart
            InputStream is = context.getAssets().open("symbol.html");
            size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String str = new String(buffer);

            str = str.replace("XXXX", ex_sym.toUpperCase());

            // Get a handle on your webview
            WebView webViewHeroes = v.findViewById(R.id.symbol_webview);
            webViewHeroes.getSettings().setAllowContentAccess(true);
            webViewHeroes.getSettings().setAllowFileAccess(true);
            // Populate webview with your html
            webViewHeroes.getSettings().setJavaScriptEnabled(true);
            webViewHeroes.setOnTouchListener((v1, event) -> true);
            webViewHeroes.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }



        // Create the adapter to convert the array to views
        CustomStockDetailAdapter adapter = new CustomStockDetailAdapter(context, arrayOfNames, arrayOfNameData);
        // Attach the adapter to a ListView
        ListView listView = v.findViewById(R.id.stock_detail_listview);
        listView.setAdapter(adapter);



        detailedChart = v.findViewById(R.id.detailed_chart);
        detailedChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), DetailedChart.class);
                intent1.putExtra("clickedStock", clickedStock);
                startActivity(intent1);
            }
        });
        companyPro = v.findViewById(R.id.company_profile);
        companyPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getActivity(), CompanyProfile.class);
                intent2.putExtra("clickedStock", clickedStock);
                startActivity(intent2);
            }
        });
        return v;
    }

}

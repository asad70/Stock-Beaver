package com.example.stockbeaver;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class DetailedChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_chart);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detailed Chart");

        Intent intent = getIntent();
        String clickedStock = intent.getStringExtra("clickedStock");

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
            InputStream is = this.getAssets().open("DetailedChart.html");
            size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String str = new String(buffer);

            str = str.replace("XXXX", ex_sym.toUpperCase());

            // Get a handle on your webview
            WebView webViewHeroes = findViewById(R.id.detailed_chart_webview);
            webViewHeroes.getSettings().setAllowContentAccess(true);
            webViewHeroes.getSettings().setAllowFileAccess(true);
            // Populate webview with your html
            webViewHeroes.getSettings().setJavaScriptEnabled(true);
            webViewHeroes.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // show back menu
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), companyInfoTabs.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
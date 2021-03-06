package com.example.stockbeaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFrag} factory method to
 * create an instance of this fragment.
 */
public class NewsFrag extends Fragment {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        context = container.getContext();


        Intent intent = getActivity().getIntent();
        String clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE + "1");

        // name
        Stock stock = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            stock = YahooFinance.get(clickedStock);
            String exchange = stock.getStockExchange();

            if (exchange.startsWith("Nasdaq")) {
                exchange = "nasdaq";
            }

            String ex_sym = exchange.toUpperCase() + ":" + clickedStock.toUpperCase();
            Log.d("Tagsym", ex_sym);

            InputStream is = context.getAssets().open("CompanyTimeline.html");
            int size = is.available();

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert the buffer into a string.
            String str = new String(buffer);

            str = str.replace("XXXX", ex_sym);


            // Get a handle on your webview
            WebView webViewHeroes = v.findViewById(R.id.company_timeline_webview);
            webViewHeroes.getSettings().setUseWideViewPort(true);
            webViewHeroes.getSettings().setAllowContentAccess(true);
            webViewHeroes.getSettings().setAllowFileAccess(true);
            // Populate webview with your html
            webViewHeroes.getSettings().setJavaScriptEnabled(true);
            webViewHeroes.setWebViewClient(new WebViewClient() {
                @Override //for APIs 24 and later
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return true;
                }
            });
            //webViewHeroes.loadUrl("file:///android_asset/technicalIndicator.html");
            webViewHeroes.loadDataWithBaseURL(null, str, "text/html", "UTF-8", null);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;

    }
}
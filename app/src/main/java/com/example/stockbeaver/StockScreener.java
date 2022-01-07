package com.example.stockbeaver;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class StockScreener extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screener_activity);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Stock Screener");

        WebView webViewHeroes = findViewById(R.id.screener_webview);
        webViewHeroes.getSettings().setAllowContentAccess(true);
        webViewHeroes.getSettings().setAllowFileAccess(true);
        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadUrl("file:///android_asset/Screener.html");
    }

    // show back menu
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
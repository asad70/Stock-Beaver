package com.example.stockbeaver;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AddStockPortfolio extends AppCompatActivity{

        private static final String TAG = "MainActivity";

        public void sendInput(String input) {
            mSymbol = input;
            mSize = input;
            mPrice = input;

        }

        //vars
        public String mSymbol;
        public String mSize;
        public String mPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_portfolio);

    }

}




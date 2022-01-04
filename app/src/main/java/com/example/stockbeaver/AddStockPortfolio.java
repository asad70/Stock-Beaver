package com.example.stockbeaver;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddStockPortfolio extends AppCompatActivity{

        private static final String TAG = "MainActivity";

        public void sendInput(String input) {
            mSymbol = input;
            mSize = input;
            mPrice = input;

        }

        //widgets
        private FloatingActionButton mOpenDialog;

        //vars
        public String mSymbol;
        public String mSize;
        public String mPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_portfolio);
        mOpenDialog = findViewById(R.id.fab_add_portfolio);
        mOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPortfolioDialogFrag dialog = new AddPortfolioDialogFrag();
                dialog.show(getFragmentManager(), "AddPortfolioDialogFrag");
            }
        });

    }

}




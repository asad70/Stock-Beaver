package com.example.stockbeaver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddStockPortfolio extends AppCompatActivity implements AddPortfolioDialogFrag.OnInputListener {

        private static final String TAG = "MainActivity";

        public void sendInput(String input) {
            Log.d(TAG, "sendInput: got the input: " + input);
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
                    Log.d(TAG, "onClick: opening dialog.");
                    AddPortfolioDialogFrag dialog = new AddPortfolioDialogFrag();
                    dialog.show(getFragmentManager(), "AddPortfolioDialogFrag");
                }
            });

        }
}




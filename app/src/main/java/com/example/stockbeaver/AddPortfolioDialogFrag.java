package com.example.stockbeaver;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;

public class AddPortfolioDialogFrag extends DialogFragment {

    private static final String TAG = "MyCustomDialog";


    public interface OnInputListener{
        void sendInput(String input);
    }

    public OnInputListener mSymbol;
    public OnInputListener mSize;
    public OnInputListener mPrice;

    //widgets
    private AutoCompleteTextView symbol;
    private EditText size, price;
    private Button mActionOk, mActionCancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_porfolio_dialog, container, false);
        mActionCancel = view.findViewById(R.id.cancel_add_port);
        mActionOk = view.findViewById(R.id.add_port);
        symbol = view.findViewById(R.id.portfolioSearch);
        size = view.findViewById(R.id.postition_size);
        price = view.findViewById(R.id.position_price);


        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Data not saved",
                        Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });


        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_symbol = symbol.getText().toString();
                String input_size = size.getText().toString();
                String input_price = price.getText().toString();


                URL url = new URL("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=tesco&apikey=demo");
                Gson gson = new Gson();

                String json = readUrl("http://api.wunderground.com/api/57dd9039b81a9c21/conditions/q/CA/San_Francisco.json");

                // Page page = gson.fromJson(json, Page.class);
                Response response = gson.fromJson(json, Response.class);

                String toastOutput = input_symbol.toUpperCase() + " added to the portfolio for price of " + input_price + " per share. Total share(s): " + input_size;

                if(!input_symbol.equals("") && !input_size.equals("") && !input_price.equals("")){
                    Toast.makeText(getActivity(), toastOutput,
                            Toast.LENGTH_LONG).show();

                    //Easiest way: just set the value
                    //((MainActivity)getActivity()).mInputDisplay.setText(input);

                    getDialog().dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "All field are Required",
                            Toast.LENGTH_LONG).show();
                }

                //"Best Practice" but it takes longer
               /* mSymbol.sendInput(input);
                mSize.sendInput(input1);
                mPrice.sendInput(input2);
                getDialog().dismiss();*/
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mSymbol = (OnInputListener) getActivity();
            mSize = (OnInputListener) getActivity();
            mPrice = (OnInputListener) getActivity();

        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}



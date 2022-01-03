package com.example.stockbeaver;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;


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
        Context c = getActivity().getApplicationContext();
        mActionCancel = view.findViewById(R.id.cancel_add_port);
        mActionOk = view.findViewById(R.id.add_port);
        symbol = view.findViewById(R.id.portfolioSearch);
        size = view.findViewById(R.id.postition_size);
        price = view.findViewById(R.id.position_price);


        AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.portfolioSearch);
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                selection  = selection.split("\n")[0];
                autoComplete.setText(selection);
            }
        });


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

                if(!input_symbol.equals("")){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        Stock stock = YahooFinance.get(input_symbol);
                        Log.d("Tagname", stock.getName());

                        input_symbol = input_symbol.toUpperCase();
                        String toastOutput = input_symbol + " added to the portfolio for price of " + input_price + " per share. Total share(s): " + input_size;
                        if (!input_symbol.equals("") && !input_size.equals("") && !input_price.equals("")) {

                            TradeInfo trade = new TradeInfo(input_symbol.toUpperCase(), input_size, input_price, "Public");
                            UserFirebase userFirebase = new UserFirebase();
                            userFirebase.addNewTrade(trade, input_symbol);

                            Toast.makeText(getActivity(), toastOutput,
                                    Toast.LENGTH_LONG).show();

                            //Easiest way: just set the value
                            //((MainActivity)getActivity()).mInputDisplay.setText(input);

                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getActivity(), "All field are Required",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Ticker isn't valid",
                                Toast.LENGTH_LONG).show();
                        Log.d("Tagfail", e.getMessage());
                        e.printStackTrace();
                    }
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





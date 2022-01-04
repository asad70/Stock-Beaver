package com.example.stockbeaver;

import static com.example.stockbeaver.MainPage.email;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;


public class AddPortfolioDialogFrag extends DialogFragment {

    Map<String, Object> map;
    //widgets
    private AutoCompleteTextView symbol;
    private EditText size, price;
    private Button mActionOk, mActionCancel;

    String sizeDb;
    String priceDb;



    String input_symbol;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Initialize
        View view = inflater.inflate(R.layout.add_porfolio_dialog, container, false);
        mActionCancel = view.findViewById(R.id.cancel_add_port);
        mActionOk = view.findViewById(R.id.add_port);
        symbol = view.findViewById(R.id.portfolioSearch);
        size = view.findViewById(R.id.postition_size);
        price = view.findViewById(R.id.position_price);

        // set the values if edit
        Bundle bundle = this.getArguments();
        if(bundle != null){
            String editStock = String.valueOf(bundle.get("stockClicked"));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("portfolio").document(email);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            map = document.getData();
                            Map mapComp = (Map) map.get(editStock);
                            sizeDb = (String) mapComp.get("size");
                            priceDb = (String) mapComp.get("price");
                            symbol.setText(editStock);
                            symbol.setEnabled(false);
                            size.setText(sizeDb);
                            price.setText(priceDb);
                            onCancelPress();
                            onAddPressEdit();
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
        }

        else {
            autoCompleteSet(view);
            onCancelPress();
            onAddPress();
        }
        return view;
    }

    // Add method for editing the position
    private void onAddPressEdit() {
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data
                String input_symbol = symbol.getText().toString();
                String input_size = size.getText().toString();
                String input_price = price.getText().toString();

                TradeInfo trade = new TradeInfo(input_symbol, input_size, input_price, "Public");
                UserFirebase userFirebase = new UserFirebase();
                userFirebase.editTrade(trade);
                String toastOutput = input_symbol + " edited now the price is " + input_price + " per share. Total share(s): " + input_size;
                Toast.makeText(getActivity(), toastOutput,
                        Toast.LENGTH_LONG).show();

                PortfolioFragment.getInstance().addTrade(input_symbol, input_price, input_size);
                getDialog().dismiss();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void autoCompleteSet(View view) {

        // continue
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.portfolioSearch);

        // get data and set the auto suggestions
        Data newClassObj = new Data();
        String[] symbols = newClassObj.getData();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item, symbols);
        autoComplete.setThreshold(1);
        autoComplete.setAdapter(adapter1);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                selection  = selection.split("\n")[0];
                autoComplete.setText(selection);
            }
        });
    }

    private void onAddPress() {
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_symbol = symbol.getText().toString();
                String input_size = size.getText().toString();
                String input_price = price.getText().toString();

                if(!input_symbol.equals("")){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    try {
                        Stock stock = YahooFinance.get(input_symbol);

                        input_symbol = input_symbol.toUpperCase();
                        String toastOutput = input_symbol + " added to the portfolio for price of " + input_price + " per share. Total share(s): " + input_size;
                        if (!input_symbol.equals("") && !input_size.equals("") && !input_price.equals("")) {

                            TradeInfo trade = new TradeInfo(input_symbol.toUpperCase(), input_size, input_price, "Public");
                            UserFirebase userFirebase = new UserFirebase();
                            userFirebase.addNewTrade(trade, input_symbol);

                            Toast.makeText(getActivity(), toastOutput,
                                    Toast.LENGTH_LONG).show();

                            PortfolioFragment.getInstance().addTrade(input_symbol, input_price, input_size);
                            getDialog().dismiss();
                        } else {
                            Toast.makeText(getActivity(), "All field are Required",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Ticker isn't valid",
                                Toast.LENGTH_LONG).show();
                        Log.d("tagerror", String.valueOf(e));
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onCancelPress() {
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Data not saved",
                        Toast.LENGTH_LONG).show();
                getDialog().dismiss();
            }
        });
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}





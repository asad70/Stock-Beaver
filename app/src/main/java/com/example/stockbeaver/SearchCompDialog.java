package com.example.stockbeaver;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SearchCompDialog extends DialogFragment{


    //widgets
    private AutoCompleteTextView symbol;
    private Button mActionSearch;
    String input_symbol;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Initialize
        View view = inflater.inflate(R.layout.search_company_dialog, container, false);
        mActionSearch = view.findViewById(R.id.search_company_ok);
        symbol = view.findViewById(R.id.company_search);


        autoCompleteSet(view);
        onSearchPress();
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void autoCompleteSet(View view) {

        // continue
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.company_search);

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


    private void onSearchPress() {
        mActionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_symbol = symbol.getText().toString();
                Intent intent = new Intent(getActivity(), companyInfoTabs.class); //Adding stock name to intent
                intent.putExtra(MainActivity.EXTRA_MESSAGE + "1", input_symbol);
                startActivity(intent);
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}





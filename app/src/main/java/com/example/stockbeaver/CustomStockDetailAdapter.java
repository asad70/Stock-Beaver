package com.example.stockbeaver;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomStockDetailAdapter extends ArrayAdapter<String> {

    private ArrayList<String> arrayOfNames;
    private ArrayList<String> arrayOfNameData;
    Context context;

    public CustomStockDetailAdapter(Context context,  ArrayList<String> arrayOfNames, ArrayList<String> arrayOfNameData){
        super(context, 0, arrayOfNames);
        this.arrayOfNames = arrayOfNames;
        this.arrayOfNameData = arrayOfNameData;
        this.context = context;    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.stock_detail_content, parent, false);
        }

        // set the content
        TextView detailName = convertView.findViewById(R.id.detail_name);
        TextView detailPerformance = convertView.findViewById(R.id.detail_data);
        // Populate the data into the template view using the data object
        detailName.setText(arrayOfNames.get(position));
        detailPerformance.setText(arrayOfNameData.get(position));

        // Return the completed view to render on screen
        return convertView;
    }
}
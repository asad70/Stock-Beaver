package com.example.stockbeaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<String> {

    private ArrayList<String> arrayOfHeadlines;
    private ArrayList<String> arrayOfDetail;
    Context context;

    public NewsAdapter(Context context,  ArrayList<String> arrayOfHeadlines, ArrayList<String> arrayOfDetail){
        super(context, 0, arrayOfHeadlines);
        this.arrayOfHeadlines = arrayOfHeadlines;
        this.arrayOfDetail = arrayOfDetail;
        this.context = context;    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_content, parent, false);
        }

        // set the content
        TextView detailName = convertView.findViewById(R.id.headline);
        TextView detailPerformance = convertView.findViewById(R.id.new_detail);
        // Populate the data into the template view using the data object
        detailName.setText(arrayOfHeadlines.get(position));
        detailPerformance.setText(arrayOfDetail.get(position));

        // Return the completed view to render on screen
        return convertView;
    }
}
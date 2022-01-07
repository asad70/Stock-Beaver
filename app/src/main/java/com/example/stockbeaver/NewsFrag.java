package com.example.stockbeaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFrag} factory method to
 * create an instance of this fragment.
 */
public class NewsFrag extends Fragment {
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        context = container.getContext();


        Intent intent = getActivity().getIntent();
        String clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE + "1");

        // detail list
        ArrayList<String> arrayOfHeadlines = new ArrayList<String>();
        ArrayList<String> arrayOfDetail = new ArrayList<String>();


        // Create the adapter to convert the array to views
        CustomStockDetailAdapter adapter = new CustomStockDetailAdapter(context, arrayOfHeadlines, arrayOfDetail);
        // Attach the adapter to a ListView
        ListView listView = v.findViewById(R.id.news_listview);
        listView.setAdapter(adapter);



        return v;


    }
}
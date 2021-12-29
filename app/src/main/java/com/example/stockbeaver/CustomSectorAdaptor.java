package com.example.stockbeaver;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomSectorAdaptor extends ArrayAdapter<String> {

    private ArrayList<String> arrayOfSectors;
    private ArrayList<String> arrayOfSectorsPerf;
    Context context;

    public CustomSectorAdaptor(AddStockWatchlist context, ArrayList<String> arrayOfSectorsPerf, ArrayList<String> arrayOfSectors) {
        super(context, 0, arrayOfSectors);
        this.arrayOfSectors = arrayOfSectors;
        this.arrayOfSectorsPerf = arrayOfSectorsPerf;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sector_content, parent, false);
        }

        // Get the data item for this position
        String sectorNames = getItem(position);

        // Lookup view for data population
        TextView sectorName = convertView.findViewById(R.id.sector_name);
        TextView sectorPerformance = convertView.findViewById(R.id.sector_performance);
        // Populate the data into the template view using the data object
        sectorName.setText(arrayOfSectors.get(position));
        if (Double.parseDouble(arrayOfSectorsPerf.get(position)) < 0){
            sectorPerformance.setTextColor(Color.RED);
        }
        else {
            sectorPerformance.setTextColor(Color.GREEN);
        }
        sectorPerformance.setText(arrayOfSectorsPerf.get(position));
        // Return the completed view to render on screen
        return convertView;
    }
}
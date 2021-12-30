package com.example.stockbeaver;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import io.github.mainstringargs.alphavantagescraper.AlphaVantageConnector;
import io.github.mainstringargs.alphavantagescraper.SectorPerformances;
import io.github.mainstringargs.alphavantagescraper.output.AlphaVantageException;
import io.github.mainstringargs.alphavantagescraper.output.sectorperformances.Sectors;
import io.github.mainstringargs.alphavantagescraper.output.sectorperformances.data.SectorData;

public class AddStockWatchlist extends AppCompatActivity {

    ListView sectorList;
    TextView sectorName;
    TextView sectorPerformance;


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_watchlist);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectorList = findViewById(R.id.sector_list);
        sectorName = findViewById(R.id.sector_name);
        sectorPerformance = findViewById(R.id.sector_performance);

        // sectors list
        ArrayList<String> arrayOfSectors = new ArrayList<String>(Arrays.asList("Consumer Discretionary", "Consumer Staples", "Energy", "Financials", "HealthCare", "Industrials", "InformationTechnology", "Materials", "Real Estate", "Utilities"));
        ArrayList<String> arrayOfSectorsPerf = new ArrayList<String>();

        // get live  sector performance
        String apiKey = "B0LKVW4P80ZWF54E";
        int timeout = 3000;
        AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
        SectorPerformances sectorPerformances = new SectorPerformances(apiConnector);
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Sectors response = sectorPerformances.sector();
            Map<String, String> metaData = response.getMetaData();
            List<SectorData> sectors = response.getSectors();
            sectors.forEach(data -> {
                if(data.getKey().equals("Rank A: Real-Time Performance")) {
                    arrayOfSectorsPerf.add(String.valueOf(data.getConsumerDiscretionary()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getConsumerStaples()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getEnergy()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getFinancials()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getHealthCare()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getIndustrials()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getInformationTechnology()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getMaterials()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getRealEstate()));
                    arrayOfSectorsPerf.add(String.valueOf(data.getUtilities()));
                }
            });
        } catch (AlphaVantageException e) {
            System.out.println("something went wrong");
        }

        // Create the adapter to convert the array to views
        CustomSectorAdaptor adapter = new CustomSectorAdaptor(this, arrayOfSectorsPerf, arrayOfSectors);
        // Attach the adapter to a ListView
        ListView listView = findViewById(R.id.sector_list);
        listView.setAdapter(adapter);

    }


    // show back menu
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}


package com.example.stockbeaver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class AddStockWatchlist extends AppCompatActivity {

    ListView sectorList;
    TextView sectorName;
    TextView sectorPerformance;
    AutoCompleteTextView autocomplete;
    Button addWatchlist;

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_watchlist);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectorList = findViewById(R.id.sector_list);
        sectorName = findViewById(R.id.company_symbol);
        sectorPerformance = findViewById(R.id.sector_performance);
        addWatchlist = findViewById(R.id.add_watchlist);

        // get data and set the auto suggestions
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.search_watchlist);
        Data newClassObj = new Data();
        String[] symbols = newClassObj.getData();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, symbols);
        autocomplete.setThreshold(1);
        autocomplete.setAdapter(adapter1);

        // on click of add to watchlist button
        onClickAddWatchlist();

        // display sector information
        displaySectorInfo();
    }


    // show back menu
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void onClickAddWatchlist() {
        addWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_symbol = autocomplete.getText().toString();
                // check if stock is already in watchlist
                ArrayList<String> stockNames = (ArrayList<String>) getIntent().getSerializableExtra("stockName");
                UserFirebase userFirebase = new UserFirebase();

                // if symbol not empty checks -> valid and not in watchlist, else error
                if(!input_symbol.equals("")){
                    if(!stockNames.contains((input_symbol))) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        try {
                            Stock stock = YahooFinance.get(input_symbol);
                            // add to firebase
                            userFirebase.addToWatchList(input_symbol);
                            finish();
                            WatchlistFragment.getInstance().addSymbol(input_symbol);

                        } catch (Exception e) {
                            Toast.makeText(AddStockWatchlist.this, "Ticker isn't valid",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    else {
                        String msg = input_symbol + " is already in Watchlist";
                        Toast.makeText(AddStockWatchlist.this, msg,
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(AddStockWatchlist.this, "Ticker is required",  Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void displaySectorInfo() {
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

}


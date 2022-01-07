package com.example.stockbeaver;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.stockbeaver.databinding.ActivityCompanyInfoBinding;
import com.example.stockbeaver.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * companyInfoTabs holds tab layout information; finished
 */
public class companyInfoTabs extends AppCompatActivity {

    private ActivityCompanyInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.stockTabs;
        tabs.setupWithViewPager(viewPager);

        // get the name of company clicked and set it as the title
        Intent intent = this.getIntent();
        String clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE + "1");
        Stock stock = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            stock = YahooFinance.get(clickedStock);
            String compName = stock.getName();
            String title = compName + " (" + clickedStock + ")";
            setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    // show back menu
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainPage.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
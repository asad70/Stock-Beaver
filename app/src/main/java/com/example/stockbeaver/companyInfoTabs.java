package com.example.stockbeaver;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.stockbeaver.ui.main.SectionsPagerAdapter;
import com.example.stockbeaver.databinding.ActivityCompanyInfoBinding;

/**
 * companyInfoTabs holds tab layout information; finished
 */
public class companyInfoTabs extends AppCompatActivity {

    private ActivityCompanyInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String clickedStock = intent.getStringExtra(MainActivity.EXTRA_MESSAGE + "1");
        // passing clicked stock to fragment
        Bundle bundle = new Bundle();
        bundle.putString("stocktosearch", clickedStock);
        currentInfoFrag fragobj = new currentInfoFrag();
        fragobj.setArguments(bundle);

        binding = ActivityCompanyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.stockTabs;
        tabs.setupWithViewPager(viewPager);

    }

}
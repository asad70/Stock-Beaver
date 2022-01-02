package com.example.stockbeaver;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockbeaver.databinding.FragmentPortfolioBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class PortfolioFragment extends Fragment{

    private FragmentPortfolioBinding binding;

    private String TAG = "SAMPLE";
    ArrayList<String> stockName = new ArrayList<>();
    FloatingActionButton addPortfolio;
    private PortfolioRecyclerAdapter.RecyclerViewClickListener listener;
    public String clickedStock;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);

        RecyclerView portfolio = view.findViewById(R.id.portfolio_listview);

        UserFirebase userFirebase = new UserFirebase();
        userFirebase.getPortfolio(new UserFirebase.UserInterface() {
            @Override
            public void getPortfolio(Map<String, Object> hashMap) {
                if(getActivity() != null) {
                    ArrayList<String> stockNameList = new ArrayList<>(hashMap.keySet());
                    Log.d("Tagmap", String.valueOf(hashMap));
                    PortfolioRecyclerAdapter recyclerAdaptor = new PortfolioRecyclerAdapter(stockNameList, hashMap, listener);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    portfolio.setLayoutManager(layoutManager);
                    portfolio.setItemAnimator(new DefaultItemAnimator());
                    portfolio.setAdapter(recyclerAdaptor);
                }
            }

            @Override
            public void getUserInterfaceWatchList(ArrayList<String> watchlistArrayList) {
            }

        });

        addPortfolio = view.findViewById(R.id.fab_add_portfolio);
        addPortfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getFragmentManager();
                AddPortfolioDialogFrag dialog = new AddPortfolioDialogFrag();
                dialog.show(fm, "AddPortfolioDialogFrag");

            }
        });



        return view;
    }


    /**
     * Listens for when a symbol is clicked.
     */
    private void setOnClickListener() {
        listener = new PortfolioRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickedStock = (String) stockName.get(position); //Getting name of stock
                Intent intent = new Intent(getActivity(), companyInfoTabs.class); //Adding stock name to intent
                intent.putExtra(MainActivity.EXTRA_MESSAGE + "1", clickedStock);
                startActivity(intent);
            }
        };
    }


    // move around watchlist item
}
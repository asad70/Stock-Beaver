package com.example.stockbeaver;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Map;

public class WatchlistFragment extends Fragment {

    private String TAG = "SAMPLE";
    ArrayList<String> stockName = new ArrayList<>();
    FloatingActionButton addStockWatchList;
    RecyclerAdapter recyclerAdaptor;
    private RecyclerAdapter.RecyclerViewClickListener listener;
    public String clickedStock;
    private static WatchlistFragment instance = null;
    public static WatchlistFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        instance = this;
        RecyclerView watchList = view.findViewById(R.id.watchlist_listview);
        UserFirebase userFirebase = new UserFirebase();
        userFirebase.getListOfWatchList(new UserFirebase.UserInterface() {
            @Override
            public void getUserInterfaceWatchList(ArrayList<String> watchlistArrayList) {
                if(getActivity() != null) {
                    stockName = watchlistArrayList;
                    setOnClickListener();
                    recyclerAdaptor = new RecyclerAdapter(watchlistArrayList, listener);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    watchList.setLayoutManager(layoutManager);
                    watchList.setItemAnimator(new DefaultItemAnimator());
                    watchList.setAdapter(recyclerAdaptor);

                    // add swipe to delete button
                    SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
                        @Override
                        public void onRightClicked(int position) {
                            // remove from firebase
                            UserFirebase userFirebase = new UserFirebase();
                            String stock = stockName.get(position);
                            userFirebase.removeFromWatchList(stock);
                            String msg = stock + " removed from the watchlist";
                            Toast.makeText(getContext(), msg,
                                    Toast.LENGTH_LONG).show();
                            stockName.remove(position);
                            recyclerAdaptor.notifyItemRemoved(position);
                            recyclerAdaptor.notifyItemRangeChanged(position, recyclerAdaptor.getItemCount());
                        }
                    });

                    ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                    itemTouchhelper.attachToRecyclerView(watchList);
                    watchList.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                            swipeController.onDraw(c);
                        }
                    });
                }
            }

            @Override
            public void getPortfolio(Map<String, Object> hashMap) {
            }

        });

        addStockWatchList = view.findViewById(R.id.fab_add_watchlist);
        addStockWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddStockWatchlist.class);
                intent.putExtra("stockName",stockName);
                startActivity(intent);
            }
        });


        return view;
    }

    // add the symbol to list / from add watchlist activity
    public void addSymbol(String symbol){
        stockName.add(symbol);
        recyclerAdaptor.notifyDataSetChanged();
        String msg = symbol + " Added to the Watchlist";
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * Listens for when a symbol is clicked.
     */
    private void setOnClickListener() {
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickedStock = (String) stockName.get(position); //Getting name of stock
                Intent intent = new Intent(getActivity(), companyInfoTabs.class); //Adding stock name to intent
                intent.putExtra(MainActivity.EXTRA_MESSAGE + "1", clickedStock);
                startActivity(intent);
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
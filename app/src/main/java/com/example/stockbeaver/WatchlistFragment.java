package com.example.stockbeaver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WatchlistFragment extends Fragment {

    private String TAG = "SAMPLE";
    ArrayList<String> stockName = new ArrayList<>();
    FloatingActionButton addStockWatchList;
    private RecyclerAdapter.RecyclerViewClickListener listener;
    public String clickedStock;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        RecyclerView watchList = view.findViewById(R.id.watchlist_listview);

        UserFirebase userFirebase = new UserFirebase();
        userFirebase.getListOfWatchList(new UserFirebase.UserInterface() {
            @Override
            public void getUserInterfaceWatchList(ArrayList<String> watchlistArrayList) {
                if(getActivity() != null) {
                    stockName = watchlistArrayList;
                    setOnClickListener();
                    RecyclerAdapter recyclerAdaptor = new RecyclerAdapter(watchlistArrayList, listener);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    watchList.setLayoutManager(layoutManager);
                    watchList.setItemAnimator(new DefaultItemAnimator());
                    watchList.setAdapter(recyclerAdaptor);
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
                startActivity(intent);
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(watchList);
        return view;
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


    // move around watchlist item
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(stockName, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped( @NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

    };
}
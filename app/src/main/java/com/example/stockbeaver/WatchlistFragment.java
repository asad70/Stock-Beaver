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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class WatchlistFragment extends Fragment {

    private String TAG = "SAMPLE";
    ArrayList<String> stockName = new ArrayList<>();
    FloatingActionButton addStockWatchList;
    private RecyclerAdapter.RecyclerViewClickListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);

        RecyclerView watchList = view.findViewById(R.id.watchlist_listview);

        UserFirebase userFirebase = new UserFirebase();
        userFirebase.getListOfWatchList(new UserFirebase.UserInterface() {
            @Override
            public void getUserInterface(ArrayList<String> watchlistArrayList) {
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

        });

        addStockWatchList = view.findViewById(R.id.fab_add_watchlist);
        addStockWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddStockWatchlist.class);
                startActivity(intent);
            }
        });

        userFirebase.rootRef.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userFirebase.getListOfWatchList(new UserFirebase.UserInterface() {
                    public void getUserInterface(ArrayList<String> watchlistArrayList) {
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

                });
            }
        });


        return view;
    }




    /**
     * Listens for when a symbol is clicked.
     */
    private void setOnClickListener() {
        listener = new RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                RecyclerView habitList = view.findViewById(R.id.watchlist_listview);
                String clickedHabit = (String) stockName.get(position); //Getting name of habit
                //Intent intent = new Intent(getActivity(), watchListInfo.class); //Adding habit name to intent
                //intent.putExtra(MainActivity.EXTRA_MESSAGE + "1", clickedHabit);
                //startActivity(intent);
            }
        };
    }
}
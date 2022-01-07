package com.example.stockbeaver;

import static com.example.stockbeaver.MainPage.email;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PortfolioFragment extends Fragment{

    FloatingActionButton addPortfolio;
    private PortfolioRecyclerAdapter.RecyclerViewClickListener listener;
    public String clickedStock;
    PortfolioRecyclerAdapter recyclerAdaptor;
    ArrayList<String> stockNameList = new ArrayList<>();
    Map<String, Object> map;

    private static PortfolioFragment instance;
    public static PortfolioFragment getInstance() {
        return instance;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);
        instance = this;
        RecyclerView portfolio = view.findViewById(R.id.portfolio_listview);
        setHasOptionsMenu(true);

        UserFirebase userFirebase = new UserFirebase();
        userFirebase.getPortfolio(new UserFirebase.UserInterface() {
            @Override
            public void getPortfolio(Map<String, Object> hashMap) {
                if(getActivity() != null) {
                    stockNameList = new ArrayList<>(hashMap.keySet());
                    Log.d("Tagsml", String.valueOf(stockNameList));
                    recyclerAdaptor = new PortfolioRecyclerAdapter(stockNameList, hashMap, listener);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    portfolio.setLayoutManager(layoutManager);
                    portfolio.setItemAnimator(new DefaultItemAnimator());
                    portfolio.setAdapter(recyclerAdaptor);


                    // add swipe to delete button
                    SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
                        @Override
                        public void onRightClicked(int position) {
                            // remove from firebase
                            UserFirebase userFirebase = new UserFirebase();
                            String stock = stockNameList.get(position);
                            userFirebase.removeFromPortfolio(stock);
                            String msg = stock + " removed from the portfolio";
                            Toast.makeText(getContext(), msg,
                                    Toast.LENGTH_LONG).show();
                            stockNameList.remove(position);
                            recyclerAdaptor.notifyItemRemoved(position);
                            recyclerAdaptor.notifyItemRangeChanged(position, recyclerAdaptor.getItemCount());
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onLeftClicked(int position) {
                            Bundle bundle = new Bundle();
                            bundle.putString("stockClicked",stockNameList.get(position));
                            FragmentManager fm = getActivity().getFragmentManager();
                            AddPortfolioDialogFrag dialog = new AddPortfolioDialogFrag();
                            dialog.setArguments(bundle);
                            dialog.show(fm, "AddPortfolioDialogFrag");
                            recyclerAdaptor.notifyDataSetChanged();
                        }
                    });

                    ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                    itemTouchhelper.attachToRecyclerView(portfolio);
                    portfolio.addItemDecoration(new RecyclerView.ItemDecoration() {
                        @Override
                        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                            swipeController.onDraw(c);
                        }
                    });
                }
            }

            @Override
            public void getUserInterfaceWatchList(ArrayList<String> watchlistArrayList) {
            }

        });

        return view;
    }
    // add the trade to list / from add portfolio dialog fragment activity
    public void addTrade(String symbol, String input_price, String input_size){
        // add data to list
        stockNameList.add(symbol);
        Map<String, Object> hashMap =  new HashMap<String, Object>();
        hashMap.put("price", input_price);
        hashMap.put("size", input_size);
        HashMap<String, Object> temp = new HashMap<>();
        temp.put(symbol, temp);
        RecyclerView portfolio = getActivity().findViewById(R.id.portfolio_listview);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("portfolio").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        map = document.getData();
                        Log.d("tagmanp", String.valueOf(map));
                        // update map
                        map.put(symbol,hashMap);
                        Log.d("tagmanp1", String.valueOf(map));
                        stockNameList = new ArrayList<>(map.keySet());
                        Log.d("tagmanp2", String.valueOf(stockNameList));
                        recyclerAdaptor = new PortfolioRecyclerAdapter(stockNameList, map, listener);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        portfolio.setLayoutManager(layoutManager);
                        portfolio.setItemAnimator(new DefaultItemAnimator());
                        portfolio.setAdapter(recyclerAdaptor);
                        recyclerAdaptor.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * Listens for when a symbol is clicked.
     */
    private void setOnClickListener() {
        listener = new PortfolioRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                clickedStock = (String) stockNameList.get(position); //Getting name of stock
                Intent intent = new Intent(getActivity(), companyInfoTabs.class); //Adding stock name to intent
                intent.putExtra(MainActivity.EXTRA_MESSAGE + "1", clickedStock);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add_paper_trade).setVisible(false);
        menu.findItem(R.id.add_friend).setVisible(false);
        menu.findItem(R.id.addWatchlist).setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.search_company:
            FragmentManager fm = getActivity().getFragmentManager();
            SearchCompDialog dialog = new SearchCompDialog();
            dialog.show(fm, "SearchCompDialog");
            return true;
        case R.id.add_portfolio:
            //add the function to perform here
            FragmentManager fm1 = getActivity().getFragmentManager();
            AddPortfolioDialogFrag dialog1 = new AddPortfolioDialogFrag();
            dialog1.show(fm1, "AddPortfolioDialogFrag");
            return true;

        case R.id.advance_chart:
            Intent intent0 = new Intent(getContext(), AdvancedChart.class);
            startActivity(intent0);
            return true;
        case R.id.stock_screener:
            Intent intent1 = new Intent(getContext(), StockScreener.class);
            startActivity(intent1);
            return true;
        case R.id.market_movers:
            Intent intent2 = new Intent(getContext(), MarketMovers.class);
            startActivity(intent2);
            return true;
        case R.id.market_overview:
            Intent intent3 = new Intent(getContext(), MarketOverview.class);
            startActivity(intent3);
            return true;
        case R.id.economic_calendar:
            Intent intent4 = new Intent(getContext(), EconomicCalendar.class);
            startActivity(intent4);
            return true;

    }
        return(super.onOptionsItemSelected(item));
    }

}
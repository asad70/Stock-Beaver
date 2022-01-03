package com.example.stockbeaver;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class UserFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    interface UserInterface {
        void getUserInterfaceWatchList(ArrayList<String> watchlistArrayList);
        void getPortfolio(Map<String, Object> portfolioMap);
    }


    public UserFirebase() {
    }

    public FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private final String email = MainPage.email;
    Source source = Source.SERVER;

    // gets the list of watchlist from firebase
    public void getListOfWatchList(UserInterface userInterface){
        DocumentReference docRef = db.collection("users").document("email");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.get("watchlist"));
                        ArrayList<String> watchList = (ArrayList<String>) document.get("watchlist");
                        userInterface.getUserInterfaceWatchList(watchList);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    // gets the list of portfolio from firebase
    public void getPortfolio(UserInterface userInterface){
        DocumentReference docRef = db.collection("portfolio").document("email");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        Log.d("Map", map.toString());
                        if(map == null) return;
                        userInterface.getPortfolio(map);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }

    public void addNewTrade(TradeInfo trade, String compSymbol){
        final CollectionReference collectionReference = rootRef.collection("portfolio");
        String compSymbols = trade.getCompSymbol();
        //get the reason
        String positionSizes = trade.getPositionSize();
        //get the start date
        String price = trade.getPrice();
        //get privacy status
        String privacy = trade.getPrivacy();



        // get existing data about the trade
        DocumentReference docRef = db.collection("portfolio").document("email");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int total_size = 0;
                int total_price = 0;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> map = document.getData();
                        Log.d("Map", map.toString());
                        if (map == null) return;
                        ArrayList<String> stockNameList = new ArrayList<>(map.keySet());
                        Map mapComp = (Map) map.get(compSymbol);
                        Log.d("Tagmap", String.valueOf(mapComp));
                        if (mapComp != null) {
                            // get existing position from db
                            String size = (String) mapComp.get("size");
                            String price1 = (String) mapComp.get("price");

                            // update sizing and price
                            total_size = Integer.parseInt(size) + Integer.parseInt(positionSizes);
                            total_price = (Integer.parseInt(price1) * Integer.parseInt(size)) + (Integer.parseInt(price) * Integer.parseInt(positionSizes));
                            total_price = total_price / total_size;

                            // update db
                            HashMap<String, Object> temp = new HashMap<>();
                            temp.put("size", String.valueOf(total_size));
                            temp.put("price", String.valueOf(total_price));
                            HashMap<String, HashMap<String, Object>> data = new HashMap<>();
                            data.put(compSymbol, temp);
                            collectionReference
                                    .document("email")
                                    .set(data, SetOptions.merge());
                        }
                        else{
                            // update db if no position found
                            HashMap<String, Object> temp = new HashMap<>();
                            temp.put("size", String.valueOf(positionSizes));
                            temp.put("price", String.valueOf(price));

                            HashMap<String, HashMap<String, Object>> data = new HashMap<>();
                            data.put(compSymbol, temp);
                            collectionReference
                                    .document("email")
                                    .set(data, SetOptions.merge());
                        }
                    }
                }
            }
        });

        //HashMap<String, Object> temp = new HashMap<>();
        //temp.put("compSymbol", compSymbols);
        //temp.put("status", privacy);

//        HashMap<String, HashMap<String, Object>> data = new HashMap<>();
//        data.put(compSymbol, temp);
//        collectionReference
//                .document("email")
//                .set(data, SetOptions.merge());
    }

}
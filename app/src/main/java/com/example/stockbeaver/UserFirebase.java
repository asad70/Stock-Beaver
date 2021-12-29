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
        void getUserInterface(ArrayList<String> watchlistArrayList);
    }


    public UserFirebase() {
    }


    public FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    //Switch over to Events collection once completed
//    final CollectionReference collectionReference = rootRef.collection("Events");
    private final String email = MainPage.email;
    Source source = Source.SERVER;


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
                        userInterface.getUserInterface(watchList);

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
        final CollectionReference collectionReference = rootRef.collection("users");
        String compSymbols = trade.getCompSymbol();
        //get the reason
        Integer positionSizes = trade.getPositionSize();
        //get the start date
        String compNames = trade.getCompName();
        //get privacy status
        String privacy = trade.getPrivacy();

        HashMap<String, Object> temp = new HashMap<>();
        temp.put("compSymbol", compSymbols);
        temp.put("positionSize", positionSizes);
        temp.put("compName", compNames);
        temp.put("status", privacy);

        HashMap<String, HashMap<String, Object>> data = new HashMap<>();
        data.put(compSymbol, temp);
        collectionReference
                .document(email)
                .set(data, SetOptions.merge());
    }

}
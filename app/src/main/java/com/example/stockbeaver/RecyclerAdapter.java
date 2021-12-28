package com.example.stockbeaver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RecyclerAdaptor deals with progressbar as well as the editevent details
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email = MainPage.email;
    Source source = Source.SERVER;

    private ArrayList<String> habitNames;
    private RecyclerViewClickListener listener;

    /**
     * initializes habitNames and listener
     * @param habitNames
     * @param listener
     */
    public  RecyclerAdapter(ArrayList<String> habitNames, RecyclerViewClickListener listener){
        this.habitNames = habitNames;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView ticker;
        private TextView name;
        private TextView price;
        private TextView perc;

        /**
         *
         * @param view
         */
        public MyViewHolder(final View view){
            super(view);
            ticker = view.findViewById(R.id.stockTicker_watchlist);
            name = view.findViewById(R.id.stockName_watchlist);
            price = view.findViewById(R.id.price_watchlist);
            perc = view.findViewById(R.id.dailyPerc_watchlist);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_content, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
}
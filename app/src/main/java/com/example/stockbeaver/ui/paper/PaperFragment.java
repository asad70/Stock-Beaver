package com.example.stockbeaver.ui.paper;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.stockbeaver.AdvancedChart;
import com.example.stockbeaver.EconomicCalendar;
import com.example.stockbeaver.MarketMovers;
import com.example.stockbeaver.MarketOverview;
import com.example.stockbeaver.R;
import com.example.stockbeaver.SearchCompDialog;
import com.example.stockbeaver.StockScreener;
import com.example.stockbeaver.databinding.FragmentPaperBinding;

public class PaperFragment extends Fragment {

    private PaperViewModel paperViewModel;
    private FragmentPaperBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        paperViewModel =
                new ViewModelProvider(this).get(PaperViewModel.class);

        setHasOptionsMenu(true);

        binding = FragmentPaperBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPaper;
        paperViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add_friend).setVisible(false);
        menu.findItem(R.id.addWatchlist).setVisible(false);
        menu.findItem(R.id.add_portfolio).setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.search_company:
            FragmentManager fm = getActivity().getFragmentManager();
            SearchCompDialog dialog = new SearchCompDialog();
            dialog.show(fm, "SearchCompDialog");
            return true;
        case R.id.add_paper_trade:
            //add the function to perform here
            return  true;

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
package com.example.stockbeaver.ui.paper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.stockbeaver.databinding.FragmentPaperBinding;

public class PaperFragment extends Fragment {

    private PaperViewModel paperViewModel;
    private FragmentPaperBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        paperViewModel =
                new ViewModelProvider(this).get(PaperViewModel.class);

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
}
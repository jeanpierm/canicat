package com.jeanpier.canicat.ui.records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.model.Vaccine;
import com.jeanpier.canicat.databinding.FragmentRecordsBinding;

public class RecordsFragment extends Fragment {

    RecordsAdapter recordsAdapter;
    ViewPager2 viewPager;
    FragmentRecordsBinding binding;
    NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = binding.pager;
        navController = Navigation.findNavController(view);
        recordsAdapter = new RecordsAdapter(this);
        viewPager.setAdapter(recordsAdapter);
        String[] titles = getResources().getStringArray(R.array.records);
        new TabLayoutMediator(binding.tabLayaout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        initListeners();
    }

    private void initListeners() {
        String emptyVaccine = new Gson().toJson(new Vaccine());
        binding.vaccineAdd.setOnClickListener(v -> {
            RecordsFragmentDirections.ActionNavRecordsToNavVaccineForm action =
                    RecordsFragmentDirections.actionNavRecordsToNavVaccineForm(emptyVaccine);
            Navigation.findNavController(v).navigate(action);
        });

    }

}
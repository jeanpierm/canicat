package com.jeanpier.canicat.ui.records.vaccine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentVaccineRecordListBinding;
import com.jeanpier.canicat.ui.records.vaccine.adapters.VaccineRecyclerViewAdapter;
import com.jeanpier.canicat.ui.records.vaccine.viewmodels.VaccineViewModel;

import java.util.Collections;

/**
 * A fragment representing a list of Items.
 */
public class VaccineFragment extends Fragment {

    private static final String TAG = VaccineFragment.class.getSimpleName();
    private VaccineViewModel vaccineViewModel;
    private FragmentVaccineRecordListBinding binding;
    private NavController navController;
    private VaccineRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVaccineRecordListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        initViewModels();
        vaccineViewModel.loadVaccines();
        initRecyclerView();
        initListeners();
    }

    private void initRecyclerView() {
        adapter = new VaccineRecyclerViewAdapter(getActivity(), Collections.emptyList());
        binding.list.setAdapter(adapter);
    }

    private void initListeners() {

    }

    private void initViewModels() {
        vaccineViewModel = new ViewModelProvider(requireActivity()).get(VaccineViewModel.class);
        vaccineViewModel.getVaccines().observe(getViewLifecycleOwner(), vaccineRecordList -> {
            adapter.setVaccines(vaccineRecordList);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
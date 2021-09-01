package com.jeanpier.canicat.ui.records.vaccine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.core.FormAction;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.model.VaccineRecord;
import com.jeanpier.canicat.databinding.FragmentPetListBinding;
import com.jeanpier.canicat.databinding.FragmentVaccineRecordBinding;
import com.jeanpier.canicat.databinding.FragmentVaccineRecordListBinding;
import com.jeanpier.canicat.ui.pets.adapters.PetRecyclerViewAdapter;
import com.jeanpier.canicat.ui.pets.fragments.PetFragment;
import com.jeanpier.canicat.ui.pets.fragments.PetFragmentDirections;
import com.jeanpier.canicat.ui.pets.viewmodels.PetViewModel;
import com.jeanpier.canicat.ui.records.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class VaccineRecordFragment extends Fragment {

    private static final String TAG = VaccineRecordFragment.class.getSimpleName();
    private VaccineViewModel vaccineViewModel;
    private FragmentVaccineRecordListBinding binding;
    private NavController navController;
    private VaccineRecordRecyclerViewAdapter adapter;


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
        adapter = new VaccineRecordRecyclerViewAdapter(Collections.emptyList());
        binding.list.setAdapter(adapter);
    }

    private void initListeners() {

    }

    private void initViewModels() {
        vaccineViewModel = new ViewModelProvider(requireActivity()).get(VaccineViewModel.class);
        vaccineViewModel.getVaccines().observe(getViewLifecycleOwner(), vaccineRecordList ->{
            adapter.setVaccines(vaccineRecordList);
        } );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
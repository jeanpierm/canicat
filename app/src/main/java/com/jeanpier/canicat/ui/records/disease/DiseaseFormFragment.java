package com.jeanpier.canicat.ui.records.disease;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentDiseaseFormBinding;

public class DiseaseFormFragment extends Fragment {

    private DiseaseFormViewModel diseaseFormViewModel;
    private FragmentDiseaseFormBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDiseaseFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diseaseFormViewModel = new ViewModelProvider(this).get(DiseaseFormViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        initListeners();
        initObservers();
    }

    private void initObservers() {
    }

    private void initListeners() {
        binding.buttonExample.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_disease_form_to_nav_records);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.jeanpier.canicat.ui.pets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.jeanpier.canicat.databinding.FragmentPetDetailsBinding;

public class PetDetailsFragment extends Fragment {

    private PetDetailsViewModel mViewModel;
    private FragmentPetDetailsBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPetDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PetDetailsViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        initListeners();
        initObservers();
    }

    private void initListeners() {
        binding.buttonExample.setOnClickListener(v -> {
            NavDirections action = PetDetailsFragmentDirections.actionNavPetDetailsToNavPetForm();
            navController.navigate(action);
        });
        binding.buttonExample2.setOnClickListener(v -> {
            NavDirections action = PetDetailsFragmentDirections.actionNavPetDetailsToNavRecords();
            navController.navigate(action);
        });
    }

    private void initObservers() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
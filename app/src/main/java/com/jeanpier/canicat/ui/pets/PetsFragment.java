package com.jeanpier.canicat.ui.pets;

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

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentPetsBinding;

public class PetsFragment extends Fragment {

    private PetsViewModel petsViewModel;
    private FragmentPetsBinding binding;
    private NavController navController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPetsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        petsViewModel = new ViewModelProvider(this).get(PetsViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();

    }

    private void initUI() {
        initListeners();
        initObservers();
    }

    private void initListeners() {
        binding.buttonExample.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_pets_to_nav_pet_details);
        });
        binding.buttonExample2.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_pets_to_nav_pet_form);
        });
    }

    private void initObservers() {
        petsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.textPets.setText(s);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
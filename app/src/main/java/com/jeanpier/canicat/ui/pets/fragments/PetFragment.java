package com.jeanpier.canicat.ui.pets.fragments;

import android.annotation.SuppressLint;
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

import com.google.gson.Gson;
import com.jeanpier.canicat.core.FormAction;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.databinding.FragmentPetListBinding;
import com.jeanpier.canicat.ui.pets.adapters.PetRecyclerViewAdapter;
import com.jeanpier.canicat.ui.pets.viewmodels.PetViewModel;

import java.util.ArrayList;
import java.util.List;

public class PetFragment extends Fragment {
    private static final String TAG = PetFragment.class.getSimpleName();

    private PetViewModel petViewModel;
    private FragmentPetListBinding binding;
    private NavController navController;
    private final List<Pet> pets = new ArrayList<>();
    private PetRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPetListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        // actualiza las mascotas cada vez que se muestra el fragmento
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        petViewModel.setUID("8677212f-7e97-458a-b89e-3c05fbfc0efd");
        petViewModel.loadPets();
        adapter = new PetRecyclerViewAdapter(pets);
        binding.recyclerPets.setAdapter(adapter);
        initListeners();
        initObservers();
    }

    private void initListeners() {
        Gson gson = new Gson();
        binding.fabAddPet.setOnClickListener(v -> {
            PetFragmentDirections.ActionNavPetsToNavPetForm action =
                    PetFragmentDirections.actionNavPetsToNavPetForm(gson.toJson(new Pet()), FormAction.CREATE);
            Navigation.findNavController(v).navigate(action);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initObservers() {
        petViewModel.getPets().observe(getViewLifecycleOwner(), petList -> {
            pets.clear();
            pets.addAll(petList);
            adapter.notifyDataSetChanged();
        });
        petViewModel.isLoading().observe(getViewLifecycleOwner(), integer -> {
            binding.progressIndicator.setVisibility(integer);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
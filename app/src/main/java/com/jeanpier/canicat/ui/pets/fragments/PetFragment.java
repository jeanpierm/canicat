package com.jeanpier.canicat.ui.pets.fragments;

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

import java.util.Collections;

public class PetFragment extends Fragment {

    private static final String TAG = PetFragment.class.getSimpleName();
    private PetViewModel petViewModel;
    private FragmentPetListBinding binding;
    private PetRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPetListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
    }

    private void initUI() {
        initViewModels();
//        vuelve a cargar las mascotas cada vez que se muestra el fragmento,
//        para así actualizar la lista después de añadir/editar/eliminar
        petViewModel.loadPets();
        initRecyclerView();
        initListeners();
    }

    private void initRecyclerView() {
        adapter = new PetRecyclerViewAdapter(Collections.emptyList());
        binding.recyclerPets.setAdapter(adapter);
    }

    private void initListeners() {
        Gson gson = new Gson();
        binding.fabAddPet.setOnClickListener(v -> {
            PetFragmentDirections.ActionNavPetsToNavPetForm action =
                    PetFragmentDirections.actionNavPetsToNavPetForm(gson.toJson(new Pet()), FormAction.CREATE);
            Navigation.findNavController(v).navigate(action);
        });
    }

    private void initViewModels() {
        petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        petViewModel.getPets().observe(getViewLifecycleOwner(), petList -> {
            adapter.setPets(petList);
        });
        petViewModel.isLoading().observe(getViewLifecycleOwner(), integer -> {
            binding.progressBar.setVisibility(integer);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
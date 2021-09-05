package com.jeanpier.canicat.ui.pets.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.jeanpier.canicat.R;
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
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentPetListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);

        MenuItem menuLogout = menu.findItem(R.id.menu_logout);
//        vuelve a login si se da click al icono de logout
        menuLogout.setOnMenuItemClickListener(item -> {
            navController.navigate(R.id.action_nav_pets_to_nav_login);
            petViewModel.clearUID();
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
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
        String emptyPet = new Gson().toJson(new Pet());
        binding.fabAddPet.setOnClickListener(v -> {
            PetFragmentDirections.ActionNavPetsToNavPetForm action =
                    PetFragmentDirections.actionNavPetsToNavPetForm(emptyPet);
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
package com.jeanpier.canicat.ui.pets.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.jeanpier.canicat.util.ToastUtil;

import java.util.Collections;

public class PetFragment extends Fragment {

    private static final String TAG = PetFragment.class.getSimpleName();
    private PetViewModel petViewModel;
    private FragmentPetListBinding binding;
    private PetRecyclerViewAdapter adapter;
    private NavController navController;
    private SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        menuLogout.setOnMenuItemClickListener(item -> {
            petViewModel.clearUID();
            clearLoginPreferences();
            ToastUtil.show(requireContext(), getString(R.string.logout_successful));
            return false;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initUI() {
        preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        initViewModels();
        initRecyclerView();
        initListeners();
        setNameOnNavDrawerHeader();
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
        // navegación condicional
        petViewModel.getUID().observe(getViewLifecycleOwner(), uid -> {
            if (uid == null) navigateToLogin();
        });
        petViewModel.getPets().observe(getViewLifecycleOwner(), petList -> {
            adapter.setPets(petList);
        });
        petViewModel.isLoading().observe(getViewLifecycleOwner(), integer -> {
            binding.progressBar.setVisibility(integer);
        });
    }

    private void clearLoginPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(getString(R.string.uid_key));
        editor.apply();
    }

    private void setNameOnNavDrawerHeader() {
        // TODO: Obtener el TextView correctamente
        String displayname = preferences.getString(
                getString(R.string.displayname_key),
                getString(R.string.app_name)
        );
        final TextView textDisplayname = requireActivity().findViewById(R.id.text_nav_title);
        if (textDisplayname != null) {
            textDisplayname.setText(displayname);
        }
    }

    private void navigateToLogin() {
        navController.navigate(R.id.action_nav_pets_to_nav_login);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        petViewModel.clearPets();
        binding = null;
    }
}

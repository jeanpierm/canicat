package com.jeanpier.canicat.ui.pets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.DataApi;
import com.jeanpier.canicat.databinding.FragmentPetListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        navController = Navigation.findNavController(view);
        initUI();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initUI() {
        adapter = new PetRecyclerViewAdapter(pets);
        binding.recyclerPets.setAdapter(adapter);
        binding.recyclerPets.addItemDecoration(
                new DividerItemDecoration(binding.recyclerPets.getContext(), DividerItemDecoration.VERTICAL)
        );
        initListeners();
        initObservers();
    }

    private void initListeners() {
    }

    private void initObservers() {
        petViewModel.getPets().observe(getViewLifecycleOwner(), petList -> {
            pets.clear();
            pets.addAll(petList);
            adapter.notifyDataSetChanged();
        });
        petViewModel.getIsLoading().observe(getViewLifecycleOwner(), integer -> {
            binding.progressIndicator.setVisibility(integer);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
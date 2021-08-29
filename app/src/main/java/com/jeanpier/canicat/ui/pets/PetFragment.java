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
    private DataApi dataApi;
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
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        navController = Navigation.findNavController(view);
        dataApi = RetrofitHelper.getHttpClient().create(DataApi.class);
        loadPets();
        initUI();
    }

    private void loadPets() {
        binding.progressIndicator.setVisibility(View.VISIBLE);
        Call<List<Pet>> call = dataApi.getPetsByUserId("21e0a26e-01c1-409d-9b19-f6393019aa9b");
        call.enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(@NonNull Call<List<Pet>> call, @NonNull Response<List<Pet>> response) {
                binding.progressIndicator.setVisibility(View.GONE);

                pets.clear();
                List<Pet> userPets = response.body() != null ? response.body() : Collections.emptyList();
                pets.addAll(userPets);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Mascotas cargadas con éxito", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<Pet>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al cargar mascotas", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
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
        petViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
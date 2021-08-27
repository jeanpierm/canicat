package com.jeanpier.canicat.ui.pets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentPetFormBinding;

public class PetFormFragment extends Fragment {

    public final static int CREATE_ACTION = 0;
    public final static int EDIT_ACTION = 1;

    private PetFormViewModel petFormViewModel;
    private FragmentPetFormBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
//        if (getActivity() != null) {
//            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setDisplayHomeAsUpEnabled(true);
//            }
//        }
        binding = FragmentPetFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        petFormViewModel = new ViewModelProvider(this).get(PetFormViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);
        menu.findItem(R.id.menu_save).setVisible(false);
        if (0 == CREATE_ACTION) {
            menu.findItem(R.id.menu_save).setVisible(true);
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_delete).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initUI() {
        initListeners();
        initObservers();
    }

    private void initObservers() {
    }

    private void initListeners() {
        binding.buttonExample.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_pet_form_to_nav_pets);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
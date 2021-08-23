package com.jeanpier.canicat.ui.auth;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        ((MainActivity) getActivity()).hideFloatingActionButton();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//        ((MainActivity) getActivity()).showFloatingActionButton();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        initListeners();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        // aplica estilo y texto al mensaje para ir a la pantalla registrar
        TextAppearanceSpan style = new TextAppearanceSpan(getContext(), R.style.BoldClickeable);
        builder.append(getString(R.string.text_already_have_account))
                .append(" ")
                .append(getString(R.string.text_login), style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textGoLogin.setText(builder, TextView.BufferType.SPANNABLE);
    }

    private void initListeners() {
        binding.buttonLoginExample.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_login_to_nav_pets);
        });
        binding.textGoLogin.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_register_to_nav_login);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
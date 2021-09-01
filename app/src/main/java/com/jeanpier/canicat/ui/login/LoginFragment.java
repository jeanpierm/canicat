package com.jeanpier.canicat.ui.login;

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
import com.jeanpier.canicat.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        initListeners();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        // aplica estilo y texto al mensaje para ir a la pantalla registrar
        TextAppearanceSpan style = new TextAppearanceSpan(getContext(), R.style.BoldClickeable);
        builder.append(getString(R.string.text_dont_have_account))
                .append(" ")
                .append(getString(R.string.text_register), style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textGoRegister.setText(builder, TextView.BufferType.SPANNABLE);
    }

    private void initListeners() {
        binding.buttonLogin.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_login_to_nav_pets);
        });
        binding.textGoRegister.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_login_to_nav_register);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
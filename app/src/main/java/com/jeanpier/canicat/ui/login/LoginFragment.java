package com.jeanpier.canicat.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.core.FieldValidators;
import com.jeanpier.canicat.data.network.AuthService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.data.network.responses.LoginResponse;
import com.jeanpier.canicat.databinding.FragmentLoginBinding;
import com.jeanpier.canicat.ui.pets.viewmodels.PetViewModel;
import com.jeanpier.canicat.util.AlertUtil;
import com.jeanpier.canicat.util.TextFieldUtil;
import com.jeanpier.canicat.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private String email, password;
    private Button buttonLogin;
    private PetViewModel petViewModel;
    private FragmentLoginBinding binding;
    private NavController navController;
    private TextInputEditText editEmail, editPassword;
    private TextInputLayout layoutEmail, layoutPassword;
    private TextView textGoRegister;
    private ProgressBar progressBar;
    private final AuthService authService = new AuthService();
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            email = savedInstanceState.getString(KEY_EMAIL, "");
            password = savedInstanceState.getString(KEY_PASSWORD, "");
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_EMAIL, TextFieldUtil.getString(editEmail));
        outState.putString(KEY_PASSWORD, TextFieldUtil.getString(editPassword));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(
                ((AppCompatActivity) requireActivity()).getSupportActionBar()
        ).hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(
                ((AppCompatActivity) requireActivity()).getSupportActionBar()
        ).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initUI() {
        initViewModels();
        bindViews();
        buildGoToRegisterTextSpan();
        initListeners();
        progressBar.setVisibility(View.GONE);
    }

    private void bindViews() {
        progressBar = binding.progressBar;
        editEmail = binding.editEmail;
        editPassword = binding.editPassword;
        layoutEmail = binding.layoutEmail;
        layoutPassword = binding.layoutPassword;
        textGoRegister = binding.textGoRegister;
        buttonLogin = binding.buttonLogin;

        // saved state
        editEmail.setText(email);
        editPassword.setText(password);
    }

    private void initViewModels() {
        petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
    }

    private void initListeners() {
//        listeners para validar los inputs
        editEmail.addTextChangedListener(new TextFieldValidator(editEmail));
        editPassword.addTextChangedListener(new TextFieldValidator(editPassword));

        buttonLogin.setOnClickListener(v -> {
            if (!isFormValid()) {
                AlertUtil.showErrorAlert(getString(R.string.alert_complete_fields),
                        requireContext());
                return;
            }
            login();
        });

        textGoRegister.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_login_to_nav_register));
    }

    private void navigateToPets() {
//        asegura que el id del usuario esté en el estado
        if (petViewModel.getUID().getValue() == null) {
            AlertUtil.showErrorAlert(getString(R.string.login_error), requireContext());
            return;
        }
        navController.navigate(R.id.action_nav_login_to_nav_pets);
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        String email = TextFieldUtil.getString(editEmail);
        String password = TextFieldUtil.getString(editPassword);
        authService.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() == null || response.body().getUid() == null) {
                        AlertUtil.showGenericErrorAlert(requireContext());
                        return;
                    }
                    String uid = response.body().getUid();
                    petViewModel.setUID(uid);
                    ToastUtil.show(requireContext(), getString(R.string.login_successfull));
                    navigateToPets();
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(getString(R.string.login_error), requireContext());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), requireContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showGenericErrorAlert(requireContext());
                t.printStackTrace();
            }
        });
    }

    private boolean isFormValid() {
        return isValidEmail() && isValidPassword();
    }

    private void buildGoToRegisterTextSpan() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        // aplica estilo y texto al mensaje para ir a la pantalla registrar
        TextAppearanceSpan style = new TextAppearanceSpan(getContext(), R.style.BoldClickeable);
        builder.append(getString(R.string.text_dont_have_account))
                .append(" ")
                .append(getString(R.string.text_register), style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textGoRegister.setText(builder, TextView.BufferType.SPANNABLE);
    }

//    VALIDACIONES

    private boolean isValidEmail() {
        if (FieldValidators.isTextFieldEmpty(editEmail)) {
            layoutEmail.setError(getString(R.string.required_field));
            editEmail.requestFocus();
            return false;
        }
        if (!FieldValidators.isValidEmail(editEmail)) {
            layoutEmail.setError(getString(R.string.invalid_email));
            editEmail.requestFocus();
            return false;
        }
        layoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean isValidPassword() {
        if (FieldValidators.isTextFieldEmpty(editPassword)) {
            layoutPassword.setError(getString(R.string.required_field));
            editPassword.requestFocus();
            return false;
        }
        layoutPassword.setErrorEnabled(false);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class TextFieldValidator implements TextWatcher {

        private final View view;

        public TextFieldValidator(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.equals(editEmail)) isValidEmail();
            if (view.equals(editPassword)) isValidPassword();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
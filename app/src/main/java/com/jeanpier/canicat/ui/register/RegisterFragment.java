package com.jeanpier.canicat.ui.register;

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
import com.jeanpier.canicat.data.model.User;
import com.jeanpier.canicat.data.network.UserService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.data.network.responses.PostUserResponse;
import com.jeanpier.canicat.databinding.FragmentRegisterBinding;
import com.jeanpier.canicat.ui.pets.viewmodels.PetViewModel;
import com.jeanpier.canicat.util.AlertUtil;
import com.jeanpier.canicat.util.TextFieldUtil;
import com.jeanpier.canicat.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_DNI = "dni";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PASSWORD2 = "password2";
    private String firstname, lastname, dni, email, password, password2;
    private PetViewModel petViewModel;
    private FragmentRegisterBinding binding;
    private NavController navController;
    private Button buttonLogin;
    private TextInputEditText editFirstname, editLastname, editDni, editEmail,
            editPassword, editPassword2;
    private TextInputLayout layoutFirstname, layoutLastname, layoutDni, layoutEmail,
            layoutPassword, layoutPassword2;
    private TextView textGoLogin;
    private ProgressBar progressBar;
    private final UserService userService = new UserService();
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            firstname = savedInstanceState.getString(KEY_FIRSTNAME, "");
            lastname = savedInstanceState.getString(KEY_LASTNAME, "");
            dni = savedInstanceState.getString(KEY_DNI, "");
            email = savedInstanceState.getString(KEY_EMAIL, "");
            password = savedInstanceState.getString(KEY_PASSWORD, "");
            password2 = savedInstanceState.getString(KEY_PASSWORD2, "");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(KEY_FIRSTNAME, TextFieldUtil.getString(editFirstname));
        outState.putString(KEY_LASTNAME, TextFieldUtil.getString(editLastname));
        outState.putString(KEY_DNI, TextFieldUtil.getString(editDni));
        outState.putString(KEY_EMAIL, TextFieldUtil.getString(editEmail));
        outState.putString(KEY_PASSWORD, TextFieldUtil.getString(editPassword));
        outState.putString(KEY_PASSWORD2, TextFieldUtil.getString(editPassword2));
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
        buildGoToLoginTextSpan();
        initListeners();
        progressBar.setVisibility(View.GONE);
    }

    private void bindViews() {
        progressBar = binding.progressBar;
        editFirstname = binding.editFirstname;
        editLastname = binding.editLastname;
        editDni = binding.editDni;
        editEmail = binding.editEmail;
        editPassword = binding.editPassword;
        editPassword2 = binding.editPassword2;
        textGoLogin = binding.textGoLogin;
        layoutFirstname = binding.layoutFirstname;
        layoutLastname = binding.layoutLastname;
        layoutDni = binding.layoutDni;
        layoutEmail = binding.layoutEmail;
        layoutPassword = binding.layoutPassword;
        layoutPassword2 = binding.layoutPassword2;
        buttonLogin = binding.buttonLogin;

        // saved state
        editFirstname.setText(firstname);
        editLastname.setText(lastname);
        editDni.setText(dni);
        editEmail.setText(email);
        editPassword.setText(password);
        editPassword2.setText(password2);
    }

    private void initViewModels() {
        petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
    }

    private void initListeners() {
//        listeners para validar los inputs
        editFirstname.addTextChangedListener(new TextFieldValidator(editFirstname));
        editLastname.addTextChangedListener(new TextFieldValidator(editLastname));
        editEmail.addTextChangedListener(new TextFieldValidator(editEmail));
        editDni.addTextChangedListener(new TextFieldValidator(editDni));
        editPassword.addTextChangedListener(new TextFieldValidator(editPassword));
        editPassword2.addTextChangedListener(new TextFieldValidator(editPassword2));

        buttonLogin.setOnClickListener(v -> {
            if (!isFormValid()) {
                AlertUtil.showErrorAlert(getString(R.string.alert_complete_fields),
                        requireContext());
                return;
            }
            postUser();
        });

        binding.textGoLogin.setOnClickListener(v ->
                navController.navigate(R.id.action_nav_register_to_nav_login));
    }

    private void navigateToPets() {
//        asegura que el id del usuario esté en el estado
        if (petViewModel.getUID().getValue() == null) {
            AlertUtil.showErrorAlert(getString(R.string.login_error), requireContext());
            return;
        }
        navController.navigate(R.id.action_nav_register_to_nav_pets);
    }

    private void postUser() {
        buttonLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        User newUser = buildUserFromForm();
        userService.create(newUser).enqueue(new Callback<PostUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostUserResponse> call, @NonNull Response<PostUserResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() == null || response.body().getId() == null) {
                        AlertUtil.showGenericErrorAlert(requireContext());
                        return;
                    }
                    String uid = response.body().getId();
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
            public void onFailure(@NonNull Call<PostUserResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showGenericErrorAlert(requireContext());
                t.printStackTrace();
            }
        });
    }

    private User buildUserFromForm() {
        String dni = TextFieldUtil.getString(editDni);
        String firstname = TextFieldUtil.getString(editFirstname);
        String lastname = TextFieldUtil.getString(editLastname);
        String email = TextFieldUtil.getString(editEmail);
        String password = TextFieldUtil.getString(editPassword);
        return new User(dni, firstname, lastname, email, password);
    }

    private boolean isFormValid() {
        return isFirstnameValid() && isLastnameValid() && isValidDni() && isValidEmail()
                && isValidPassword() && isValidConfirmPassword();
    }

    private void buildGoToLoginTextSpan() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        // aplica estilo y texto al mensaje para ir a la pantalla registrar
        TextAppearanceSpan style = new TextAppearanceSpan(getContext(), R.style.BoldClickeable);
        builder.append(getString(R.string.text_already_have_account))
                .append(" ")
                .append(getString(R.string.text_login), style, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textGoLogin.setText(builder, TextView.BufferType.SPANNABLE);
    }

//    VALIDACIONES

    private boolean isFirstnameValid() {
        if (FieldValidators.isTextFieldEmpty(editFirstname)) {
            layoutFirstname.setError(getString(R.string.required_field));
            editFirstname.requestFocus();
            return false;
        }
        layoutFirstname.setErrorEnabled(false);
        return true;
    }

    private boolean isLastnameValid() {
        if (FieldValidators.isTextFieldEmpty(editLastname)) {
            layoutLastname.setError(getString(R.string.required_field));
            editLastname.requestFocus();
            return false;
        }
        layoutLastname.setErrorEnabled(false);
        return true;
    }

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

    private boolean isValidDni() {
        if (FieldValidators.isTextFieldEmpty(editDni)) {
            layoutDni.setError(getString(R.string.required_field));
            editDni.requestFocus();
            return false;
        }
        if (!FieldValidators.isStringFullNumbers(editDni)) {
            layoutDni.setError(getString(R.string.only_numbers));
            editDni.requestFocus();
            return false;
        }
        if (!FieldValidators.isLengthExactly(editDni, FieldValidators.DNI_LENGTH)) {
            layoutDni.setError(getString(R.string.dni_must_be_valid));
            editDni.requestFocus();
            return false;
        }
        layoutDni.setErrorEnabled(false);
        return true;
    }

    private boolean isValidPassword() {
        if (FieldValidators.isTextFieldEmpty(editPassword)) {
            layoutPassword.setError(getString(R.string.required_field));
            editPassword.requestFocus();
            return false;
        }
        if (!FieldValidators.isLengthMoreThan(editPassword, FieldValidators.PASSWORD_MIN_LENGTH - 1)) {
            layoutPassword.setError(getString(R.string.password_min_length));
            editPassword.requestFocus();
            return false;
        }
        layoutPassword.setErrorEnabled(false);
        return true;
    }

    private boolean isValidConfirmPassword() {
        if (FieldValidators.isTextFieldEmpty(editPassword)) {
            layoutPassword2.setError(getString(R.string.required_field));
            editPassword2.requestFocus();
            return false;
        }
        if (!TextFieldUtil.getString(editPassword2).equals(TextFieldUtil.getString(editPassword))) {
            layoutPassword2.setError(getString(R.string.password_must_be_equals));
            layoutPassword.setError(getString(R.string.password_must_be_equals));
            editPassword2.requestFocus();
            return false;
        }
        layoutPassword2.setErrorEnabled(false);
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
            if (view.equals(editFirstname)) isFirstnameValid();
            if (view.equals(editLastname)) isLastnameValid();
            if (view.equals(editEmail)) isValidEmail();
            if (view.equals(editDni)) isValidDni();
            if (view.equals(editPassword)) isValidPassword();
            if (view.equals(editPassword2)) isValidConfirmPassword();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
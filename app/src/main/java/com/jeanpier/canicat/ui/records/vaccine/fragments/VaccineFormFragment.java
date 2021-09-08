package com.jeanpier.canicat.ui.records.vaccine.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.Vaccine;
import com.jeanpier.canicat.data.network.VaccineService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.databinding.FragmentVaccineFormBinding;
import com.jeanpier.canicat.ui.records.vaccine.viewmodels.VaccineViewModel;
import com.jeanpier.canicat.util.AlertUtil;
import com.jeanpier.canicat.util.TextFieldUtil;
import com.jeanpier.canicat.util.ToastUtil;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineFormFragment extends Fragment {

    private VaccineViewModel vaccineViewModel;
    private FragmentVaccineFormBinding binding;
    private String petId;
    private Vaccine vaccine;
    private NavController navController;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;
    private String lastDate;
    private String nextDate;
    private TextInputEditText editName, editType, editDescription;
    private static final String TAG = "VaccineFormFragment";
    private final VaccineService vaccineService = new VaccineService();
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                siempre ir al listado de vacunas al presionar "atrás"
                navController.navigate(R.id.action_nav_vaccine_form_to_nav_records);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVaccineFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initUI();
    }

    private void initViewModels() {
        vaccineViewModel = new ViewModelProvider(requireActivity()).get(VaccineViewModel.class);
        vaccineViewModel.getPetId().observe(getViewLifecycleOwner(), currentPetId -> petId = currentPetId);
    }

    private void initUI() {
        bindViews();
        binding.progressBar.setVisibility(View.GONE);
        setVaccineFromArgs();
        if (isEditing()) {
            fillFormVaccine();
        }
        setActionBarTitle();
        setButtonTitle();
        initListeners();
        initViewModels();
    }

    private void setVaccineFromArgs() {
        String vaccineJson = VaccineFormFragmentArgs.fromBundle(getArguments()).getVaccine();
        vaccine = new Gson().fromJson(vaccineJson, Vaccine.class);
    }

    private void bindViews() {
        editName = binding.vaccineName;
        editType = binding.vaccineType;
        editDescription = binding.vaccineDescription;
    }

    private boolean isEditing() {
        return vaccine.getId() != null;
    }

    private void setActionBarTitle() {
        String title = isEditing() ? getString(R.string.title_edit_vaccine) : getString(R.string.title_add_vaccine);
        Objects.requireNonNull(
                ((AppCompatActivity) requireActivity()).getSupportActionBar()
        ).setTitle(title);
    }

    private void setButtonTitle() {
        String title = isEditing() ? getString(R.string.title_menu_edit) : getString(R.string.title_menu_save);
        binding.guardar.setText(title);
    }

    private void postVaccine() {
        binding.guardar.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        Vaccine vaccine = setValueEntityVaccine();

        vaccineService.saveVaccineRecord(vaccine).enqueue(new Callback<Vaccine>() {
            @Override
            public void onResponse(@NonNull Call<Vaccine> call, @NonNull Response<Vaccine> response) {
                binding.guardar.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    clearVaccineForm();
                    vaccineViewModel.loadVaccines();
                    ToastUtil.show(getContext(), getString(R.string.save_vaccine_success));
//                    vuelve a la pantalla de vacunas
                    navController.navigate(R.id.action_nav_vaccine_form_to_nav_records);
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(getString(R.string.save_vaccine_error), requireContext());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), requireContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Vaccine> call, @NonNull Throwable t) {
                binding.guardar.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                ToastUtil.show(requireContext(), getString(R.string.save_vaccine_fail));
                Log.d(TAG, "onResponse: error" + t.getMessage());
            }
        });
    }

    private void updateVaccine() {
        binding.guardar.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        Vaccine vaccineupdate = setValueEntityVaccine();
        Call<Void> call = vaccineService.updateVaccine(vaccine.getId(), vaccineupdate);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                binding.guardar.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    vaccineViewModel.loadVaccines();
                    ToastUtil.show(requireContext(), getString(R.string.update_vaccine));
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(getString(R.string.update_error_vaccine), requireContext());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), requireContext());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                binding.guardar.setEnabled(true);
                binding.progressBar.setVisibility(View.GONE);
                AlertUtil.showGenericErrorAlert(requireContext());
                t.printStackTrace();
            }
        });

    }

    private void initListeners() {
        binding.guardar.setOnClickListener(v -> {
            if (!isValidForm()) {
                AlertUtil.showErrorAlert(getString(R.string.alert_complete_fields),
                        requireContext());
                return;
            }
            if (isEditing()) {
                updateVaccine();
            } else {
                postVaccine();
            }
        });

        binding.datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Dialog,
                        onDateSetListener,
                        year,
                        month,
                        day);
                dialog.show();

            }
        });

        binding.dateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_WEEK);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_DeviceDefault_Dialog,
                        onDateSetListener2,
                        year,
                        month,
                        day
                );
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                String format = "yyyy-MM-dd";
                lastDate = new SimpleDateFormat(format).format(cal.getTime());
                binding.txtLastDate.setText(lastDate);
            }
        };

        onDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                String format = "yyyy-MM-dd";
                nextDate = new SimpleDateFormat(format).format(cal.getTime());
                binding.txtNextDate.setText(nextDate);
            }
        };
    }

    private void fillFormVaccine() {
        editName.setText(vaccine.getName());
        editType.setText(vaccine.getType());
        editDescription.setText(vaccine.getDescription());
        lastDate = vaccine.getLastVaccineDate();
        nextDate = vaccine.getNextVaccineDate();
        binding.txtLastDate.setText(lastDate);
        binding.txtNextDate.setText(nextDate);
    }

    private Vaccine setValueEntityVaccine() {
        String name = TextFieldUtil.getString(editName);
        String type = TextFieldUtil.getString(editType);
        String description = TextFieldUtil.getString(editDescription);
        return new Vaccine(name, type, lastDate, nextDate, description, petId);
    }

    private void clearVaccineForm() {
        editName.setText("");
        editType.setText("");
        editDescription.setText("");
        binding.txtLastDate.setText("");
        binding.txtNextDate.setText("");
        lastDate = "";
        nextDate = "";
    }

    public boolean isValidForm() {
        if (editType.getText().toString().isEmpty()) {
            return false;
        }

        if (editType.getText().toString().isEmpty()) {
            return false;
        }

        if (editDescription.getText().toString().isEmpty()) {
            return false;
        }

        if (lastDate == null || nextDate == null || lastDate.isEmpty() || nextDate.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.Vaccine;
import com.jeanpier.canicat.data.network.VaccineService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.databinding.FragmentVaccineFormBinding;
import com.jeanpier.canicat.ui.records.vaccine.viewmodels.VaccineViewModel;
import com.jeanpier.canicat.util.AlertUtil;
import com.jeanpier.canicat.util.ToastUtil;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineFormFragment extends Fragment {

    private VaccineViewModel vaccineViewModel;
    private FragmentVaccineFormBinding binding;
    private String petId;
    private NavController navController;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;
    private String lastDate;
    private String nextDate;
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
        binding.progressBar.setVisibility(View.GONE);
        initListeners();
        initViewModels();
    }

    private void postVaccine() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Vaccine vaccine = new Vaccine(
                binding.vaccineName.getText().toString(),
                binding.vaccineType.getText().toString(),
                lastDate,
                nextDate,
                binding.vaccineDescription.getText().toString(),
                petId
        );

        vaccineService.saveVaccineRecord(vaccine).enqueue(new Callback<Vaccine>() {
            @Override
            public void onResponse(@NonNull Call<Vaccine> call, @NonNull Response<Vaccine> response) {
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
                binding.progressBar.setVisibility(View.GONE);
                ToastUtil.show(requireContext(), getString(R.string.save_vaccine_fail));
                Log.d(TAG, "onResponse: error" + t.getMessage());
            }
        });
    }

    private void clearVaccineForm() {
        binding.vaccineName.setText("");
        binding.vaccineType.setText("");
        binding.vaccineDescription.setText("");
        binding.textDate.setText("");
        binding.textDateNext.setText("");
        lastDate = "";
        nextDate = "";
    }

    private void initListeners() {
        binding.guardar.setOnClickListener(v -> {
            if (!isValidForm()) {
                AlertUtil.showErrorAlert(getString(R.string.alert_complete_fields),
                        requireContext());
                return;
            }
            postVaccine();
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
                String hoy = DateFormat.getDateInstance().format(cal.getTime());
                binding.textDate.setText(hoy);
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
                String hoy = DateFormat.getDateInstance().format(cal.getTime());
                binding.textDateNext.setText(hoy);
            }
        };
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean isValidForm() {
        if (binding.vaccineName.getText().toString().isEmpty()) {
            return false;
        }

        if (binding.vaccineType.getText().toString().isEmpty()) {
            return false;
        }

        if (binding.vaccineDescription.getText().toString().isEmpty()) {
            return false;
        }

        if (lastDate == null || nextDate == null || lastDate.isEmpty() || nextDate.isEmpty()) {
            return false;
        }

        return true;
    }
}
package com.jeanpier.canicat.ui.records.vaccine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.jeanpier.canicat.MainActivity;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.databinding.FragmentVaccineFormBinding;

import java.text.DateFormat;
import java.util.Calendar;

public class VaccineFormFragment extends Fragment {

    private VaccineFormViewModel vaccineFormViewModel;
    private FragmentVaccineFormBinding binding;
    private NavController navController;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVaccineFormBinding.inflate(inflater, container, false);

        binding.datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal =  Calendar.getInstance();
                int year =  cal.get(Calendar.YEAR);
                int month =  cal.get(Calendar.MONTH);
                int day =  cal.get(Calendar.DAY_OF_WEEK);

                DatePickerDialog dialog =  new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, onDateSetListener, year, month, day);
                dialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal =  Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String hoy =  DateFormat.getDateInstance().format(cal.getTime());
                binding.textDate.setText(hoy);
            }
        };

        binding.dateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal =  Calendar.getInstance();
                int year =  cal.get(Calendar.YEAR);
                int month =  cal.get(Calendar.MONTH);
                int day =  cal.get(Calendar.DAY_OF_WEEK);

                DatePickerDialog dialog =  new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, onDateSetListener2, year, month, day);
                dialog.show();
            }
        });

        onDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal =  Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String hoy =  DateFormat.getDateInstance().format(cal.getTime());
                binding.textDateNext.setText(hoy);
            }
        };

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vaccineFormViewModel = new ViewModelProvider(this).get(VaccineFormViewModel.class);
        navController = Navigation.findNavController(view);
        initUI();
    }


    private void initUI() {
        initListeners();
        initObservers();
    }

    private void initObservers() {
    }

    private void initListeners() {
        binding.buttonExample.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_vaccine_form_to_nav_records);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
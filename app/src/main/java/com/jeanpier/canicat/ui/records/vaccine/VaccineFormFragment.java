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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.jeanpier.canicat.MainActivity;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.VaccineRecord;
import com.jeanpier.canicat.data.network.DataApi;
import com.jeanpier.canicat.data.network.VaccineRecordService;
import com.jeanpier.canicat.databinding.FragmentVaccineFormBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineFormFragment extends Fragment {

    private VaccineFormViewModel vaccineFormViewModel;
    private FragmentVaccineFormBinding binding;
    private NavController navController;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener2;
    private String lastDate;
    private String nextDate;
    private final VaccineRecordService vaccineRecordService = new VaccineRecordService();

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
                lastDate= new SimpleDateFormat("yyyy-mm-dd").format(cal.getTime());
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
                nextDate= new SimpleDateFormat("yyyy-mm-dd").format(cal.getTime());
                String hoy =  DateFormat.getDateInstance().format(cal.getTime());
                binding.textDateNext.setText(hoy);
            }
        };

        return binding.getRoot();
    }

    private void onSubmit(View view) {
        if(isValidForm()){
            VaccineRecord vaccineRecord = new VaccineRecord(
                    "VACCINEEEE",
                    "VACCINEEEEE",
                    "2021-08-01",
                    "2021-08-01",
                    "CDNCD",
                    "533b12fd-2a36-4600-95f0-fc388d9cacc1"
            );
            Call<VaccineRecord> call= vaccineRecordService.saveVaccineRecord(vaccineRecord);
            call.enqueue(new Callback<VaccineRecord>() {
                @Override
                public void onResponse(Call<VaccineRecord> call, Response<VaccineRecord> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(getContext(), "Error al guardar vacuna" + response.code(), Toast.LENGTH_LONG);

                    }
                }

                @Override
                public void onFailure(Call<VaccineRecord> call, Throwable t) {
                    Toast.makeText(getContext(), "Error!" + t.getMessage(), Toast.LENGTH_LONG);
                }
            });
        }else{
            Toast.makeText(getContext(), "FALTAN DATOS", Toast.LENGTH_LONG);
        }
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
        binding.guardar.setOnClickListener(v -> {
            onSubmit(v);
            //navController.navigate(R.id.action_nav_vaccine_form_to_nav_records);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean isValidForm(){

        if(binding.vaccineName.getText().toString().isEmpty()){
            return false;
        }

        if(binding.vaccineType.getText().toString().isEmpty()){
            return false;
        }

        if(binding.vaccineDescription.getText().toString().isEmpty()){
            return false;
        }

        if(lastDate.isEmpty() || nextDate.isEmpty()){
            return false;
        }

        return true;
    }

}
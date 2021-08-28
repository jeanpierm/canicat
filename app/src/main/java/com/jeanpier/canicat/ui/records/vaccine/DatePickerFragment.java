package com.jeanpier.canicat.ui.records.vaccine;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar cal =  Calendar.getInstance();
        int year =  cal.get(Calendar.YEAR);
        int month =  cal.get(Calendar.MONTH);
        int day =  cal.get(Calendar.DAY_OF_WEEK);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), day, month , year);
    }
}

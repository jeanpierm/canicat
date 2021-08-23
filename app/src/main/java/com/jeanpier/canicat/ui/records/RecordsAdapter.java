package com.jeanpier.canicat.ui.records;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jeanpier.canicat.ui.records.disease.DiseaseRecordFragment;
import com.jeanpier.canicat.ui.records.treatment.TreatmentRecordFragment;
import com.jeanpier.canicat.ui.records.vaccine.VaccineRecordFragment;

public class RecordsAdapter extends FragmentStateAdapter {

    public RecordsAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = new VaccineRecordFragment();
                break;
            case 1:
                fragment = new DiseaseRecordFragment();
                break;
            case 2:
                fragment = new TreatmentRecordFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

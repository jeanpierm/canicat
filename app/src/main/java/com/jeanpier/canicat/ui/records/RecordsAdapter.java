package com.jeanpier.canicat.ui.records;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jeanpier.canicat.ui.records.vaccine.fragments.VaccineFragment;

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
                fragment = new VaccineFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}

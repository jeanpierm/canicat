package com.jeanpier.canicat.ui.records.vaccine;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.jeanpier.canicat.databinding.FragmentVaccineRecordBinding;

import java.util.List;

public class VaccineRecordRecyclerViewAdapter extends RecyclerView.Adapter<VaccineRecordRecyclerViewAdapter.ViewHolder> {

    // TODO: Saber como obtener el navController desde un RecyclerView
    private NavController navController;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        navController = Navigation.findNavController(parent);
        return new ViewHolder(FragmentVaccineRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.buttonEdit.setOnClickListener(v -> {
//            navController.navigate(R.id.action_nav_vaccine_form_to_nav_records);
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final Button buttonEdit;

        public ViewHolder(FragmentVaccineRecordBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            buttonEdit = binding.buttonEdit;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
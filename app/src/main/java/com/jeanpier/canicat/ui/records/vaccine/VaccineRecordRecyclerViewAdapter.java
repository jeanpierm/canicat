package com.jeanpier.canicat.ui.records.vaccine;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.jeanpier.canicat.data.model.VaccineRecord;
import com.jeanpier.canicat.databinding.PetItemBinding;
import com.jeanpier.canicat.ui.records.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class VaccineRecordRecyclerViewAdapter extends RecyclerView.Adapter<VaccineRecordRecyclerViewAdapter.ViewHolder> {

    // TODO: Saber como obtener el navController desde un RecyclerView
    private NavController navController;
    private final List<VaccineRecord> vaccines;

    public VaccineRecordRecyclerViewAdapter(List<VaccineRecord> vaccines) {
        this.vaccines = vaccines;
    }

    @NonNull
    @Override
    public VaccineRecordRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineRecordRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final VaccineRecordFragment binding;

        public ViewHolder(VaccineRecordFragment binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
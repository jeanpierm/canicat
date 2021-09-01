package com.jeanpier.canicat.ui.records.vaccine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.core.FormAction;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.model.VaccineRecord;
import com.jeanpier.canicat.data.network.VaccineRecordService;
import com.jeanpier.canicat.databinding.FragmentVaccineFormBinding;
import com.jeanpier.canicat.databinding.FragmentVaccineRecordBinding;
import com.jeanpier.canicat.databinding.FragmentVaccineRecordListBinding;
import com.jeanpier.canicat.databinding.PetItemBinding;
import com.jeanpier.canicat.ui.pets.adapters.PetRecyclerViewAdapter;
import com.jeanpier.canicat.ui.pets.fragments.PetFormFragmentDirections;
import com.jeanpier.canicat.ui.pets.fragments.PetFragmentDirections;
import com.jeanpier.canicat.ui.records.RecordsFragmentDirections;
import com.jeanpier.canicat.ui.records.placeholder.PlaceholderContent.PlaceholderItem;
import com.jeanpier.canicat.util.ToastUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VaccineRecordRecyclerViewAdapter extends RecyclerView.Adapter<VaccineRecordRecyclerViewAdapter.ViewHolder> {

    private NavController navController;
    private static final String TAG = VaccineRecordRecyclerViewAdapter.class.getSimpleName();
    private List<VaccineRecord> vaccines;
    private VaccineRecordService vaccineRecordService;

    public VaccineRecordRecyclerViewAdapter(List<VaccineRecord> vaccines) {
        this.vaccines = vaccines;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVaccines(List<VaccineRecord> vaccines) {
        this.vaccines = vaccines;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentVaccineRecordBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VaccineRecordRecyclerViewAdapter.ViewHolder holder, int position) {
        VaccineRecord currentVaccine = vaccines.get(position);
        holder.binding.vaccinename.setText(currentVaccine.getName());
        holder.binding.vaccineNextdate.setText("Proxima dosis:" + currentVaccine.getNextVaccineDate());


        holder.binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteVaccine(v, currentVaccine.getId());
            }
        });

    }

    private void deleteVaccine(View v, String id) {
        Call<Void> call = vaccineRecordService.deleteVaccine(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ToastUtil.show(v.getContext(), "Vacuna eliminada correctamente");
                } else {
                    ToastUtil.show(v.getContext(), "Error al elimnar vacuna");
                    Log.d(TAG, "onResponse: " + response.errorBody());
                }
                Log.d(TAG, "onResponse: " + response.toString());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                ToastUtil.show(v.getContext(), "Error!!");
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final FragmentVaccineRecordBinding binding;

        public ViewHolder(FragmentVaccineRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
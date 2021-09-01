package com.jeanpier.canicat.ui.records.vaccine.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.data.model.Vaccine;
import com.jeanpier.canicat.data.network.VaccineService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.databinding.FragmentVaccineRecordBinding;
import com.jeanpier.canicat.ui.records.vaccine.viewmodels.VaccineViewModel;
import com.jeanpier.canicat.util.AlertUtil;
import com.jeanpier.canicat.util.ToastUtil;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VaccineRecyclerViewAdapter extends RecyclerView.Adapter<VaccineRecyclerViewAdapter.ViewHolder> {

    private VaccineViewModel vaccineViewModel;
    private static final String TAG = VaccineRecyclerViewAdapter.class.getSimpleName();
    private List<Vaccine> vaccines;
    private FragmentActivity fragmentActivity;
    private final VaccineService vaccineService = new VaccineService();
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();

    public VaccineRecyclerViewAdapter(FragmentActivity fragmentActivity, List<Vaccine> vaccines) {
        this.fragmentActivity = fragmentActivity;
        this.vaccines = vaccines;
        vaccineViewModel = new ViewModelProvider(fragmentActivity).get(VaccineViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVaccines(List<Vaccine> vaccines) {
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
    public void onBindViewHolder(@NonNull VaccineRecyclerViewAdapter.ViewHolder holder, int position) {
        Vaccine currentVaccine = vaccines.get(position);
        holder.binding.vaccinename.setText(currentVaccine.getName());
        holder.binding.vaccineNextdate.setText("Proxima dosis:" + currentVaccine.getNextVaccineDate());
        holder.binding.buttonDelete.setOnClickListener(v -> deleteVaccine(currentVaccine.getId())
        );
    }

    private void deleteVaccine(String id) {
        vaccineService.deleteVaccine(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                vaccineViewModel.loadVaccines();
                if (response.isSuccessful()) {
                    ToastUtil.show(fragmentActivity, fragmentActivity.getString(R.string.vaccine_deleted_successful));
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(
                                fragmentActivity.getString(R.string.vaccine_deleted_error),
                                fragmentActivity);
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), fragmentActivity);
                    Log.d(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                AlertUtil.showGenericErrorAlert(fragmentActivity);
                t.printStackTrace();
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
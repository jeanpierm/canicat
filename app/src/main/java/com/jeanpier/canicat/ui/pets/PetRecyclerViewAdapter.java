package com.jeanpier.canicat.ui.pets;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.core.RetrofitHelper;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.databinding.FragmentPetBinding;

import java.util.List;

public class PetRecyclerViewAdapter extends RecyclerView.Adapter<PetRecyclerViewAdapter.ViewHolder> {

    private final List<Pet> pets;

    public PetRecyclerViewAdapter(List<Pet> pets) {
        this.pets = pets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentPetBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Resources res = context.getResources();
        Pet currentPet = pets.get(position);

        holder.binding.textName.setText(currentPet.getName());
        holder.binding.textType.setText(
                String.format(res.getString(R.string.text_pet_type), currentPet.getRace(),
                        currentPet.getSpecies())
        );

//        Load pet picture
        Glide.with(context)
                .load(RetrofitHelper.BASE_URI + currentPet.getPicture())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.binding.circlePicture);

        holder.binding.layoutPet.setOnClickListener(v -> {
            Toast.makeText(context, "Click a " + currentPet.getName(), Toast.LENGTH_SHORT).show();
            NavDirections action = PetFragmentDirections.actionNavPetsToNavPetForm();
            Navigation.findNavController(v).navigate(action);
        });


    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final FragmentPetBinding binding;

        public ViewHolder(FragmentPetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

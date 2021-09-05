package com.jeanpier.canicat.ui.pets.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.databinding.PetItemBinding;
import com.jeanpier.canicat.ui.pets.fragments.PetFragmentDirections;

import java.util.List;

public class PetRecyclerViewAdapter extends RecyclerView.Adapter<PetRecyclerViewAdapter.ViewHolder> {

    private List<Pet> pets;

    public PetRecyclerViewAdapter(List<Pet> pets) {
        this.pets = pets;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPets(List<Pet> pets) {
        this.pets = pets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(PetItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.render(pets.get(position));
        holder.setClickLayoutListener(pets.get(position));
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final PetItemBinding binding;

        public ViewHolder(PetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void render(Pet pet) {
            Resources res = itemView.getResources();
            binding.textName.setText(pet.getName());
            String type = String.format(res.getString(R.string.text_pet_type), pet.getBreed(), pet.getSpecies());
            binding.textType.setText(type);
            if (pet.getPicture() != null) loadPicture(pet.getPicture());
        }

        public void setClickLayoutListener(Pet pet) {
            String petSelected = new Gson().toJson(pet);
            binding.layoutPet.setOnClickListener(v -> {
                PetFragmentDirections.ActionNavPetsToNavPetForm action =
                        PetFragmentDirections.actionNavPetsToNavPetForm(petSelected);
                Navigation.findNavController(v).navigate(action);
            });
        }

        private void loadPicture(String picture) {
            Context context = itemView.getContext();
            Glide.with(context)
                    .load(Routes.BASE_URI + picture)
                    .placeholder(R.drawable.ic_pet_placeholder)
                    .error(R.drawable.ic_pet_placeholder)
//                  Se desactiva el caché para evitar que al cambiar de imagen aparezca la antigua
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.circlePicture);
        }
    }
}

package com.jeanpier.canicat.ui.pets.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.core.GlideApp;
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
<<<<<<< HEAD
        Context context = holder.itemView.getContext();
        Resources res = context.getResources();
        Pet currentPet = pets.get(position);

        holder.binding.textName.setText(currentPet.getName());
        holder.binding.textType.setText(
                String.format(res.getString(R.string.text_pet_type), currentPet.getBreed(),
                        currentPet.getSpecies())
        );

        if (currentPet.getPicture() != null) {
//          Load pet picture
            Glide.with(context)
                    .load(Routes.BASE_URI + currentPet.getPicture())
                    .placeholder(R.drawable.ic_pet_placeholder)
                    .error(R.drawable.ic_pet_placeholder)
//                  Se desactiva el caché para evitar que al cambiar de imagen aparezca la antigua
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.binding.circlePicture);
        }

        holder.binding.layoutPet.setOnClickListener(v -> {
            PetFragmentDirections.ActionNavPetsToNavPetForm action =
                    PetFragmentDirections.actionNavPetsToNavPetForm(
                            new Gson().toJson(currentPet), FormAction.EDIT
                    );
            Navigation.findNavController(v).navigate(action);
        });

=======
        holder.render(pets.get(position));
        holder.setClickLayoutListener(pets.get(position));
>>>>>>> cea51c83c3f40fcb173618906bb1b7f6008e38ac
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
//            CircularProgressDrawable progressDrawable = new CircularProgressDrawable(context);
//            progressDrawable.setStrokeWidth(5);
//            progressDrawable.setCenterRadius(30);
//            progressDrawable.start();

            GlideApp.with(context)
                    .load(Routes.BASE_URI + picture)
//                    .placeholder(progressDrawable)
                    .error(R.drawable.ic_pet_placeholder)
//                  Se desactiva el caché para evitar que al cambiar de imagen aparezca la antigua
                    .skipMemoryCache(true)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.circlePicture);
        }
    }
}

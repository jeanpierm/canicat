package com.jeanpier.canicat.ui.pets.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeanpier.canicat.R;
import com.jeanpier.canicat.config.Routes;
import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.PetService;
import com.jeanpier.canicat.data.network.responses.ErrorResponse;
import com.jeanpier.canicat.data.network.responses.PostPetResponse;
import com.jeanpier.canicat.databinding.FragmentPetFormBinding;
import com.jeanpier.canicat.ui.pets.viewmodels.PetViewModel;
import com.jeanpier.canicat.ui.records.vaccine.viewmodels.VaccineViewModel;
import com.jeanpier.canicat.util.AlertUtil;
import com.jeanpier.canicat.util.KeyboardUtil;
import com.jeanpier.canicat.util.ParseUtil;
import com.jeanpier.canicat.util.TextFieldUtil;
import com.jeanpier.canicat.util.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetFormFragment extends Fragment {

    private static final String TAG = PetFormFragment.class.getSimpleName();
    private final PetService petService = new PetService();
    private String uid;
    private NavController navController;
    private FragmentPetFormBinding binding;
    private Bitmap pictureBitmap;
    private Pet pet;
    private TextInputEditText editName, editSpecies, editBreed;
    private AutoCompleteTextView editSexo;
    private ProgressBar progressBar;
    private FloatingActionButton fabPicture;
    private CircleImageView circlePicture;
    private PetViewModel petViewModel;
    private VaccineViewModel vaccineViewModel;
    private MenuItem menuSave, menuEdit, menuDelete, menuVaccines;
    private final Gson gson = new Gson();
    private final Type errorType = new TypeToken<ErrorResponse>() {
    }.getType();
    private final ActivityResultLauncher<String> fileChooserContract =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri == null) return;
                        try {
                            pictureBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().getContentResolver(), uri
                            );
                            circlePicture.setImageBitmap(pictureBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = FragmentPetFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        initUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);
        menuSave = menu.findItem(R.id.menu_save);
        menuEdit = menu.findItem(R.id.menu_edit);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuVaccines = menu.findItem(R.id.menu_vaccines);

        if (isEditing()) {
            readMode();
        } else {
            writeMode();
        }

        menuEdit.setOnMenuItemClickListener(item -> {
            writeMode();
//            pone focus al editText del nombre, indicandole al usuario que puede editar
            InputMethodManager imm = (InputMethodManager) requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
            return false;
        });

        menuSave.setOnMenuItemClickListener(item -> {
            if (!isValidForm()) {
                AlertUtil.showErrorAlert(getString(R.string.alert_complete_fields),
                        requireContext());
                return false;
            }
            KeyboardUtil.hideKeyboard(requireActivity());
            if (isEditing()) {
                updatePet();
            } else {
                postPet();
            }
            readMode();
            return false;
        });

        menuDelete.setOnMenuItemClickListener(item -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
            dialogBuilder.setMessage(
                    String.format(getString(R.string.alert_delete_confirmation), pet.getName())
            );
            dialogBuilder.setPositiveButton(getString(R.string.label_yes), (dialog, which) -> {
                dialog.dismiss();
                deletePet();
            });
            dialogBuilder.setNegativeButton(getString(R.string.label_cancel), (dialog, which) -> dialog.dismiss());
            dialogBuilder.show();
            return false;
        });

        menuVaccines.setOnMenuItemClickListener(item -> {
            if (pet.getId() == null) {
                AlertUtil.showErrorAlert(getString(R.string.pet_not_loaded), requireContext());
                return false;
            }
//            set el petId en el viewmodel para poderlo usar el VaccineFragment y VaccineFormFragment
            vaccineViewModel.setPetId(pet.getId());
//            navegación a las vacunas
            navController.navigate(R.id.action_nav_pet_form_to_nav_records);
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initUI() {
        initViews();
        progressBar.setVisibility(View.GONE);
        setPetFromArgs();
        if (isEditing()) fillFormFields();
        setActionBarTitle();
        loadDropdownOptions();
        initListeners();
        initViewModels();
    }

    private void initViews() {
        editName = binding.editName;
        editSpecies = binding.editSpecies;
        editBreed = binding.editBreed;
        editSexo = binding.editSexo;
        progressBar = binding.progressBar;
        fabPicture = binding.fabPicture;
        circlePicture = binding.circlePicture;
    }

    private void postPet() {
        progressBar.setVisibility(View.VISIBLE);
        Pet newPet = buildPetFromForm();
        Call<PostPetResponse> call = petService.create(newPet);
        call.enqueue(new Callback<PostPetResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostPetResponse> call, @NonNull Response<PostPetResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    String petId = response.body() != null ? response.body().getId() : null;
//                    se actualiza el id para poder eliminar una mascota sin errores
                    pet = newPet;
                    pet.setId(petId);
                    ToastUtil.show(requireContext(), getString(R.string.pet_created_successfull));
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(getString(R.string.pet_created_error), requireContext());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), requireContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostPetResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showGenericErrorAlert(requireContext());
                t.printStackTrace();
            }
        });
    }

    private void updatePet() {
        progressBar.setVisibility(View.VISIBLE);
        Pet newPet = buildPetFromForm();
        Call<Void> call = petService.updateById(pet.getId(), newPet);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
//                    se actualiza localmente el nombre de la mascota para la alerta de "borrar",
//                    alerta la cual muestra el nombre d ela mascota
                    pet.setName(newPet.getName());
                    ToastUtil.show(requireContext(), getString(R.string.pet_updated_successfull));
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(getString(R.string.pet_updated_error), requireContext());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), requireContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showGenericErrorAlert(requireContext());
                t.printStackTrace();
            }
        });
    }

    private void deletePet() {
        progressBar.setVisibility(View.VISIBLE);
        Call<Void> call = petService.deleteById(pet.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ToastUtil.show(requireContext(), getString(R.string.pet_deleted_succesfull));
                    navController.navigate(PetFormFragmentDirections.actionNavPetFormToNavPets());
                } else {
                    if (response.errorBody() == null) {
                        AlertUtil.showErrorAlert(getString(R.string.pet_deleted_error), requireContext());
                        return;
                    }
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), errorType);
                    AlertUtil.showErrorAlert(errorResponse.getError(), requireContext());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showGenericErrorAlert(requireContext());
                t.printStackTrace();
            }
        });
    }


    private void fillFormFields() {
        if (pet.getPicture() != null) {
            Glide.with(requireContext())
                    .load(Routes.BASE_URI + pet.getPicture())
                    .placeholder(R.drawable.ic_pet_placeholder)
                    .error(R.drawable.ic_pet_placeholder)
//                  Se desactiva el caché para evitar que al cambiar de foto de la mascota aparezca la antigua
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(circlePicture);
        }
        editName.setText(pet.getName());
        editSpecies.setText(pet.getSpecies());
        editBreed.setText(pet.getBreed());
//        filtro en falso para que aparezcan las demás opciones del dropdown
        editSexo.setText(pet.getSexo(), false);
    }

    private void initViewModels() {
        petViewModel = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        petViewModel.getUID().observe(getViewLifecycleOwner(), currentUid -> uid = currentUid);

        vaccineViewModel = new ViewModelProvider(requireActivity()).get(VaccineViewModel.class);
    }

    private void initListeners() {
        fabPicture.setOnClickListener(v -> openFileChooser());
    }

    private void openFileChooser() {
        fileChooserContract.launch("image/*");
    }

    private void setActionBarTitle() {
        String title = isEditing() ? getString(R.string.title_edit) : getString(R.string.title_create);
        Objects.requireNonNull(
                ((AppCompatActivity) requireActivity()).getSupportActionBar()
        ).setTitle(title);
    }

    @SuppressWarnings("RawUseOfParameterized")
    private void loadDropdownOptions() {
        String[] sexos = getResources().getStringArray(R.array.sexos);
        ArrayAdapter adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, sexos);
        editSexo.setAdapter(adapter);
    }

    private void removeDropdownOptions() {
        editSexo.setAdapter(null);
    }

    private void setPetFromArgs() {
        String petJson = PetFormFragmentArgs.fromBundle(getArguments()).getPet();
        pet = new Gson().fromJson(petJson, Pet.class);
    }

    private boolean isValidForm() {
        return !Objects.requireNonNull(editName.getText()).toString().isEmpty()
                && !Objects.requireNonNull(editSpecies.getText()).toString().isEmpty()
                && !Objects.requireNonNull(editBreed.getText()).toString().isEmpty()
                && !editSexo.getText().toString().isEmpty();
    }

    private Pet buildPetFromForm() {
        String name = TextFieldUtil.getString(editName);
        String species = TextFieldUtil.getString(editSpecies);
        String breed = TextFieldUtil.getString(editBreed);
        String sexo = editSexo.getText().toString().trim();
        String picture = pictureBitmap != null ? ParseUtil.parseBitmapToBase64(pictureBitmap) : null;
        return new Pet(name, species, breed, sexo, picture, uid);
    }

    private void readMode() {
        menuSave.setVisible(false);
        menuEdit.setVisible(true);
        menuDelete.setVisible(true);
        menuVaccines.setVisible(true);

        editName.setFocusable(false);
        editSpecies.setFocusable(false);
        editBreed.setFocusable(false);
        editSexo.setFocusable(false);
        removeDropdownOptions();
        fabPicture.setVisibility(View.INVISIBLE);
    }

    private void writeMode() {
        menuSave.setVisible(true);
        menuEdit.setVisible(false);
        menuDelete.setVisible(false);
        menuVaccines.setVisible(false);

        editName.setFocusableInTouchMode(true);
        editSpecies.setFocusableInTouchMode(true);
        editBreed.setFocusableInTouchMode(true);
        editSexo.setFocusableInTouchMode(true);
        loadDropdownOptions();
        fabPicture.setVisibility(View.VISIBLE);
    }

    private boolean isEditing() {
        return pet.getId() != null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
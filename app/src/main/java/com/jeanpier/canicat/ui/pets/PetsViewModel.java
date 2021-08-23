package com.jeanpier.canicat.ui.pets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PetsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PetsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pets fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
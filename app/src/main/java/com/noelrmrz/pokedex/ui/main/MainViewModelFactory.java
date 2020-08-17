package com.noelrmrz.pokedex.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;

    public MainViewModelFactory(Application application) {
        mApplication = application;
    }

    @NonNull
    @Override
    public  <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mApplication);
    }
}

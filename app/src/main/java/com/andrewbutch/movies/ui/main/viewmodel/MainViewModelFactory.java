package com.andrewbutch.movies.ui.main.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.andrewbutch.movies.domain.MoviesUseCase;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    MoviesUseCase useCase;

    public MainViewModelFactory(MoviesUseCase useCase) {
        this.useCase = useCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            return (T) new MainViewModel(useCase);
        }
        return null;
    }
}

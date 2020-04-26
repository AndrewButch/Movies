package com.andrewbutch.movies.di.main;

import androidx.lifecycle.ViewModel;

import com.andrewbutch.movies.di.ViewModelKey;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    public abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);
}

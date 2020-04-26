package com.andrewbutch.movies.di;

import androidx.lifecycle.ViewModelProvider;

import com.andrewbutch.movies.viewmodel.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {
    @Binds
    abstract ViewModelProvider.Factory bindsViewModelProvider(ViewModelProviderFactory factory);
}

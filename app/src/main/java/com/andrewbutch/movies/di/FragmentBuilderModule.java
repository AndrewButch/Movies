package com.andrewbutch.movies.di;

import com.andrewbutch.movies.ui.detail.DetailFragment;
import com.andrewbutch.movies.ui.main.MainFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    public abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    public abstract DetailFragment contributeDetailFragment();
}

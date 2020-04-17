package com.andrewbutch.movies.di;

import com.andrewbutch.movies.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}

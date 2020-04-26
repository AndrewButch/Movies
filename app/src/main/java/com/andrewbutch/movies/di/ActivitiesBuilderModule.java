package com.andrewbutch.movies.di;

import com.andrewbutch.movies.di.main.FragmentBuilderModule;
import com.andrewbutch.movies.di.main.MainModule;
import com.andrewbutch.movies.di.main.MainViewModelModule;
import com.andrewbutch.movies.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector(
            modules = {
                    FragmentBuilderModule.class,
                    MainModule.class,
                    MainViewModelModule.class,
            }
    )
    abstract MainActivity contributeMainActivity();


}

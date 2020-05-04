package com.andrewbutch.movies.di.main;

import android.app.Application;

import com.andrewbutch.movies.ui.NetworkStatusWatcher;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainModule {

    @Provides
    static NetworkStatusWatcher providesNetworkStatusWatcher(Application application) {
        return new NetworkStatusWatcher(application);
    }
}

package com.andrewbutch.movies.di.main;

import android.app.Application;

import com.andrewbutch.movies.domain.MoviesUseCase;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.ui.NetworkStatusWatcher;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MainModule {

    @Provides
    static MoviesUseCase provideUseCase(Repository repository) {
        return new MoviesUseCase(repository);
    }

    @Provides
    static NetworkStatusWatcher providesNetworkStatusWatcher(Application application) {
        return new NetworkStatusWatcher(application);
    }
}

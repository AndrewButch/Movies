package com.andrewbutch.movies.di;

import com.andrewbutch.movies.domain.MoviesUseCase;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.ui.main.MainActivity;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModelFactory;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesBuilderModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

    @Provides
    static MoviesUseCase provideUseCase(Repository repository) {
        return new MoviesUseCase(repository);
    }

    @Provides
    static MainViewModelFactory provideMainViewModelFactory(MoviesUseCase useCase) {
        return new MainViewModelFactory(useCase);
    }
}

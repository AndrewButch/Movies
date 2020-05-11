package com.andrewbutch.movies.di.main;

import com.andrewbutch.movies.ui.detail.DetailFragment;
import com.andrewbutch.movies.ui.favorite.FavoriteMoviesFragment;
import com.andrewbutch.movies.ui.searchhistory.SearchHistoryFragment;
import com.andrewbutch.movies.ui.searchmovie.SearchMovieFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    public abstract SearchMovieFragment contributeMainFragment();

    @ContributesAndroidInjector
    public abstract DetailFragment contributeDetailFragment();

    @ContributesAndroidInjector
    public abstract SearchHistoryFragment contributeSearchRequestsFragment();

    @ContributesAndroidInjector
    public abstract FavoriteMoviesFragment contributeFavoriteMoviesFragment();
}

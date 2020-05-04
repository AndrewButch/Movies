package com.andrewbutch.movies.di.main;

import com.andrewbutch.movies.ui.detail.DetailFragment;
import com.andrewbutch.movies.ui.favorite.FavoriteMoviesFragment;
import com.andrewbutch.movies.ui.searchmovie.MainFragment;
import com.andrewbutch.movies.ui.searchrequests.SearchRequestsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    public abstract MainFragment contributeMainFragment();

    @ContributesAndroidInjector
    public abstract DetailFragment contributeDetailFragment();

    @ContributesAndroidInjector
    public abstract SearchRequestsFragment contributeSearchRequestsFragment();

    @ContributesAndroidInjector
    public abstract FavoriteMoviesFragment contributeFavoriteMoviesFragment();
}

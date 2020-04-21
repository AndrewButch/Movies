package com.andrewbutch.movies.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.domain.model.MoviePreview;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

import javax.inject.Inject;

public class MoviesRepository implements Repository {
    private MovieLoader loader;
    private MutableLiveData<SearchResource<List<MoviePreview>>> moviesPreviewLiveData;

    @Inject
    public MoviesRepository(MovieLoader loader) {
        this.loader = loader;
        moviesPreviewLiveData = new MutableLiveData<>();
        SearchResource<List<MoviePreview>> resource = SearchResource.complete(null);
        moviesPreviewLiveData.setValue(resource);
    }

    @Override
    public void searchMovie(String search) {
        loadMoviesBySearch(search);
        SearchResource<List<MoviePreview>> resource = SearchResource.loading(null);
        moviesPreviewLiveData.setValue(resource);
    }

    @Override
    public LiveData<SearchResource<List<MoviePreview>>> getMovieSearch() {
        return moviesPreviewLiveData;
    }

    private void loadMoviesBySearch(String search) {
        loader.loadMovies(search, new MovieLoader.OnCompleteListener() {
            @Override
            public void onLoadComplete() {
                SearchResource<List<MoviePreview>> resource = SearchResource.complete(loader.getMovies());
                moviesPreviewLiveData.setValue(resource);
            }

            @Override
            public void onLoadFailure() {

            }
        });
    }
}

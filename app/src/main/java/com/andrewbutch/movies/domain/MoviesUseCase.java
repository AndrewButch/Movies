package com.andrewbutch.movies.domain;

import androidx.lifecycle.LiveData;

import com.andrewbutch.movies.domain.model.MoviePreview;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

import javax.inject.Inject;

public class MoviesUseCase {
    private Repository repository;

    @Inject
    public MoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public void searchMovies(String search) {
        repository.searchMovie(search);
    }

    public LiveData<SearchResource<List<MoviePreview>>> getMovieSearch() {
        return repository.getMovieSearch();
    }
}

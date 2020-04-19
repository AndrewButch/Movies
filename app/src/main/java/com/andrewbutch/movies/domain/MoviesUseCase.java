package com.andrewbutch.movies.domain;

import com.andrewbutch.movies.domain.model.MoviePreview;

import java.util.List;

import javax.inject.Inject;

public class MoviesUseCase {
    private Repository repository;

    @Inject
    public MoviesUseCase(Repository repository) {
        this.repository = repository;
    }

    public void searchMovies(String search, SearchMoviesCallback callback) {
        repository.loadMoviesBySearch(search, callback);
    }

    public List<MoviePreview> getSearchResult() {
        return repository.getLastSearchResult();
    }

    public interface SearchMoviesCallback {
        void onComplete();
    }
}

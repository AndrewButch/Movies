package com.andrewbutch.movies.data;

import com.andrewbutch.movies.domain.MoviesUseCase;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.domain.model.Movie;
import com.andrewbutch.movies.domain.model.MoviePreview;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class MoviesRepository implements Repository {
    private MovieLoader loader;
    private List<MoviePreview> moviePreviews;
    private List<Movie> movies;

    @Inject
    public MoviesRepository(MovieLoader loader) {
        this.loader = loader;
    }

    @Override
    public List<MoviePreview> getLastSearchResult() {
        if (moviePreviews == null) {
            return Collections.emptyList();
        }
        return moviePreviews;
    }

    @Override
    public void insertMovie(MoviePreview movie) {

    }

    @Override
    public void insertMovie(List<MoviePreview> movies) {

    }

    @Override
    public Movie getMovieByID(String movieId) {
        return null;
    }

    @Override
    public void addMovie(Movie movie) {

    }

    @Override
    public List<String> getSearchRequests() {
        return null;
    }

    @Override
    public void insertSearchRequest(String request) {

    }

    @Override
    public void loadMoviesBySearch(String search, MoviesUseCase.SearchMoviesCallback callback) {
        loader.loadMovies(search, new MovieLoader.OnCompleteListener() {
            @Override
            public void onLoadComplete() {
                moviePreviews = loader.getMovies();
                callback.onComplete();
            }

            @Override
            public void onLoadFailure() {

            }
        });
    }
}

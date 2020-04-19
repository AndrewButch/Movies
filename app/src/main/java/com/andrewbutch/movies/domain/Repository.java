package com.andrewbutch.movies.domain;

import com.andrewbutch.movies.domain.model.Movie;
import com.andrewbutch.movies.domain.model.MoviePreview;

import java.util.List;

public interface Repository {
    List<MoviePreview> getLastSearchResult();
    void insertMovie(MoviePreview movie);
    void insertMovie(List<MoviePreview> movies);

    Movie getMovieByID(String movieId);
    void addMovie(Movie movie);

    List<String> getSearchRequests();
    void insertSearchRequest(String request);

    void loadMoviesBySearch(String search, MoviesUseCase.SearchMoviesCallback callback);

}

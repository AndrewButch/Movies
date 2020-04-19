package com.andrewbutch.movies.domain;

import com.andrewbutch.movies.domain.model.MoviePreview;

import java.util.List;

public interface Repository {
    List<MoviePreview> getLastSearchResult();
    void insertMovie(MoviePreview movie);
    void insertMovie(List<MoviePreview> movies);

    MoviePreview getMovieByID(String movieId);

    List<String> getSearchRequests();
        void insertSearchRequest(String request);
}

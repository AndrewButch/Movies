package com.andrewbutch.movies.domain;

import androidx.lifecycle.LiveData;

import com.andrewbutch.movies.domain.model.MoviePreview;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

public interface Repository {
//    List<MoviePreview> getLastSearchResult();
//    void insertMovie(MoviePreview movie);
//    void insertMovie(List<MoviePreview> movies);
//
//    Movie getMovieByID(String movieId);
//    void addMovie(Movie movie);
//
//    List<String> getSearchRequests();
//    void insertSearchRequest(String request);

    void searchMovie(String search);
    LiveData<SearchResource<List<MoviePreview>>> getMovieSearch();


}

package com.andrewbutch.movies.domain;

import androidx.lifecycle.LiveData;

import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

public interface Repository {

    /** Movie search */
    void searchMovie(String searchRequest);

    LiveData<SearchResource<List<MoviePreview>>> getSearchResult();

    void removeAllSearchRequests();


    /** Current movie */
    void setCurrentMovieId(String movieId);

    LiveData<SearchResource<Movie>> getCurrentMovie();


    /**  Favorite */
    void addToFavorite();

    LiveData<SearchResource<List<MoviePreview>>> getFavoriteMovies();
}

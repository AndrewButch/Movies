package com.andrewbutch.movies.domain;

import androidx.lifecycle.LiveData;

import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
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
        return repository.getSearchResult();
    }

    public void setDetailMovie(String movieID) {
        repository.setCurrentMovieId(movieID);
    }


    public LiveData<SearchResource<Movie>> getDetailMovie() {
        return repository.getCurrentMovie();
    }

    public void removeAllSearchRequests() {
        repository.removeAllSearchRequests();
    }
}

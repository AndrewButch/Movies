package com.andrewbutch.movies.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andrewbutch.movies.data.database.entity.SearchRequest;
import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private Repository repository;

    @Inject
    public MainViewModel(Repository repository) {
        this.repository = repository;
    }

    // Search movie
    public void search(String search) {
        repository.searchMovie(search);
    }

    public LiveData<SearchResource<List<MoviePreview>>> observeMovieSearch() {
        return repository.getSearchResult();
    }

    // Current movie
    public void setCurrentMovieID(String movieID) {
        repository.setCurrentMovieId(movieID);
    }

    public LiveData<SearchResource<Movie>> observeCurrentMovie() {
        return repository.getCurrentMovie();
    }

    // Search requests
    public LiveData<SearchResource<List<SearchRequest>>> observeSearchRequests() {
        return repository.getAllSearchRequests();
    }

    public void removeAllSearchRequests() {
        repository.removeAllSearchRequests();
    }

    // Favorite
    public void addToFavorite() {
        repository.addToFavorite();
    }
    public LiveData<SearchResource<List<MoviePreview>>> observeFavorites() {
        return repository.getFavoriteMovies();
    }
}

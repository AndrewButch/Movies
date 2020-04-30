package com.andrewbutch.movies.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.domain.MoviesUseCase;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private MoviesUseCase useCase;

    @Inject
    public MainViewModel(MoviesUseCase useCase) {
        this.useCase = useCase;
    }

    public void search(String search) {
        useCase.searchMovies(search);
    }

    public LiveData<SearchResource<List<MoviePreview>>> observeMovieSearch() {
        return useCase.getMovieSearch();
    }

    public void setDetailMovieID(String movieID) {
        useCase.setDetailMovie(movieID);
    }

    public LiveData<SearchResource<Movie>> observeDetailMovie() {
        return useCase.getDetailMovie();
    }

    public void removeAllSearchRequests() {
        useCase.removeAllSearchRequests();
    }
}

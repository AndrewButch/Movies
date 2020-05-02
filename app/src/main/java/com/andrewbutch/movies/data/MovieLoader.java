package com.andrewbutch.movies.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.data.pojo.Search;
import com.andrewbutch.movies.ui.main.SearchResource;
import com.andrewbutch.movies.utils.Constatnts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieLoader {

    private MoviesAPI moviesAPI;


    private MutableLiveData<SearchResource<List<MoviePreview>>> searchResult;
    private SearchResource<List<MoviePreview>> searchResource;

    private MutableLiveData<SearchResource<Movie>> movieLoadingResult;
    private SearchResource<Movie> movieLoadingResource;


    @Inject
    public MovieLoader(MoviesAPI moviesAPI) {
        this.moviesAPI = moviesAPI;

        // search movie Live Data
        searchResult = new MutableLiveData<>();
        searchResource = SearchResource.complete(null);
        searchResult.setValue(searchResource);

        // loading movie by ID Live Data
        movieLoadingResult = new MutableLiveData<>();
        movieLoadingResource = SearchResource.complete(null);
        movieLoadingResult.setValue(movieLoadingResource);
    }

    public void loadMovies(String search) {
        searchResource = SearchResource.loading(null);
        searchResult.setValue(searchResource);

        Call<Search> callMovies =  moviesAPI.getSearchMovies(search, Constatnts.API_KEY);
        Request req = callMovies.request();
        callMovies.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = response.body();
                if (search != null) {
                    List<MoviePreview> movies = search.getMoviesSearch();
                    if (movies == null) {
                        movies = new ArrayList<>();
                        MoviePreview noResult = new MoviePreview();
                        noResult.setTitle("Не найдено фильмов");
                        movies.add(noResult);
                    }
                    searchResource = SearchResource.complete(movies);
                    searchResult.setValue(searchResource);
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                searchResult.setValue(SearchResource.error());
                searchResult.setValue(searchResource);
            }
        });
    }

    public void loadMovieById(String movieId) {
        movieLoadingResource = SearchResource.loading(null);
        movieLoadingResult.setValue(movieLoadingResource);

        Call<Movie> callMovies =  moviesAPI.getSearchMoviesById(movieId, Constatnts.API_KEY);
        Request req = callMovies.request();
        callMovies.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                if (movie == null) {
                    movie = new Movie();
                    movie.setTitle("Ошибка при загрузке");
                }
                movieLoadingResource = SearchResource.complete(movie);
                movieLoadingResult.setValue(movieLoadingResource);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movieLoadingResource = SearchResource.error();
                movieLoadingResult.setValue(movieLoadingResource);
            }
        });
    }

    public LiveData<SearchResource<List<MoviePreview>>> getMovies() {
        return searchResult;
    }

    public LiveData<SearchResource<Movie>> getMovie() {
        return movieLoadingResult;
    }
}

package com.andrewbutch.movies;

import com.andrewbutch.movies.model.Movie;
import com.andrewbutch.movies.model.Search;
import com.andrewbutch.movies.utils.Constatnts;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieLoader {

    private MoviesAPI moviesAPI;
    private List<Movie> movies;

    @Inject
    public MovieLoader(MoviesAPI moviesAPI) {
        this.moviesAPI = moviesAPI;
    }

    public void loadMovies(String search, final OnCompleteListener callback) {
        Call<Search> callMovies =  moviesAPI.getSearchMovies(search, Constatnts.API_KEY);
        callMovies.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = response.body();
                if (search != null) {
                    movies = search.getMoviesSearch();
                    if (movies == null) {
                        movies = new ArrayList<>();
                        Movie noResult = new Movie();
                        noResult.setTitle("Не найдено фильмов");
                        movies.add(noResult);
                    }
                }
                callback.onLoadComplete();
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                callback.onLoadFailure();
            }
        });
    }


    public List<Movie> getMovies() {
        return movies;
    }

    public interface OnCompleteListener {
        void onLoadComplete();
        void onLoadFailure();
    }
}

package com.andrewbutch.movies;

import com.andrewbutch.movies.model.Movie;
import com.andrewbutch.movies.model.Search;
import com.andrewbutch.movies.utils.Constatnts;

import java.util.List;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieLoader {

    public Retrofit retrofit;

    private MoviesAPI moviesAPI;
    private List<Movie> movies;
    private OnCompleteListener callback;

    @Inject
    public MovieLoader(Retrofit retrofit) {
        this.retrofit = retrofit;
//        this.moviesAPI = moviesAPI;

        moviesAPI = retrofit.create(MoviesAPI.class);
    }

    public void loadMovies(String search, final OnCompleteListener callback) {
//        Call<MoviesSearch> callMovies =  moviesAPI.getStarTrekMovies();
        Call<Search> callMovies =  moviesAPI.getSearchMovies(search, Constatnts.API_KEY);
        HttpUrl url = callMovies.request().url();
        callMovies.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = response.body();
                if (search != null) {
                    movies = search.getMoviesSearch();
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

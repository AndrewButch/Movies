package com.andrewbutch.movies.data;

import androidx.lifecycle.MutableLiveData;

import com.andrewbutch.movies.domain.model.MoviePreview;
import com.andrewbutch.movies.domain.model.Search;
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
    private List<MoviePreview> movies;
    private MutableLiveData<List<MoviePreview>> moviesLiveData;

    @Inject
    public MovieLoader(MoviesAPI moviesAPI) {
        this.moviesAPI = moviesAPI;
    }

    public void loadMovies(String search, final OnCompleteListener callback) {
        Call<Search> callMovies =  moviesAPI.getSearchMovies(search, Constatnts.API_KEY);
        Request req = callMovies.request();
        callMovies.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = response.body();
                if (search != null) {
                    movies = search.getMoviesSearch();
                    moviesLiveData = new MutableLiveData<>();
                    moviesLiveData.setValue(movies);
                    if (movies == null) {
                        movies = new ArrayList<>();
                        MoviePreview noResult = new MoviePreview();
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


    public List<MoviePreview> getMovies() {
        return movies;
    }

    public interface OnCompleteListener {
        void onLoadComplete();
        void onLoadFailure();
    }
}

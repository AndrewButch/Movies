package com.andrewbutch.movies.data;

import com.andrewbutch.movies.domain.model.Movie;
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
    private Movie movie;

    @Inject
    public MovieLoader(MoviesAPI moviesAPI) {
        this.moviesAPI = moviesAPI;
    }

    public void loadMovies(String search, final OnCompleteListener callback) {
        movie = null;
        Call<Search> callMovies =  moviesAPI.getSearchMovies(search, Constatnts.API_KEY);
        Request req = callMovies.request();
        callMovies.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {
                Search search = response.body();
                if (search != null) {
                    movies = search.getMoviesSearch();
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

    public void loadMovieById(String movieId, final OnCompleteListener callback) {
        movie = null;
        Call<Movie> callMovies =  moviesAPI.getSearchMoviesById(movieId, Constatnts.API_KEY);
        Request req = callMovies.request();
        callMovies.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movie = response.body();
                if (movie == null) {
                    movie = new Movie();
                    movie.setTitle("Ошибка при загрузке");
                }
                callback.onLoadComplete();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                callback.onLoadFailure();
            }
        });
    }

    public List<MoviePreview> getMovies() {
        return movies;
    }

    public Movie getMovie() {
        return movie;
    }

    public interface OnCompleteListener {
        void onLoadComplete();
        void onLoadFailure();
    }
}

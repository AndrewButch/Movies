package com.andrewbutch.movies.data;

import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoviesAPI {

    @GET("?s=star%20trek&apikey=dfdc2177")
    Call<Search> getStarTrekMovies();

    @GET("/")
    Call<Search> getSearchMovies(@Query("s") String searchText,
                                 @Query("apikey") String apiKey);

    @GET("/")
    Call<Movie> getSearchMoviesById(@Query("i") String movieId,
                                    @Query("apikey") String apiKey);
}

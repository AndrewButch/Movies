package com.andrewbutch.movies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("Search")
    private List<Movie> moviesSearch;

    public List<Movie> getMoviesSearch() {
        return moviesSearch;
    }

    public void setMoviesSearch(List<Movie> moviesSearch) {
        this.moviesSearch = moviesSearch;
    }
}

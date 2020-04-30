package com.andrewbutch.movies.data.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("Search")
    private List<MoviePreview> moviesSearch;

    public List<MoviePreview> getMoviesSearch() {
        return moviesSearch;
    }

    public void setMoviesSearch(List<MoviePreview> moviesSearch) {
        this.moviesSearch = moviesSearch;
    }
}

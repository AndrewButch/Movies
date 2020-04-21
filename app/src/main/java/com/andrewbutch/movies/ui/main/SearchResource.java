package com.andrewbutch.movies.ui.main;

import androidx.annotation.Nullable;

public class SearchResource<T> {
    SearchStatus status;
    T data;
    String message;

    public SearchResource(SearchStatus status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> SearchResource<T> loading(@Nullable T data) {
        return new SearchResource<>(SearchStatus.LOADING, data, null);
    }

    public static <T> SearchResource<T> error() {
        return new SearchResource<>(SearchStatus.ERROR, null, null);
    }

    public static <T> SearchResource<T> complete(@Nullable T data) {
        return new SearchResource<>(SearchStatus.COMPLETE, data, null);
    }

    public enum SearchStatus {LOADING, ERROR, COMPLETE}
}

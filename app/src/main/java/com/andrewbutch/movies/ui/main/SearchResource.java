package com.andrewbutch.movies.ui.main;

import androidx.annotation.Nullable;

public class SearchResource<T> {
    public SearchStatus status;
    public T data;
    public String message;

    public SearchResource(SearchStatus status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> SearchResource<T> loading(@Nullable T data) {
        return new SearchResource<>(SearchStatus.LOADING, data, null);
    }

    public static <T> SearchResource<T> error(@Nullable String msg) {
        return new SearchResource<>(SearchStatus.ERROR, null, msg);
    }

    public static <T> SearchResource<T> complete(@Nullable T data) {
        return new SearchResource<>(SearchStatus.COMPLETE, data, null);
    }

    public enum SearchStatus {LOADING, ERROR, COMPLETE}
}

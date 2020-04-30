package com.andrewbutch.movies.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "search_requests")
public class SearchRequest {
    @PrimaryKey
    @NonNull
    private String request;

    @NonNull
    private long time;

    public SearchRequest(@NonNull String request, long time) {
        this.request = request;
        this.time = time;
    }

    @NonNull
    public String getRequest() {
        return request;
    }

    public long getTime() {
        return time;
    }
}

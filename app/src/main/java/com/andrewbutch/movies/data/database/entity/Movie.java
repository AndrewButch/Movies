package com.andrewbutch.movies.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {
    @PrimaryKey
    @NonNull
    private String id;
    private String posterUrl;
    private String title;
    private String year;
    private String duration;
    private String rating;

    public Movie(@NonNull String id, String posterUrl, String title, String year, String duration, String rating) {
        this.id = id;
        this.posterUrl = posterUrl;
        this.title = title;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDuration() {
        return duration;
    }

    public String getRating() {
        return rating;
    }
}

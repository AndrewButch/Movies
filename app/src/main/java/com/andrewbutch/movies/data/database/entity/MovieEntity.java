package com.andrewbutch.movies.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class MovieEntity {
    @PrimaryKey
    @NonNull
    private String id;
    @NonNull
    private String posterUrl;
    @NonNull
    private String title;
    @NonNull
    private String year;
    @NonNull
    private String duration;
    @NonNull
    private String rating;
    @NonNull
    private boolean favorite;


    public MovieEntity(@NonNull String id, String posterUrl, String title, String year,
                       String duration, String rating, boolean favorite) {
        this.id = id;
        this.posterUrl = posterUrl;
        this.title = title;
        this.year = year;
        this.duration = duration;
        this.rating = rating;
        this.favorite = favorite;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

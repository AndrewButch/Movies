package com.andrewbutch.movies.domain.model;

//{
//        Search: [
//        {
//        Title: "Star Wars: Episode IV - A New Hope",
//        Year: "1977",
//        imdbID: "tt0076759",
//        Type: "movie",
//        Poster: https://m.media-amazon.com/images/M/MV5BNzVlY2MwMjktM2E4OS00Y2Y3LWE3ZjctYzhkZGM3YzA1ZWM2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg
//        },
//        {
//        Title: "Star Wars: Episode V - The Empire Strikes Back",
//        Year: "1980",
//        imdbID: "tt0080684",
//        Type: "movie",
//        Poster: https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg
//        },
//        {
//        Title: "Rogue One: A Star Wars Story",
//        Year: "2016",
//        imdbID: "tt3748528",
//        Type: "movie",
//        Poster: https://m.media-amazon.com/images/M/MV5BMjEwMzMxODIzOV5BMl5BanBnXkFtZTgwNzg3OTAzMDI@._V1_SX300.jpg
//        }
//        ],
//        totalResults: "2915",
//        Response: "True"
//        }


import com.google.gson.annotations.SerializedName;

public class MoviePreview {
    @SerializedName("Poster")
    private String posterUrl;
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("imdbID")
    private String id;

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

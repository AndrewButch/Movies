package com.andrewbutch.movies.model;

/*{
    "Title":"Guardians of the Galaxy Vol. 2",
    "Year":"2017",
    "Rated":"PG-13",
    "Released":"05 May 2017",
    "Runtime":"136 min",
    "Genre":"Action, Adventure, Comedy, Sci-Fi",
    "Director":"James Gunn",
    "Writer":"James Gunn, Dan Abnett (based on the Marvel comics by), Andy Lanning (based on the Marvel comics by), Steve Englehart (Star-Lord created by), Steve Gan (Star-Lord created by), Jim Starlin (Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Steve Gerber (Howard the Duck created by), Val Mayerik (Howard the Duck created by)",
    "Actors":"Chris Pratt, Zoe Saldana, Dave Bautista, Vin Diesel",
    "Plot":"The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father the ambitious celestial being Ego.",
    "Language":"English",
    "Country":"USA",
    "Awards":"Nominated for 1 Oscar. Another 14 wins & 52 nominations.",
    "Poster":"https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg",
    "Ratings":[
    {
    "Source":"Internet Movie Database",
    "Value":"7.6/10"
    },
    {
    "Source":"Rotten Tomatoes",
    "Value":"85%"
    },
    {
    "Source":"Metacritic",
    "Value":"67/100"
    }
    ],
    "Metascore":"67",
    "imdbRating":"7.6",
    "imdbVotes":"518,435",
    "imdbID":"tt3896198",
    "Type":"movie",
    "DVD":"22 Aug 2017",
    "BoxOffice":"$389,804,217",
    "Production":"Walt Disney Pictures",
    "Website":"N/A",
    "Response":"True"
}*/


public class Movie {
    private String posterUrl;
    private String title;
    private String year;
//    private String duration;
//    private String imdbRating;

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

//    public String getDuration() {
//        return duration;
//    }
//
//    public void setDuration(String duration) {
//        this.duration = duration;
//    }
//
//    public String getImdbRating() {
//        return imdbRating;
//    }
//
//    public void setImdbRating(String imdbRating) {
//        this.imdbRating = imdbRating;
//    }
}
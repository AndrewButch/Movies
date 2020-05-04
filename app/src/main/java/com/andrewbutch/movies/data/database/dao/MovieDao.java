package com.andrewbutch.movies.data.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.andrewbutch.movies.data.database.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table WHERE favorite = :isFavorite ")
    List<Movie> getAllFavoriteMovies(boolean isFavorite);

//    @Query("SELECT * FROM movie_table")
//    List<Movie> getAllFavoriteMovies();
}

package com.andrewbutch.movies.data.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.andrewbutch.movies.data.database.entity.MovieEntity;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntity movie);

    @Query("SELECT * FROM movie_table WHERE id = :id")
    MovieEntity get(String id);

    @Query("SELECT * FROM movie_table LIMIT 1")
    MovieEntity[] getAnyMovie();

    @Update
    void update(MovieEntity movie);

    @Delete
    void delete(MovieEntity movie);

    @Query("DELETE FROM movie_table WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM movie_table WHERE favorite = :isFavorite ")
    List<MovieEntity> getAllFavoriteMovies(boolean isFavorite);

}

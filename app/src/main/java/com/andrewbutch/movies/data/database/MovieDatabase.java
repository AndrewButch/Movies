package com.andrewbutch.movies.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.andrewbutch.movies.data.database.dao.MovieDao;
import com.andrewbutch.movies.data.database.entity.MovieEntity;

import java.util.ArrayList;
import java.util.List;

// Database of favorite movies
@Database(entities = {MovieEntity.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;

    public abstract MovieDao dao();

    public static MovieDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MovieDatabase.class, "movie_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateFavorite(instance).execute();
        }
    };

    private static class PopulateFavorite extends AsyncTask<Void, Void, Void> {
        MovieDao dao;

        public PopulateFavorite(MovieDatabase db) {
            this.dao = db.dao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (dao.getAnyMovie().length < 1) {
                List<MovieEntity> entities = getFakeFavoriteMovies();
                for (MovieEntity entity : entities) {
                    dao.insert(entity);
                }
            }
            return null;
        }
    }

    // get fake favorite movies for first time open
    private static List<MovieEntity> getFakeFavoriteMovies() {
        List<MovieEntity> list = new ArrayList<>();
        list.add(new MovieEntity(
                "tt5180504",
                "https://m.media-amazon.com/images/M/MV5BOGE4MmVjMDgtMzIzYy00NjEwLWJlODMtMDI1MGY2ZDlhMzE2XkEyXkFqcGdeQXVyMzY0MTE3NzU@._V1_SX300.jpg",
                "The Witcher",
                "2019–",
                "60 min",
                "8.3",
                true));
        list.add(new MovieEntity(
                "tt0371746",
                "https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_SX300.jpg",
                "Iron Man",
                "2008",
                "126 min",
                "7.9",
                true));
        return list;
    }
}

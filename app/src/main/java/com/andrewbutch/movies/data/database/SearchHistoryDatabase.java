package com.andrewbutch.movies.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.andrewbutch.movies.data.database.dao.SearchHistoryDao;
import com.andrewbutch.movies.data.database.entity.SearchRequest;

// Database of search history
@Database(entities = {SearchRequest.class}, exportSchema = false, version = 1)
public abstract class SearchHistoryDatabase extends RoomDatabase {
    private static SearchHistoryDatabase instance;

    public abstract SearchHistoryDao dao();

    public static SearchHistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    SearchHistoryDatabase.class, "search_request_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private SearchHistoryDao wordDAO;
        private String[] words = {"Star wars", "Steel", "Bond", "Man"};

        PopulateDbAsyncTask(SearchHistoryDatabase db) {
            this.wordDAO = db.dao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SearchRequest[] search = wordDAO.getAnyWord();
            if (search.length < 1) {
                for (String s : words) {
                    SearchRequest word = new SearchRequest(s, 1);
                    wordDAO.insert(word);
                }
            }
            return null;
        }
    }
}

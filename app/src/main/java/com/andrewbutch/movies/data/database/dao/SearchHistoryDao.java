package com.andrewbutch.movies.data.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.andrewbutch.movies.data.database.entity.SearchRequest;

import java.util.List;

@Dao
public interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchRequest request);

    @Query("SELECT * FROM search_requests ORDER BY time DESC")
    List<SearchRequest> selectAllByTime();

    @Query("SELECT * FROM search_requests LIMIT 1")
    SearchRequest[] getAnyWord();

    @Query("DELETE FROM search_requests")
    void removeAll();
}

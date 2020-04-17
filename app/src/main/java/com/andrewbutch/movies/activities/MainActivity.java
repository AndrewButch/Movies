package com.andrewbutch.movies.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.MovieLoader;
import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.MovieAdapter;
import com.andrewbutch.movies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    public static final int RC_PERMISSION_INTERNET = 123;

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movies;

    @Inject
    MovieLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieAdapter(this);
        movies = new ArrayList<>();

        recyclerView.setAdapter(adapter);
        checkInternetPermission();
    }

    private void checkInternetPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            getMovies("Star Wars");
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.INTERNET},
                    RC_PERMISSION_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_PERMISSION_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMovies("Star Wars");
                } else {
                    throw new RuntimeException("Need internet permission");
                }
        }
    }

    private void getMovies(String search) {
//        loader = new MovieLoader();
        loader.loadMovies(search, new MovieLoader.OnCompleteListener() {
            @Override
            public void onLoadComplete() {
                movies = loader.getMovies();
                adapter.setData(movies);
            }

            @Override
            public void onLoadFailure() {
                Toast.makeText(MainActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            getMovies(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }
}

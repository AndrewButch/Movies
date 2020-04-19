package com.andrewbutch.movies.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.MovieLoader;
import com.andrewbutch.movies.domain.model.MoviePreview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int RC_PERMISSION_INTERNET = 123;

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<MoviePreview> movies;
    private MenuItem searchMenuItem;
    private ProgressBar progressBar;
    private NetworkStatusWatcher networkStatusWatcher;
    private boolean networkConnected;
    private SearchView searchView;
    private FloatingActionButton fab;

    @Inject
    MovieLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> {
            searchMenuItem.expandActionView();
            searchMenuItem.getActionView().requestFocus();
        });


        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieAdapter(this);
        movies = new ArrayList<>();

        recyclerView.setAdapter(adapter);

        networkStatusWatcher = new NetworkStatusWatcher(this);
        registerReceiver(networkStatusWatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        checkInternetPermission();

        if (savedInstanceState != null) {
            progressBar.setVisibility(View.VISIBLE);
            movies = loader.getMovies();
            adapter.setData(movies);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkInternetPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            checkOldRequestData();
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
                    checkOldRequestData();
                } else {
                    throw new RuntimeException("Need internet permission");
                }
        }
    }

    private void getMovies(String search) {
        if (networkConnected) {
            progressBar.setVisibility(View.VISIBLE);
            loader.loadMovies(search, new MovieLoader.OnCompleteListener() {
                @Override
                public void onLoadComplete() {
                    movies = loader.getMovies();
                    adapter.setData(movies);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadFailure() {
                    Toast.makeText(MainActivity.this, "Error loading", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSearchRequested() {
        Log.d(TAG, "onSearchRequested: ");
        return super.onSearchRequested();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStatusWatcher);
    }

    public void setNetworkConnected(boolean isConnected) {
        this.networkConnected = isConnected;
    }

    private void checkOldRequestData() {
        movies = loader.getMovies();
        if (movies != null) {
            adapter.setData(movies);
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchMenuItem.collapseActionView();
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
        searchMenuItem = menu.findItem(R.id.menu_search);
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                fab.hide();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                fab.show();
                return true;
            }
        });
        searchView = (SearchView) searchMenuItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }
}
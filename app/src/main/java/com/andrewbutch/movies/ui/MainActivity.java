package com.andrewbutch.movies.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.andrewbutch.movies.domain.MoviesUseCase;
import com.andrewbutch.movies.domain.model.MoviePreview;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int RC_PERMISSION_INTERNET = 123;

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private SearchView searchView;
    private FloatingActionButton fab;
    private MenuItem searchMenuItem;
    private ProgressBar progressBar;

    private NetworkStatusWatcher networkStatusWatcher;

    @Inject
    MoviesUseCase useCase;

    @Override
    protected void onResume() {
        super.onResume();
        networkStatusWatcher = new NetworkStatusWatcher(this);
        registerReceiver(networkStatusWatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        fab = findViewById(R.id.fab);
        // fab activates search
        fab.setOnClickListener(v -> {
            searchMenuItem.expandActionView();
            searchMenuItem.getActionView().requestFocus();
        });


        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieAdapter(this);

        recyclerView.setAdapter(adapter);


        checkInternetPermission();

        if (savedInstanceState != null) {
            // check last seaarch recult
            getSearchResult();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStatusWatcher);
    }

    // Handle intent from Search
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_PERMISSION_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    throw new RuntimeException("Need internet permission");
                }
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

        // listen expand/collapse SearchView to hide/show FAB
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

    private void checkInternetPermission() {
        int permissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            // permission granted
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.INTERNET},
                    RC_PERMISSION_INTERNET);
        }
    }

    private void getMovies(String search) {
        if (networkStatusWatcher.isNetworkConnected) {
            progressBar.setVisibility(View.VISIBLE);
            useCase.searchMovies(search, new MoviesUseCase.SearchMoviesCallback() {
                @Override
                public void onComplete() {
                    getSearchResult();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getSearchResult() {
        List<MoviePreview> movies = useCase.getSearchResult();
        if (movies.size() > 0) {
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
}

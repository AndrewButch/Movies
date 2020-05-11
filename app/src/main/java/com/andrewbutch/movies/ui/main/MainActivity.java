package com.andrewbutch.movies.ui.main;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.ui.NetworkStatusWatcher;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements MainView {
    private static final String TAG = "!@#MainActivity";
    public static final int RC_PERMISSION_INTERNET = 123;

    private SearchView searchView;
    private MenuItem searchMenuItem;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private NavController navController;
    private AppBarLayout appBarLayout;
    private BottomNavigationView bottomNavigationView;

    private boolean isTablet;

    @Inject
    NetworkStatusWatcher networkStatusWatcher;
    @Inject
    MainViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(networkStatusWatcher, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        handleIntent(getIntent());
        initViews();

        checkInternetPermission();
        viewModel.observeMovieSearch().observe(this, listSearchResource -> {
            if (listSearchResource.status == SearchResource.SearchStatus.LOADING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStatusWatcher);
    }

    // Intent intercept for finding Intent.ACTION_SEARCH
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
        inflater.inflate(R.menu.search_movie_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.menu_search);

        // listen expand/collapse SearchView to hide/show FAB
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                fab.hide();
                bottomNavigationView.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                fab.show();
                bottomNavigationView.setVisibility(View.VISIBLE);
                return true;
            }
        });
        searchView = (SearchView) searchMenuItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_search:
                activateSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navToDetailMovie() {
        if (isTablet) {
            // tablet
//            navController.navigate(R.id.detail_dest);
        } else {
            // phone
            int currentDestId = navController.getCurrentDestination().getId();
            switch (currentDestId) {
                case R.id.search_dest:
                    navController.navigate(R.id.action_searchDest_to_detailDest);
                    break;
                case R.id.favorite_dest:
                    navController.navigate(R.id.action_favoriteDest_to_detailDest);
                    break;
            }
        }
    }

    public void navToSearchRequests() {
        if (isTablet) {
            // tablet
            navController.navigate(R.id.search_history_dest);
        } else {
            // phone
            navController.navigate(R.id.action_favoriteDest_to_searchHistoryDest);
        }
    }

    public void navToFavorite() {
        if (isTablet) {
            // tablet
            navController.navigate(R.id.favorite_dest);
        } else {
            // phone
            navController.navigate(R.id.action_favoriteDest_to_searchDest);
        }
    }

    public void navToSearch() {
        if (isTablet) {
            // tablet
            navController.navigate(R.id.search_dest);
        } else {
            // phone
            navController.navigate(R.id.action_favoriteDest_to_searchDest);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
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

    // Handle intent with Intent.ACTION_SEARCH
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if (searchMenuItem != null) {
                searchMenuItem.collapseActionView();
            }
            searchMovies(query);
        }
    }

    private void searchMovies(String search) {
        if (networkStatusWatcher.isNetworkConnected()) {
            viewModel.search(search);
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar);
        progressBar = findViewById(R.id.progressBar);

        fab = findViewById(R.id.fab);
        // fab activates search
        fab.setOnClickListener(v -> {
            activateSearch();
        });
        bottomNavigationView = findViewById(R.id.bottom_nav_view);


        navController = Navigation.findNavController(this, R.id.main_nav_host);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            fab.hide();
            appBarLayout.setExpanded(true);
            int destId = destination.getId();
            switch (destId) {
                case R.id.search_dest:
                    fab.show();
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    break;
                case R.id.detail_dest:
                    bottomNavigationView.setVisibility(View.GONE);
                    break;
                case R.id.favorite_dest:
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    break;
            }
        });

        if(!isTablet) {
            // phone
            NavigationUI.setupWithNavController(bottomNavigationView,
                    Navigation.findNavController(MainActivity.this, R.id.main_nav_host));
        } else {
            // tablet
            // manual setup bottomNavigationView
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    item -> {
                        switch (item.getItemId()) {
                            case R.id.search_dest:
                                navToSearch();
                                return true;
                            case R.id.search_history_dest:
                                navToSearchRequests();
                                return true;
                            case R.id.favorite_dest:
                                navToFavorite();
                                return true;
                        }
                        return false;
                    });
        }
    }

    @Override
    public void activateSearch() {
        searchMenuItem.expandActionView();
        searchMenuItem.getActionView().requestFocus();
    }
}

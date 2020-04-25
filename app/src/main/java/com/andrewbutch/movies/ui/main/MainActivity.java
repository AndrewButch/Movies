package com.andrewbutch.movies.ui.main;

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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.ui.NetworkStatusWatcher;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModelFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements MainView {
    private static final String TAG = "!@#MainActivity";
    public static final int RC_PERMISSION_INTERNET = 123;

    private SearchView searchView;
    private FloatingActionButton fab;
    private MenuItem searchMenuItem;
    private ProgressBar progressBar;
    private NavController navController;
    private NetworkStatusWatcher networkStatusWatcher;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    MainViewModel viewModel;
    @Inject
    MainViewModelFactory providerFactory;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar);

        fab = findViewById(R.id.fab);
        // fab activates search
        fab.setOnClickListener(v -> {
            searchMenuItem.expandActionView();
            searchMenuItem.getActionView().requestFocus();
        });


        progressBar = findViewById(R.id.progressBar);

        setupNavController();

        checkInternetPermission();

        viewModel = new ViewModelProvider(this, providerFactory).get(MainViewModel.class);
        viewModel.observeMovieSearch().observe(this, listSearchResource -> {
            if (listSearchResource.status == SearchResource.SearchStatus.LOADING){
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
        Log.d(TAG, "onCreateOptionsMenu: ");
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

    @Override
    public void navToDetailMovie() {
        navController.navigate(R.id.action_mainFragment_to_detailFragment);
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
            searchMenuItem.collapseActionView();
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

    private void setupNavController() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int destId = destination.getId();
                switch (destId) {
                    case R.id.mainFragment:
                        fab.show();
                        appBarLayout.setExpanded(true);
                        break;
                    case R.id.detailFragment:
                        fab.hide();
                        appBarLayout.setExpanded(true);
                        break;
                }
            }
        });
    }
}

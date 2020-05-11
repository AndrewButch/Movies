package com.andrewbutch.movies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewbutch.movies.data.database.MovieDatabase;
import com.andrewbutch.movies.data.database.SearchRequestDatabase;
import com.andrewbutch.movies.data.database.dao.MovieDao;
import com.andrewbutch.movies.data.database.dao.SearchRequestDao;
import com.andrewbutch.movies.data.database.entity.MovieEntity;
import com.andrewbutch.movies.data.database.entity.SearchRequest;
import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviesRepository implements Repository {
    private static final String TAG = "MoviesRepository";
    private MovieLoader loader;

    private SearchRequestDatabase searchRequestDatabase;
    private SearchRequestDao searchRequestDao;

    private MovieDatabase movieDatabase;
    private MovieDao movieDao;

    private MutableLiveData<SearchResource<List<SearchRequest>>> searchRequestsLiveData;
    private SearchResource<List<SearchRequest>> searchRequestsResource;

    private MutableLiveData<SearchResource<List<MoviePreview>>> favoriteLiveData;
    private SearchResource<List<MoviePreview>> favoriteResource;

    private MutableLiveData<SearchResource<MovieEntity>> currentMovie;
    private SearchResource<MovieEntity> currentMovieResource;

    private Map<String, MovieEntity> favoriteMovies;

    @Inject
    public MoviesRepository(MovieLoader loader, Application application) {
        this.loader = loader;

        // Database
        searchRequestDatabase = SearchRequestDatabase.getInstance(application);
        searchRequestDao = searchRequestDatabase.dao();

        movieDatabase = MovieDatabase.getInstance(application);
        movieDao = movieDatabase.dao();

        // Live Data
        searchRequestsLiveData = new MutableLiveData<>();
        searchRequestsResource = SearchResource.complete(null);
        searchRequestsLiveData.setValue(searchRequestsResource);

        favoriteLiveData = new MutableLiveData<>();
        favoriteResource = SearchResource.complete(null);
        favoriteLiveData.setValue(favoriteResource);

        currentMovie = new MutableLiveData<>();
        currentMovieResource = SearchResource.complete(null);
        currentMovie.setValue(currentMovieResource);

        init();
    }

    private void init() {
        // get all favorite movies and cache to HashMap
        Single.fromCallable(() -> movieDao.getAllFavoriteMovies(true))
                .subscribeOn(Schedulers.io())
                .subscribe(movieEntities -> {
                    favoriteMovies = new HashMap<>();
                    for (MovieEntity entity : movieEntities) {
                        favoriteMovies.put(entity.getId(), entity);
                    }
                });
    }

    @Override
    public void searchMovie(String searchRequest) {
        loader.loadMovies(searchRequest);
        Completable.fromCallable(() -> {
            searchRequestDao.insert(new SearchRequest(searchRequest,
                    GregorianCalendar.getInstance().getTimeInMillis()));
            Log.d(TAG, "Search movie: " + Thread.currentThread().getName() + " " + searchRequest);
            return null;
        })
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    Log.d(TAG, "onComplete: " + searchRequest + " inserted");
                });
    }

    @Override
    public void removeAllSearchRequests() {
        Completable.fromCallable(() -> {
            Log.d(TAG, "removeAllSearchRequests: " + Thread.currentThread().getName());
            searchRequestDao.removeAll();
            return null;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    searchRequestsResource = SearchResource.complete(Collections.emptyList());
                    searchRequestsLiveData.setValue(searchRequestsResource);
                });
    }

    @Override
    public LiveData<SearchResource<List<MoviePreview>>> getSearchResult() {
        return loader.getMovies();
    }

    @Override
    public LiveData<SearchResource<List<SearchRequest>>> getAllSearchRequests() {
        Single.fromCallable(() -> searchRequestDao.selectAllByTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(searchRequests -> {
                    for (SearchRequest request : searchRequests) {
                        Log.d(TAG, "Requests: " + Thread.currentThread().getName() + " " + request.getRequest());
                    }
                    searchRequestsResource = SearchResource.complete(searchRequests);
                    searchRequestsLiveData.setValue(searchRequestsResource);
                });
        return searchRequestsLiveData;
    }

    @Override
    public void setCurrentMovieId(String movieId) {
        loader.loadMovieById(movieId);
    }

    @Override
    public LiveData<SearchResource<MovieEntity>> getCurrentMovie() {
        loader.getMovie().observeForever(movieSearchResource -> {
            switch (movieSearchResource.status) {
                case LOADING:
                    currentMovieResource = SearchResource.loading(null);
                    currentMovie.setValue(currentMovieResource);
                case COMPLETE:
                    if (movieSearchResource.data != null) {
                        Movie pojo = movieSearchResource.data;
                        MovieEntity movieEntity = mapMoviePojoToEntity(pojo);
                        String movieId = movieEntity.getId();
                        MovieEntity favoriteMovie = favoriteMovies.get(movieId);
                        if (favoriteMovie != null) {
                            movieEntity.setFavorite(favoriteMovie.isFavorite());
                        }
                        currentMovieResource = SearchResource.complete(movieEntity);
                        currentMovie.setValue(currentMovieResource);
                    }
                case ERROR:
                    currentMovieResource = SearchResource.error("Ошибка при загрузке current movie");
                    currentMovie.setValue(currentMovieResource);
            }
        });
        return currentMovie;
    }

    @Override
    public void addToFavorite() {
        if (getCurrentMovie() != null) {
            final Movie movie = loader.getMovie().getValue().data;
            if (movie != null) {
                // Create DB entity
                Single.fromCallable(() -> {
                    String id = movie.getId();
                    String posterUrl = movie.getPosterUrl();
                    String title = movie.getTitle();
                    String year = movie.getYear();
                    String durationg = movie.getDuration();
                    String rating = movie.getRating();
                    MovieEntity movieEntity =
                            new MovieEntity(id, posterUrl, title, year, durationg, rating, true);
                    // insert entity into DB
                    movieDao.insert(movieEntity);
                    favoriteMovies.put(id, movieEntity);
                    return movieEntity;
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(movieEntity -> {
                            // update live data with entity
                            currentMovieResource = SearchResource.complete(movieEntity);
                            currentMovie.setValue(currentMovieResource);
                        });
            }
        }
    }

    @Override
    public void removeFromFavorite(final String movieId) {
        if (!movieId.isEmpty()) {
            // remove movie by id from favorite DB
            Single.fromCallable(() -> {
                movieDao.delete(movieId);
                List<MovieEntity> favoriteMovies = movieDao.getAllFavoriteMovies(true);
                List<MoviePreview> movies = mapMovieListEntityToPojo(favoriteMovies);
                return movies;
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movies -> {
                        // update live data
                        MovieEntity movieEntity = favoriteMovies.get(movieId);
                        favoriteMovies.remove(movieId);
                        if (movieEntity != null) {
                            movieEntity.setFavorite(false);
                            currentMovieResource = SearchResource.complete(movieEntity);
                            currentMovie.setValue(currentMovieResource);
                        }
                        favoriteResource = SearchResource.complete(movies);
                        favoriteLiveData.setValue(favoriteResource);
                    });
        }
    }

    @Override
    public LiveData<SearchResource<List<MoviePreview>>> getFavoriteMovies() {
        Single.fromCallable(() -> movieDao.getAllFavoriteMovies(true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entities -> {
                    List<MoviePreview> movies = mapMovieListEntityToPojo(entities);
                    favoriteResource = SearchResource.complete(movies);
                    favoriteLiveData.setValue(favoriteResource);
                });
        return favoriteLiveData;
    }

    private List<MoviePreview> mapMovieListEntityToPojo(List<MovieEntity> dbMovies) {
        List<MoviePreview> movies = new ArrayList<>();
        for (MovieEntity movie : dbMovies) {
            MoviePreview pojoMovie = mapMovieEntityToPojo(movie);
            movies.add(pojoMovie);
        }
        return movies;
    }

    private MoviePreview mapMovieEntityToPojo(MovieEntity movieEntity) {
        Movie pojoMovie = new Movie();
        pojoMovie.setPosterUrl(movieEntity.getPosterUrl());
        pojoMovie.setTitle(movieEntity.getTitle());
        pojoMovie.setYear(movieEntity.getYear());
        pojoMovie.setRating(movieEntity.getRating());
        pojoMovie.setDuration(movieEntity.getDuration());
        pojoMovie.setId(movieEntity.getId());
        return pojoMovie;
    }

    private MovieEntity mapMoviePojoToEntity(Movie moviePojo) {
        MovieEntity movieEntity = new MovieEntity(
                moviePojo.getId(),
                moviePojo.getPosterUrl(),
                moviePojo.getTitle(),
                moviePojo.getYear(),
                moviePojo.getDuration(),
                moviePojo.getRating(),
                false);
        return movieEntity;
    }
}

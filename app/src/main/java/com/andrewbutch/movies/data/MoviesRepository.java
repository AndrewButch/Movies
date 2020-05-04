package com.andrewbutch.movies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrewbutch.movies.data.database.MovieDatabase;
import com.andrewbutch.movies.data.database.SearchRequestDatabase;
import com.andrewbutch.movies.data.database.dao.MovieDao;
import com.andrewbutch.movies.data.database.dao.SearchRequestDao;
import com.andrewbutch.movies.data.database.entity.SearchRequest;
import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
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

    private MutableLiveData<SearchResource<List<Movie>>> favoriteLiveData;
    private SearchResource<List<Movie>> favoriteResource;


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
    }

    @Override
    public void searchMovie(String searchRequest) {
        loadMoviesBySearch(searchRequest);
        Completable.fromRunnable(() -> {
            searchRequestDao.insert(new SearchRequest(searchRequest,
                    GregorianCalendar.getInstance().getTimeInMillis()));
            Log.d(TAG, "Search movie: " + Thread.currentThread().getName() + " " + searchRequest);

        })
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + searchRequest + " inserted");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }
                });
    }

    @Override
    public void removeAllSearchRequests() {
        Observable.create(emitter -> {
            if (!emitter.isDisposed()) {
                Log.d(TAG, "removeAllSearchRequests: " + Thread.currentThread().getName());
                searchRequestDao.removeAll();
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public LiveData<SearchResource<List<MoviePreview>>> getSearchResult() {
        return loader.getMovies();
    }

    @Override
    public LiveData<SearchResource<List<SearchRequest>>> getAllSearchRequests() {

        Observable.create(new ObservableOnSubscribe<List<SearchRequest>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<SearchRequest>> emitter) throws Throwable {
                emitter.onNext(searchRequestDao.selectAllByTime());
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SearchRequest>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<SearchRequest> searchRequests) {
                        for (SearchRequest request : searchRequests) {
                            Log.d(TAG, "Requests: " + Thread.currentThread().getName() + " " + request.getRequest());

                        }
                        searchRequestsResource = SearchResource.complete(searchRequests);
                        searchRequestsLiveData.setValue(searchRequestsResource);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return searchRequestsLiveData;
    }

    @Override
    public void setCurrentMovieId(String movieId) {
        loadMovieDetail(movieId);
    }

    @Override
    public LiveData<SearchResource<Movie>> getCurrentMovie() {
        return loader.getMovie();
    }

    @Override
    public void addToFavorite() {
        if (getCurrentMovie() != null) {
            final Movie movie = loader.getMovie().getValue().data;
            if (movie != null) {
                Completable.fromRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Add favorite: " + Thread.currentThread().getName() + " " + movie.getTitle());
                        String id = movie.getId();
                        String posterUrl = movie.getPosterUrl();
                        String title = movie.getTitle();
                        String year = movie.getYear();
                        String durationg = movie.getDuration();
                        String rating = movie.getRating();
                        com.andrewbutch.movies.data.database.entity.Movie movieDb =
                                new com.andrewbutch.movies.data.database.entity.Movie(id, posterUrl, title, year, durationg, rating, true);
                        movieDao.insert(movieDb);
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }
        }
    }

    @Override
    public LiveData<SearchResource<List<Movie>>> getFavoriteMovies() {
        Observable.fromCallable(() -> movieDao.getAllFavoriteMovies(true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<com.andrewbutch.movies.data.database.entity.Movie>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(List<com.andrewbutch.movies.data.database.entity.Movie> t) {
                        List<Movie> movies = mapToPojo(t);
                        favoriteResource = SearchResource.complete(movies);
                        favoriteLiveData.setValue(favoriteResource);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return favoriteLiveData;
    }

    private void loadMoviesBySearch(String search) {
        loader.loadMovies(search);
    }

    private void loadMovieDetail(String movieId) {
        loader.loadMovieById(movieId);
    }

    private List<Movie> mapToPojo(List<com.andrewbutch.movies.data.database.entity.Movie> dbMovies) {
        List<Movie> movies = new ArrayList<>();
        for (com.andrewbutch.movies.data.database.entity.Movie movie : dbMovies) {
            Movie pojoMovie = new Movie();
            pojoMovie.setPosterUrl(movie.getPosterUrl());
            pojoMovie.setTitle(movie.getTitle());
            pojoMovie.setYear(movie.getYear());
            pojoMovie.setRating(movie.getRating());
            pojoMovie.setDuration(movie.getDuration());
            pojoMovie.setId(movie.getId());
            movies.add(pojoMovie);
        }
        return movies;
    }
}

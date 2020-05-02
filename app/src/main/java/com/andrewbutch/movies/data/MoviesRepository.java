package com.andrewbutch.movies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.andrewbutch.movies.data.database.SearchRequestDatabase;
import com.andrewbutch.movies.data.database.dao.SearchRequestDao;
import com.andrewbutch.movies.data.database.entity.SearchRequest;
import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.domain.Repository;
import com.andrewbutch.movies.ui.main.SearchResource;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviesRepository implements Repository {
    private static final String TAG = "MoviesRepository";
    private MovieLoader loader;
//    private MutableLiveData<SearchResource<List<MoviePreview>>> moviesPreviewLiveData;
//    private MutableLiveData<SearchResource<Movie>> detailMovieLiveData;
    private String detailMovieId;

    private SearchRequestDatabase searchRequestDatabase;
    private SearchRequestDao searchRequestDao;

    @Inject
    public MoviesRepository(MovieLoader loader, Application application) {
        this.loader = loader;
//        moviesPreviewLiveData = new MutableLiveData<>();
//        SearchResource<List<MoviePreview>> resource = SearchResource.complete(null);
//        moviesPreviewLiveData.setValue(resource);
//
//        detailMovieLiveData = new MutableLiveData<>();
//        SearchResource<Movie> detailResource = SearchResource.complete(null);
//        detailMovieLiveData.setValue(detailResource);

        // Database
        searchRequestDatabase = SearchRequestDatabase.getInstance(application);
        searchRequestDao = searchRequestDatabase.dao();

    }

    @Override
    public void searchMovie(String searchRequest) {
        loadMoviesBySearch(searchRequest);
//        SearchResource<List<MoviePreview>> resource = SearchResource.loading(null);
//        moviesPreviewLiveData.setValue(resource);

//        searchRequestDao.insert(new SearchRequest(searchRequest, 1));

        Observable<SearchRequest> observable = Observable.create((ObservableOnSubscribe<SearchRequest>) emitter -> {
            if (!emitter.isDisposed()) {
                List<SearchRequest> requests = searchRequestDao.selectAllByTime();
                for (SearchRequest request : requests) {
                    emitter.onNext(request);
                }
            }

            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<SearchRequest>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SearchRequest request) {
                Log.d(TAG, "MoviesRepository: " + request.getRequest() + " => " + request.getTime());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

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
    public void setCurrentMovieId(String movieId) {
        this.detailMovieId = movieId;
        loadMovieDetail(movieId);
//        SearchResource<Movie> detailResource = SearchResource.loading(null);
//        detailMovieLiveData.setValue(detailResource);
    }

    @Override
    public LiveData<SearchResource<Movie>> getCurrentMovie() {
        return loader.getMovie();
    }

    @Override
    public void addToFavorite() {

    }

    @Override
    public LiveData<SearchResource<List<MoviePreview>>> getFavoriteMovies() {
        return null;
    }

    private void loadMoviesBySearch(String search) {
        loader.loadMovies(search);
    }

    private void loadMovieDetail(String movieId) {
        loader.loadMovieById(movieId);
    }
}

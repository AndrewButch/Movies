package com.andrewbutch.movies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
    private MutableLiveData<SearchResource<List<MoviePreview>>> moviesPreviewLiveData;
    private MutableLiveData<SearchResource<Movie>> detailMovieLiveData;
    private String detailMovieId;

    private SearchRequestDatabase searchRequestDatabase;
    private SearchRequestDao searchRequestDao;

    @Inject
    public MoviesRepository(MovieLoader loader, Application application) {
        this.loader = loader;
        moviesPreviewLiveData = new MutableLiveData<>();
        SearchResource<List<MoviePreview>> resource = SearchResource.complete(null);
        moviesPreviewLiveData.setValue(resource);

        detailMovieLiveData = new MutableLiveData<>();
        SearchResource<Movie> detailResource = SearchResource.complete(null);
        detailMovieLiveData.setValue(detailResource);

        // Database
        searchRequestDatabase = SearchRequestDatabase.getInstance(application);
        searchRequestDao = searchRequestDatabase.dao();

    }

    @Override
    public void searchMovie(String search) {
        loadMoviesBySearch(search);
        SearchResource<List<MoviePreview>> resource = SearchResource.loading(null);
        moviesPreviewLiveData.setValue(resource);
//        searchRequestDao.insert(new SearchRequest(search, 1));
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
    public LiveData<SearchResource<List<MoviePreview>>> getMovieSearch() {
        return moviesPreviewLiveData;
    }

    @Override
    public void setDetailMovieId(String movieId) {
        this.detailMovieId = movieId;
        loadMovieDetail(movieId);
        SearchResource<Movie> detailResource = SearchResource.loading(null);
        detailMovieLiveData.setValue(detailResource);
    }

    @Override
    public LiveData<SearchResource<Movie>> getDetailMovie() {
        return detailMovieLiveData;
    }

    private void loadMoviesBySearch(String search) {
        loader.loadMovies(search, new MovieLoader.OnCompleteListener() {
            @Override
            public void onLoadComplete() {
                SearchResource<List<MoviePreview>> resource = SearchResource.complete(loader.getMovies());
                moviesPreviewLiveData.setValue(resource);
            }

            @Override
            public void onLoadFailure() {

            }
        });
    }

    private void loadMovieDetail(String movieId) {
        loader.loadMovieById(movieId, new MovieLoader.OnCompleteListener() {

            @Override
            public void onLoadComplete() {
                SearchResource<Movie> resource = SearchResource.complete(loader.getMovie());
                detailMovieLiveData.setValue(resource);
            }

            @Override
            public void onLoadFailure() {

            }
        });
    }
}

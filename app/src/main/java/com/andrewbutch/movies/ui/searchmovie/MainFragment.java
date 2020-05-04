package com.andrewbutch.movies.ui.searchmovie;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.ui.NetworkStatusWatcher;
import com.andrewbutch.movies.ui.main.MainView;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainFragment extends DaggerFragment implements MovieAdapter.ViewHolderClickListener {
    private static final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private MainView view;

    @Inject
    NetworkStatusWatcher networkStatusWatcher;
    @Inject
    MainViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        view = (MainView) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MovieAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        viewModel.observeMovieSearch().observe(getViewLifecycleOwner(), listSearchResource -> {
            switch (listSearchResource.status) {
                case LOADING:
                    this.view.showProgress();
                    break;
                case COMPLETE:
                    this.view.hideProgress();
                    List<MoviePreview> list = listSearchResource.data;
                    if (list != null) {
                        adapter.setData(list);
                    }
                    break;
                case ERROR:
                    this.view.hideProgress();
                    break;
            }
        });
//        RxLoad();
    }

    @Override
    public void onClick(String movieID) {
        viewModel.setCurrentMovieID(movieID);
        view.navToDetailMovie();
    }

    private void RxLoad() {
        ArrayList<Integer> iterable = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            iterable.add(i);
        }
        Observable<String> observable = Observable.fromIterable(iterable)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Throwable {
                        Thread.sleep(1000);
                        Log.d(TAG, "apply: " + Thread.currentThread().getName() + " => " +  integer);
                        return String.valueOf(integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull String string) {
                        Log.d(TAG, "onNext: " + Thread.currentThread().getName() + " -> " + string);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: error");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: complete");
                    }
                });
    }
}

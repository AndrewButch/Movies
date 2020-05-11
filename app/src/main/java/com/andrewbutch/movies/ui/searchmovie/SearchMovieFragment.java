package com.andrewbutch.movies.ui.searchmovie;

import android.content.Context;
import android.os.Bundle;
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

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class SearchMovieFragment extends DaggerFragment implements MovieAdapter.ViewHolderClickListener {
    private static final String TAG = "MainFragment";
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private MainView view;

    @Inject
    NetworkStatusWatcher networkStatusWatcher;
    @Inject
    MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        view = (MainView) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
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
    }

    @Override
    public void onClick(String movieID) {
        viewModel.setCurrentMovieID(movieID);
        view.navToDetailMovie();
    }
}

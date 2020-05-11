package com.andrewbutch.movies.ui.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.andrewbutch.movies.ui.main.MainView;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class FavoriteMoviesFragment extends DaggerFragment implements FavoriteAdapter.ViewHolderClickListener {
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private MainView view;

    @Inject
    MainViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        view = (MainView) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.favorites_recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        viewModel.observeFavorites().observe(getViewLifecycleOwner(), favorites -> {
            switch (favorites.status) {
                case LOADING:
                    FavoriteMoviesFragment.this.view.showProgress();
                    break;
                case COMPLETE:
                    FavoriteMoviesFragment.this.view.hideProgress();
                    List<MoviePreview> movies = favorites.data;
                    if (movies != null) {
                        adapter.setData(movies);
                    }

                    break;
                case ERROR:
                    FavoriteMoviesFragment.this.view.hideProgress();
                    break;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClickDetail(String movieID) {
        viewModel.setCurrentMovieID(movieID);
        view.navToDetailMovie();
    }

    @Override
    public void onClickRemoveFavorite(String movieID) {
        viewModel.removeFromFavorite(movieID);
    }
}

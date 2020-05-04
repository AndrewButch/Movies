package com.andrewbutch.movies.ui.favorite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.pojo.Movie;
import com.andrewbutch.movies.ui.main.MainView;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class FavoriteMoviesFragment extends DaggerFragment {
    private MainView view;

    @Inject
    MainViewModel viewModel;

    TextView favorite;

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
        favorite = view.findViewById(R.id.favorite);

        viewModel.observeFavorites().observe(getViewLifecycleOwner(), favorites -> {
            switch (favorites.status) {
                case LOADING:
                    FavoriteMoviesFragment.this.view.showProgress();
                    break;
                case COMPLETE:
                    FavoriteMoviesFragment.this.view.hideProgress();
                    List<Movie> movies = favorites.data;
                    if (movies == null) {
                        break;
                    }
                    StringBuilder builder = new StringBuilder();
                    for (Movie movie : movies) {
                        builder.append(movie.getTitle());
                        builder.append("( ");
                        builder.append(movie.getYear());
                        builder.append(" )");
                        builder.append("\n");
                    }
                    FavoriteMoviesFragment.this.favorite.setText(builder.toString());
                    break;
                case ERROR:
                    FavoriteMoviesFragment.this.view.hideProgress();
                    break;
            }
        });
    }
}

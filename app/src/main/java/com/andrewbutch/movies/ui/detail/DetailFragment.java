package com.andrewbutch.movies.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.database.entity.MovieEntity;
import com.andrewbutch.movies.ui.main.MainView;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DetailFragment extends DaggerFragment {
    private MainView view;
    @Inject
    MainViewModel viewModel;

    private ImageView posterImage;
    private Button addToFavorite;
    private TextView title;
    private TextView year;
    private TextView rating;
    private TextView duration;

    private boolean isFavorite;
    private String movieId;


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
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        posterImage = view.findViewById(R.id.poster_image);
        title = view.findViewById(R.id.title_tv);
        year = view.findViewById(R.id.year_tv);
        rating = view.findViewById(R.id.rating_tv);
        duration = view.findViewById(R.id.duration_tv);
        addToFavorite = view.findViewById(R.id.add_to_favorite_btn);
        addToFavorite.setOnClickListener(v -> {
            if (isFavorite) {
                if (!movieId.isEmpty()) {
                    viewModel.removeFromFavorite(movieId);
//                    addToFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_unchecked_24dp, 0, 0, 0);
                }
            } else {
                viewModel.addToFavorite();
//                addToFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_checked_24dp, 0, 0, 0);
            }
        });

        viewModel.observeCurrentMovie().observe(getViewLifecycleOwner(), movieSearchResource -> {
            switch (movieSearchResource.status) {
                case LOADING:
                    DetailFragment.this.view.showProgress();
                    break;
                case COMPLETE:
                    DetailFragment.this.view.hideProgress();
                    MovieEntity movie = movieSearchResource.data;
                    if (movie == null) {
                        break;
                    }
                    title.setText(movie.getTitle());
                    year.setText(movie.getYear());
                    duration.setText(movie.getDuration());
                    rating.setText(movie.getRating());
                    Picasso.get().load(movie.getPosterUrl())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.ic_local_movies_black_24dp)
                            .error(R.drawable.ic_error_outline_black_24dp)
                            .into(posterImage);
                    isFavorite = movie.isFavorite();
                    if (isFavorite) {
                        addToFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_checked_24dp, 0,0, 0);
                        addToFavorite.setText(R.string.remove_from_favorite);
                    } else {
                        addToFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_unchecked_24dp, 0,0, 0);
                        addToFavorite.setText(R.string.add_to_favorite);
                    }
                    addToFavorite.setVisibility(View.VISIBLE);
                    movieId = movie.getId();
                    break;
                case ERROR:
                    DetailFragment.this.view.hideProgress();
                    break;
            }
        });
    }
}

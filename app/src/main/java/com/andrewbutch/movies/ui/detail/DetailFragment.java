package com.andrewbutch.movies.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.domain.model.Movie;
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
    private TextView title;
    private TextView year;
    private TextView rating;
    private TextView duration;


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

        viewModel.observeDetailMovie().observe(getViewLifecycleOwner(), movieSearchResource -> {
            switch (movieSearchResource.status) {
                case LOADING:
                    DetailFragment.this.view.showProgress();
                    break;
                case COMPLETE:
                    DetailFragment.this.view.hideProgress();
                    Movie movie = movieSearchResource.data;

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
                    break;
                case ERROR:
                    DetailFragment.this.view.hideProgress();
                    break;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.detail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.menu_fragment_hello:
                Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

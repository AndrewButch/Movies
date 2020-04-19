package com.andrewbutch.movies.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.domain.model.MoviePreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviewVH> {
    private List<MoviePreview> movies;
    private LayoutInflater inflater;

    public MovieAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MoviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MoviewVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviewVH holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void setData(List<MoviePreview> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public class MoviewVH extends RecyclerView.ViewHolder {

        ImageView posterImage;
        TextView title;
        TextView year;

        public MoviewVH(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.poster_image);
            title = itemView.findViewById(R.id.title_tv);
            year = itemView.findViewById(R.id.year_tv);
        }

        void bind(MoviePreview movie) {
            title.setText(movie.getTitle());
            year.setText(movie.getYear());
            Picasso.get().load(movie.getPosterUrl())
                    .fit()
                    .centerInside()
                    .into(posterImage);
        }
    }
}

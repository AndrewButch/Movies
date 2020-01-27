package com.andrewbutch.movies.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviewVH> {
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MoviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MoviewVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviewVH holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MoviewVH extends RecyclerView.ViewHolder {

        ImageView posterImage;
        TextView title;
        TextView year;
//        TextView duration;
//        TextView imdbRating;

        public MoviewVH(@NonNull View itemView) {
            super(itemView);

            posterImage = itemView.findViewById(R.id.poster_image);
            title = itemView.findViewById(R.id.title_tv);
            year = itemView.findViewById(R.id.year_tv);
//            duration = itemView.findViewById(R.id.duration_tv);
//            imdbRating = itemView.findViewById(R.id.rating_tv);
        }

        void bind(Movie movie) {
            title.setText(movie.getTitle());
            year.setText(movie.getYear());
//            duration.setText(movie.getDuration());
//            imdbRating.setText(movie.getImdbRating());
            Picasso.get().load(movie.getPosterUrl())
                    .fit()
                    .centerInside()
                    .into(posterImage);
        }
    }
}

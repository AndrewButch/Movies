package com.andrewbutch.movies.ui.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.R;
import com.andrewbutch.movies.data.pojo.MoviePreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteVH> {
    private List<MoviePreview> data;
    private LayoutInflater inflater;

    public FavoriteAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FavoriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.movie_item_favorite, parent, false);
        return new FavoriteVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteVH holder, int position) {
        MoviePreview movie = data.get(position);
        holder.bind(movie);
    }

    public void setData(List<MoviePreview> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class FavoriteVH extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView title;
        TextView year;
        TextView duration;
        TextView rating;
        ImageButton favoriteBtn;

        public FavoriteVH(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.poster_image);
            title = itemView.findViewById(R.id.title_tv);
            year = itemView.findViewById(R.id.year_tv);
            duration = itemView.findViewById(R.id.duration_tv);
            rating = itemView.findViewById(R.id.rating_tv);
            favoriteBtn = itemView.findViewById(R.id.btn_uncheck_favorite);

        }

        public void bind(MoviePreview movie) {
            title.setText(movie.getTitle());
            year.setText(movie.getYear());

            Picasso.get().load(movie.getPosterUrl())
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.ic_local_movies_black_24dp)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(posterImage);
        }
    }
}

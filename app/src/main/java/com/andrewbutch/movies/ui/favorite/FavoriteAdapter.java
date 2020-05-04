package com.andrewbutch.movies.ui.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.data.pojo.Movie;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteVH> {
    private List<Movie> data;
    private LayoutInflater inflater;

    public FavoriteAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FavoriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FavoriteVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteVH holder, int position) {
        Movie movie = data.get(position);
        holder.bind(movie);
    }

    public void setData(List<Movie> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class FavoriteVH extends RecyclerView.ViewHolder {
        TextView textView;

        public FavoriteVH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(Movie movie) {
            String text = movie.getTitle() + "( " + movie.getYear() + " )";
            textView.setText(text);
        }
    }
}

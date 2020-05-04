package com.andrewbutch.movies.ui.searchrequests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewbutch.movies.data.database.entity.SearchRequest;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchRequestVH> {
    private List<SearchRequest> data;
    private LayoutInflater inflater;

    public SearchAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SearchRequestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SearchRequestVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRequestVH holder, int position) {
        SearchRequest request = data.get(position);
        holder.bind(request);
    }

    public void setData(List<SearchRequest> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class SearchRequestVH extends RecyclerView.ViewHolder {
        TextView textView;

        public SearchRequestVH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(SearchRequest request) {
            textView.setText(request.getRequest());
        }
    }
}

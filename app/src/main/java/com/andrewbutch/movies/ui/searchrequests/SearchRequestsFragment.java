package com.andrewbutch.movies.ui.searchrequests;

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
import com.andrewbutch.movies.data.database.entity.SearchRequest;
import com.andrewbutch.movies.ui.main.MainView;
import com.andrewbutch.movies.ui.main.viewmodel.MainViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class SearchRequestsFragment extends DaggerFragment {
    private MainView view;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;

    @Inject
    MainViewModel viewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        view = (MainView) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_requests, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.search_request_recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchAdapter(getContext());
        recyclerView.setAdapter(adapter);

        viewModel.observeSearchRequests().observe(getViewLifecycleOwner(), searchRequests -> {
            switch (searchRequests.status) {
                case LOADING:
                    SearchRequestsFragment.this.view.showProgress();
                    break;
                case COMPLETE:
                    SearchRequestsFragment.this.view.hideProgress();
                    List<SearchRequest> requests = searchRequests.data;
                    if (requests != null) {
                        adapter.setData(requests);
                    }
                    break;
                case ERROR:
                    SearchRequestsFragment.this.view.hideProgress();
                    break;
            }
        });
    }

}

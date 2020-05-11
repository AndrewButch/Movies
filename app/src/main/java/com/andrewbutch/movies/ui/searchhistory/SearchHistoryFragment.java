package com.andrewbutch.movies.ui.searchhistory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class SearchHistoryFragment extends DaggerFragment {
    private MainView view;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;

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
        View v = inflater.inflate(R.layout.fragment_search_history, container, false);
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
                    SearchHistoryFragment.this.view.showProgress();
                    break;
                case COMPLETE:
                    SearchHistoryFragment.this.view.hideProgress();
                    List<SearchRequest> requests = searchRequests.data;
                    if (requests != null) {
                        adapter.setData(requests);
                    }
                    break;
                case ERROR:
                    SearchHistoryFragment.this.view.hideProgress();
                    break;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.menu_remove_search_requests:
                viewModel.removeAllSearchRequests();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

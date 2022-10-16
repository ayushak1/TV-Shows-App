package com.ayush.myapplication.activities;


import android.content.Intent;
import android.os.Bundle;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import com.ayush.myapplication.R;
import com.ayush.myapplication.adapters.TVShowsAdapter;
import com.ayush.myapplication.databinding.ActivityMvvmBinding;
import com.ayush.myapplication.listeners.TVShowsListener;
import com.ayush.myapplication.models.TVShow;

import com.ayush.myapplication.viewmodels.MostPopularTVShowsViewModel;


import java.util.ArrayList;
import java.util.List;

public class mvvm extends AppCompatActivity implements TVShowsListener {

    private ActivityMvvmBinding activityMvvmBinding;
    private MostPopularTVShowsViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage=1;
    private int totalAvailablePages=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        activityMvvmBinding = DataBindingUtil.setContentView(this,R.layout.activity_mvvm);
        doInitialization();

    }


    private void doInitialization() {
        activityMvvmBinding.tvShowsRecyclerview.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows,this);
        activityMvvmBinding.tvShowsRecyclerview.setAdapter(tvShowsAdapter);
        activityMvvmBinding.tvShowsRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!activityMvvmBinding.tvShowsRecyclerview.canScrollVertically(1)){
                    if(currentPage <= totalAvailablePages) {
                        currentPage +=1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMvvmBinding.imageWatchlist.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), WatchlistActivity.class)));
        getMostPopularTVShows();
    }
    private void getMostPopularTVShows() {
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, mostPopularTVShowsResponse -> {
            toggleLoading();
            if(mostPopularTVShowsResponse != null) {
                totalAvailablePages = mostPopularTVShowsResponse.getTotalPages();
                if(mostPopularTVShowsResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();

                    tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMvvmBinding.getIsLoading() != null && activityMvvmBinding.getIsLoading()) {
                activityMvvmBinding.setIsLoading(false);
            } else {
                activityMvvmBinding.setIsLoading(true);
            }
        } else {
            if (activityMvvmBinding.getIsLoadingMore() != null && activityMvvmBinding.getIsLoadingMore()) {
                activityMvvmBinding.setIsLoadingMore(false);
            } else {
                activityMvvmBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}
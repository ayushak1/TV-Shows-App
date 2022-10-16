package com.ayush.myapplication.listeners;

import com.ayush.myapplication.models.TVShow;

public interface WatchlistListener {

    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchlist(TVShow tvShow, int position);

}

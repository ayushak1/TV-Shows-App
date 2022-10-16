package com.ayush.myapplication.network;

import com.ayush.myapplication.responses.TVShowDetailsResponse;
import com.ayush.myapplication.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TVShowDetailsResponse> getTvShowDetails(@Query("q") String tvShowId);
}

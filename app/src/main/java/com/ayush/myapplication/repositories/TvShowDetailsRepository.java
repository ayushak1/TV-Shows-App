package com.ayush.myapplication.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ayush.myapplication.network.ApiClient;
import com.ayush.myapplication.network.ApiService;
import com.ayush.myapplication.responses.TVShowDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsRepository {

        private ApiService apiService;

        public TvShowDetailsRepository() {
            apiService = ApiClient.getRetrofit().create(ApiService.class);
        }

        public LiveData<TVShowDetailsResponse> getTvShowDetails(String tvShowId) {
            MutableLiveData<TVShowDetailsResponse> data = new MutableLiveData<>();
            apiService.getTvShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<TVShowDetailsResponse> call,@NonNull Response<TVShowDetailsResponse> response) {
                    data.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<TVShowDetailsResponse> call,@NonNull Throwable t) {
                    data.setValue(null);
                }
            });
            return data;
        }
}

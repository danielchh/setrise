package com.dannie.setrise.logic.networking.clients.googleplaces;

import androidx.lifecycle.LiveData;
import com.dannie.setrise.logic.models.network.Candidates;
import com.dannie.setrise.logic.networking.ApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesWebService {

    @GET("textsearch/json")
    LiveData<ApiResponse<Candidates>> getPlacesResults(@Query("query") String query);

}

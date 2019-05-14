package com.dannie.setrise.logic.networking.clients.sunsetrise;

import androidx.lifecycle.LiveData;
import com.dannie.setrise.logic.models.network.SunInfo;
import com.dannie.setrise.logic.networking.ApiResponse;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SunWebService {

    @GET("json")
    LiveData<ApiResponse<SunInfo>> getSunInfo(@Query("lat") Double lat, @Query("lng") Double lng);

}

package com.dannie.setrise.ui.screens.city

import androidx.lifecycle.LiveData
import com.dannie.setrise.logic.models.network.Candidates
import com.dannie.setrise.logic.networking.ApiResponse
import com.dannie.setrise.logic.networking.NetworkBoundResource
import com.dannie.setrise.logic.networking.Resource
import com.dannie.setrise.logic.networking.clients.googleplaces.PlacesApiClient

class CityRepository {

    fun searchPlaces(query: String): LiveData<Resource<Candidates>> {
        return object : NetworkBoundResource<Candidates>() {
            override fun createCall(): LiveData<ApiResponse<Candidates>> {
                return PlacesApiClient.getClient().webService.getPlacesResults(query)
            }
        }.asLiveData()
    }
}
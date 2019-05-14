package com.dannie.setrise.ui.screens.sun

import androidx.lifecycle.LiveData
import com.dannie.setrise.logic.models.network.SunInfo
import com.dannie.setrise.logic.networking.ApiResponse
import com.dannie.setrise.logic.networking.NetworkBoundResource
import com.dannie.setrise.logic.networking.Resource
import com.dannie.setrise.logic.networking.clients.sunsetrise.SunApiClient

class SunRepository {
    fun getSunInfo(lat: Double, lon: Double): LiveData<Resource<SunInfo>> {
        return object : NetworkBoundResource<SunInfo>() {
            override fun createCall(): LiveData<ApiResponse<SunInfo>> {
                return SunApiClient.getClient().webService.getSunInfo(lat, lon)
            }
        }.asLiveData()
    }
}
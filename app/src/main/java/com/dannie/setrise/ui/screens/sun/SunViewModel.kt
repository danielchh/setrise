package com.dannie.setrise.ui.screens.sun

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dannie.setrise.R
import com.dannie.setrise.logic.models.local.SunInfoLite
import com.dannie.setrise.logic.models.network.SunInfo
import com.dannie.setrise.logic.networking.Resource
import com.dannie.setrise.logic.networking.State
import com.dannie.setrise.ui.screens.base.BaseViewModel
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

class SunViewModel(application: Application) : BaseViewModel(application) {

    val repo = SunRepository()
    private var sunInfoResponse: LiveData<Resource<SunInfo>>? = null
    var sunInfo: SunInfoLite? = null

    val screenState = MutableLiveData<ScreenState>()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    var fetchedInitial = false

    var cityAt: String? = null

    /**
     * Gets last known location and makes call to sunset/rise info
     * Suppressed, as called from "access granted" location
     */
    @SuppressLint("MissingPermission")
    fun getSunInfoForUserLocation() {
        checkNetworkAndCall {
            cityAt = context.getString(R.string.my_location)
            screenState.value = ScreenState.LOADING
            fusedLocationClient.lastLocation
                .addOnSuccessListener {
                    fetchedInitial = true
                    getInfoForLocation(it.latitude, it.longitude)
                }
                .addOnFailureListener {
                    screenState.postValue(ScreenState.FAILURE)
                }
        }
    }

    fun getInfoForLocation(lat: Double, lon: Double) {
        checkNetworkAndCall {
            sunInfoResponse = repo.getSunInfo(lat, lon)
            sunInfoResponse?.observeForever(object : Observer<Resource<SunInfo>> {
                override fun onChanged(resource: Resource<SunInfo>?) {
                    when (resource?.state) {
                        State.LOADING -> screenState.value = ScreenState.LOADING
                        State.SUCCESS -> {
                            resource.data?.also {
                                sunInfo = convertTimes(resource.data)
                                screenState.value = ScreenState.INFO_LOADED
                            } ?: run {
                                screenState.value = ScreenState.FAILURE
                            }
                            sunInfoResponse?.removeObserver(this)
                        }
                        State.FAILURE -> {
                            resource.message?.also { showWarning(it) }
                                ?: run { showWarning(R.string.unexpected_error) }
                            screenState.value = ScreenState.FAILURE
                            sunInfoResponse?.removeObserver(this)
                        }
                    }
                }
            })
        }
    }

    private fun convertTimes(from: SunInfo): SunInfoLite {
        val utcFormat = SimpleDateFormat("h:mm:ss a").apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val prettyFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        return SunInfoLite(
            prettyFormat.format(utcFormat.parse(from.results?.sunrise)),
            prettyFormat.format(utcFormat.parse(from.results?.sunset))
        )
    }

    fun onCityChosen(placeName: String, placeLat: Double, placeLon: Double) {
        cityAt = placeName
        getInfoForLocation(placeLat, placeLon)
    }
}
package com.dannie.setrise.ui.screens.city

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dannie.setrise.R
import com.dannie.setrise.logic.models.network.Candidate
import com.dannie.setrise.logic.models.network.Candidates
import com.dannie.setrise.logic.networking.Resource
import com.dannie.setrise.logic.networking.State
import com.dannie.setrise.ui.screens.base.BaseViewModel

class CityViewModel(application: Application) : BaseViewModel(application) {

    private val repo = CityRepository()
    private var searchResponse: LiveData<Resource<Candidates>>? = null
    var candidates: ArrayList<Candidate>? = null

    val screenState = MutableLiveData<ScreenState>()

    fun onSearchClicked(query: String) {
        checkNetworkAndCall {
            searchResponse = repo.searchPlaces(query)
            searchResponse?.observeForever(object : Observer<Resource<Candidates>> {
                override fun onChanged(resource: Resource<Candidates>?) {
                    when (resource?.state) {
                        State.LOADING -> screenState.value = ScreenState.LOADING
                        State.SUCCESS -> {
                            processSearchResults(resource.data?.candidates)
                            searchResponse?.removeObserver(this)
                        }
                        State.FAILURE -> {
                            resource.message?.also { showWarning(it) }
                                ?: run { showWarning(R.string.unexpected_error) }
                            screenState.value = ScreenState.FAILURE
                            searchResponse?.removeObserver(this)
                        }
                    }
                }

            })
        }
    }

    private fun processSearchResults(data: ArrayList<Candidate>?) {
        when {
            data == null -> {
                showWarning(R.string.unexpected_error)
                screenState.value = ScreenState.FAILURE
            }
            data.isEmpty() -> {
                screenState.value = ScreenState.LOADED_EMPTY
            }
            else -> {
                candidates = data
                screenState.value = ScreenState.LOADED_SHOW
            }
        }
    }

}
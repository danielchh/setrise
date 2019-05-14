package com.dannie.setrise.ui.screens.city

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dannie.setrise.R
import com.dannie.setrise.logic.models.local.PlaceInfo
import com.dannie.setrise.ui.screens.base.BaseFragment
import com.dannie.setrise.ui.screens.city.rv.PlacesRVAdapter
import com.dannie.setrise.ui.screens.sun.SunFragment
import kotlinx.android.synthetic.main.fragment_city.*

class CityFragment : BaseFragment<CityViewModel>() {

    override fun provideViewModel() =
        ViewModelProviders.of(this).get(CityViewModel::class.java)

    override val layoutId = R.layout.fragment_city

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_LAT = "extra_lat"
        const val EXTRA_LON = "extra_lon"
    }

    private val placesAdapter = PlacesRVAdapter { onItemChosen(it) }

    override fun initUi(view: View) {
        super.initUi(view)
        ivSearch.setOnClickListener {
            searchForPlaces(etCity.text.toString())
        }
        rvCities.apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addItemDecoration(DividerItemDecoration(context, lm.orientation))
            adapter = placesAdapter
        }
        etCity.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchForPlaces(etCity.text.toString())
                true
            } else false
        }
        showKeyboard(etCity)
    }

    private fun searchForPlaces(query: String) {
        txtMessage.isVisible = false
        placesAdapter.clearItems()
        viewModel.onSearchClicked(query)
    }

    override fun subscribeToObservables() {
        super.subscribeToObservables()
        viewModel.screenState.observe(viewLifecycleOwner, Observer {
            when (it) {
                ScreenState.LOADING -> showLoading()
                ScreenState.FAILURE -> hideLoading()
                ScreenState.LOADED_EMPTY -> {
                    hideLoading()
                    txtMessage.isVisible = true
                }
                ScreenState.LOADED_SHOW -> {
                    hideLoading()
                    viewModel.candidates?.also { placesAdapter.addItems(it) }
                }
            }
        })
    }

    private fun onItemChosen(chosenPlace: PlaceInfo) {
        val intent = Intent().apply {
            putExtra(EXTRA_NAME, chosenPlace.name)
            putExtra(EXTRA_LAT, chosenPlace.lat)
            putExtra(EXTRA_LON, chosenPlace.lon)
        }

        targetFragment?.onActivityResult(
            SunFragment.REQUEST_CITY,
            Activity.RESULT_OK,
            intent
        )
        fragmentManager?.popBackStack()
    }
}
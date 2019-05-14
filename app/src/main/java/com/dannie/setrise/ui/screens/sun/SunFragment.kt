package com.dannie.setrise.ui.screens.sun

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dannie.setrise.R
import com.dannie.setrise.ui.other.onAnimationEnd
import com.dannie.setrise.ui.other.onAnimationStart
import com.dannie.setrise.ui.screens.base.BaseFragment
import com.dannie.setrise.ui.screens.city.CityFragment
import kotlinx.android.synthetic.main.fragment_sun.*

class SunFragment : BaseFragment<SunViewModel>() {
    override fun provideViewModel() =
        ViewModelProviders.of(this).get(SunViewModel::class.java)

    override val layoutId = R.layout.fragment_sun

    companion object {
        const val REQUEST_PERMISSION_COARSE_LOCATION = 100
        const val REQUEST_CITY = 101
    }

    override fun initUi(view: View) {
        super.initUi(view)

        btnInMyCity.setOnClickListener { refreshInfoAtUsersLocation() }
        btnInAnyCity.setOnClickListener { startCityFragment() }
    }

    override fun subscribeToObservables() {
        viewModel.screenState.observe(viewLifecycleOwner, Observer {
            when (it) {
                ScreenState.LOADING -> showLoading()
                ScreenState.INFO_LOADED -> {
                    hideLoading()
                    viewModel.sunInfo?.apply {
                        showSunInfo(sunrise, sunset, viewModel.cityAt)
                    }
                }
                ScreenState.FAILURE -> hideLoading()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!viewModel.fetchedInitial) askPermissionAndGetInfo()
    }

    private fun showSunInfo(sSunrise: String?, sSunset: String?, sAt: String?) {
        txtSunrise.text = sSunrise
        txtSunset.text = sSunset
        txtInLabel.text = getString(R.string.in_city, sAt)

        val anim = AlphaAnimation(0F, 1F).apply {
            duration = 300
            onAnimationStart {
                txtSunrise.isVisible = true
                txtSunset.isVisible = true
                sAt?.also { txtInLabel.isVisible = true }
            }
        }

        txtSunrise.startAnimation(anim)
        txtSunset.startAnimation(anim)
        sAt?.also { txtInLabel.startAnimation(anim) }
    }

    private fun refreshInfoAtUsersLocation() {
        val anim = AlphaAnimation(1F, 0F).apply {
            duration = 300
            onAnimationEnd {
                txtSunrise.isInvisible = true
                txtSunset.isInvisible = true
                txtInLabel.isInvisible = true
            }
        }

        txtSunrise.startAnimation(anim)
        txtSunset.startAnimation(anim)
        if (txtInLabel.isVisible) {
            txtInLabel.startAnimation(anim)
        }

        askPermissionAndGetInfo()
    }

    private fun askPermissionAndGetInfo() {
        context?.also {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                //need to request permission
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_PERMISSION_COARSE_LOCATION
                )
            } else {
                //permission granted
                viewModel.getSunInfoForUserLocation()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_COARSE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getSunInfoForUserLocation()
                }
            }
        }
    }

    private fun startCityFragment() {
        val cityFragment = CityFragment()
        cityFragment.setTargetFragment(this, REQUEST_CITY)
        replaceFragment(cityFragment, R.anim.appear_right, R.anim.gone_left, R.anim.appear_left, R.anim.gone_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CITY -> {
                    val placeName = data?.getStringExtra(CityFragment.EXTRA_NAME) as String
                    val placeLat = data.getDoubleExtra(CityFragment.EXTRA_LAT, 0.0)
                    val placeLon = data.getDoubleExtra(CityFragment.EXTRA_LON, 0.0)
                    viewModel.onCityChosen(placeName, placeLat, placeLon)
                }
            }
        }
    }
}
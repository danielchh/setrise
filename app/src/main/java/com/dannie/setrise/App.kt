package com.dannie.setrise

import android.app.Application
import com.dannie.setrise.logic.networking.AppExecutors
import com.dannie.setrise.logic.networking.ConnectionStateMonitor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppExecutors()
        initNetworkState()
    }

    private fun initAppExecutors() {
        AppExecutors()
    }

    /**
     * Register network state callback
     */
    private fun initNetworkState() {
        ConnectionStateMonitor(this)
    }
}
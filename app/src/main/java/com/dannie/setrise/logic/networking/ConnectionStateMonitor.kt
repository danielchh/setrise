package com.dannie.setrise.logic.networking

import android.content.Context
import android.net.*
import com.dannie.setrise.logic.other.Global

/**
 * Subscribed to network state, changes [Global.networkAvailable] variable
 */
class ConnectionStateMonitor(val context: Context) : ConnectivityManager.NetworkCallback() {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()

    init {
        Global.networkAvailable = isNetworkAvailable(context)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        Global.networkAvailable = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        Global.networkAvailable = activeNetwork?.isConnected ?: false
    }
}
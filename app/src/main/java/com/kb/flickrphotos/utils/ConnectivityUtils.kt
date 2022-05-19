package com.kb.flickrphotos.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.kb.flickrphotos.ui.activity.MainActivity

class
ConnectivityUtils {
    companion object {
        fun isNetworkAvailable(mainActivity: Context): Boolean {
            val connectivityManager =
                mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.activeNetwork
            } else {
                TODO("VERSION.SDK_INT < M")
            } // network is currently in a high power state for performing data transmission.
            Log.d("Network", "active network $network")
            network ?: return false  // return false if network is null
            val actNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false // return false if Network Capabilities is null
            return when {
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> { // check if wifi is connected
                    Log.d("Network", "wifi connected")
                    true
                }
                actNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> { // check if mobile dats is connected
                    Log.d("Network", "cellular network connected")
                    true
                }
                else -> {
                    Log.d("Network", "internet not connected")
                    false
                }
            }
        }
    }
}

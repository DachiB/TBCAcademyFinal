package com.example.tbcacademyfinal.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.example.tbcacademyfinal.domain.util.ConnectivityObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Add @Inject to constructor for @Binds
class ConnectivityObserverImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ConnectivityObserver {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // Helper function to check current status
    private fun getCurrentStatus(): ConnectivityObserver.Status {
        val activeNetwork =
            connectivityManager.activeNetwork ?: return ConnectivityObserver.Status.Lost
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            ?: return ConnectivityObserver.Status.Lost
        return if (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            ConnectivityObserver.Status.Available
        } else {
            // Consider cases like connecting but no internet validation yet
            ConnectivityObserver.Status.Unavailable // Or Losing? Unavailable seems better
        }
    }

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch { send(ConnectivityObserver.Status.Available) }
                }

                // ... other overrides (onLost, onUnavailable, onLosing) ...
                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch { send(ConnectivityObserver.Status.Lost) }
                }

                override fun onUnavailable() { // This is called when WIFI connects but has no internet access
                    super.onUnavailable()
                    launch { send(ConnectivityObserver.Status.Unavailable) }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch { send(ConnectivityObserver.Status.Losing) }
                }
            }

            launch { send(getCurrentStatus()) }

            connectivityManager.registerDefaultNetworkCallback(callback)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged() // Keep distinctUntilChanged
    }
}

//            override fun onLost(network: Network) {
//                super.onLost(network)
//            }
//
//            override fun onLosing(network: Network, maxMsToLive: Int) {
//                super.onLosing(network, maxMsToLive)
//            }
//
//            override fun onUnavailable() {
//                super.onUnavailable()
//            }




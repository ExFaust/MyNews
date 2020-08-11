package com.exfaust.mynews.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import io.reactivex.Single

class NetHelper(private val context: Context) {

    private val secInHour = 3600

    private val isConnected: Boolean
        get() {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    val networkInfo=connectivityManager.activeNetworkInfo
                    return  networkInfo!=null && networkInfo.isConnected
                }
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
            return false
        }

    fun checkConnection(): Single<Boolean> {
        return Single.just(isConnected)
            .flatMap<Boolean> { isConnected: Boolean ->
                if (isConnected) {
                    return@flatMap Single.just(true)
                } else {
                    return@flatMap Single.error { NoConnectivityException() }
                }
            }
    }

    @Throws(NoConnectivityException::class)
    fun isNeedUpdate(timestamp: Long): Boolean {
        val timeout = secInHour
        return if (timeout > 0 && isConnected) {
            //timeout > 0 если инет есть
            val currentTimestamp = System.currentTimeMillis() / 1000

            //Если ранее на было сохранения данных или прошёл таймаут
            timestamp == -1L || currentTimestamp - timestamp > timeout
        } else {
            throw NoConnectivityException()
        }
    }
}
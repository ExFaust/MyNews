package com.exfaust.mynews.utils

import android.content.SharedPreferences
import javax.inject.Inject

class UpdateManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :UpdateManager {

    private val newsUpdateTimestampPref = "NEWS_UPDATE_TIMESTAMP_PREF"

    fun setNewsUpdateTimestamp(timestamp: Long){
        val editor = sharedPreferences.edit()
        editor.putLong(newsUpdateTimestampPref, timestamp)
        editor.apply()
    }

    override val newsUpdateTimestamp: Long
        get() = sharedPreferences.getLong(newsUpdateTimestampPref, -1)
}
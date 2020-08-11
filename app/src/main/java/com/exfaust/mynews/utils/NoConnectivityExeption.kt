package com.exfaust.mynews.utils

import java.io.IOException

class NoConnectivityException : IOException() {

    override fun getLocalizedMessage(): String {
        return "No connectivity exception"
    }

    override val message: String?
        get() = "No connectivity exception"
}
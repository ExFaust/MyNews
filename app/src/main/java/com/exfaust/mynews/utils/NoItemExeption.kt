package com.exfaust.testforskyeng.utils

import java.io.IOException

class NoItemException : IOException() {

    override fun getLocalizedMessage(): String {
        return "No item exception"
    }
}
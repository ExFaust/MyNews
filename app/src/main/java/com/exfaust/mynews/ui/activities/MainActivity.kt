package com.exfaust.mynews.ui.activities

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.exfaust.mynews.R


class MainActivity : MvpAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
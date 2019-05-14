package com.dannie.setrise.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dannie.setrise.R
import com.dannie.setrise.ui.screens.sun.SunFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            initStartFragment()
        }
    }

    private fun initStartFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, SunFragment(), SunFragment::class.java.simpleName)
            .commit()
    }

    fun showLoading() {
        progressBar.isVisible = true
    }

    fun hideLoading() {
        progressBar.isVisible = false
    }
}

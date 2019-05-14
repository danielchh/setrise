package com.dannie.setrise.ui.screens.base

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.dannie.setrise.App
import com.dannie.setrise.R
import com.dannie.setrise.logic.other.Global

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    protected val app: App by lazy { getApplication<App>() }
    protected val context: Context by lazy { app.applicationContext }

    open fun onViewCreated() {}

    open fun onResume() {}

    open fun onPause() {}

    protected fun showWarning(@StringRes textId: Int) {
        Toast.makeText(context, textId, Toast.LENGTH_SHORT).show()
    }

    protected fun showWarning(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    protected fun checkNetworkAndCall(callFun: () -> Unit) {
        if (Global.networkAvailable) {
            callFun()
        } else {
            showWarning(R.string.no_connection)
        }
    }
}
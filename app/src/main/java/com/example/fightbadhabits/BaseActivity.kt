package com.example.fightbadhabits

import androidx.activity.ComponentActivity
import com.google.android.material.snackbar.Snackbar
import android.view.View

open class BaseActivity : ComponentActivity() {

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val view = findViewById<View>(android.R.id.content)
        if (view != null) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val snackbarView = snackbar.view

            if (errorMessage) {
                snackbarView.setBackgroundColor(android.graphics.Color.parseColor("#B71C1C"))
            } else {
                snackbarView.setBackgroundColor(android.graphics.Color.parseColor("#2E7D32"))
            }
            snackbar.setTextColor(android.graphics.Color.WHITE)
            snackbar.show()
        }
    }
}
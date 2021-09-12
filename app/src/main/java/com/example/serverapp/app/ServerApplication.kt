package com.example.serverapp.app

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ServerApplication : Application() {

    companion object {

        val TAG: String = ServerApplication::class.java.simpleName

        /**
         * @param context An Activity or Application Context.
         * @param stringRes A string resource that to be displayed inside a Toast.
         */
        fun showToast(context: Context, @StringRes stringRes: Int) {
            Toast.makeText(context, stringRes, Toast.LENGTH_LONG).show()
        }

        /**
         * Logs messages for Debugging Purposes.
         *
         * @param tag     TAG is a class name in which the log come from.
         * @param message Type of a Log Message.
         */
        fun printLog(tag: String, message: String) {
            Log.d(tag, message)
        }

        /**
         * Logs messages for Debugging Purposes.
         *
         * @param tag     TAG is a class name in which the log come from.
         * @param message Type of a Log Error Message.
         */
        fun printError(tag: String, message: String) {
            Log.e(TAG, message)
        }
    }
}

package com.example.serverapp.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.serverapp.server.workermanager.CovidWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ServerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workManager: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workManager).build()

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate: ")
        createNotificationChannel()
        setupHandleInformationCovid()
    }

    private fun setupHandleInformationCovid() {
        Log.e(TAG, "setupHandleInformationCovid: ")
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val repeatingRequest =
            PeriodicWorkRequestBuilder<CovidWorker>(1, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(applicationContext).enqueue(repeatingRequest)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, NAME, IMPORTANCE).apply {
                this.description = DESCRIPTION_TEXT
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {

        val TAG: String = ServerApplication::class.java.simpleName
        const val CHANNEL_ID = "Server"
        const val NAME = "Server Service"
        const val DESCRIPTION_TEXT = "running..."
        const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

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

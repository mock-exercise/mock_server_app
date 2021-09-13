package com.example.serverapp.app

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.serverapp.workermanager.CovidWorker
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
        Log.e(TAG, "onCreate: application")

        setupHandleInformationCovid()

    }

    private fun setupHandleInformationCovid() {
        Log.e(TAG, "setupHandleInformationCovid: ")
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()
        val repeatingRequest =
            PeriodicWorkRequestBuilder<CovidWorker>(6, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
        val oneRequest =
            OneTimeWorkRequestBuilder<CovidWorker>().setConstraints(constraints).build()
        val one = OneTimeWorkRequest.from(CovidWorker::class.java)
        WorkManager.getInstance(applicationContext).enqueue(one)
        /*
          WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
              CovidWorker.WORK_NAME,
              ExistingPeriodicWorkPolicy.KEEP,
              repeatingRequest
          )*/
    }

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

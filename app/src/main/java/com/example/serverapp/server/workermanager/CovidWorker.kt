package com.example.serverapp.server.workermanager

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.connectorlibrary.enitity.HistoryCovid
import com.example.connectorlibrary.enitity.PeopleInDay
import com.example.serverapp.app.ServerApplication
import com.example.serverapp.server.data.repository.ServiceRepository
import com.example.serverapp.server.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat

@HiltWorker
class CovidWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: ServiceRepository
) : Worker(context, params) {

    private val coroutineDefault = CoroutineScope(Dispatchers.Default)

    override fun doWork(): Result {
        Log.e(TAG, "doWork: ")
        try {
            handleHistoryCovidVn()
            handleHistoryCovidWorld()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
        return Result.success()
    }

    private fun handleHistoryCovidVn() = coroutineDefault.launch {
        when (val historyCovid = repository.getHistoryCovidVn()) {
            is Resource.Success -> {
                val jsonObject = JSONObject(historyCovid.value.body()!!.string())
                val historyJson = jsonObject.getJSONObject("timeline")
                handleSuccessHistoryCovid(historyJson, "vn")
            }
            is Resource.Failure -> {
                handleFailure(historyCovid)
                Result.retry()
            }
        }

    }

    private fun handleHistoryCovidWorld() = coroutineDefault.launch {
        when (val historyCovid = repository.getHistoryCovidWorld()) {
            is Resource.Success -> {
                handleSuccessHistoryCovid(JSONObject(historyCovid.value.body()!!.string()), "all")
            }
            is Resource.Failure -> {
                handleFailure(historyCovid)
                Result.retry()
            }
        }
    }

    private suspend fun handleSuccessHistoryCovid(jsonObject: JSONObject, area: String) {
        val listHistory = arrayListOf<HistoryCovid>()
        val keySets = jsonObject.keys()
        while (keySets.hasNext()) {
            val key_a = keySets.next()
            if (key_a == "cases") {
                val list =
                    convertJsonObjectToList(jsonObject.getJSONObject(key_a))
                listHistory.add(HistoryCovid(area = area, status = 1, listPeopleInDay = list))
            }
            if (key_a == "deaths") {
                val list =
                    convertJsonObjectToList(jsonObject.getJSONObject(key_a))
                listHistory.add(HistoryCovid(area = area, status = 2, listPeopleInDay = list))
            }
            if (key_a == "recovered") {
                val list =
                    convertJsonObjectToList(jsonObject.getJSONObject(key_a))
                listHistory.add(HistoryCovid(area = area, status = 3, listPeopleInDay = list))
            }
        }
        if (listHistory.isNotEmpty()) {
            if (area == "vn") {
                repository.deleteHistoryCovidVn()
            } else if (area == "all") repository.deleteHistoryCovidWorld()
            repository.insertHistoryCovid(*listHistory.toTypedArray())
        } else Result.retry()
    }

    private fun handleFailure(failure: Resource.Failure) {
        if (failure.isNetworkError)
            ServerApplication.printError(TAG, "Error network...")
        if (failure.errorBody != null)
            ServerApplication.printError(TAG, "Error body: ${failure.errorBody}")
        if (failure.errorCode != null) ServerApplication.printError(
            TAG,
            "Error code: ${failure.errorCode}"
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertJsonObjectToList(jsonObject: JSONObject): List<PeopleInDay> {
        val listPeopleInDay = arrayListOf<PeopleInDay>()
        val keySets = jsonObject.keys()
        while (keySets.hasNext()) {
            val key = keySets.next()
            val date = SimpleDateFormat("MM/dd/yy").parse(key).time
            listPeopleInDay.add(PeopleInDay(date, jsonObject.getInt(key)))
        }
        return listPeopleInDay
    }

    companion object {
        val TAG: String = CovidWorker::class.java.name
    }
}
package com.example.serverapp.workermanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.connectorlibrary.enitity.HistoryCovid
import com.example.connectorlibrary.enitity.PeopleInDay
import com.example.serverapp.app.ServerApplication
import com.example.serverapp.model.repository.ServiceRepository
import com.example.serverapp.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import javax.inject.Inject

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
            coroutineDefault.launch {
                handleStatisticCovidVn()
                handleStatisticCovidWorld()
                handleHistoryCovid()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
        return Result.success()
    }

    private suspend fun handleStatisticCovidVn() {
        when (val statisticCovid = repository.getStatisticCovidVn()) {
            is Resource.Success ->{
                Log.e(TAG, "handleStatisticCovidVn: $statisticCovid.value", )
                repository.insertStatisticCovidVn(statisticCovid.value)
            }
            is Resource.Failure -> {
                if (statisticCovid.isNetworkError)
                    ServerApplication.printError(TAG, "Error network...")
                if (statisticCovid.errorBody != null)
                    ServerApplication.printError(
                        TAG,
                        "Error body: ${statisticCovid.errorBody}"
                    )
                if (statisticCovid.errorCode != null) ServerApplication.printError(
                    TAG,
                    "Error code: ${statisticCovid.errorCode}"
                )
                Result.retry()
            }
        }
    }

    private suspend fun handleStatisticCovidWorld() {
        when (val statisticCovid = repository.getStatisticCovidWorld()) {
            is Resource.Success ->
                repository.insertStatisticCovidWorld(statisticCovid.value)
            is Resource.Failure -> {
                if (statisticCovid.isNetworkError)
                    ServerApplication.printError(TAG, "Error network...")
                if (statisticCovid.errorBody != null)
                    ServerApplication.printError(
                        TAG,
                        "Error body: ${statisticCovid.errorBody}"
                    )
                if (statisticCovid.errorCode != null) ServerApplication.printError(
                    TAG,
                    "Error code: ${statisticCovid.errorCode}"
                )
                Result.retry()
            }
        }
    }

    private suspend fun handleHistoryCovid() {
        val listHistory = arrayListOf<HistoryCovid>()
        when (val historyCovid = repository.getHistoryCovidVn()) {
            is Resource.Success -> {
                val body = historyCovid.value.body()!!.toString()
                Log.e(TAG, "handleHistoryCovid: $body.", )
               /* val jsonObject = JSONObject(body)
                val cases = jsonObject.getJSONObject("timeline").getJSONObject("cases")
                val listCases = convertJsonObjectToList(cases)
                listHistory.add(HistoryCovid(area = "vn", status = 1, listPeopleInDay = listCases))
                val deaths = jsonObject.getJSONObject("timeline").getJSONObject("deaths")
                val listDeaths = convertJsonObjectToList(deaths)
                listHistory.add(HistoryCovid(area = "vn", status = 2, listPeopleInDay = listDeaths))
                val recovered = jsonObject.getJSONObject("timeline").getJSONObject("recovered")
                val listRecovered = convertJsonObjectToList(recovered)
                listHistory.add(
                    HistoryCovid(
                        area = "vn",
                        status = 3,
                        listPeopleInDay = listRecovered
                    )
                )*/

            }
            is Resource.Failure -> {
                if (historyCovid.isNetworkError)
                    ServerApplication.printError(TAG, "Error network...")
                if (historyCovid.errorBody != null)
                    ServerApplication.printError(TAG, "Error body: ${historyCovid.errorBody}")
                if (historyCovid.errorCode != null) ServerApplication.printError(
                    TAG,
                    "Error code: ${historyCovid.errorCode}"
                )
                Result.retry()
            }
        }
    /*    when (val historyCovid = repository.getHistoryCovidWorld()) {
            is Resource.Success -> {
                val body = historyCovid.value.body()!!.toString()
                val jsonObject = JSONObject(body)
                val cases = jsonObject.getJSONObject("cases")
                val listCases = convertJsonObjectToList(cases)
                listHistory.add(HistoryCovid(status = 1, listPeopleInDay = listCases))
                val deaths = jsonObject.getJSONObject("deaths")
                val listDeaths = convertJsonObjectToList(deaths)
                listHistory.add(HistoryCovid(status = 2, listPeopleInDay = listDeaths))
                val recovered = jsonObject.getJSONObject("recovered")
                val listRecovered = convertJsonObjectToList(recovered)
                listHistory.add(
                    HistoryCovid(
                        status = 3,
                        listPeopleInDay = listRecovered
                    )
                )

            }
            is Resource.Failure -> {
                if (historyCovid.isNetworkError)
                    ServerApplication.printError(TAG, "Error network...")
                if (historyCovid.errorBody != null)
                    ServerApplication.printError(TAG, "Error body: ${historyCovid.errorBody}")
                if (historyCovid.errorCode != null) ServerApplication.printError(
                    TAG,
                    "Error code: ${historyCovid.errorCode}"
                )
                Result.retry()
            }
        }*/
        if (listHistory.isNotEmpty()) {
            repository.deleteHistoryCovid()
            repository.insertHistoryCovid(listHistory)
            Log.e(TAG, "handleHistoryCovid: $listHistory")
        } else {
            Result.retry()
        }
    }

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
        const val WORK_NAME = "com.example.serverapp.workermanager.CovidWorker"
    }
}
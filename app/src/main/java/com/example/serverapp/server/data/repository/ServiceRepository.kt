package com.example.serverapp.server.data.repository

import com.example.connectorlibrary.enitity.HistoryCovid
import com.example.serverapp.base.BaseRepository
import com.example.serverapp.server.data.local.dao.IHistoryCovidDao
import com.example.serverapp.server.data.remote.serviceapi.IServiceCovid
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val iServiceCovid: IServiceCovid,
    private val historyCovidDao: IHistoryCovidDao
) :
    BaseRepository() {

    suspend fun getHistoryCovidVn() = safeApiCall {
        iServiceCovid.getHistoryCovidVn()
    }

    suspend fun getHistoryCovidWorld() = safeApiCall {
        iServiceCovid.getHistoryCovidWorld()
    }

    suspend fun insertHistoryCovid(vararg listHistoryCovid: HistoryCovid) = safeApiCall {
        historyCovidDao.insertHistoryCovid(*listHistoryCovid)
    }

    suspend fun deleteHistoryCovidVn() = safeApiCall {
        historyCovidDao.deleteHistoryCovidVn()
    }

    suspend fun deleteHistoryCovidWorld() = safeApiCall {
        historyCovidDao.deleteHistoryCovidWorld()
    }

    suspend fun getHistory() = safeApiCall {
        historyCovidDao.getHistoryCovidOfVn()
    }
}
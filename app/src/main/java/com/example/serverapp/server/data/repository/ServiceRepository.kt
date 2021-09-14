package com.example.serverapp.server.data.repository

import com.example.connectorlibrary.enitity.HistoryCovid
import com.example.connectorlibrary.enitity.StatisticCovidVn
import com.example.connectorlibrary.enitity.StatisticCovidWorld
import com.example.serverapp.base.BaseRepository
import com.example.serverapp.server.data.local.dao.IHistoryCovidDao
import com.example.serverapp.server.data.local.dao.IStatisticCovidDao
import com.example.serverapp.server.data.remote.serviceapi.IServiceCovid
import javax.inject.Inject

class ServiceRepository @Inject constructor(
    private val iServiceCovid: IServiceCovid,
    private val historyCovidDao: IHistoryCovidDao,
    private val statisticDao: IStatisticCovidDao
) :
    BaseRepository() {

    suspend fun getStatisticCovidVn() = safeApiCall {
        iServiceCovid.getStatisticCovidVn()
    }

    suspend fun getStatisticCovidWorld() = safeApiCall {
        iServiceCovid.getStatisticCovidWorld()
    }

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

    suspend fun insertStatisticCovidVn(list: StatisticCovidVn) = safeApiCall {
        statisticDao.insertStatisticCovidVn(list)
    }

    suspend fun insertStatisticCovidWorld(list: StatisticCovidWorld) = safeApiCall {
        statisticDao.insertStatisticCovidWorld(list)
    }

    suspend fun getHistory() = safeApiCall {
        historyCovidDao.getHistoryCovidOfVn()
    }
}
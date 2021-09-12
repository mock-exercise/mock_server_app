package com.example.serverapp.model.repository

import com.example.connectorlibrary.enitity.HistoryCovid
import com.example.connectorlibrary.enitity.StatisticCovidVn
import com.example.connectorlibrary.enitity.StatisticCovidWorld
import com.example.serverapp.base.BaseRepository
import com.example.serverapp.model.dao.IHistoryCovidDao
import com.example.serverapp.model.dao.IStatisticCovidDao
import com.example.serverapp.model.serviceapi.IServiceCovid
import javax.inject.Inject
import javax.inject.Singleton

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

    suspend fun insertHistoryCovid(listHistoryCovid: List<HistoryCovid>) = safeApiCall {
        historyCovidDao.insertHistoryCovid(listHistoryCovid)
    }

    suspend fun deleteHistoryCovid() = safeApiCall {
        historyCovidDao.deleteHistoryCovid()
    }

    suspend fun insertStatisticCovidVn(list: StatisticCovidVn) = safeApiCall {
        statisticDao.insertStatisticCovidVn(list)
    }

    suspend fun insertStatisticCovidWorld(list: StatisticCovidWorld) = safeApiCall {
        statisticDao.insertStatisticCovidWorld(list)
    }

}
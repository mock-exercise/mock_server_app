package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.HistoryCovid

@Dao
interface IHistoryCovidDao {

    @Query("SELECT * FROM history_covid WHERE area = 'all' ")
    fun getHistoryCovidOfWorld(): List<HistoryCovid>?

    @Query("SELECT * FROM history_covid WHERE area = 'vn'")
    fun getHistoryCovidOfVn(): List<HistoryCovid>?

    @Insert
    fun insertHistoryCovid(listHistoryCovid: List<HistoryCovid>): List<Long>

    @Query("DELETE FROM history_covid")
    fun deleteHistoryCovid(): Int
}

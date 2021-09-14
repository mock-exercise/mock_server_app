package com.example.serverapp.model.server.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.connectorlibrary.enitity.HistoryCovid

@Dao
interface IHistoryCovidDao {

    @Query("SELECT * FROM history_covid WHERE area = :area ")
    fun getHistoryCovidOfWorld(area: String = "all"): List<HistoryCovid>?

    @Query("SELECT * FROM history_covid WHERE area = :area")
    fun getHistoryCovidOfVn(area: String = "vn"): List<HistoryCovid>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistoryCovid(vararg listHistoryCovid:HistoryCovid): List<Long>

    @Query("DELETE FROM history_covid WHERE area = :area")
    fun deleteHistoryCovidVn(area: String = "vn"): Int

    @Query("DELETE FROM history_covid WHERE area = :area ")
    fun deleteHistoryCovidWorld(area: String = "all"): Int
}

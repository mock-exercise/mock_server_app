package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.StatisticCovid

@Dao
interface IStatisticCovidDao {

    @Query("SELECT * FROM statistic_covid")
    fun getStatisticCovid(): List<StatisticCovid>?

    @Insert
    fun insertStatisticCovid(list: List<StatisticCovid>): List<Long>
}
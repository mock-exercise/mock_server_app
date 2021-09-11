package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.StatisticCovidVn

@Dao
interface IStatisticCovidDao {

    @Query("SELECT * FROM statistic_covid_vn")
    fun getStatisticCovid(): List<StatisticCovidVn>?

    @Insert
    fun insertStatisticCovid(list: List<StatisticCovidVn>): List<Long>
}
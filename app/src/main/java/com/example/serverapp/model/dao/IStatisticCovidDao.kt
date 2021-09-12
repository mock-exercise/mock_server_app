package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.StatisticCovidVn
import com.example.connectorlibrary.enitity.StatisticCovidWorld

@Dao
interface IStatisticCovidDao {

    @Query("SELECT * FROM statistic_covid_vn ORDER BY statistic_id DESC LIMIT 1")
    fun getStatisticCovidVn(): StatisticCovidVn?

    @Insert
    fun insertStatisticCovidVn(list: StatisticCovidVn): Long

    @Query("SELECT * FROM statistic_covid_world ORDER BY statistic_id DESC LIMIT 1")
    fun getStatisticCovidWorld(): StatisticCovidWorld?

    @Insert
    fun insertStatisticCovidWorld(list:StatisticCovidWorld): Long
}

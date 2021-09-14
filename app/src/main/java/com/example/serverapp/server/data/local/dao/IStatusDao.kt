package com.example.serverapp.server.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.Status

@Dao
interface IStatusDao {

    @Query("SELECT * FROM status")
    fun getStatus(): List<Status>?

    @Insert
    fun insertListStatus(listStatus: List<Status>): List<Long>
}

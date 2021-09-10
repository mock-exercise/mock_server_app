package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.connectorlibrary.enitity.Status

@Dao
interface IStatusDao {

    @Query("SELECT * FROM status")
    fun getStatus(): List<Status>
}
package com.example.serverapp.model.server.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.Health

@Dao
interface IUserHealthDao {

    @Insert
    fun insertUserHealth(health: Health): Long

    @Query("SELECT * FROM health")
    fun getUserHealths(): List<Health>?
}

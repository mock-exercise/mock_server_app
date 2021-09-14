package com.example.serverapp.model.server.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.Active

@Dao
interface IActiveDao {

    @Query("SELECT * from active")
    fun getActives(): List<Active>?

    @Insert
    fun insertActivies(listActivies: List<Active>): List<Long>
}

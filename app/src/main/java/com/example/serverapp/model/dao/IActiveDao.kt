package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.connectorlibrary.enitity.Active

@Dao
interface IActiveDao {

    @Query("SELECT * from active")
    fun getActives(): List<Active>
}
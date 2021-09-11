package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.Gender

@Dao
interface IGenderDao {

    @Query("SELECT * FROM gender")
    fun getGender() : List<Gender>?

    @Insert
    fun insertGender(listGender: List<Gender>): List<Long>
}
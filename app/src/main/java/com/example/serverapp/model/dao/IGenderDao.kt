package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.connectorlibrary.enitity.Gender

@Dao
interface IGenderDao {

    @Query("SELECT * FROM gender")
    fun getGender() : List<Gender>
}
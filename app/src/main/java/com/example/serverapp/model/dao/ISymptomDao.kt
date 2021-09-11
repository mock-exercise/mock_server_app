package com.example.serverapp.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.connectorlibrary.enitity.Symptom

@Dao
interface ISymptomDao {

    @Query("SELECT * FROM symptom")
    fun getSymptoms(): List<Symptom>
}
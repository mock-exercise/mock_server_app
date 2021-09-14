package com.example.serverapp.model.server.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.connectorlibrary.enitity.Symptom

@Dao
interface ISymptomDao {

    @Query("SELECT * FROM symptom")
    fun getSymptoms(): List<Symptom>?

    @Insert
    fun insertSymptoms(listSymptom: List<Symptom>): List<Long>
}

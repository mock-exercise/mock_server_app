package com.example.serverapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.connectorlibrary.enitity.*
import com.example.serverapp.model.dao.*

@Database(
    entities = [User::class, Health::class, Gender::class, Active::class, StatisticCovid::class, Status::class, Symptom::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun getActiveDao(): IActiveDao

    abstract fun getGenderDao(): IGenderDao

    abstract fun getStatisticCovidDao(): IStatisticCovidDao

    abstract fun getStatusDao(): IStatusDao

    abstract fun getSymptomDao(): ISymptomDao

    abstract fun getUserDao(): IUserDao

    abstract fun getUserHealthDao(): IUserHealthDao
}
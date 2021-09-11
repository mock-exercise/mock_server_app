package com.example.serverapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.connectorlibrary.enitity.*
import com.example.serverapp.di.ApplicationScope
import com.example.serverapp.model.dao.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [User::class, Health::class, Gender::class, Active::class, StatisticCovidVn::class, Status::class, Symptom::class],
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

    class Callback @Inject constructor(
        private val database: Provider<ApplicationDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : androidx.room.RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val activeDao = database.get().getActiveDao()
            val statusDao = database.get().getStatusDao()
            val genderDao = database.get().getGenderDao()
            val symptomDao = database.get().getSymptomDao()
            applicationScope.launch {
                activeDao.insertActivies(
                    listOf(
                        Active(active_name = "Hoạt động"),
                        Active(active_name = "Đã khóa")
                    )
                )
                statusDao.insertListStatus(
                    listOf(
                        Status(status_name = "An toàn"),
                        Status(status_name = "Nguy cơ nhiễm bệnh"),
                    )
                )
                genderDao.insertGender(
                    listOf(
                        Gender(gender_name = "Nữ"),
                        Gender(gender_name = "Nam"),
                        Gender(gender_name = "Giới tính khác")
                    )
                )
                symptomDao.insertSymptoms(
                    listOf(
                        Symptom(symptom_name = "Sốt"),
                        Symptom(symptom_name = "Ho"),
                        Symptom(symptom_name = "Khó thở"),
                        Symptom(symptom_name = "Đau người, mệt mỏi"),
                        Symptom(symptom_name = "Sức khỏe tốt")
                    )
                )
            }
        }
    }
}
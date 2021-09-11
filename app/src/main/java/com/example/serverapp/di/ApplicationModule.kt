package com.example.serverapp.di

import android.content.Context
import android.os.RemoteCallbackList
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.connectorlibrary.controller.IServerServiceCallback
import com.example.connectorlibrary.enitity.*
import com.example.serverapp.model.dao.*
import com.example.serverapp.model.database.ApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun providesApplicationDatabase(@ApplicationContext context: Context, callback: ApplicationDatabase.Callback): ApplicationDatabase =
        Room.databaseBuilder(context, ApplicationDatabase::class.java, "ManagementCovid.db")
            .fallbackToDestructiveMigration().addCallback(callback).build()

    @Provides
    @Singleton
    fun providesActiveDao(applicationDatabase: ApplicationDatabase): IActiveDao =
        applicationDatabase.getActiveDao()

    @Provides
    @Singleton
    fun providesGenderDao(applicationDatabase: ApplicationDatabase): IGenderDao =
        applicationDatabase.getGenderDao()

    @Provides
    @Singleton
    fun providesStatisticDao(applicationDatabase: ApplicationDatabase): IStatisticCovidDao =
        applicationDatabase.getStatisticCovidDao()

    @Provides
    @Singleton
    fun providesStatusDao(applicationDatabase: ApplicationDatabase): IStatusDao =
        applicationDatabase.getStatusDao()

    @Provides
    @Singleton
    fun providesSymptomDao(applicationDatabase: ApplicationDatabase): ISymptomDao =
        applicationDatabase.getSymptomDao()

    @Provides
    @Singleton
    fun providesUserDao(applicationDatabase: ApplicationDatabase): IUserDao =
        applicationDatabase.getUserDao()

    @Provides
    @Singleton
    fun providesUserHealthDao(applicationDatabase: ApplicationDatabase): IUserHealthDao =
        applicationDatabase.getUserHealthDao()

    @CoroutineScopeIO
    @Provides
    @Singleton
    fun providesCoroutineScope() = CoroutineScope(Dispatchers.IO)

    @Provides
    @Singleton
    fun providesStudentCallback(): RemoteCallbackList<IServerServiceCallback> =
        RemoteCallbackList()

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())

}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CoroutineScopeIO
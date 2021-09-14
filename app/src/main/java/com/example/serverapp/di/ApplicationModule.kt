package com.example.serverapp.di

import android.content.Context
import androidx.room.Room
import com.example.serverapp.di.qualifiers.ApplicationScope
import com.example.serverapp.di.qualifiers.CoroutineScopeIO
import com.example.serverapp.model.server.dao.*
import com.example.serverapp.model.server.database.ApplicationDatabase
import com.example.serverapp.model.server.repository.ServiceRepository
import com.example.serverapp.model.server.serviceapi.IServiceCovid
import com.example.serverapp.utils.Constrants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun providesApplicationDatabase(
        @ApplicationContext context: Context,
        callback: ApplicationDatabase.Callback
    ): ApplicationDatabase =
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
    fun providesHistoryDao(applicationDatabase: ApplicationDatabase): IHistoryCovidDao =
        applicationDatabase.getHistoryCovidDao()

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

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): IServiceCovid =
        retrofit.create(IServiceCovid::class.java)

    @Provides
    @Singleton
    fun providesServiceRepository(
        iServiceCovid: IServiceCovid,
        historyCovidDao: IHistoryCovidDao,
        statisticDao: IStatisticCovidDao
    ): ServiceRepository = ServiceRepository(iServiceCovid, historyCovidDao, statisticDao)

    @CoroutineScopeIO
    @Provides
    @Singleton
    fun providesCoroutineScope() = CoroutineScope(Dispatchers.IO)

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())

}

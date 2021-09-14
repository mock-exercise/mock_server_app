package com.example.serverapp.model.server.serviceapi

import com.example.connectorlibrary.enitity.StatisticCovidVn
import com.example.connectorlibrary.enitity.StatisticCovidWorld
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IServiceCovid {

    @GET("countries/{country}")
    suspend fun getStatisticCovidVn(@Path("country") country: String ="vn") :StatisticCovidVn

    @GET("all")
    suspend fun getStatisticCovidWorld(): StatisticCovidWorld

    @GET("historical/{country}")
    suspend fun getHistoryCovidVn(
        @Path("country") country: String = "Vietnam",
        @Query("lastdays") number: Int = 30
    ): retrofit2.Response<ResponseBody>

    @GET("historical/all")
    suspend fun getHistoryCovidWorld(
        @Query("lastdays") number: Int = 30
    ): retrofit2.Response<ResponseBody>
}
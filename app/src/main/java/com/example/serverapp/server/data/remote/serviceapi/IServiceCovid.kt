package com.example.serverapp.server.data.remote.serviceapi

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IServiceCovid {

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
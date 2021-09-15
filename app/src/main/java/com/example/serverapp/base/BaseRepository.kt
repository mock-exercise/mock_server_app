package com.example.serverapp.base

import android.os.RemoteException
import com.example.serverapp.server.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository() {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            false,
                            throwable.code(),
                            throwable.response()?.errorBody(),
                            null
                        )
                    }
                    is RemoteException -> {
                        Resource.Failure(false, null, null, throwable.message)
                    }
                    else -> {
                        Resource.Failure(true, null, null, null)
                    }
                }
            }
        }

}
package com.example.serverapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import com.example.connectorlibrary.controller.IServerService
import com.example.connectorlibrary.controller.IServerServiceCallback
import com.example.connectorlibrary.enitity.*
import com.example.serverapp.R
import com.example.serverapp.app.ServerApplication
import com.example.serverapp.model.dao.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ServerService : Service() {

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var serviceCallbacks: RemoteCallbackList<IServerServiceCallback>

    @Inject
    lateinit var activeDao: IActiveDao

    @Inject
    lateinit var genderDao: IGenderDao

    @Inject
    lateinit var statisticCovidDao: IStatisticCovidDao

    @Inject
    lateinit var statusDao: IStatusDao

    @Inject
    lateinit var symptomDao: ISymptomDao

    @Inject
    lateinit var userDao: IUserDao

    @Inject
    lateinit var userHealthDao: IUserHealthDao

    override fun onBind(intent: Intent?): IBinder? {
        if (intent == null) return null
        if (intent.action == getString(R.string.action_server_service)) {
            return RemoteBinder()
        }
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceCallbacks.kill()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ServerApplication.printLog(TAG, "onLowMemory")
    }

    inner class RemoteBinder() : IServerService.Stub() {

        override fun registerCallback(callback: IServerServiceCallback?) {
            if (callback != null) {
                serviceCallbacks.register(callback, this@RemoteBinder)
            }
        }

        override fun unregisterCallback(callback: IServerServiceCallback?) {
            if (callback != null) serviceCallbacks.unregister(callback)
        }

        override fun userSignUp(user: User) {
            ServerApplication.printLog(TAG, "Server service is proccessing sign up ...")
            scope.launch {
                val resultId = userDao.userSignUp(user)
                if (resultId <= -1) {
                    postFailureResponse(RequestCode.SIGN_UP_REQ, ResponseCode.ERROR_SIGN_UP)
                    return@launch
                } else {
                    val resultUser = userDao.getUser(resultId.toInt())
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onUserSignUp(
                            AuthResponse(
                                RequestCode.SIGN_UP_REQ,
                                ResponseCode.OK,
                                resultId.toInt(),
                                resultUser.name
                            )
                        )
                    }
                }
            }
        }

        override fun userSignIn(phone_number: String) {
            ServerApplication.printLog(TAG, "Server service is proccessing sign in ...")
            scope.launch {
                val isUserExists = userDao.userSignIn(phone_number)
                if (!isUserExists) {
                    postFailureResponse(RequestCode.SIGN_IN_REQ, ResponseCode.ERROR_SIGN_IN)
                } else {
                    val userByPhone = userDao.getUserByPhone(phone_number)
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onUserSignIn(
                            AuthResponse(
                                RequestCode.SIGN_IN_REQ,
                                ResponseCode.OK,
                                userByPhone.user_id,
                                userByPhone.name
                            )
                        )
                    }
                }
            }
        }

        override fun insertHealth(health: Health) {
            
        }

        override fun getUserHealths() {
            TODO("Not yet implemented")
        }

        override fun getUser(user_id: Int) {
            TODO("Not yet implemented")
        }

        override fun getAllUsers() {
            TODO("Not yet implemented")
        }

        override fun updateUser(user: User?) {
            TODO("Not yet implemented")
        }

        override fun deleteUser(user: User?) {
            TODO("Not yet implemented")
        }

        override fun lockUser(user: User?) {
            TODO("Not yet implemented")
        }

        override fun getStatus() {
            TODO("Not yet implemented")
        }

        override fun getStatisticCovid() {
            TODO("Not yet implemented")
        }

        override fun getSymptom() {
            TODO("Not yet implemented")
        }

        override fun getActive() {
            TODO("Not yet implemented")
        }

        override fun getGender() {
            TODO("Not yet implemented")
        }

        private fun postFailureResponse(
            @RequestCode requestCode: Int,
            @ResponseCode responseCode: Int
        ) {
            remoteBroadcast { index ->
                serviceCallbacks.getBroadcastItem(index).onFailureResponse(
                    FailureResponse(requestCode, responseCode)
                )
            }
        }

        private fun remoteBroadcast(block: (Int) -> Unit) {
            val count = serviceCallbacks.beginBroadcast()
            for (index in 0 until count) {
                if (serviceCallbacks.getBroadcastCookie(index) == this@RemoteBinder) {
                    block.invoke(index)
                    break
                }
            }
            serviceCallbacks.finishBroadcast()
        }

    }

    companion object {
        val TAG: String = ServerService::class.java.name
    }
}
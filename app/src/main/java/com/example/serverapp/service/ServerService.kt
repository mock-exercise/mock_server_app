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
import com.example.serverapp.di.qualifiers.CoroutineScopeIO
import com.example.serverapp.model.dao.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ServerService : Service() {

    @CoroutineScopeIO
    @Inject
    lateinit var scope: CoroutineScope

    private val serviceCallbacks = RemoteCallbackList<IServerServiceCallback>()

    @Inject
    lateinit var activeDao: IActiveDao

    @Inject
    lateinit var genderDao: IGenderDao

    @Inject
    lateinit var statisticCovidDao: IStatisticCovidDao

    @Inject
    lateinit var historyCovidDao: IHistoryCovidDao

    @Inject
    lateinit var statusDao: IStatusDao

    @Inject
    lateinit var symptomDao: ISymptomDao

    @Inject
    lateinit var userDao: IUserDao

    @Inject
    lateinit var userHealthDao: IUserHealthDao

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind: 1")
        if (intent == null) return null
        if (intent.action == getString(R.string.action_server_service)) {
            Log.e(TAG, "onBind: action")
            return RemoteBinder()
        }
        Log.e(TAG, "onBind: 2")
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind: ")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ")
        serviceCallbacks.kill()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ServerApplication.printLog(TAG, "onLowMemory")
    }

    inner class RemoteBinder : IServerService.Stub() {

        override fun registerCallback(callback: IServerServiceCallback?) {
            Log.e(TAG, "registerCallback: 1")
            if (callback != null) {
                Log.e(TAG, "registerCallback: 2")
                serviceCallbacks.register(callback, this@RemoteBinder)
            }
        }

        override fun unregisterCallback(callback: IServerServiceCallback?) {
            if (callback != null) serviceCallbacks.unregister(callback)
        }

        override fun userSignUp(user: User) {
            ServerApplication.printLog(TAG, "Server service is proccessing sign up ...")
            scope.launch {
                val isUserExists = userDao.userSignIn(user.phone_number)
                if (isUserExists) {
                    ServerApplication.printError(TAG, "User is exists... ")
                    postFailureResponse(
                        RequestCode.SIGN_UP_REQ,
                        ResponseCode.ERROR_SIGN_UP_WITH_USER_EXISTS
                    )
                } else {
                    val resultId = userDao.userSignUp(user)
                    if (resultId <= -1) {
                        ServerApplication.printError(TAG, "user sign up unsuccessfully... ")
                        postFailureResponse(RequestCode.SIGN_UP_REQ, ResponseCode.ERROR_SIGN_UP)
                        return@launch
                    } else {
                        ServerApplication.printLog(TAG, "User sign up successfully...")
                        val resultUser = userDao.getUser(resultId.toInt())
                        var name: String? = null
                        if (resultUser != null) {
                            name = resultUser.name
                        }
                        remoteBroadcast { index ->
                            serviceCallbacks.getBroadcastItem(index).onUserSignUp(
                                AuthResponse(
                                    RequestCode.SIGN_UP_REQ,
                                    ResponseCode.OK,
                                    resultId.toInt(),
                                    name.toString()
                                )
                            )
                        }
                    }
                }
            }
        }

        override fun userSignIn(phone_number: String) {
            ServerApplication.printLog(TAG, "Server service is proccessing sign in ...")
            scope.launch {
                val isUserExists = userDao.userSignIn(phone_number)
                if (!isUserExists) {
                    ServerApplication.printError(
                        TAG,
                        "Sign in unseccessfully...User is not exists..."
                    )
                    postFailureResponse(
                        RequestCode.SIGN_IN_REQ,
                        ResponseCode.ERROR_SIGN_IN_USER_NOT_FOUND
                    )
                } else {
                    ServerApplication.printLog(TAG, "Sign in seccessfully...")
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
            ServerApplication.printLog(TAG, "Server service is proccessing insert user health ...")
            scope.launch {
                val healthId = userHealthDao.insertUserHealth(health)
                if (healthId <= -1) {
                    ServerApplication.printError(TAG, "insert user health unsuccessfully ... ")
                    postFailureResponse(RequestCode.INSERT_HEALTH, ResponseCode.ERROR_INSERT_HEALTH)
                    return@launch
                } else {
                    val listHealths = userHealthDao.getUserHealths()!!
                    ServerApplication.printLog(TAG, "insert user health successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onInsertHealth(
                            HealthResponse(
                                RequestCode.INSERT_HEALTH,
                                ResponseCode.OK,
                                healthId.toInt(),
                                listHealths
                            )
                        )
                    }
                }
            }
        }

        override fun getUserHealths() {
            ServerApplication.printLog(
                TAG,
                "Server service is proccessing get all history health of user..."
            )
            scope.launch {
                val listHealths = userHealthDao.getUserHealths()
                if (listHealths == null) {
                    ServerApplication.printError(TAG, "List user healths is null  ... ")
                    postFailureResponse(
                        RequestCode.GET_HEALTHS,
                        ResponseCode.ERROR_LIST_HEATHS_NOT_FOUND
                    )
                    return@launch
                } else {
                    remoteBroadcast { index ->
                        ServerApplication.printLog(TAG, " get list user healths successfully ... ")
                        serviceCallbacks.getBroadcastItem(index).onGetUserHealths(
                            HealthResponse(
                                RequestCode.GET_HEALTHS,
                                ResponseCode.OK,
                                null,
                                listHealths
                            )
                        )
                    }
                }
            }
        }

        override fun getUser(user_id: Int) {
            ServerApplication.printLog(TAG, "Server service is proccessing get user...")
            scope.launch {
                val user = userDao.getUser(user_id)
                if (user == null) {
                    ServerApplication.printError(TAG, "Get user unsuccessfully ... ")
                    postFailureResponse(RequestCode.GET_USER, ResponseCode.ERROR_USER_NOT_FOUND)
                    return@launch
                } else {
                    ServerApplication.printLog(TAG, "Get user successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onGetUser(
                            UserResponse(
                                ResponseCode.OK,
                                RequestCode.GET_USER,
                                user,
                                user.user_id
                            )
                        )
                    }
                }
            }
        }

        override fun getAllUsers() {
            ServerApplication.printLog(TAG, "Server service is proccessing get user...")
            scope.launch {
                val listAllUsers = userDao.getListUser()
                if (listAllUsers == null) {
                    ServerApplication.printError(TAG, "List users is null ... ")
                    postFailureResponse(RequestCode.GET_HEALTHS, ResponseCode.ERROR_LIST_USER_NULL)
                    return@launch
                } else {
                    ServerApplication.printLog(TAG, "Get all users successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index)
                            .onGetAllUsers(ListUsersResponse(ResponseCode.OK, listAllUsers))
                    }
                }
            }
        }

        override fun updateUser(user: User) {
            ServerApplication.printLog(TAG, "Server service is proccessing update user...")
            scope.launch {
                val userId = userDao.updateUser(user)
                if (userId > 0) {
                    ServerApplication.printLog(TAG, "Update user successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onUpdateUser(
                            UserResponse(
                                ResponseCode.OK,
                                RequestCode.UPDATE_USER,
                                null,
                                userId
                            )
                        )
                    }
                } else {
                    ServerApplication.printError(TAG, "Update user unsuccessfully ... ")
                    postFailureResponse(RequestCode.UPDATE_USER, ResponseCode.ERROR_UPDATE_USER)
                    return@launch
                }
            }
        }

        override fun deleteUser(user: User) {
            ServerApplication.printLog(TAG, "Server service is processing delete user...")
            scope.launch {
                val userId = userDao.deleteUser(user)
                if (userId > 0) {
                    ServerApplication.printLog(TAG, "Delete user successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onDeleteUser(
                            UserResponse(
                                ResponseCode.OK,
                                RequestCode.DELETE_USER,
                                null,
                                userId
                            )
                        )
                    }
                } else {
                    ServerApplication.printError(TAG, "Delete user unsuccessfully ... ")
                    postFailureResponse(RequestCode.DELETE_USER, ResponseCode.ERROR_DELETE_USER)
                    return@launch
                }
            }
        }

        override fun lockUser(user: User) {
            ServerApplication.printLog(TAG, "Server service is proccessing lock user...")
            scope.launch {
                val userId = userDao.lockUser(user)
                if (userId > 0) {
                    ServerApplication.printLog(TAG, "Lock user successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onDeleteUser(
                            UserResponse(
                                ResponseCode.OK,
                                RequestCode.LOCK_USER,
                                null,
                                userId
                            )
                        )
                    }
                } else {
                    ServerApplication.printError(TAG, "Lock user unsuccessfully ... ")
                    postFailureResponse(RequestCode.LOCK_USER, ResponseCode.ERROR_LOCK_USER)
                    return@launch
                }
            }
        }

        override fun getStatus() {
            ServerApplication.printLog(TAG, "Server service is proccessing get status...")
            scope.launch {
                val listStatus = statusDao.getStatus()
                if (listStatus == null) {
                    ServerApplication.printError(TAG, "Get status unsuccessfully ... ")
                    postFailureResponse(RequestCode.GET_STATUS, ResponseCode.ERROR_LIST_STATUS_NULL)
                    return@launch
                } else {
                    ServerApplication.printLog(TAG, "Get status successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index)
                            .onGetStatus(StatusResponse(ResponseCode.OK, listStatus))
                    }
                }
            }
        }

        override fun getStatisticCovidVn() {
            ServerApplication.printLog(TAG, "Server service is proccessing get statistic covid...")
            scope.launch {
                val statistic = statisticCovidDao.getStatisticCovidVn()
                if (statistic == null) {
                    ServerApplication.printError(TAG, "List statistic covid Vietnam is null ... ")
                    postFailureResponse(
                        RequestCode.GET_STATISTIC_COVID_VN,
                        ResponseCode.ERROR_STATISTIC_COVID_VN_NULL
                    )
                    return@launch
                } else {
                    ServerApplication.printLog(
                        TAG,
                        "List statistic covid Vietnam successfully ... "
                    )
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index)
                            .onGetStatisticCovidVn(
                                StatisticCovidVnResponse(
                                    ResponseCode.OK,
                                    statistic
                                )
                            )
                    }
                }
            }
        }

        override fun getStatisticCovidWorld() {
            ServerApplication.printLog(
                TAG,
                "Server service is proccessing get statistic covid of world ..."
            )
            scope.launch {
                val statistic = statisticCovidDao.getStatisticCovidWorld()
                if (statistic == null) {
                    ServerApplication.printError(
                        TAG,
                        "List statistic covid World wide is null ... "
                    )
                    postFailureResponse(
                        RequestCode.GET_STATISTIC_COVID_WORLD,
                        ResponseCode.ERROR_STATISTIC_COVID_WORLD_NULL
                    )
                    return@launch
                } else {
                    ServerApplication.printLog(
                        TAG,
                        "List statistic covid World wide successfully ... "
                    )
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index)
                            .onGetStatisticCovidWorld(
                                StatisticCovidWorldResponse(ResponseCode.OK, statistic)
                            )
                    }
                }
            }
        }

        override fun getSymptom() {
            ServerApplication.printLog(TAG, "Server service is proccessing get symptom ...")
            scope.launch {
                val listSymptom = symptomDao.getSymptoms()
                if (listSymptom == null) {
                    ServerApplication.printError(TAG, "List symptom is null ... ")
                    postFailureResponse(
                        RequestCode.GET_SYMPTOMS,
                        ResponseCode.ERROR_LIST_SYMPTOMS_NULL
                    )
                    return@launch
                } else {
                    ServerApplication.printLog(TAG, "List symptom is Successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onGetSymptom(
                            SymptomResponse(
                                ResponseCode.OK,
                                listSymptom
                            )
                        )
                    }
                }
            }
        }

        override fun getActive() {
            ServerApplication.printLog(TAG, "Server service is proccessing get active...")
            scope.launch {
                val listActive = activeDao.getActives()
                if (listActive == null) {
                    ServerApplication.printError(TAG, "List active is null ... ")
                    postFailureResponse(RequestCode.GET_ACTIVE, ResponseCode.ERROR_LIST_ACTIVE_NULL)
                    return@launch
                } else {
                    ServerApplication.printLog(TAG, "List symptom is Successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index)
                            .onGetActive(ActiveResponse(ResponseCode.OK, listActive))
                    }
                }
            }
        }

        override fun getGender() {
            ServerApplication.printLog(TAG, "Server service is proccessing get gender...")
            scope.launch {
                val listGender = genderDao.getGender()
                if (listGender == null) {
                    ServerApplication.printError(TAG, "List gender is null ... ")
                    postFailureResponse(RequestCode.GET_GENDER, ResponseCode.ERROR_LIST_GENDER_NULL)
                    return@launch
                } else {
                    ServerApplication.printLog(TAG, "List symptom is successfully ... ")
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index)
                            .onGetGender(GenderResponse(ResponseCode.OK, listGender))
                    }
                }
            }
        }

        override fun getHistoryCovidVn() {
            ServerApplication.printLog(
                TAG,
                "Server service is proccessing get all history covid of VietNam ..."
            )
            scope.launch {
                val listHistory = historyCovidDao.getHistoryCovidOfVn()
                if (listHistory == null) {
                    ServerApplication.printError(TAG, "List history covid Vietnam is null ... ")
                    postFailureResponse(
                        RequestCode.GET_HISTORY_COVID_VN,
                        ResponseCode.ERROR_HISTORY_COVID_VN_NULL
                    )
                    return@launch
                } else {
                    ServerApplication.printLog(
                        TAG,
                        "List history covid Vietnam is successfully ... "
                    )
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onGetHistoryCovidVn(
                            HistoryCovidResponse(
                                RequestCode.GET_HISTORY_COVID_VN,
                                ResponseCode.OK,
                                listHistory
                            )
                        )
                    }
                }
            }
        }

        override fun getHistoryCovidWorld() {
            ServerApplication.printLog(
                TAG,
                "Server service is proccessing get all history covid of World wide  ..."
            )
            scope.launch {
                val listHistory = historyCovidDao.getHistoryCovidOfWorld()
                if (listHistory == null) {
                    ServerApplication.printLog(TAG, "List history covid World wide is null ... ")
                    postFailureResponse(
                        RequestCode.GET_HISTORY_COVID_WORLD,
                        ResponseCode.ERROR_HISTORY_COVID_WORLD_NULL
                    )
                    return@launch
                } else {
                    ServerApplication.printLog(
                        TAG,
                        "List history covid World wide is successfully ... "
                    )
                    remoteBroadcast { index ->
                        serviceCallbacks.getBroadcastItem(index).onGetHistoryCovidWorld(
                            HistoryCovidResponse(
                                RequestCode.GET_HISTORY_COVID_WORLD,
                                ResponseCode.OK,
                                listHistory
                            )
                        )
                    }
                }
            }
        }

        private fun postFailureResponse(
            @RequestCode requestCode: Int,
            @ResponseCode responseCode: Int
        ) {
            remoteBroadcast { index ->
                Log.e(TAG, "postFailureResponse: $index")
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

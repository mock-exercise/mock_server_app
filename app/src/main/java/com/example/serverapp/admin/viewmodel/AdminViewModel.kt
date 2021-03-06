package com.example.serverapp.admin.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.connectorlibrary.callback.CallbackConnector
import com.example.connectorlibrary.controller.ServiceControllerAdmin
import com.example.connectorlibrary.enitity.*
import com.example.serverapp.R
import com.example.serverapp.admin.data.repository.AdminRepository
import com.example.serverapp.app.ServerApplication
import com.example.serverapp.base.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: AdminRepository,
    private val service: ServiceControllerAdmin
) : ViewModel(),
    LifecycleObserver, CallbackConnector.CallbackConnectorAdmin {

    var isServerConnected = MutableLiveData(false)
        private set

    var listUsers = MutableLiveData<List<User>>()
        private set

    var eventLoading = MutableLiveData<Event<Boolean>>()
        private set

    var eventError = MutableLiveData<Event<String>>()
        private set

    var listGender = MutableLiveData<List<Gender>>()
        private set

    var listStatus = MutableLiveData<List<Status>>()
        private set

    var listSymptom = MutableLiveData<List<Symptom>>()
        private set

    var listUserHealths = MutableLiveData<List<Health>>()
        private set

    private fun showLoading(value: Boolean) {
        eventLoading.value = Event(value)
    }

    private fun showError(errorString: String) {
        eventError.value = Event(errorString)
    }

    override fun onDeleteUser(user: UserResponse) {}

    override fun onFailureResponse(failureResponse: FailureResponse) {
        when (failureResponse.requestCode) {
            RequestCode.GET_GENDER -> {
                when (failureResponse.responseCode) {
                    ResponseCode.ERROR_LIST_GENDER_NULL -> {
                        showError("OOPS! Nh???n d??? li???u gi???i t??nh th???t b???i ")
                    }
                }
            }
            RequestCode.GET_STATUS -> {
                when (failureResponse.responseCode) {
                    ResponseCode.ERROR_LIST_GENDER_NULL -> {
                        showError("OOPS! Nh???n d??? li???u tr???ng th??i th???t b???i ")
                    }
                }
            }
            RequestCode.GET_SYMPTOMS -> {
                when (failureResponse.responseCode) {
                    ResponseCode.ERROR_LIST_GENDER_NULL -> {
                        showError("OOPS! Nh???n d??? li???u tri???u ch???ng th???t b???i ")
                    }
                }
            }
        }
    }

    override fun onGetAllUsers(listUsersResponse: ListUsersResponse) {
        when (listUsersResponse.responseCode) {
            ResponseCode.SUCCESS -> {
                Log.d(TAG, "onGetAllUsers: nhan list users thanh cong...")
                listUsers.value = listUsersResponse.listUsers
            }
        }
    }

    override fun onGetGender(genderResponse: GenderResponse) {
        when (genderResponse.responseCode) {
            ResponseCode.SUCCESS -> {
                Log.d(TAG, "onGetGender: nhan active thanh cong...")
                listGender.value = genderResponse.listGender
            }
        }
    }

    override fun onGetStatus(statusResponse: StatusResponse) {
        when (statusResponse.responseCode) {
            ResponseCode.SUCCESS -> {
                Log.d(TAG, "onGetStatus: nhan status thanh cong... ")
                listStatus.value = statusResponse.listStatuses
            }
        }
    }

    override fun onGetSymptom(symptomResponse: SymptomResponse) {
        when (symptomResponse.responseCode) {
            ResponseCode.SUCCESS -> {
                Log.d(TAG, "onGetSymptom: nhan symptom thanh cong...")
                listSymptom.value = symptomResponse.listSymptom
            }
        }
    }

    override fun onGetUserHealths(healthResponse: HealthResponse) {
    }

    override fun onLockUser(userResponse: UserResponse) {
        when (userResponse.requestCode) {
            RequestCode.LOCK_USER -> {
                when (userResponse.responseCode) {
                    ResponseCode.SUCCESS -> {
                        Log.e(TAG, "onLockUser: lock user thanh cong...")
                        ServerApplication.showToast(context, R.string.success_lock_user)
                    }
                }
            }
        }
    }

    fun getStatus() = viewModelScope.launch {
        repository.getStatus()
    }

    fun getSymptom() = viewModelScope.launch {
        repository.getSymptom()
    }

    fun getGender() = viewModelScope.launch {
        Log.e(TAG, "getGender: 11111", )
        repository.getGender()
    }

    fun getUserHealths() = viewModelScope.launch {
        showLoading(true)
        repository.getUserHealths()
    }

    fun getAllUsers() = viewModelScope.launch {
        showLoading(true)
        repository.getAllUsers()
    }

    fun lockUser(user: User) = viewModelScope.launch {
        showLoading(true)
        repository.lockUser(user)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.e(TAG, "addCallback: vao di")
        service.addCallback(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        service.removeCallback(this)
        isServerConnected.value = false
    }

    override fun onServerConnected() {
        Log.d(TAG, "onServerConnected: ")
        ServerApplication.showToast(context, R.string.success_connected)
        isServerConnected.value = true
    }

    companion object {
        val TAG: String = AdminViewModel::class.java.name
    }
}
package com.example.serverapp.admin.data.repository

import com.example.connectorlibrary.enitity.User
import com.example.serverapp.base.BaseRepository
import com.example.serverapp.admin.data.remoteservice.AdminService
import javax.inject.Inject

class AdminRepository @Inject constructor(private val adminService: AdminService) :
    BaseRepository() {

    suspend fun getStatus() = safeApiCall {
        adminService.getStatus()
    }

    suspend fun getSymptom() = safeApiCall {
        adminService.getSymptom()
    }

    suspend fun getActive() {
        adminService.getActive()
    }

    suspend fun getGender() {
        adminService.getGender()
    }

    suspend fun getUserHealths() = safeApiCall {
        adminService.getUserHealths()
    }

    suspend fun getAllUsers() = safeApiCall {
        adminService.getAllUsers()
    }

    suspend fun lockUser(user: User) = safeApiCall {
        adminService.lockUser(user)
    }
}
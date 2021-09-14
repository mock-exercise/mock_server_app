package com.example.serverapp.admin.data.remoteservice

import com.example.connectorlibrary.controller.ServiceControllerAdmin
import com.example.connectorlibrary.enitity.User
import javax.inject.Inject

class AdminService @Inject constructor(private val controller: ServiceControllerAdmin) {

    fun getStatus() {
        controller.getStatus()
    }

    fun getSymptom() {
        controller.getSymptom()
    }

    fun getActive() {
        controller.getActive()
    }

    fun getGender() {
        controller.getGender()
    }

    fun getUserHealths() {
        controller.getUserHealths()
    }

    fun getAllUsers() {
        controller.getAllUsers()
    }

    fun deleteUser(user: User) {
        controller.deleteUser(user)
    }

    fun lockUser(user: User) {
        controller.lockUser(user)
    }
}
package com.example.serverapp.admin.data.repository

import com.example.serverapp.base.BaseRepository
import com.example.serverapp.admin.data.remoteservice.AdminService
import javax.inject.Inject

class AdminRepository @Inject constructor(private val adminService: AdminService) : BaseRepository() {


}
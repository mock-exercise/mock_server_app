package com.example.serverapp.admin.di

import android.content.Context
import com.example.connectorlibrary.controller.ServiceControllerAdmin
import com.example.serverapp.admin.data.remoteservice.AdminService
import com.example.serverapp.admin.data.repository.AdminRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdminModule {

    @Provides
    @Singleton
    fun provideServiceController(@ApplicationContext context: Context): ServiceControllerAdmin =
        ServiceControllerAdmin(context)

    @Provides
    @Singleton
    fun provideAdminService(controller: ServiceControllerAdmin) =
        AdminService(controller)

    @Provides
    @Singleton
    fun providesAdminRepository(adminService: AdminService) = AdminRepository(adminService)
}
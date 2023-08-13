package com.skid.kodetestapp

import android.app.Application
import com.skid.kodetestapp.data.remote.UserService
import com.skid.kodetestapp.data.repositories.UserRepositoryImpl
import com.skid.kodetestapp.domain.repositories.UserRepository
import com.skid.kodetestapp.domain.usecases.GetFilteredUsersUseCase

class KodeTestApp : Application() {
    private val userService by lazy { UserService() }

    private val userRepository: UserRepository by lazy { UserRepositoryImpl(userService) }

    val getFilteredUsersUseCase by lazy { GetFilteredUsersUseCase(userRepository) }
}
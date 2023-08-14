package com.skid.kodetestapp.domain.repositories

import com.skid.kodetestapp.domain.model.UserItem

interface UserRepository {

    suspend fun getUsers(refresh: Boolean): Result<List<UserItem>>
}
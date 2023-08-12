package com.skid.kodetestapp.data.repositories

import com.skid.kodetestapp.data.mapper.toUserItem
import com.skid.kodetestapp.data.remote.UserService
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userService: UserService,
) : UserRepository {

    private var cachingUsers: List<UserItem> = emptyList()

    override suspend fun getUsers(refresh: Boolean): List<UserItem> {
        if (refresh || cachingUsers.isEmpty()) {
            cachingUsers = userService.getUsers().items.map { it.toUserItem() }
        }
        return cachingUsers
    }
}
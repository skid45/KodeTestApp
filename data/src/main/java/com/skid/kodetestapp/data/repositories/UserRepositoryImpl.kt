package com.skid.kodetestapp.data.repositories

import android.content.Context
import com.skid.kodetestapp.data.R
import com.skid.kodetestapp.data.mapper.toUserItem
import com.skid.kodetestapp.data.remote.UserService
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.domain.repositories.UserRepository
import java.io.IOException

class UserRepositoryImpl(
    private val userService: UserService,
    private val context: Context,
) : UserRepository {

    private var cachingUsers: List<UserItem> = emptyList()

    override suspend fun getUsers(refresh: Boolean): Result<List<UserItem>> {
        if (refresh || cachingUsers.isEmpty()) {
            try {
                val response = userService.getUsers()
                if (response.isSuccessful) {
                    cachingUsers = response.body()!!.items.map { it.toUserItem() }
                } else {
                    return Result.failure(Exception(context.getString(R.string.api_error)))
                }
            } catch (e: IOException) {
                return Result.failure(Exception(context.getString(R.string.network_connection_error)))
            } catch (e: Exception) {
                return Result.failure(e)
            }
        }
        return Result.success(cachingUsers)
    }
}
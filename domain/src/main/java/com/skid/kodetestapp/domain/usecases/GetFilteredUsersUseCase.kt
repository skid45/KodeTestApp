package com.skid.kodetestapp.domain.usecases

import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.domain.model.UserListItem
import com.skid.kodetestapp.domain.repositories.UserRepository

class GetFilteredUsersUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(query: String, refresh: Boolean): List<UserListItem> {
        val userList = userRepository.getUsers(refresh)
        return filteredUsers(userList, query)
    }

    private fun filteredUsers(userList: List<UserItem>, query: String): List<UserItem> {
        return if (query.isBlank()) userList
        else userList.filter { userItem ->
            userItem.name.contains(query, ignoreCase = true) ||
                    userItem.userTag.contains(query, ignoreCase = true)
        }
    }
}
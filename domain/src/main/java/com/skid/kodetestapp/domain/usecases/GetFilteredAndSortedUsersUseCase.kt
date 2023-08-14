package com.skid.kodetestapp.domain.usecases

import com.skid.kodetestapp.domain.model.Sorting
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.domain.model.UserListItem
import com.skid.kodetestapp.domain.repositories.UserRepository

class GetFilteredAndSortedUsersUseCase(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        query: String,
        sortBy: Sorting,
        refresh: Boolean,
    ): List<UserListItem> {
        val userList = userRepository.getUsers(refresh)
        val filteredUserList = filteredUsers(userList, query)
        return sortedUsers(filteredUserList, sortBy)
    }

    private fun filteredUsers(userList: List<UserItem>, query: String): List<UserItem> {
        return if (query.isBlank()) userList
        else userList.filter { userItem ->
            userItem.name.contains(query, ignoreCase = true) ||
                    userItem.userTag.contains(query, ignoreCase = true)
        }
    }

    private fun sortedUsers(userList: List<UserItem>, sortBy: Sorting): List<UserListItem> {
        return when (sortBy) {
            Sorting.BY_ALPHABET -> userList.sortedBy { it.name }
            Sorting.BY_BIRTHDAY -> userList // TODO(Sorting by birthday)
        }
    }
}
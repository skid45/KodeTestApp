package com.skid.kodetestapp.domain.usecases

import com.skid.kodetestapp.domain.model.SeparatorItem
import com.skid.kodetestapp.domain.model.Sorting
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.domain.model.UserListItem
import com.skid.kodetestapp.domain.repositories.UserRepository
import java.time.LocalDate

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
            Sorting.BY_BIRTHDAY -> sortByBirthday(userList)
        }
    }

    private fun sortByBirthday(userList: List<UserItem>): List<UserListItem> {
        val now = LocalDate.now()
        val sortedUserList = userList.sortedWith(compareBy {
            val birthday = it.birthday.withYear(now.year)
            if (birthday.isBefore(now)) birthday.plusYears(1) else birthday
        })

        val separatorIndex = sortedUserList.indexOfFirst { userItem ->
            userItem.birthday.withYear(now.year).isBefore(now)
        }

        return if (separatorIndex == -1) sortedUserList
        else mutableListOf<UserListItem>().apply {
            addAll(sortedUserList.subList(0, separatorIndex))
            add(separatorIndex, SeparatorItem((now.year + 1).toString()))
            addAll(sortedUserList.subList(separatorIndex, sortedUserList.size))
        }
    }
}
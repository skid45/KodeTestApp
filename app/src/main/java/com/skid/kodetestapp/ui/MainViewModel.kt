package com.skid.kodetestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skid.kodetestapp.domain.model.UserListItem
import com.skid.kodetestapp.domain.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _userList = MutableStateFlow(emptyList<UserListItem>())
    val userList = _userList.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _department = MutableStateFlow(0)
    val department = _department.asStateFlow()

    init {
        viewModelScope.launch {
            _userList.value = userRepository.getUsers(true)
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onDepartmentChange(position: Int) {
        _department.value = position
    }
}
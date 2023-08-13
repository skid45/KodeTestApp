package com.skid.kodetestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skid.kodetestapp.KodeTestApp
import com.skid.kodetestapp.domain.model.UserListItem
import com.skid.kodetestapp.domain.usecases.GetFilteredUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    private val getFilteredUsersUseCase: GetFilteredUsersUseCase,
) : ViewModel() {

    private val _userList = MutableStateFlow(emptyList<UserListItem>())
    val userList = _userList.asStateFlow()

    private val query = MutableStateFlow("")

    private val _department = MutableStateFlow(0)
    val department = _department.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val combine = combine(query, isRefreshing) { query, refresh ->
        _userList.value = getFilteredUsersUseCase(query, refresh)
        _isRefreshing.value = false
    }

    init {
        _isRefreshing.value = true
        viewModelScope.launch {
            combine.collect()
        }
    }

    fun onQueryChange(query: String) {
        this.query.value = query
    }

    fun onDepartmentChange(position: Int) {
        _department.value = position
    }

    fun onIsRefreshingChange(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val application: KodeTestApp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application.getFilteredUsersUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
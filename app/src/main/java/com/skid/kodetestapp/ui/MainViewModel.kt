package com.skid.kodetestapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skid.kodetestapp.KodeTestApp
import com.skid.kodetestapp.domain.model.Sorting
import com.skid.kodetestapp.domain.model.UserListItem
import com.skid.kodetestapp.domain.usecases.GetFilteredAndSortedUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val getFilteredAndSortedUsersUseCase: GetFilteredAndSortedUsersUseCase,
) : ViewModel() {

    private val _userList = MutableStateFlow(emptyList<UserListItem>())
    val userList = _userList.asStateFlow()

    private val query = MutableStateFlow("")

    private val _department = MutableStateFlow(0)
    val department = _department.asStateFlow()

    private val _sortBy = MutableStateFlow(Sorting.BY_ALPHABET)
    val sortBy = _sortBy.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _wasSkeletonShown = MutableStateFlow(false)
    val wasSkeletonShown = _wasSkeletonShown.asStateFlow()

    val networkError = combine(
        query, sortBy, isRefreshing
    ) { query, sortBy, refresh ->
        val result = getFilteredAndSortedUsersUseCase(query, sortBy, refresh)
        _isRefreshing.value = false
        return@combine if (result.isSuccess) {
            _userList.value = result.getOrNull()!!
            null
        } else result.exceptionOrNull()?.localizedMessage
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        _isRefreshing.value = true
    }

    fun onQueryChange(query: String) {
        this.query.value = query
    }

    fun onDepartmentChange(position: Int) {
        _department.value = position
    }

    fun onSortByChange(sortBy: Sorting) {
        _sortBy.value = sortBy
    }

    fun onIsRefreshingChange(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    fun onWasSkeletonShown(wasSkeletonShown: Boolean) {
        _wasSkeletonShown.value = wasSkeletonShown
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val application: KodeTestApp) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application.getFilteredAndSortedUsersUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
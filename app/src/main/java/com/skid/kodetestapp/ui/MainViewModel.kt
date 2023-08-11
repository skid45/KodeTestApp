package com.skid.kodetestapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel() : ViewModel() {


    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _department = MutableStateFlow(0)
    val department = _department.asStateFlow()


    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onDepartmentChange(position: Int) {
        _department.value = position
    }
}
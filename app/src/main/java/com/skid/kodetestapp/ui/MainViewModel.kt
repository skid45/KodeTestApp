package com.skid.kodetestapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel() : ViewModel() {


    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


    fun onQueryChange(query: String) {
        _query.value = query
    }
}
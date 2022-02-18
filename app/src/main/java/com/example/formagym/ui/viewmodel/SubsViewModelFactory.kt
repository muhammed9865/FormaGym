package com.example.formagym.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.formagym.pojo.datasource.FormaDatabase

@Suppress("UNCHECKED_CAST")
class SubsViewModelFactory(private val dataSource: FormaDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SubsViewModel(dataSource) as T
    }
}
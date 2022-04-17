package com.example.formagym.ui.fragment.inactive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.formagym.pojo.datasource.FormaDatabase

@Suppress("UNCHECKED_CAST")
class InactiveViewModelFactory(private val database: FormaDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InactiveViewModel(database.getDao()) as T
    }
}
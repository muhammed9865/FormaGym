package com.example.formagym.ui.fragment.active

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.formagym.pojo.datasource.FormaDatabase

@Suppress("UNCHECKED_CAST")
class ActiveViewModelFactory(private val database: FormaDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ActiveViewModel(database.getDao()) as T
    }
}
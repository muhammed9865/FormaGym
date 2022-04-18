package com.example.formagym.ui.mainviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.formagym.pojo.datasource.FormaDatabase

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val dataSource: FormaDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(dataSource.getDao()) as T
    }
}
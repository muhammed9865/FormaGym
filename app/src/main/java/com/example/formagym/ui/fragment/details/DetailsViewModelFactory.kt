package com.example.formagym.ui.fragment.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.formagym.pojo.datasource.FormaDatabase

@Suppress("UNCHECKED_CAST")
class DetailsViewModelFactory(private val database: FormaDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(database.getDao()) as T
    }
}
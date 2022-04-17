package com.example.formagym.ui.fragment.active

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ActiveViewModel constructor(private val formaDao: FormaDao): ViewModel() {
    private val _activeMembers = MutableLiveData<List<User>>(mutableListOf())
    val activeMembers: LiveData<List<User>> get() = _activeMembers


    fun getActiveMembers() {
        viewModelScope.launch {
            _activeMembers.value = formaDao.getActiveMembers(currentDate = System.currentTimeMillis())
        }
    }

    fun searchActiveMembers(query: String) {
        viewModelScope.launch {
            formaDao.searchActiveMembers(
                currentDate = System.currentTimeMillis(),
                searchQuery = "$query%"
            ).collect {
                _activeMembers.value = it
            }
        }
    }


}
package com.example.formagym.ui.fragment.inactive.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class InactiveViewModel constructor(private val formaDao: FormaDao): ViewModel() {
    private val _inActiveMembers = MutableLiveData<List<User>>(mutableListOf())
    val inactiveMembers: LiveData<List<User>> get() = _inActiveMembers


    fun getInactiveMembers() {
        viewModelScope.launch {
            _inActiveMembers.value = formaDao.getInActiveMembers(currentDate = System.currentTimeMillis())
        }
    }

    fun searchActiveMembers(query: String) {
        viewModelScope.launch {
            formaDao.searchInActiveMembers(
                currentDate = System.currentTimeMillis(),
                searchQuery = "$query%"
            ).collect {
                _inActiveMembers.value = it
            }
        }
    }


}
package com.example.formagym.ui.mainviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.datasource.FormaDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val db: FormaDao) : ViewModel() {
    private val _activeSubsCount = MutableLiveData(0)
    val activeSubsCount: LiveData<Int> = _activeSubsCount

    private val _inactiveSubsCount = MutableLiveData(0)
    val inactiveSubsCount: LiveData<Int> = _inactiveSubsCount


    var selectedUserId: Int? = null



    fun getActiveMembersLength() {
        viewModelScope.launch {
            _activeSubsCount.value = db.getActiveMembersCount(System.currentTimeMillis())
        }
    }

    fun getInactiveMembersLength() {
        viewModelScope.launch {
            _inactiveSubsCount.value = db.getInactiveMembersCount(System.currentTimeMillis())
        }

    }


    fun setUserIdForDetails(userId: Int) {
        selectedUserId = userId
    }

    fun onNewMember() {
        selectedUserId = null
    }



    companion object {
        private const val TAG = "SubsViewModel"
    }
}
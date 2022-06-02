package com.example.formagym.ui.mainviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.pojo.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val db: FormaDao) : ViewModel() {
    private val _activeSubsCount = MutableLiveData(0)
    val activeSubsCount: LiveData<Int> = _activeSubsCount

    private val _inactiveSubsCount = MutableLiveData(0)
    val inactiveSubsCount: LiveData<Int> = _inactiveSubsCount

    private val _activeMembers = MutableLiveData<List<User>>(mutableListOf())
    val activeMembers: LiveData<List<User>> get() = _activeMembers

    private val _inActiveMembers = MutableLiveData<List<User>>(mutableListOf())
    val inactiveMembers: LiveData<List<User>> get() = _inActiveMembers

    var selectedUserId: Int? = null

    init {
        getMembers()
    }


    fun getMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getActiveMembers(System.currentTimeMillis()).also {
                withContext(Dispatchers.Main) {
                    _activeMembers.value = it
                }
            }

           db.getInActiveMembers(System.currentTimeMillis()).also {
               withContext(Dispatchers.Main) {
                   _inActiveMembers.value = it
               }
           }
        }
    }

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
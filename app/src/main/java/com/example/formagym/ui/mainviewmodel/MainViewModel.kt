package com.example.formagym.ui.mainviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
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
        refreshActives()
        refreshInactives()
    }


    fun refreshActives() {
        tryAsync {
            db.getActiveMembers(System.currentTimeMillis()).also {
                withContext(Dispatchers.Main) {
                    _activeMembers.value = it
                }
            }
        }
    }
    fun searchActiveMembers(query: String) {
        tryAsync {
            db.searchActiveMembers(
                currentDate = System.currentTimeMillis(),
                searchQuery = "%$query%"
            ).collect {
                withContext(Dispatchers.Main) {
                    _activeMembers.value = it
                }
            }
        }
    }

    fun refreshInactives() {
        tryAsync {
            db.getInActiveMembers(System.currentTimeMillis()).also {
                withContext(Dispatchers.Main) {
                    _inActiveMembers.value = it
                }
            }
        }
    }

    fun searchInActiveMembers(query: String) {
        tryAsync {
            db.searchInActiveMembers(
                currentDate = System.currentTimeMillis(),
                searchQuery = "%$query%"
            ).collect {
                withContext(Dispatchers.Main) {
                    _inActiveMembers.value = it
                }
            }
        }
    }

    private fun tryAsync(func: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                func()
            } catch (e: Exception) {
                Log.e(TAG, "tryAsync: ${e.message}")
            }
        }
    }


    fun getActiveMembersLength() {
        tryAsync {
            db.getActiveMembersCount(System.currentTimeMillis()).also {
                withContext(Dispatchers.Main) {
                    _activeSubsCount.value = it
                }
            }
        }
    }

    fun getInactiveMembersLength() {
        tryAsync {
            db.getInactiveMembersCount(System.currentTimeMillis()).also {
                withContext(Dispatchers.Main) {
                    _inactiveSubsCount.value = it
                }
            }
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
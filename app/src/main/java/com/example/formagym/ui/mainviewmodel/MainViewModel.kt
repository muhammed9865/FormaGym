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

    /*fun save(user: User) {
        viewModelScope.launch {
            db.saveUser(user)
            if (_selectedMember.value == null) {
                if (isDateOutdated(user.subscribeEndDate)) {
                    val list = _inactiveSubs.value!!.toMutableList().apply { add(user) }
                    _inactiveSubs.postValue(list)
                } else {
                    val list = activeSubs.value!!.toMutableList().apply { add(user) }
                    _activeSubs.postValue(list)
                }
            } else {
                var list = _inactiveSubs.value!!.toMutableList()
                val mMember = _selectedMember.value
                if (list.contains(mMember)) {
                    if (!isDateOutdated(user.subscribeEndDate)) {
                        list.remove(mMember)
                        _inactiveSubs.postValue(list)
                        list = _activeSubs.value!!.toMutableList().apply { add(mMember!!) }
                        _activeSubs.postValue(list)
                    }
                }
            }
        }
    }
*/


    fun onViewDetails(userId: Int) {
        selectedUserId = userId
    }

    fun onNewMember() {
        selectedUserId = null
    }


/*
//    fun remove(user: User) {
//        viewModelScope.launch {
//            db.removeUser(user)
//        }
//        if (isDateOutdated(user.subscribeEndDate)) {
//            val list = _inactiveSubs.value!!.toMutableList().apply { remove(user) }
//            _inactiveSubs.postValue(list)
//        } else {
//            val list = activeSubs.value!!.toMutableList().apply { remove(user) }
//            _activeSubs.postValue(list)
//        }
//    }
*/

  /*  fun searchActives(query: String) {
        val ct = System.currentTimeMillis()
        viewModelScope.launch {
            db.searchActiveMembers(ct, "%$query%").collect {
                Log.d(TAG, "searchActives: $it")
                _activeSubs.postValue(it)
            }
        }
    }*/

   /* fun searchInActives(query: String) {
        val ct = System.currentTimeMillis()
        viewModelScope.launch {
            db.searchInActiveMembers(ct, "%$query%").collect {
                _inactiveSubs.postValue(it)
            }
        }
    }*/

    companion object {
        private const val TAG = "SubsViewModel"
    }
}
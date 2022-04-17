package com.example.formagym.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.utils.isDateOutdated
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SubsViewModel(dataSource: FormaDatabase) : ViewModel() {
    private val _activeSubs = MutableLiveData<List<User>>(mutableListOf())
    val activeSubs: LiveData<List<User>> = _activeSubs
    private val _inactiveSubs = MutableLiveData<List<User>>(mutableListOf())
    val inactiveSubs: LiveData<List<User>> = _inactiveSubs
    private val _selectedMember = MutableLiveData<User?>(null)
    val selectedUser: LiveData<User?> = _selectedMember
    private val db = dataSource.getDao()


    fun getActiveMembers(): LiveData<List<User>> {
        viewModelScope.launch {
            _activeSubs.value = db.getActiveMembers(System.currentTimeMillis())
        }
        return activeSubs
    }

    fun getInactiveMembers(): LiveData<List<User>> {
        viewModelScope.launch {
            _inactiveSubs.value = db.getInActiveMembers(System.currentTimeMillis())
        }
        return inactiveSubs
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


    fun onViewDetails(user: User) {
        _selectedMember.postValue(user)
    }

    fun onNewMember() {
        _selectedMember.postValue(null)
    }


    fun remove(user: User) {
        viewModelScope.launch {
            db.removeUser(user)
        }
        if (isDateOutdated(user.subscribeEndDate)) {
            val list = _inactiveSubs.value!!.toMutableList().apply { remove(user) }
            _inactiveSubs.postValue(list)
        } else {
            val list = activeSubs.value!!.toMutableList().apply { remove(user) }
            _activeSubs.postValue(list)
        }
    }

    fun searchActives(query: String) {
        val ct = System.currentTimeMillis()
        viewModelScope.launch {
            db.searchActiveMembers(ct, "%$query%").collect {
                Log.d(TAG, "searchActives: $it")
                _activeSubs.postValue(it)
            }
        }
    }

    fun searchInActives(query: String) {
        val ct = System.currentTimeMillis()
        viewModelScope.launch {
            db.searchInActiveMembers(ct, "%$query%").collect {
                _inactiveSubs.postValue(it)
            }
        }
    }

    companion object {
        private const val TAG = "SubsViewModel"
    }
}
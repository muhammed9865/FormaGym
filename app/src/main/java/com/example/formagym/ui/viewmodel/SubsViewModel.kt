package com.example.formagym.ui.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.isOutdated
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.pojo.model.Member
import kotlinx.coroutines.launch
import java.util.function.Predicate

class SubsViewModel(dataSource: FormaDatabase) : ViewModel() {
    private val _activeSubs = MutableLiveData<List<Member>>(mutableListOf())
    val activeSubs: LiveData<List<Member>> = _activeSubs
    private val _inactiveSubs = MutableLiveData<List<Member>>(mutableListOf())
    val inactiveSubs: LiveData<List<Member>> = _inactiveSubs
    private val _selectedMember = MutableLiveData<Member?>(null)
    val selectedMember: LiveData<Member?> = _selectedMember
    private val db = dataSource.getDao()


    fun getActiveMembers(): LiveData<List<Member>> {
        viewModelScope.launch {
            _activeSubs.value = db.getActiveMembers(System.currentTimeMillis())
        }
        return activeSubs
    }

    fun getInactiveMembers(): LiveData<List<Member>> {
        viewModelScope.launch {
            _inactiveSubs.value = db.getInActiveMembers(System.currentTimeMillis())
        }
        return inactiveSubs
    }

    fun save(member: Member) {
        viewModelScope.launch {
            db.save(member)
        }

        if (_selectedMember.value == null) {
            if (isOutdated(member.subscribeEndDate)) {
                val list = _inactiveSubs.value!!.toMutableList().apply { add(member) }
                _inactiveSubs.postValue(list)
            } else {
                val list = activeSubs.value!!.toMutableList().apply { add(member) }
                _activeSubs.postValue(list)
            }
        } else {
            var list = _inactiveSubs.value!!.toMutableList()
            val mMember =_selectedMember.value
            if (list.contains(mMember)) {
                if (!isOutdated(member.subscribeEndDate)) {
                    list.remove(mMember)
                    _inactiveSubs.postValue(list)
                    list = _activeSubs.value!!.toMutableList().apply { add(mMember!!) }
                    _activeSubs.postValue(list)
                }
            }
        }
    }

    fun onViewDetails(member: Member) {
        _selectedMember.postValue(member)
    }

    fun onNewMember() {
        _selectedMember.postValue(null)
    }

    fun remove(member: Member) {
        viewModelScope.launch {
            db.remove(member)
        }
        if (isOutdated(member.subscribeEndDate)) {
            val list = _inactiveSubs.value!!.toMutableList().apply { remove(member) }
            _inactiveSubs.postValue(list)
        } else {
            val list = activeSubs.value!!.toMutableList().apply { remove(member) }
            _activeSubs.postValue(list)
        }

    }
}
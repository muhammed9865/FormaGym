package com.example.formagym.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.pojo.model.Member
import kotlinx.coroutines.launch

class SubsViewModel(dataSource: FormaDatabase) : ViewModel() {
    private val _activeSubs = MutableLiveData<List<Member>>(mutableListOf())
    val activeSubs: LiveData<List<Member>> = _activeSubs
    private val _inactiveSubs = MutableLiveData<List<Member>>(mutableListOf())
    val inactiveSubs: LiveData<List<Member>> = _inactiveSubs
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

    fun dummyData(): MutableList<Member> {
        return mutableListOf<Member>().apply {
            repeat(10) {
                add(
                    Member(
                        "Muhammed Salman",
                        System.currentTimeMillis() - 100000000,
                        System.currentTimeMillis() - 1000,
                    )
                )
            }
            val cTime = System.currentTimeMillis().minus(423110)
            val lTime = System.currentTimeMillis().plus(42311023)
            add(
                Member(
                    "Osama Bin Ladin",
                    cTime,
                    lTime
                )
            )
        }

    }
}
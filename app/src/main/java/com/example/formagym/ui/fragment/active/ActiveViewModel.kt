package com.example.formagym.ui.fragment.active

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User
import com.example.formagym.pojo.model.relations.UserWithPayments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveViewModel @Inject constructor(private val formaDao: FormaDao): ViewModel() {
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
                searchQuery = "%$query%"
            ).collect {
                _activeMembers.value = it
            }
        }
    }

    fun searchMemberByID(userId: Int): LiveData<User> {
        val user = MutableLiveData<User>()
        viewModelScope.launch {
            user.value = formaDao.getUserByID(userId)
        }
        return user
    }

    fun loadUserPayments(userId: Int): LiveData<List<Payment>> {
        val payments = MutableLiveData<List<Payment>>(mutableListOf())
        viewModelScope.launch {
            payments.value = formaDao.getUserPayments(userId).payments
        }
        return payments
    }


}
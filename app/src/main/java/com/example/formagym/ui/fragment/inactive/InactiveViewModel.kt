package com.example.formagym.ui.fragment.inactive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InactiveViewModel @Inject constructor(private val formaDao: FormaDao): ViewModel() {
    private val _inActiveMembers = MutableLiveData<List<User>>(mutableListOf())
    val inactiveMembers: LiveData<List<User>> get() = _inActiveMembers


    fun getInactiveMembers() {
        viewModelScope.launch {
            _inActiveMembers.value = formaDao.getInActiveMembers(currentDate = System.currentTimeMillis())
        }
    }

    fun searchInActiveMembers(query: String) {
        viewModelScope.launch {
            formaDao.searchInActiveMembers(
                currentDate = System.currentTimeMillis(),
                searchQuery = "%$query%"
            ).collect {
                _inActiveMembers.value = it
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
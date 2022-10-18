package com.example.formagym.presentation.activity.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.data.model.User
import com.example.formagym.domain.usecases.GetMembersDetailsUseCase
import com.example.formagym.domain.usecases.GetMembersUseCase
import com.example.formagym.domain.usecases.SearchMembersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchMembers: SearchMembersUseCase,
    private val getMembers: GetMembersUseCase,
    private val getMembersDetails: GetMembersDetailsUseCase,
) : ViewModel() {


    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    // The following states are used in the fragments (ActiveFragment, InactiveFragment).
    // As a way of caching so no need to reload them on navigating between fragments.
    // Refreshing lists is done by refreshXXXX() which is called inside the fragments.
    private val _activeMembers = MutableLiveData<List<User>>(mutableListOf())
    val activeMembers: LiveData<List<User>> get() = _activeMembers

    private val _inActiveMembers = MutableLiveData<List<User>>(mutableListOf())
    val inactiveMembers: LiveData<List<User>> get() = _inActiveMembers

    var selectedUserId: Int? = null

    init {
        // Initially calling refreshXXXX() to load data for the first time.
        refreshActives()
        refreshInactives()
    }


    fun refreshActives() {
        tryAsync {
            getMembers(getActives = true).also {
                withContext(Dispatchers.Main) {
                    _activeMembers.value = it
                }
            }
        }
    }


    fun searchActiveMembers(query: String) {
        tryAsync {
            val result = searchMembers(query, searchActives = true)
            withContext(Dispatchers.Main) {
                _activeMembers.value = result
            }

        }
    }

    fun refreshInactives() {
        tryAsync {
            getMembers(getActives = false).also {
                withContext(Dispatchers.Main) {
                    _inActiveMembers.value = it
                }
            }
        }
    }

    fun searchInActiveMembers(query: String) {
        tryAsync {
            val result = searchMembers(query, searchActives = false)

            withContext(Dispatchers.Main) {
                _inActiveMembers.value = result
            }

        }
    }


    fun getActiveMembersDetails() {
        tryAsync {
            getMembersDetails(getActives = true).also { details ->
                _state.value = _state.value.copy(activesDetails = details)
            }
        }
    }

    fun getInactiveMembersDetails() {
        tryAsync {
            getMembersDetails(getActives = false).also { details ->
                _state.value = _state.value.copy(inactiveDetails = details)
            }
        }

    }


    fun setUserIdForDetails(userId: Int) {
        selectedUserId = userId
    }

    fun onNewMember() {
        selectedUserId = null
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

    companion object {
        private const val TAG = "SubsViewModel"
    }
}
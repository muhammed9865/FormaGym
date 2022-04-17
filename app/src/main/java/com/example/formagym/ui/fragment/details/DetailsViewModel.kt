package com.example.formagym.ui.fragment.details

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.Payment
import com.example.formagym.pojo.model.User
import kotlinx.coroutines.launch

class DetailsViewModel constructor(private val dao: FormaDao) : ViewModel() {
    private var memberName = MutableLiveData<String>()
    val name: LiveData<String> = memberName
    private var memberPhoto = MutableLiveData<Bitmap?>()
    val photo: LiveData<Bitmap?> = memberPhoto
    private var memberSubDate = MutableLiveData<Long>()
    val date: LiveData<Long> = memberSubDate
    private var payment: Payment? = null
    var userId: Int? = null
    private var _selectedUser = MutableLiveData<User?>()
    val selectedUser: LiveData<User?> = _selectedUser


    fun setName(text: String) {
        memberName.postValue(text)
    }


    fun setPhoto(bitmap: Bitmap) {
        memberPhoto.postValue(bitmap)
    }

    fun setSubDate(date: Long) {
        memberSubDate.postValue(date)
        userId?.let {
            payment = Payment.fromDays(it, date)
        } ?: Payment.fromDays(-1, date).also {
            // To handle payment being null if user isn't editing.
            // Handled in FormaDao.
            payment = it
        }
    }

    fun setSubDate(date: Long, months: Int) {
        memberSubDate.value = date
        userId?.let {
            payment = Payment.fromMonths(it, months)
        } ?: Payment.fromMonths(-1, months).also {
            // To handle payment being null if user isn't editing.
            // Handled in FormaDao.
            payment = it
        }


    }

    fun deletePhoto() {
        memberPhoto.postValue(null)
        Log.d("image", "deletePhoto: ${memberPhoto.value.toString()}")
    }

    fun saveMember() {
        viewModelScope.launch {
            val ct = System.currentTimeMillis()
            selectedUser.value?.let {
                val editedMember = it
                editedMember.apply {
                    name = memberName.value!!
                    memberPhoto = photo.value
                    subscribeStartDate = ct
                    subscribeEndDate = memberSubDate.value!!
                }

                Log.d(TAG, "saveMember: $editedMember")
                // If payment is set, then save the user with payment.
                payment?.let { payment ->
                    Log.d(TAG, "saveMember: $payment")
                    dao.saveUserWithPayment(editedMember, payment)
                }

            } ?: if (!memberName.value.isNullOrEmpty() && memberSubDate.value != null) {
                val newUser = User(memberName.value!!, ct, memberSubDate.value!!, memberPhoto.value)
                Log.d(TAG, "saveNewUser`: $newUser")
                payment?.let { payment ->
                    Log.d(TAG, "saveMember: $payment")
                    dao.saveUserWithPayment(newUser, payment)
                }
            }
        }
    }

    fun deleteMember() {
        viewModelScope.launch {
            _selectedUser.value?.let {
                dao.removeUser(it)
            }
        }
    }


    fun searchIfUserExists(userId: Int) {
        this.userId = userId
        viewModelScope.launch {
            _selectedUser.value = dao.getUserByID(userId)
        }
    }


    companion object {
        private const val TAG = "DetailsScreen"
    }


}
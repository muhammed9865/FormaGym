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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val dao: FormaDao) : ViewModel() {
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

    private var _paymentPrice: Double = 0.0

    private val _responses = MutableLiveData<SaveResponse>()
    val response: LiveData<SaveResponse> = _responses

    fun setName(text: String) {
        memberName.value = text
    }


    fun setPhoto(bitmap: Bitmap) {
        memberPhoto.postValue(bitmap)
    }

    fun setSubDate(date: Long) {
        memberSubDate.postValue(date)
    }

    fun setPaymentPrice(price: Double) {
        _paymentPrice = price
    }

    /* Was used before generalizing the counting technique
     fun setSubDate(date: Long, months: Int) {
         memberSubDate.value = date
         userId?.let {
             payment = Payment.fromDays(memberName.value!!, it, date)
         } ?: Payment.fromDays(memberName.value!!, -1, date).also {
             // To handle payment being null if user isn't editing.
             // Handled in FormaDao.
             payment = it
         }

     }*/

    fun deletePhoto() {
        memberPhoto.postValue(null)
        Log.d("image", "deletePhoto: ${memberPhoto.value.toString()}")
    }

    fun saveMember() {
        viewModelScope.launch {
            val ct = System.currentTimeMillis()
            // If the user exists and i'm just editing him.
            selectedUser.value?.let {
                val editedMember = it
                editedMember.apply {
                    name = memberName.value!!
                    memberPhoto = photo.value
                    subscribeStartDate = ct
                    subscribeEndDate = memberSubDate.value!!
                    paymentPrice = _paymentPrice
                }
                payment = Payment.fromDays(
                    memberName.value!!,
                    it.id,
                    memberSubDate.value!!,
                    _paymentPrice
                )
                // If payment is set, then save the user with payment.
                Log.d(TAG, "saveMember: $payment")
                dao.saveUserWithPayment(editedMember, payment!!)
                _responses.value = SaveResponse.SavedSuccessfully


                // If User doesn't exist, then create a new user..
            } ?: if (!memberName.value.isNullOrEmpty() && memberSubDate.value != null && _paymentPrice != 0.0) {
                val newUser = User(
                    memberName.value!!,
                    ct,
                    memberSubDate.value!!,
                    memberPhoto.value,
                    _paymentPrice
                )
                payment = Payment.fromDays(
                    memberName.value!!,
                    -1,
                    memberSubDate.value!!,
                    _paymentPrice
                )

                dao.saveUserWithPayment(newUser, payment!!)
                _responses.value = SaveResponse.SavedSuccessfully

            } else {
                _responses.value = SaveResponse.EmptyBoxError
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
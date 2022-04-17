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
import java.lang.Exception
import java.util.*

class DetailsViewModel constructor(private val dao: FormaDao) : ViewModel() {
    private var memberName = MutableLiveData<String>()
    val name: LiveData<String> = memberName
    private var memberPhoto = MutableLiveData<Bitmap?>()
    val photo: LiveData<Bitmap?> = memberPhoto
    private var memberSubDate = MutableLiveData<Long>()
    val date: LiveData<Long> = memberSubDate
    private var payment: Payment? = null
    var userId: Int? = null

    fun setName(text: String) {
        memberName.postValue(text)
    }


    fun setPhoto(bitmap: Bitmap) {
        memberPhoto.postValue(bitmap)
    }

    fun setSubDate(date: Long) {
        userId?.let { payment = Payment.fromDays(it, date) }
        memberSubDate.postValue(date)
    }

    fun setSubDate(date: Long, months: Int) {
        userId?.let { payment = Payment.fromMonths(it, months) }
        memberSubDate.value = date

    }

    fun deletePhoto() {
        memberPhoto.postValue(null)
        Log.d("image", "deletePhoto: ${memberPhoto.value.toString()}")
    }

    fun saveMember(user: User?) {
        viewModelScope.launch {
            val ct = System.currentTimeMillis()
            user?.let {
                val editedMember = it
                editedMember.apply {
                    name = memberName.value!!
                    memberPhoto = photo.value
                    subscribeStartDate = ct
                    subscribeEndDate = memberSubDate.value!!
                }

                // If payment is set, then save the user with payment.
                payment?.let { payment -> dao.saveUserWithPayment(editedMember, payment) }

            } ?: if (!memberName.value.isNullOrEmpty() && memberSubDate.value != null) {
                val newUser = User(memberName.value!!, ct, memberSubDate.value!!, memberPhoto.value)
                payment?.let { payment -> dao.saveUserWithPayment(newUser, payment) }
            }
        }
    }


    companion object {
        private const val TAG = "DetailsScreen"
    }


}
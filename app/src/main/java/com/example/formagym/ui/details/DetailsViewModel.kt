package com.example.formagym.ui.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.formagym.R
import com.example.formagym.pojo.model.Member

class DetailsViewModel: ViewModel() {
    private var memberName = MutableLiveData<String>()
    val name: LiveData<String> = memberName
    private var memberPhoto = MutableLiveData<Bitmap?>()
    val photo: LiveData<Bitmap?> = memberPhoto
    private var memberSubDate = MutableLiveData<Long>()
    val date: LiveData<Long> = memberSubDate


    fun setName(text: String) {
        memberName.postValue(text)
    }


    fun setPhoto(bitmap: Bitmap) {
        memberPhoto.postValue(bitmap)
    }

    fun setSubDate(date: Long) {
        memberSubDate.postValue(date)
    }

    fun deletePhoto() {
        memberPhoto.postValue(null)
        Log.d("image", "deletePhoto: ${memberPhoto.value.toString()}")
    }

    fun saveMember(member: Member?): Member? {
        val ct = System.currentTimeMillis()
        member?.let {
            val editedMember = it
            editedMember.apply {
                name = memberName.value!!
                memberPhoto = photo.value
                subscribeStartDate = ct
                subscribeEndDate = memberSubDate.value!!
            }
            return editedMember

        } ?:
        if (!memberName.value.isNullOrEmpty() && memberSubDate.value != null) {
            Log.d(TAG, "saveMember: ${memberSubDate.value}")
            return Member(memberName.value!!, ct, memberSubDate.value!!, memberPhoto.value)
        }
        return null
    }

    companion object {
        private const val TAG = "DetailsScreen"
    }


}
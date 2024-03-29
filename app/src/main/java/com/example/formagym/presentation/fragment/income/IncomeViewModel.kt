package com.example.formagym.presentation.fragment.income

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.data.datasource.FormaDao_FormaRepositoryImpl
import com.example.formagym.data.model.Payment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(private val dao: FormaDao_FormaRepositoryImpl): ViewModel() {
    private val _paymentsList = MutableLiveData<List<Payment>>(mutableListOf())
    val paymentsList: LiveData<List<Payment>> = _paymentsList

    private val _totalIncome = MutableLiveData<Double>(0.0)
    val totalIncome: LiveData<Double> = _totalIncome

    val calculatedIncome = MutableLiveData(0)
    fun fetchPayments() {
        viewModelScope.launch {
            _paymentsList.value = dao.getAllPayments()
        }
    }

    fun getTotalIncome() {
        viewModelScope.launch {
            _totalIncome.value = dao.getTotalIncome()
        }
    }


    fun deletePayment(payment: Payment):LiveData<Boolean> {
        val result = MutableLiveData(false)
        viewModelScope.launch(Dispatchers.IO) {
            dao.deletePayment(payment)
            withContext(Dispatchers.Main) {
                result.value = true
            }
        }
        return result
    }
    

    fun getAverageIncomeBetweenTwoDates(from: Long, to: Long) {
        viewModelScope.launch {
            Log.d(TAG, "getAverageIncomeBetweenTwoDates: from: $from, to: $to")
            calculatedIncome.value = dao.getAvgIncomeBetweenTwoDates(from, to)
        }
    }

    fun getTotalIncomeBetweenTwoDates(from: Long, to: Long) {
        viewModelScope.launch {
            Log.d(TAG, "getAverageIncomeBetweenTwoDates: from: $from, to: $to")
            calculatedIncome.value = dao.getTotalIncomeBetweenTwoDates(from, to)
        }
    }

    companion object {
        private const val TAG = "IncomeViewModel"
    }

}
package com.example.formagym.ui.fragment.income

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formagym.pojo.datasource.FormaDao
import com.example.formagym.pojo.model.Payment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeViewModel @Inject constructor(private val dao: FormaDao): ViewModel() {
    private val _paymentsList = MutableLiveData<List<Payment>>(mutableListOf())
    val paymentsList: LiveData<List<Payment>> = _paymentsList

    private val _totalIncome = MutableLiveData<Double>(0.0)
    val totalIncome: LiveData<Double> = _totalIncome

    private val _avgIncome = MutableLiveData<Double>(0.0)
    val avgIncome: LiveData<Double> = _avgIncome


    val calculatedAvgIncome = MutableLiveData(0)
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

    fun getAverageIncome() {
        viewModelScope.launch {
            _avgIncome.value = dao.getAvgIncome()
        }
    }

    fun getAverageIncomeBetweenTwoDates(from: Long, to: Long) {
        viewModelScope.launch {
            Log.d(TAG, "getAverageIncomeBetweenTwoDates: from: $from, to: $to")
            calculatedAvgIncome.value = dao.getAvgIncomeBetweenTwoDates(from, to)
        }
    }

    companion object {
        private const val TAG = "IncomeViewModel"
    }

}
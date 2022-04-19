package com.example.formagym.utils

import android.app.DatePickerDialog
import android.content.Context
import java.util.*

abstract class DatePickerHelper {
    companion object {
        fun selectDate(context: Context, setToCurrentDate: Boolean, onDateSelected: (milli: Long) -> Unit) {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(context, { datePicker, _, _, _ ->
                cal.apply {
                    set(Calendar.YEAR, datePicker.year)
                    set(Calendar.MONTH, datePicker.month)
                    set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
                    onDateSelected(timeInMillis)
                }

            }, year, month, day)

            // Setting the minium date to be chosen to today's date.
            if (setToCurrentDate) datePicker.datePicker.minDate = cal.timeInMillis

            datePicker.show()
        }


    }
}
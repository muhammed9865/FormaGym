package com.example.formagym.presentation.adapter.paymentadapter

import androidx.recyclerview.widget.RecyclerView
import com.example.formagym.databinding.ListItemPaymentBinding
import com.example.formagym.data.model.Payment
import com.example.formagym.utils.getDateAsString
import com.example.formagym.utils.setBottomMargin

class PaymentsViewHolder(private val binding: ListItemPaymentBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(payment: Payment, isLastPosition: Boolean) = with(binding) {
        userNameTv.text = payment.userName
        moneyPaidTv.text = payment.moneyPaid.toString()
        paymentDateTv.text = getDateAsString(payment.date)

        if (isLastPosition) {
            paymentCardLayout.setBottomMargin(190)
        }

    }
}
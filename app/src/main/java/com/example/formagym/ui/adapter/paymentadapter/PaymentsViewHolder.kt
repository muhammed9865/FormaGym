package com.example.formagym.ui.adapter.paymentadapter

import androidx.recyclerview.widget.RecyclerView
import com.example.formagym.databinding.ListItemPaymentBinding
import com.example.formagym.pojo.model.Payment
import com.example.formagym.utils.getDateAsString

class PaymentsViewHolder(private val binding: ListItemPaymentBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(payment: Payment) = with(binding) {
        userNameTv.text = payment.userName
        moneyPaidTv.text = payment.moneyPaid.toString()
        paymentDateTv.text = getDateAsString(payment.date)

    }
}
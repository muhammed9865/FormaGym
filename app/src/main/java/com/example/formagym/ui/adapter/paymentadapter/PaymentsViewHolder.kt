package com.example.formagym.ui.adapter.paymentadapter

import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.formagym.databinding.ListItemPaymentBinding
import com.example.formagym.pojo.model.Payment
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
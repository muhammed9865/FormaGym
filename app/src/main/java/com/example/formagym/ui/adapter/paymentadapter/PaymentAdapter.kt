package com.example.formagym.ui.adapter.paymentadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.formagym.databinding.ListItemPaymentBinding
import com.example.formagym.pojo.model.Payment

class PaymentAdapter: ListAdapter<Payment, PaymentsViewHolder>(PaymentDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsViewHolder {
        val binding = ListItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  PaymentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class PaymentDiffCallback: DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem.paymentId == newItem.paymentId
        }

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return  oldItem == newItem
        }
    }
}
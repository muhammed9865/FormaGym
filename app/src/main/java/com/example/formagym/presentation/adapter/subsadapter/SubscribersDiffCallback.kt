package com.example.formagym.presentation.adapter.subsadapter

import androidx.recyclerview.widget.DiffUtil
import com.example.formagym.data.model.User

class SubscribersDiffCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
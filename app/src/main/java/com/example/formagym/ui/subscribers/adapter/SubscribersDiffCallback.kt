package com.example.formagym.ui.subscribers.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.formagym.pojo.model.Member

class SubscribersDiffCallback: DiffUtil.ItemCallback<Member>() {
    override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
        return oldItem == newItem
    }
}
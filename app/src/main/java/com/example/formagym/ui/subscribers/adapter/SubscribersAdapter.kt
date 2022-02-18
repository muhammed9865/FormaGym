package com.example.formagym.ui.subscribers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.formagym.R
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.pojo.model.Member

class SubscribersAdapter : ListAdapter<Member, SubscribersViewHolder>(SubscribersDiffCallback()) {
    private var selectedMember: SelectedMember? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribersViewHolder {
        val binding: ListItemMemberBinding = DataBindingUtil
            .inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_member,
            parent,
            false
        )
        return SubscribersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribersViewHolder, position: Int) {
        holder.bind(getItem(position), selectedMember)
    }

    fun onMemberSelected(selectedMember: SelectedMember) {
        this.selectedMember = selectedMember
    }
}
package com.example.formagym.presentation.adapter.subsadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.formagym.R
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.data.model.User

class SubscribersAdapter : ListAdapter<User, SubscribersViewHolder>(SubscribersDiffCallback()) {
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
        if (currentList.lastIndex == position) {
            holder.bind(getItem(position), selectedMember, true)
        }else {
            holder.bind(getItem(position), selectedMember, false)
        }
    }

    fun onMemberSelected(selectedMember: SelectedMember) {
        this.selectedMember = selectedMember
    }
}
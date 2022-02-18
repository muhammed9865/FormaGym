package com.example.formagym.ui.subscribers.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.getData
import com.example.formagym.pojo.model.Member

class SubscribersViewHolder(private val binding: ListItemMemberBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(member: Member, selectedMember: SelectedMember?) = with(binding) {
        memberName.text = member.name
        memberSubStart.text = getData(member.subscribeStartDate)
        memberSubEnd.text = getData(member.subscribeEndDate)
        memberPhoto.load(member.memberPhoto)

        memberShowDetails.setOnClickListener {
            selectedMember?.onSelectedMember(member)
        }
    }


}
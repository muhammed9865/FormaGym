package com.example.formagym.ui.subscribers.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.example.formagym.R
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.getData
import com.example.formagym.pojo.model.Member

class SubscribersViewHolder(private val binding: ListItemMemberBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(member: Member, selectedMember: SelectedMember?) = with(binding) {
        memberName.text = member.name
        memberSubStart.text = getData(member.subscribeStartDate)
        memberSubEnd.apply {
            text = getData(member.subscribeEndDate)
            val color = if (isSubscriptionFinished(member.subscribeEndDate)) {
                Color.RED
            }else {
                Color.GREEN
            }
            setTextColor(color)
        }

        member.memberPhoto?.let { memberPhoto.load(it) }
        memberShowDetails.setOnClickListener {
            selectedMember?.onSelectedMember(member)
        }
    }

    private fun isSubscriptionFinished(date: Long): Boolean {
        if (System.currentTimeMillis() > date){
            return true
        }
        return false
    }


}
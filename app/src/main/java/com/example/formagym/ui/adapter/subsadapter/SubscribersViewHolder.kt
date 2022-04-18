package com.example.formagym.ui.adapter.subsadapter

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.pojo.model.User
import com.example.formagym.utils.getDateAsString

class SubscribersViewHolder(private val binding: ListItemMemberBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User, selectedMember: SelectedMember?) = with(binding) {
        memberName.text = user.name
        memberSubStart.text = getDateAsString(user.subscribeStartDate)
        memberSubEnd.apply {
            text = getDateAsString(user.subscribeEndDate)
            val color = if (isSubscriptionFinished(user.subscribeEndDate)) {
                Color.RED
            }else {
                Color.GREEN
            }
            setTextColor(color)
        }

        user.memberPhoto?.let { memberPhoto.load(it) }
        memberShowDetails.setOnClickListener {
            selectedMember?.onSelectedMember(user.id)
        }
    }

    private fun isSubscriptionFinished(date: Long): Boolean {
        if (System.currentTimeMillis() > date){
            return true
        }
        return false
    }


}
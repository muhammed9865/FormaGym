package com.example.formagym.ui.adapter.subsadapter

import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.formagym.R
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.pojo.model.User
import com.example.formagym.utils.getDateAsString
import com.google.android.material.bottomsheet.BottomSheetDialog

class SubscribersViewHolder(private val binding: ListItemMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User, selectedMember: SelectedMember?) = with(binding) {
        memberName.text = user.name
        memberSubStart.text = getDateAsString(user.subscribeStartDate)
        memberSubEnd.apply {
            text = getDateAsString(user.subscribeEndDate)
            val color = if (isSubscriptionFinished(user.subscribeEndDate)) {
                Color.RED
            } else {
                Color.GREEN
            }
            setTextColor(color)
        }

        userCard.setOnLongClickListener {
            selectedMember?.onSelectedMember(user.id)
            true
        }

        val emptyPersonPhoto = ResourcesCompat.getDrawable(itemView.context.resources, R.drawable.ic_baseline_person_24, null)
        user.memberPhoto?.let { memberPhoto.load(it) } ?: memberPhoto.load(emptyPersonPhoto)

        memberShowDetails.setOnClickListener {
            selectedMember?.onSelectedMember(user.id)
        }
    }

    private fun isSubscriptionFinished(date: Long): Boolean {
        if (System.currentTimeMillis() > date) {
            return true
        }
        return false
    }


}
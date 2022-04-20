package com.example.formagym.ui.adapter.subsadapter

import android.graphics.Color
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.formagym.R
import com.example.formagym.databinding.ListItemMemberBinding
import com.example.formagym.pojo.model.User
import com.example.formagym.utils.getDateAsString
import com.example.formagym.utils.setBottomMargin

class SubscribersViewHolder(private val binding: ListItemMemberBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User, selectedMember: SelectedMember?, isLastPosition: Boolean) = with(binding) {
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

        userCardLayout.setOnLongClickListener {
            selectedMember?.onSelectedMember(user.id)
            true
        }
        memberShowDetails.setOnClickListener {
            selectedMember?.onSelectedMember(user.id)
        }

        if (isLastPosition) {
            userCardLayout.setBottomMargin(170)
        }


        val emptyPersonPhoto = ResourcesCompat.getDrawable(itemView.context.resources, R.drawable.ic_baseline_person_24, null)
        user.memberPhoto?.let { memberPhoto.load(it) } ?: memberPhoto.load(emptyPersonPhoto)


    }

    private fun isSubscriptionFinished(date: Long): Boolean {
        if (System.currentTimeMillis() > date) {
            return true
        }
        return false
    }


}
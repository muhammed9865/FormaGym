package com.example.formagym.ui.fragment.subscribers.adapter

import com.example.formagym.pojo.model.User

interface SelectedMember {
    fun onSelectedMember(user: User)
}
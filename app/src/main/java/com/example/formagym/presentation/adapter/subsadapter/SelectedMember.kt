package com.example.formagym.presentation.adapter.subsadapter

import com.example.formagym.data.model.User

interface SelectedMember {
    fun onSelectedMember(user: User)
}
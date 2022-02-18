package com.example.formagym.ui.subscribers.adapter

import com.example.formagym.pojo.model.Member

interface SelectedMember {
    fun onSelectedMember(member: Member)
}
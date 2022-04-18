package com.example.formagym.ui.fragment.details

sealed class SaveResponse {
    object SavedSuccessfully : SaveResponse()
    object EmptyBoxError : SaveResponse()
}

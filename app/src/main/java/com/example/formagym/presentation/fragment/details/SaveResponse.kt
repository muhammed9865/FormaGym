package com.example.formagym.presentation.fragment.details

sealed class SaveResponse {
    object SavedSuccessfully : SaveResponse()
    object EmptyBoxError : SaveResponse()
}

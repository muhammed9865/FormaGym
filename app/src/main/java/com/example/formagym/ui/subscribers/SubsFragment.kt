package com.example.formagym.ui.subscribers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.formagym.R
import com.example.formagym.databinding.FragmentSubsBinding

class SubsFragment : Fragment() {
    private lateinit var binding: FragmentSubsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubsBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

}
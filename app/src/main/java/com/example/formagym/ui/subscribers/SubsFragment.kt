package com.example.formagym.ui.subscribers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formagym.databinding.FragmentSubsBinding
import com.example.formagym.ui.subscribers.adapter.SubscribersAdapter
import com.example.formagym.ui.viewmodel.SubsViewModel
import kotlinx.coroutines.runBlocking

class SubsFragment : Fragment() {
    private lateinit var binding: FragmentSubsBinding
    private val mainViewModel: SubsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubsBinding.inflate(inflater, container, false)
        val adapter = SubscribersAdapter()

        /*mainViewModel.activeSubs.observe(requireActivity()) {*/
            adapter.submitList(mainViewModel.dummyData())
            setupSubsRv(adapter)




        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupSubsRv(mAdapter: SubscribersAdapter) {
        binding.subsRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}
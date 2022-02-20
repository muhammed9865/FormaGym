package com.example.formagym.ui.inactive

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formagym.R
import com.example.formagym.databinding.FragmentInactiveBinding
import com.example.formagym.pojo.model.Member
import com.example.formagym.ui.subscribers.adapter.SelectedMember
import com.example.formagym.ui.subscribers.adapter.SubscribersAdapter
import com.example.formagym.ui.viewmodel.SubsViewModel
import kotlinx.coroutines.runBlocking

class InactiveFragment : Fragment(), SearchView.OnQueryTextListener {
    private val binding: FragmentInactiveBinding by lazy {
        FragmentInactiveBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val mainViewModel: SubsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = SubscribersAdapter()
        binding.searchInactive.setOnQueryTextListener(this)

        adapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(member: Member) {
                mainViewModel.onViewDetails(member)
                findNavController().navigate(R.id.action_inactiveFragment_to_detailsFragment)
            }
        })

        mainViewModel.inactiveSubs.observe(requireActivity()) { list ->
            runBlocking {
                adapter.submitList(list)
                if(list.isNotEmpty()) {
                    binding.emptyMembers.visibility = View.GONE
                }else {
                    binding.emptyMembers.visibility = View.VISIBLE
                }
            }
        }

        setupInactiveRv(adapter)

        // setupInactiveRv(adapter)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupInactiveRv(mAdapter: SubscribersAdapter) {
        binding.inactiveSubsRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            mainViewModel.searchInActives(query)
        } ?: mainViewModel.getInactiveMembers()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            mainViewModel.searchInActives(query)
        } ?: mainViewModel.getInactiveMembers()
        return true
    }


}
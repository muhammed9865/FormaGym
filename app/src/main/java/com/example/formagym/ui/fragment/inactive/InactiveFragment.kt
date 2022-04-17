package com.example.formagym.ui.fragment.inactive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formagym.R
import com.example.formagym.databinding.FragmentInactiveBinding
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.pojo.model.User
import com.example.formagym.ui.fragment.active.adapter.SelectedMember
import com.example.formagym.ui.fragment.active.adapter.SubscribersAdapter
import com.example.formagym.ui.fragment.inactive.viewmodel.InactiveViewModel
import com.example.formagym.ui.fragment.inactive.viewmodel.InactiveViewModelFactory
import com.example.formagym.ui.viewmodel.MainViewModel

class InactiveFragment : Fragment(), SearchView.OnQueryTextListener {
    private val binding: FragmentInactiveBinding by lazy {
        FragmentInactiveBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: InactiveViewModel by viewModels {
        InactiveViewModelFactory(FormaDatabase.getInstance(requireContext()))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = SubscribersAdapter()
        binding.searchInactive.setOnQueryTextListener(this)

        adapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(userId: Int) {
                mainViewModel.onViewDetails(userId)
                findNavController().navigate(R.id.action_inactiveFragment_to_detailsFragment)
            }
        })

        viewModel.inactiveMembers.observe(requireActivity()) { list ->
                adapter.submitList(list)
                if(list.isNotEmpty()) {
                    binding.emptyMembers.visibility = View.GONE
                }else {
                    binding.emptyMembers.visibility = View.VISIBLE
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
            viewModel.searchActiveMembers(query)
        } ?: viewModel.getInactiveMembers()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            viewModel.searchActiveMembers(query)
        } ?: viewModel.getInactiveMembers()
        return true
    }


}
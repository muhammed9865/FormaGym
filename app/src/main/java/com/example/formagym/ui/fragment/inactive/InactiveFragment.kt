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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.formagym.R
import com.example.formagym.databinding.FragmentInactiveBinding
import com.example.formagym.ui.adapter.subsadapter.SelectedMember
import com.example.formagym.ui.adapter.subsadapter.SubscribersAdapter
import com.example.formagym.ui.mainviewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InactiveFragment : Fragment(), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private val binding: FragmentInactiveBinding by lazy {
        FragmentInactiveBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: InactiveViewModel by viewModels()
    @Inject
    lateinit var adapter: SubscribersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.apply {
            searchInactive.setOnQueryTextListener(this@InactiveFragment)
            inactiveRefresher.setOnRefreshListener(this@InactiveFragment)
        }

        viewModel.getInactiveMembers()
        mainViewModel.getInactiveMembersLength()

        adapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(userId: Int) {
                mainViewModel.onViewDetails(userId)
                findNavController().navigate(R.id.action_inactiveFragment_to_detailsFragment)
            }
        })

        viewModel.inactiveMembers.observe(requireActivity()) { list ->
                adapter.submitList(list)
            binding.inactiveRefresher.isRefreshing = false
                if(list.isNotEmpty()) {
                    binding.emptyMembers.visibility = View.GONE
                }else {
                    binding.emptyMembers.visibility = View.VISIBLE
                }
        }

        setupInactiveRv(adapter)


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

    override fun onRefresh() {
        viewModel.getInactiveMembers()
        mainViewModel.getInactiveMembersLength()
        binding.inactiveRefresher.isRefreshing = false
    }


}
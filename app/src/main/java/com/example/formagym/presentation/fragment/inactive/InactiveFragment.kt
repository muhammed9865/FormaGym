package com.example.formagym.presentation.fragment.inactive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.formagym.R
import com.example.formagym.data.model.User
import com.example.formagym.databinding.FragmentInactiveBinding
import com.example.formagym.presentation.activity.main.MainViewModel
import com.example.formagym.presentation.adapter.subsadapter.SelectedMember
import com.example.formagym.presentation.adapter.subsadapter.SubscribersAdapter
import com.example.formagym.presentation.fragment.common.member_options.MemberOptionsDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InactiveFragment : Fragment(), SearchView.OnQueryTextListener,
    SwipeRefreshLayout.OnRefreshListener {
    private val binding: FragmentInactiveBinding by lazy {
        FragmentInactiveBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: InactiveViewModel by viewModels()

    @Inject
    lateinit var adapter: SubscribersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding.apply {
            searchInactive.setOnQueryTextListener(this@InactiveFragment)
            inactiveRefresher.setOnRefreshListener(this@InactiveFragment)
        }

        viewModel.getInactiveMembers()
        mainViewModel.getInactiveMembersDetails()

        adapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(user: User) {
                showMemberOptionsDialog(user)
            }
        })

        mainViewModel.inactiveMembers.observe(requireActivity()) { list ->
            adapter.submitList(list)
            binding.inactiveRefresher.isRefreshing = false
            if (list.isNotEmpty()) {
                binding.emptyMembers.visibility = View.GONE
            } else {
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

    private fun showMemberOptionsDialog(user: User) {
        val optionsDialog = MemberOptionsDialog(user)
        optionsDialog.setOnShowDetailsListener { _, _ ->
            mainViewModel.setUserIdForDetails(user.id)
            findNavController().navigate(R.id.action_inactiveFragment_to_detailsFragment)
        }
        optionsDialog.show(parentFragmentManager, "options")
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            mainViewModel.searchInActiveMembers(query)
        } ?: mainViewModel.refreshInactives()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            mainViewModel.searchInActiveMembers(query)
        } ?: mainViewModel.refreshInactives()
        return true
    }

    override fun onRefresh() {
        mainViewModel.refreshInactives()
        mainViewModel.getInactiveMembersDetails()
        // binding.inactiveRefresher.isRefreshing = false
    }


}
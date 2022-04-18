package com.example.formagym.ui.fragment.active

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
import com.example.formagym.databinding.FragmentSubsBinding
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.ui.adapter.subsadapter.SelectedMember
import com.example.formagym.ui.adapter.subsadapter.SubscribersAdapter
import com.example.formagym.ui.mainviewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActiveFragment : Fragment(), SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentSubsBinding
    private val viewModel: ActiveViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    private lateinit var mAdapter: SubscribersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubsBinding.inflate(inflater, container, false)

        binding.apply {
            progressBar.visibility = View.VISIBLE
            searchActive.setOnQueryTextListener(this@ActiveFragment)
            activeRefresher.setOnRefreshListener(this@ActiveFragment)
        }

        viewModel.getActiveMembers()
        mainViewModel.getActiveMembersLength()


        // On ViewDetails button clicked, it will navigate to details frag
        mAdapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(userId: Int) {
                mainViewModel.onViewDetails(userId)
                findNavController().navigate(R.id.action_activeFragment_to_detailsFragment)
            }
        })

        viewModel.activeMembers.observe(viewLifecycleOwner) {
            binding.activeRefresher.isRefreshing = false
            mAdapter.submitList(it)
            binding.progressBar.visibility = View.GONE
            if (it.isEmpty()) {
                binding.emptyMembers.visibility = View.VISIBLE
            } else {
                binding.emptyMembers.visibility = View.GONE
            }

        }


        setupSubsRv()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupSubsRv() {
        binding.subsRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        private const val TAG = "SubsFragment"
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            viewModel.searchActiveMembers(query)
        } ?: viewModel.getActiveMembers()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            viewModel.searchActiveMembers(query)
        } ?: viewModel.getActiveMembers()
        return true
    }

    override fun onRefresh() {
        viewModel.getActiveMembers()
        mainViewModel.getActiveMembersLength()

    }

}
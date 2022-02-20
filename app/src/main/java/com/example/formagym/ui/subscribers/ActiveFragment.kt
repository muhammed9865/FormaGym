package com.example.formagym.ui.subscribers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.formagym.R
import com.example.formagym.databinding.FragmentSubsBinding
import com.example.formagym.pojo.model.Member
import com.example.formagym.ui.subscribers.adapter.SelectedMember
import com.example.formagym.ui.subscribers.adapter.SubscribersAdapter
import com.example.formagym.ui.utils.PreLoadingLinearLayoutManager
import com.example.formagym.ui.viewmodel.SubsViewModel
import kotlinx.coroutines.runBlocking

class ActiveFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentSubsBinding
    private val mainViewModel: SubsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubsBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        binding.searchActive.setOnQueryTextListener(this)
        val adapter = SubscribersAdapter()

        // On ViewDetails button clicked, it will navigate to details frag
        adapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(member: Member) {
                mainViewModel.onViewDetails(member)
                findNavController().navigate(R.id.action_activeFragment_to_detailsFragment)
            }
        })

        mainViewModel.activeSubs.observe(this) {
            it?.let {
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.progressBar.visibility = View.GONE
                    binding.emptyMembers.visibility = View.VISIBLE
                }else {
                    binding.progressBar.visibility = View.GONE
                    binding.emptyMembers.visibility = View.GONE
                }
            }
        }
        binding.subsRv.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                binding.progressBar.visibility = View.GONE
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (adapter.currentList.isEmpty()) {
                    binding.emptyMembers.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.emptyMembers.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        })


        setupSubsRv(adapter, mainViewModel.activeSubs.value?.size ?: 0)

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setupSubsRv(mAdapter: SubscribersAdapter, size: Int) {
        binding.subsRv.apply {
            adapter = mAdapter
            layoutManager = PreLoadingLinearLayoutManager(requireContext(), size)
        }
    }

    companion object {
        private const val TAG = "SubsFragment"
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            mainViewModel.searchActives(query)
        } ?: mainViewModel.getActiveMembers()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            mainViewModel.searchActives(query)
        } ?: mainViewModel.getInactiveMembers()
        return true
    }

}
package com.example.formagym.ui.fragment.subscribers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.formagym.R
import com.example.formagym.databinding.FragmentSubsBinding
import com.example.formagym.pojo.model.User
import com.example.formagym.ui.fragment.subscribers.adapter.SelectedMember
import com.example.formagym.ui.fragment.subscribers.adapter.SubscribersAdapter
import com.example.formagym.ui.viewmodel.SubsViewModel

class ActiveFragment : Fragment(), SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentSubsBinding
    private val mainViewModel: SubsViewModel by activityViewModels()
    private val mAdapter: SubscribersAdapter by lazy { SubscribersAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSubsBinding.inflate(inflater, container, false)
        binding.apply {
            progressBar.visibility = View.VISIBLE
            searchActive.setOnQueryTextListener(this@ActiveFragment)
        }

        mainViewModel

        // On ViewDetails button clicked, it will navigate to details frag
        mAdapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(user: User) {
                mainViewModel.onViewDetails(user)
                findNavController().navigate(R.id.action_activeFragment_to_detailsFragment)
            }
        })

        mainViewModel.activeSubs.observe(viewLifecycleOwner) {
            it?.let {
                mAdapter.submitList(it)
                if (it.isEmpty()) {
                    binding.progressBar.visibility = View.GONE
                    binding.emptyMembers.visibility = View.VISIBLE
                } else {
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
                if (mAdapter.currentList.isEmpty()) {
                    binding.emptyMembers.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.emptyMembers.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
            }
        })


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
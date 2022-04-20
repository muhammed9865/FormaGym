package com.example.formagym.ui.fragment.inactive

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import com.example.formagym.R
import com.example.formagym.databinding.FragmentInactiveBinding
import com.example.formagym.databinding.UserOptionsBottomSheetBinding
import com.example.formagym.ui.adapter.paymentadapter.PaymentAdapter
import com.example.formagym.ui.adapter.subsadapter.SelectedMember
import com.example.formagym.ui.adapter.subsadapter.SubscribersAdapter
import com.example.formagym.ui.mainviewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
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
               showMemberOptionsDialog(userId)
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

    private fun showMemberOptionsDialog(userId: Int) {
        val _binding = UserOptionsBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheet = BottomSheetDialog(requireContext())
        // Handling ClickListeners and Displaying User Data
        viewModel.searchMemberByID(userId).observe(viewLifecycleOwner) {
            it.let { user ->
                _binding.apply {
                    // Setting User details
                    val emptyPhoto = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.ic_baseline_person_24,
                        null
                    )
                    user.memberPhoto?.let { photo -> userPhoto.load(photo) }
                        ?: userPhoto.load((emptyPhoto))
                    userName.text = user.name

                    showDetails.setOnClickListener {
                        mainViewModel.onViewDetails(userId)
                        bottomSheet.cancel()
                        findNavController().navigate(R.id.action_inactiveFragment_to_detailsFragment)
                    }

                    showPayments.setOnClickListener {
                        val mAdapter = PaymentAdapter()
                        userPaymentsRv.apply {
                            adapter = mAdapter
                            userPaymentsRv.layoutManager = LinearLayoutManager(requireContext())

                        }


                        viewModel.loadUserPayments(userId).observe(viewLifecycleOwner) { payments ->
                            mAdapter.submitList(payments)
                            userPaymentsRv.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        bottomSheet.setContentView(_binding.root)
        bottomSheet.show()

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
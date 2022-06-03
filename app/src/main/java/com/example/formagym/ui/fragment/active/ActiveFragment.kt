package com.example.formagym.ui.fragment.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import com.example.formagym.R
import com.example.formagym.databinding.FragmentActiveBinding
import com.example.formagym.databinding.UserOptionsBottomSheetBinding
import com.example.formagym.ui.adapter.paymentadapter.PaymentAdapter
import com.example.formagym.ui.adapter.subsadapter.SelectedMember
import com.example.formagym.ui.adapter.subsadapter.SubscribersAdapter
import com.example.formagym.ui.mainviewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActiveFragment : Fragment(), SearchView.OnQueryTextListener,
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: FragmentActiveBinding
    private val viewModel: ActiveViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var mAdapter: SubscribersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveBinding.inflate(inflater, container, false)

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
                showMemberOptionsDialog(userId)

            }
        })

        mainViewModel.activeMembers.observe(viewLifecycleOwner) {
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
                        mainViewModel.setUserIdForDetails(userId)
                        bottomSheet.cancel()
                        findNavController().navigate(R.id.action_activeFragment_to_detailsFragment)
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
            mainViewModel.searchActiveMembers(query)
        } ?: mainViewModel.refreshActives()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        query?.let {
            mainViewModel.searchActiveMembers(query)
        } ?: mainViewModel.refreshActives()
        return true
    }

    override fun onRefresh() {
        mainViewModel.refreshActives()
        mainViewModel.getActiveMembersLength()

    }

}
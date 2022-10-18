package com.example.formagym.presentation.fragment.active

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
import com.example.formagym.databinding.FragmentActiveBinding
import com.example.formagym.presentation.activity.main.MainViewModel
import com.example.formagym.presentation.adapter.subsadapter.SelectedMember
import com.example.formagym.presentation.adapter.subsadapter.SubscribersAdapter
import com.example.formagym.presentation.fragment.common.member_options.MemberOptionsDialog
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
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentActiveBinding.inflate(inflater, container, false)

        binding.apply {
            progressBar.visibility = View.VISIBLE
            searchActive.setOnQueryTextListener(this@ActiveFragment)
            activeRefresher.setOnRefreshListener(this@ActiveFragment)
        }


        mainViewModel.getActiveMembersDetails()


        // On ViewDetails button clicked, it will navigate to details frag
        mAdapter.onMemberSelected(object : SelectedMember {
            override fun onSelectedMember(user: User) {
                showMemberOptionsDialog(user)

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


    private fun showMemberOptionsDialog(user: User) {
        val optionsDialog = MemberOptionsDialog(user)
        optionsDialog.setOnShowDetailsListener { _, _ ->
            mainViewModel.setUserIdForDetails(user.id)
            findNavController().navigate(R.id.action_activeFragment_to_detailsFragment)
        }
        optionsDialog.show(parentFragmentManager, "options")
    }

    private fun setupSubsRv() {

        binding.subsRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
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
        mainViewModel.getActiveMembersDetails()

    }

    companion object {
        private const val TAG = "SubsFragment"
    }


    /*private fun testFunc() {
        val _binding = UserOptionsBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheet = BottomSheetDialog(requireContext())
        val optionFragment = MemberOptionsDialog(user).show(parentFragmentManager, "options")

        // Handling ClickListeners and Displaying User Data
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
                mainViewModel.setUserIdForDetails(user.id)
                bottomSheet.cancel()
                findNavController().navigate(R.id.action_activeFragment_to_detailsFragment)
            }

            showPayments.setOnClickListener {
                val mAdapter = PaymentAdapter()
                userPaymentsRv.apply {
                    adapter = mAdapter
                    userPaymentsRv.layoutManager = LinearLayoutManager(requireContext())

                }


                viewModel.loadUserPayments(user.id).observe(viewLifecycleOwner) { payments ->
                    mAdapter.submitList(payments)
                    userPaymentsRv.visibility = View.VISIBLE
                }
            }


        }



        bottomSheet.setContentView(_binding.root)
        bottomSheet.show()

    }*/
}
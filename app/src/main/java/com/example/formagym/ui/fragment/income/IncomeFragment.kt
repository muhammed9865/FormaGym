package com.example.formagym.ui.fragment.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formagym.databinding.FragmentIncomeBinding
import com.example.formagym.ui.adapter.paymentadapter.PaymentAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.floor

@AndroidEntryPoint
class IncomeFragment : Fragment() {
    private lateinit var binding: FragmentIncomeBinding
    private val viewModel: IncomeViewModel by viewModels()
    private val mAdapter: PaymentAdapter by lazy { PaymentAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeBinding.inflate(inflater, container, false)

        // Setting up Payments RV
        binding.paymentsRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Fetching data
        viewModel.fetchPayments()
        viewModel.getTotalIncome()
        viewModel.getAverageIncome()

        // Observing On Data changes
        viewModel.apply {
            paymentsList.observe(viewLifecycleOwner) {
                mAdapter.submitList(it)
            }

            totalIncome.observe(viewLifecycleOwner) {
                binding.totalIncomeTv.text = floor(it).toInt().toString()
            }

            avgIncome.observe(viewLifecycleOwner) {
                binding.avgIncomeTv.text = floor(it).toInt().toString()
            }
        }

        return binding.root
    }

}
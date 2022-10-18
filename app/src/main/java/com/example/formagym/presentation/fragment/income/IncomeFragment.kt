package com.example.formagym.presentation.fragment.income

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.formagym.databinding.AvgCalculationBottomSheetBinding
import com.example.formagym.databinding.FragmentIncomeBinding
import com.example.formagym.presentation.adapter.paymentadapter.PaymentAdapter
import com.example.formagym.utils.DatePickerHelper
import com.example.formagym.utils.getDateAsString
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
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

        // Observing On Data changes
        viewModel.apply {
            paymentsList.observe(viewLifecycleOwner) {
                mAdapter.submitList(it)
            }

            totalIncome.observe(viewLifecycleOwner) {
                it?.let {
                    binding.totalIncomeTv.text = floor(it).toInt().toString()
                }

            }

        }

        binding.calcAvergBtn.setOnClickListener { onCalculateAverageIncomeClicked() }

        return binding.root
    }


    private fun onCalculateAverageIncomeClicked() {
        val binding =
            AvgCalculationBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(binding.root)
        bottomSheet.show()
        var from: Long = 0
        var to: Long = 0
        binding.fromBtn.setOnClickListener {
            DatePickerHelper.selectDate(requireContext(), false) { time ->
                from = time
                binding.fromTv.text = getDateAsString(time)
            }
        }

        binding.toBtn.setOnClickListener {
            DatePickerHelper.selectDate(requireContext(), false) { time ->
                to = time
                binding.toTv.text = getDateAsString(time)
            }
        }
        var option = -1
        binding.calculatorOptions.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                binding.calculatorAvg.id -> option = 0
                binding.calculatorTotal.id -> option = 1
            }
        }

        binding.calcAvgBtn.setOnClickListener {
            when(option) {
                0 -> viewModel.getAverageIncomeBetweenTwoDates(from, to)
                1 -> viewModel.getTotalIncomeBetweenTwoDates(from, to)
            }
        }

        viewModel.calculatedIncome.observe(viewLifecycleOwner) {
            Log.d(TAG, "onCalculateAverageIncomeClicked: $it")
            binding.calculatedIncomeTv.text = it?.toString() ?: "0"
        }


    }

    companion object {
        private const val TAG = "IncomeFragment"
    }

}
package com.example.formagym.ui.fragment.details

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.formagym.*
import com.example.formagym.databinding.FragmentDetailsBinding
import com.example.formagym.pojo.model.User
import com.example.formagym.utils.checkForPermission
import com.example.formagym.utils.showError
import com.example.formagym.ui.mainviewmodel.MainViewModel
import com.example.formagym.utils.DatePickerHelper
import com.example.formagym.utils.getDateAsString
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailsFragment : Fragment(), View.OnCreateContextMenuListener {
    private val binding: FragmentDetailsBinding by lazy {
        FragmentDetailsBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setToolbarMenu()
        onNameChanged()
        setPaymentPrice()

        // Observing on Data changes
        viewModel.apply {
            with(binding) {
                // Observing on Photo changes
                photo.observe(viewLifecycleOwner) {
                    it?.let { memberPhotoDetails.load(it) }
                        ?: memberPhotoDetails.load(R.drawable.ic_baseline_person_24)
                }
                // Observing on Date changes
                date.observe(viewLifecycleOwner) {
                    Log.d(TAG, "selectDate: $it")
                    subDurationManual.setText(getDateAsString(it))
                }
                // binding.memberName.setText(name.value)

                // Observing on Saving Member Response
                response.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is SaveResponse.SavedSuccessfully -> findNavController().navigateUp()
                        is SaveResponse.EmptyBoxError -> showError(
                            binding.root,
                            getString(R.string.error_message)
                        )
                    }
                }
            }
        }

        editMemberIfNotNull()
        viewModel.selectedUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                setMemberDetails(it)
            }
        }

        // onClick Listeners
        binding.apply {
            takePhotoBtn.setOnClickListener { takePhoto() }
            clearPhotoBtn.setOnClickListener {
                viewModel.deletePhoto()
            }
            setManualDurationBtn.setOnClickListener {
                selectDateManually()
            }
        }

        selectDateFromRg()

        // Inflate the layout for this fragment
        return binding.root
    }

    private val photoIntent = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        viewModel.setPhoto(bitmap)
    }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                photoIntent.launch()
            }
        }

    private fun takePhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkForPermission(android.Manifest.permission.CAMERA)) {
                photoIntent.launch()
            } else {
                requestCamera.launch(android.Manifest.permission.CAMERA)
            }
        } else {
            photoIntent.launch()
        }
    }

    private fun onNameChanged() {
        binding.memberName.doOnTextChanged { text, start, before, count ->
            viewModel.setName(text.toString())
        }
    }

    private fun selectDateManually() {
        DatePickerHelper.selectDate(requireContext(), true) { time ->
            viewModel.setSubDate(time)
            binding.subDurationRg.clearCheck()
        }
    }

    private fun selectDateFromRg() {
        binding.subDurationRg.setOnCheckedChangeListener { radioGroup, i ->
            val current = System.currentTimeMillis()
            val month = Constants.MONTH_IN_MILLI
            binding.subDurationManual.text?.clear()
            when (radioGroup.checkedRadioButtonId) {
                R.id.sub_1_month -> viewModel.setSubDate(current + (month.floorDiv(2)))
                R.id.sub_2_month -> viewModel.setSubDate(current + (month))
                R.id.sub_3_month -> viewModel.setSubDate(current + (month * 2))
            }
        }
    }

    private fun setPaymentPrice() {
        binding.subPrice.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                Log.d(TAG, "setPaymentPrice: $text")
                val price = text.toString().toDouble()
                viewModel.setPaymentPrice(price)
            } else viewModel.setPaymentPrice(0.0)
        }
    }


    private fun editMemberIfNotNull() {
        mainViewModel.selectedUserId?.let { userId ->
            viewModel.searchIfUserExists(userId)
        }
    }

    private fun removeUser(user: User) {
                AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.exclamation)
                    .setTitle("Deleting Subscriber")
                    .setMessage(getString(R.string.delete_member) + user.name)
                    .setPositiveButton(getString(R.string.delete)) { d, _ ->
                        viewModel.deleteMember()
                        d.dismiss()
                        d.cancel()
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { d, _ ->
                        d.dismiss()
                    }.show()

    }

    private fun setMemberDetails(user: User) {
        viewModel.apply {
            userId = user.id
            user.memberPhoto?.let { setPhoto(it) }
            setName(user.name)
            binding.memberName.setText(user.name)
            setPaymentPrice(user.paymentPrice)
            binding.subPrice.setText(user.paymentPrice.toString())
            setSubDate(user.subscribeEndDate)
        }
    }



    private fun setToolbarMenu() {
        binding.toolbar.apply {
            // Inflating accurate menu
            mainViewModel.selectedUserId?.let {
                inflateMenu(R.menu.details_topbar_with_delete)
            } ?: inflateMenu(R.menu.details_topbar)

            // Setting on menuItem click listener
            setOnMenuItemClickListener { item ->
                Log.d(TAG, "setToolbarMenu: ${item.itemId}")
                when(item.itemId) {
                    R.id.save_details -> {
                        viewModel.saveMember()
                        true
                    }
                    R.id.delete_details -> {
                        viewModel.selectedUser.value?.let {
                            removeUser(it)
                        }
                        true
                    }
                    else -> false
                }

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_details -> {
                viewModel.saveMember()
                true
            }

            R.id.delete_details -> {
                viewModel.selectedUser.value?.let {
                    removeUser(it)
                }
                true
            }
            else -> false
        }
    }

    companion object {
        private const val TAG = "DetailsFragment"
    }

}
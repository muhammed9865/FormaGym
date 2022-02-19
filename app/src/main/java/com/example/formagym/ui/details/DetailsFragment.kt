package com.example.formagym.ui.details

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.MemoryFile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.formagym.*
import com.example.formagym.databinding.FragmentDetailsBinding
import com.example.formagym.pojo.model.Member
import com.example.formagym.ui.viewmodel.SubsViewModel
import java.util.*


class DetailsFragment : Fragment() {
    private val binding: FragmentDetailsBinding by lazy {
        FragmentDetailsBinding.inflate(LayoutInflater.from(requireContext()))
    }
    private val mainViewModel: SubsViewModel by activityViewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.navigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        onNameChanged()


        // Observing on views
        detailsViewModel.apply {
            with(binding) {
                photo.observe(this@DetailsFragment) {
                    it?.let { memberPhotoDetails.load(it) }
                }
                date.observe(this@DetailsFragment) {
                    subDurationManual.setText(getData(it))
                }
                binding.memberName.setText(name.value)
            }
        }

        editMemberIfNotNull()

        // onClick Listeners
        binding.apply {
            takePhotoBtn.setOnClickListener { takePhoto() }
            clearPhotoBtn.setOnClickListener {
                detailsViewModel.deletePhoto()
            }
            subDurationManual.setOnClickListener {
                selectDateManually()
            }
            saveDetails.setOnClickListener {
                val member = mainViewModel.selectedMember.value
                detailsViewModel.saveMember(member)?.let {
                    mainViewModel.save(it)
                    findNavController().navigateUp()
                } ?: showError(binding.root, getString(R.string.error_message))
            }
        }

        selectDateFromRg()

        // Inflate the layout for this fragment
        return binding.root
    }

    private val photoIntent = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        detailsViewModel.setPhoto(bitmap)
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
            detailsViewModel.setName(text.toString())
        }
    }

    private fun selectDateManually() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(requireContext(), { datePicker, _, _, _ ->
            cal.apply {
                set(Calendar.YEAR, datePicker.year)
                set(Calendar.MONTH, datePicker.month)
                set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
                Log.d(TAG, "selectDate: ${cal.timeInMillis}")
                binding.subDurationManual.text?.clear()
                detailsViewModel.setSubDate(cal.timeInMillis)
                binding.subDurationRg.clearCheck()
            }

        }, year, month, day)
        datePicker.datePicker.minDate = cal.timeInMillis
        datePicker.show()
    }

    private fun selectDateFromRg() {
        binding.subDurationRg.setOnCheckedChangeListener { radioGroup, i ->
            val current = System.currentTimeMillis()
            val month = Constants.MONTH_IN_MILLI
            binding.subDurationManual.text?.clear()
            when (radioGroup.checkedRadioButtonId) {
                R.id.sub_1_month -> detailsViewModel.setSubDate(current + month)
                R.id.sub_2_month -> detailsViewModel.setSubDate(current + (month * 2))
                R.id.sub_3_month -> detailsViewModel.setSubDate(current + (month * 3))
            }
        }
    }


    private fun editMemberIfNotNull() {
        mainViewModel.selectedMember.observe(requireActivity()) { member ->
            member?.let {
                showEditControls(member)
                setMemberDetails(member)
            }
        }
    }

    private fun showEditControls(member: Member) {
        binding.removeMember.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.exclamation)
                    .setTitle("Deleting Subscriber")
                    .setMessage(getString(R.string.delete_member) + member.name)
                    .setPositiveButton(getString(R.string.delete)) { d, _ ->
                        mainViewModel.remove(member)
                        d.dismiss()
                        d.cancel()
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { d, _ ->
                        d.dismiss()
                    }.show()
            }
        }
    }

    private fun setMemberDetails(member: Member) {
        detailsViewModel.apply {
            member.memberPhoto?.let { setPhoto(it) }
            setName(member.name)
            binding.memberName.setText(member.name)
            setSubDate(member.subscribeEndDate)
        }
    }


    companion object {
        private const val TAG = "DetailsFragment"
    }

}
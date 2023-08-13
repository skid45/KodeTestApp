package com.skid.kodetestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skid.kodetestapp.KodeTestApp
import com.skid.kodetestapp.R
import com.skid.kodetestapp.databinding.SortingBottomSheetBinding
import com.skid.kodetestapp.domain.model.Sorting

class SortingBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: SortingBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(activity?.application as KodeTestApp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SortingBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sortingBottomSheetRadioGroup.apply {
            val initialSelectedButtonId = if (mainViewModel.sortBy.value == Sorting.BY_ALPHABET) {
                R.id.sorting_bottom_sheet_radio_button_by_alphabet
            } else R.id.sorting_bottom_sheet_radio_button_by_birthday
            check(initialSelectedButtonId)

            setOnCheckedChangeListener { _, id ->
                when (id) {
                    R.id.sorting_bottom_sheet_radio_button_by_alphabet -> {
                        mainViewModel.onSortByChange(Sorting.BY_ALPHABET)
                    }

                    R.id.sorting_bottom_sheet_radio_button_by_birthday -> {
                        mainViewModel.onSortByChange(Sorting.BY_BIRTHDAY)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SortingBottomSheetFragment"
    }
}
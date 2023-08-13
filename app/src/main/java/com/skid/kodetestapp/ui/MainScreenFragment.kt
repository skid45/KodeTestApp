package com.skid.kodetestapp.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.skid.kodetestapp.R
import com.skid.kodetestapp.databinding.FragmentMainScreenBinding
import com.skid.kodetestapp.ui.adapter.DepartmentPagerAdapter
import com.skid.kodetestapp.utils.autoAnimation

class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private var departmentPagerAdapter: DepartmentPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        departmentPagerAdapter = DepartmentPagerAdapter(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar()
        viewPagerInit()
        tab()
        swipeToRefresh()
    }

    private fun viewPagerInit() {
        binding.mainScreenViewPager.adapter = departmentPagerAdapter
    }

    private fun searchBar() = with(binding) {
        mainScreenSearchBar.apply {
            setOnFocusChangeListener { _, hasFocus ->
                root.autoAnimation(100)
                if (hasFocus) {
                    mainScreenCancelButton.visibility = View.VISIBLE
                    mainScreenSortingButton.visibility = View.GONE
                } else {
                    mainScreenCancelButton.visibility = View.GONE
                    mainScreenSortingButton.visibility = View.VISIBLE
                }
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    mainViewModel.onQueryChange(p0.toString())
                    if (p0.isNullOrBlank()) mainScreenClearButton.visibility = View.GONE
                    else mainScreenClearButton.visibility = View.VISIBLE
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
        }

        mainScreenCancelButton.setOnClickListener {
            mainScreenSearchBar.clearFocus()
            mainScreenSearchBar.text.clear()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mainScreenSearchBar.windowToken, 0)
        }

        mainScreenSortingButton.setOnClickListener {
            // TODO (SortingBottomSheet)
        }

        mainScreenClearButton.setOnClickListener {
            mainScreenSearchBar.text.clear()
        }
    }

    private fun tab() = with(binding) {
        val tabTitles = resources.getStringArray(R.array.departments)
        TabLayoutMediator(mainScreenTabLayout, mainScreenViewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        mainScreenViewPager.apply {
            currentItem = mainViewModel.department.value

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mainViewModel.onDepartmentChange(position)
                }
            })
        }
    }

    private fun swipeToRefresh() = with(binding.mainScreenSwipeRefreshLayout) {
        setColorSchemeResources(R.color.colorActivePrimary, R.color.colorSecondary)
        setProgressBackgroundColorSchemeResource(R.color.colorPrimary)

        setOnRefreshListener {
            mainViewModel.onIsRefreshingChange(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        departmentPagerAdapter = null
    }
}
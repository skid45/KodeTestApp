package com.skid.kodetestapp.ui.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.skid.kodetestapp.R
import com.skid.kodetestapp.ui.UserListFragment
import com.skid.kodetestapp.utils.Constants.DEPARTMENT

class DepartmentPagerAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private val tabTitles = fragment.resources.getStringArray(R.array.departments)

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        val userListFragment = UserListFragment()
        userListFragment.arguments = bundleOf(
            DEPARTMENT to tabTitles[position]
        )
        return userListFragment
    }
}
package com.skid.kodetestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.skid.kodetestapp.KodeTestApp
import com.skid.kodetestapp.R
import com.skid.kodetestapp.databinding.FragmentUserListBinding
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.ui.adapter.UserAdapter
import com.skid.kodetestapp.ui.adapter.UserAdapterActionListener
import com.skid.kodetestapp.utils.Constants.DEPARTMENT
import kotlinx.coroutines.launch

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val department by lazy { arguments?.getString(DEPARTMENT) }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(activity?.application as KodeTestApp)
    }

    private val userAdapter by lazy {
        UserAdapter(object : UserAdapterActionListener {
            override fun onUserDetails(userItem: UserItem) {
                //TODO(Navigate to user details screen)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewInit()
        collectUserList()
    }

    private fun recyclerViewInit() = with(binding.userListRecyclerView) {
        layoutManager = LinearLayoutManager(context)
        adapter = userAdapter
    }

    private fun collectUserList() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.userList.collect { userList ->
                    val filteredUserListByDepartment =
                        if (department == getString(R.string.all) || department == null) userList
                        else {
                            userList.filter {
                                if (it is UserItem) it.department == department
                                else true
                            }
                        }
                    userAdapter.submitList(filteredUserListByDepartment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
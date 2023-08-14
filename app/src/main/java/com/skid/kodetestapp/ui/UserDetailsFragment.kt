package com.skid.kodetestapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.skid.kodetestapp.R
import com.skid.kodetestapp.databinding.FragmentUserDetailsBinding
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.utils.Constants.USER_ITEM
import com.skid.kodetestapp.utils.customGetSerializable
import com.skid.kodetestapp.utils.getYearsDeclension
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userItem = arguments?.customGetSerializable<UserItem>(USER_ITEM)


        binding.apply {
            userDetailsToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            userItem?.let { user ->
                Picasso.get().load(user.avatarUrl)
                    .placeholder(R.drawable.user_photo_stub)
                    .error(R.drawable.user_photo_stub)
                    .into(userDetailsAvatar)

                userDetailsName.text = user.name
                userDetailsTag.text = user.userTag
                userDetailsDepartment.text = user.department

                val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
                userDetailsBirthday.text = user.birthday.format(formatter)

                val age = user.birthday.until(LocalDate.now(), ChronoUnit.YEARS)
                val ageString = "$age ${age.getYearsDeclension()}"
                userDetailsAge.text = ageString

                userDetailsPhone.text = user.phone
                userDetailsPhone.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${user.phone}"))
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
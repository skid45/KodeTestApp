package com.skid.kodetestapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skid.kodetestapp.R
import com.skid.kodetestapp.databinding.SeparatorItemBinding
import com.skid.kodetestapp.databinding.UserItemBinding
import com.skid.kodetestapp.domain.model.SeparatorItem
import com.skid.kodetestapp.domain.model.UserItem
import com.skid.kodetestapp.domain.model.UserListItem
import com.squareup.picasso.Picasso

class UserAdapter(
    private val actionListener: UserAdapterActionListener,
) : ListAdapter<UserListItem, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UserItem -> R.layout.user_item
            is SeparatorItem -> R.layout.separator_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.user_item -> {
                val binding = UserItemBinding.inflate(view, parent, false)
                binding.root.setOnClickListener {
                    actionListener.onUserDetails(it.tag as UserItem)
                }

                UserViewHolder(binding)
            }

            else -> {
                val binding = SeparatorItemBinding.inflate(view, parent, false)
                SeparatorViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is UserViewHolder -> holder.bind(item as UserItem)
            is SeparatorViewHolder -> holder.bind(item as SeparatorItem)
        }
    }

    class UserViewHolder(
        private val binding: UserItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userItem: UserItem) = with(binding) {
            root.tag = userItem

            Picasso.get()
                .load(userItem.avatarUrl)
                .placeholder(R.drawable.user_photo_stub)
                .error(R.drawable.user_photo_stub)
                .into(userItemAvatar)

            userItemName.text = userItem.name
            userItemTag.text = userItem.userTag
            userItemDepartment.text = userItem.department
            userItemBirthday.text = userItem.monthDayOfBirthday
        }
    }

    class SeparatorViewHolder(
        private val binding: SeparatorItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(separatorItem: SeparatorItem) = with(binding) {
            separatorItemText.text = separatorItem.text
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<UserListItem>() {
        override fun areItemsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean {
            return when {
                oldItem is UserItem && newItem is UserItem -> oldItem.id == newItem.id
                oldItem is SeparatorItem && newItem is SeparatorItem -> oldItem.text == newItem.text
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean {
            return when {
                oldItem is UserItem && newItem is UserItem -> oldItem == newItem
                oldItem is SeparatorItem && newItem is SeparatorItem -> oldItem == newItem
                else -> false
            }
        }
    }
}


interface UserAdapterActionListener {
    fun onUserDetails(userItem: UserItem)
}
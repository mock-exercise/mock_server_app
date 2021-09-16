package com.example.serverapp.admin.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.connectorlibrary.enitity.User
import com.example.serverapp.databinding.ItemUserListBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    private var mOnItemClickListener: ((User) -> Unit)? = null

    fun setOnItemClickListener(listener: ((User) -> Unit)?) {
        mOnItemClickListener = listener
    }

    private val differCallback = object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.user_id == newItem.user_id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder =
        UserHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bindData(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class UserHolder(private val binding: ItemUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(itemUser: User) {
            binding.apply {
                user = itemUser
                root.setOnClickListener {
                    mOnItemClickListener?.let {
                        it(itemUser)
                    }
                }
            }
        }
    }

}
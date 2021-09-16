package com.example.serverapp.admin.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.connectorlibrary.enitity.Active
import com.example.connectorlibrary.enitity.User
import com.example.serverapp.R
import com.example.serverapp.admin.view.adapter.UserAdapter
import com.example.serverapp.admin.viewmodel.AdminViewModel
import com.example.serverapp.databinding.FragmentListUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUserFragment : Fragment() {

    lateinit var binding: FragmentListUserBinding
    lateinit var controller: NavController
    private val userAdapter by lazy { UserAdapter() }
    private val viewModel: AdminViewModel by activityViewModels()
    private var listActive: List<Active> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUserBinding.inflate(inflater, container, false)
        controller = findNavController()

        handleTasks()

        return binding.root
    }

    private fun handleTasks() {
        initRecyclerView()
        getListUsersFromApi()
        observerListUsers()
        setItemClickListUser()
    }

    private fun initRecyclerView() {
        binding.usersRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun getListUsersFromApi() {
        viewModel.isServerConnected.observe(viewLifecycleOwner, {
            if (it) viewModel.apply {
                getAllUsers()
                getActive()
            }
        })
    }

    private fun observerListUsers() {
        viewModel.listUsers.observe(viewLifecycleOwner, {
            it?.let {
                userAdapter.differ.submitList(it)
            }
        })
    }

    private fun setItemClickListUser() {
        userAdapter.setOnItemClickListener {
            alertDialog(user = it)
        }
    }

    private fun alertDialog(user: User) {
        var isActive = false
        listActive.find { it.active_id == user.active_id }?.let { it ->
            isActive = it.active_name
        }
        val stringNegation =
            if (isActive) R.string.lock_user
            else R.string.open_lock_user

        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage(R.string.messenger_dialog)
            setPositiveButton(R.string.positive) { _, _ ->
                val action =
                    ListUserFragmentDirections.actionListUserFragmentToUserDetailFragment(user)
                controller.navigate(action)
            }
            setNegativeButton(
                stringNegation.toString()
            ) { _, _ ->
                listActive.find { it.active_name == !isActive }?.let {
                    user.active_id = it.active_id
                }
                viewModel.lockUser(user)
            }
            create()
        }
    }
}
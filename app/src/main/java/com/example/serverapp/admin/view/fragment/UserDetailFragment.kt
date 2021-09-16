package com.example.serverapp.admin.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.serverapp.admin.viewmodel.AdminViewModel
import com.example.serverapp.databinding.FragmentUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentUserDetailBinding
    lateinit var controller: NavController
    private val args: UserDetailFragmentArgs by navArgs()
    private val viewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        controller = findNavController()

        handleTasks()

        return binding.root
    }

    private fun handleTasks() {
        binding.user = args.itemUser
        getBaseData()
        observerData()
    }

    private fun getBaseData() {
        viewModel.isServerConnected.observe(viewLifecycleOwner, {
            if (it) viewModel.apply {
                getActive()
                getGender()
            }
        })
    }

    private fun observerData() {
        viewModel.listGender.observe(viewLifecycleOwner, {
            binding.listGender = it
        })
        viewModel.listActive.observe(viewLifecycleOwner, {
            binding.listActive = it
        })
    }


}
package com.example.serverapp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected lateinit var controller: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getActivityBinding(layoutInflater)
        setContentView(binding.root)

        navHostFragment = getNavHostFragment()
        controller = navHostFragment.findNavController()

        handleTask()
    }

    abstract fun getActivityBinding(layoutInflater: LayoutInflater): VB

    abstract fun getNavHostFragment(): NavHostFragment

    abstract fun handleTask()
}
package com.example.serverapp.admin.view

import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.serverapp.R
import com.example.serverapp.admin.viewmodel.AdminViewModel
import com.example.serverapp.base.BaseActivity
import com.example.serverapp.databinding.ActivityAdminBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : BaseActivity<ActivityAdminBinding>() {

    private lateinit var appBarConfig: AppBarConfiguration

    override fun getActivityBinding(layoutInflater: LayoutInflater): ActivityAdminBinding =
        ActivityAdminBinding.inflate(layoutInflater)

    override fun getNavHostFragment(): NavHostFragment =
        supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

    private val viewModel: AdminViewModel by viewModels()

    override fun handleTask() {
        lifecycle.addObserver(viewModel)
        appBarConfig = AppBarConfiguration(
            setOf(R.id.listUserFragment, R.id.userDetailFragment)
        )
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(controller, appBarConfig)
        binding.apply {
            toolbar.setupWithNavController(controller, appBarConfig)
            titleToolbar.text = toolbar.title
        }
    }

    override fun onResume() {
        super.onResume()
    }
}

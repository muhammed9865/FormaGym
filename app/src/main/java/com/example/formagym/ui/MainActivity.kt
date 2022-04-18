package com.example.formagym.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.formagym.R
import com.example.formagym.databinding.ActivityMainBinding
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.ui.mainviewmodel.MainViewModel
import com.example.formagym.ui.mainviewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }
    private val viewModel: MainViewModel by viewModels()
    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragments_layout) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        viewModel.getActiveMembersLength()
        viewModel.getInactiveMembersLength()

        setInactiveBadge()
        setActiveBadge()
        onFragmentChanged()
        onBottomNavItemSelected()
        onAddFabClicked()


    }


    private fun setInactiveBadge() {
        val badge = binding.bottomNav.getOrCreateBadge(R.id.inactives_section)
        viewModel.inactiveSubsCount.observe(this) { count ->
            if (count > 0) {
                badge.apply {
                    isVisible = true
                    number = count
                }
            } else {
                badge.apply {
                    isVisible = false
                    clearNumber()
                }
            }
        }
    }

    private fun setActiveBadge() {
        val badge = binding.bottomNav.getOrCreateBadge(R.id.active)
        viewModel.activeSubsCount.observe(this) { count ->
            if (count > 0) {
                badge.apply {
                    isVisible = true
                    number = count
                }
            } else {
                badge.apply {
                    isVisible = false
                    clearNumber()
                }
            }
        }
    }

    private fun onAddFabClicked() {
        binding.newMemberFab.setOnClickListener {
            viewModel.onNewMember()
            when (navController.currentDestination?.id) {
                R.id.inactiveFragment -> {
                    navController.navigate(R.id.action_inactiveFragment_to_detailsFragment)
                }
                R.id.activeFragment -> {
                    navController.navigate(R.id.action_activeFragment_to_detailsFragment)
                }
            }
        }
    }

    private fun onBottomNavItemSelected() {
        binding.bottomNav.setOnItemSelectedListener {
            val currentDest = navController.currentDestination?.id
            when (it.itemId) {
                R.id.inactives_section -> {
                    navController.navigate(R.id.inactiveFragment)
                    true
                }
                R.id.active -> {
                    navController.navigate(R.id.activeFragment)
                    true
                }

                R.id.payments_section -> {
                    navController.navigate(R.id.incomeFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun onFragmentChanged() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment -> {
                    binding.bottomNav.visibility = View.GONE
                    binding.newMemberFab.visibility = View.GONE

                }
                R.id.incomeFragment -> {
                    binding.newMemberFab.visibility = View.INVISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.newMemberFab.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


    companion object {
        private const val TAG = "MainActivity"
    }

}
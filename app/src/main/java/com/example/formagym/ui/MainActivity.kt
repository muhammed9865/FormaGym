package com.example.formagym.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.formagym.R
import com.example.formagym.databinding.ActivityMainBinding
import com.example.formagym.pojo.datasource.FormaDatabase
import com.example.formagym.ui.viewmodel.SubsViewModel
import com.example.formagym.ui.viewmodel.SubsViewModelFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }
    private val viewModel: SubsViewModel by viewModels {
        SubsViewModelFactory(FormaDatabase.getInstance(this))
    }
    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments_layout) as NavHostFragment
        navHostFragment.navController
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        fillMembersLists()
        setInactiveBadge()
        onFragmentChanged()
        onBottomNavItemSelected()
        onAddFabClicked()

    }

    // Will get the lists from the db and set it into the viewmodel
    private fun fillMembersLists() {
        viewModel.getActiveMembers()
        viewModel.getInactiveMembers()
    }

    private fun setInactiveBadge() {
        val badge = binding.bottomNav.getOrCreateBadge(R.id.inactive_subs)
        viewModel.inactiveSubs.observe(this) { list ->
            Log.d("inactiveList", "setInactiveBadge: ${list.size}")
            if (list.isNotEmpty()) {
                badge.apply {
                    isVisible = true
                    number = list.size
                }
            }else {
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
            when(it.itemId) {
                R.id.inactive_subs -> {
                    if (currentDest != R.id.inactiveFragment) {
                        navController.navigate(R.id.action_subsFragment_to_inactiveFragment)
                    }
                    true
                }
                R.id.active -> {
                    if (currentDest != R.id.activeFragment) {
                        navController.navigate(R.id.action_inactiveFragment_to_subsFragment)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun onFragmentChanged() {
        navController.addOnDestinationChangedListener{ controller, destination, arguments ->
            when (destination.id) {
                R.id.detailsFragment -> {
                    binding.bottomAppbar.visibility = View.INVISIBLE
                    binding.newMemberFab.visibility = View.INVISIBLE

                }
                else -> {
                    binding.bottomAppbar.visibility = View.VISIBLE
                    binding.newMemberFab.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

}
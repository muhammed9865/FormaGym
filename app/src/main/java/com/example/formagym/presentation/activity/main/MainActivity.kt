package com.example.formagym.presentation.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.formagym.R
import com.example.formagym.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
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

        navController.addOnDestinationChangedListener(this)
        doOnStateChanged()

        createBottomNavigationMenu()

        viewModel.getActiveMembersDetails()
        viewModel.getInactiveMembersDetails()

        onBottomNavItemSelected()
        onAddFabClicked()


    }


    private fun setInactiveBadge(count: Int) {
        if (count > 0) {
            binding.bottomNav.setCount(R.id.inactives_section, count.toString())
            binding.inactiveCount.text = count.toString()

        } else {
            binding.bottomNav.clearCount(R.id.inactives_section)


        }
    }

    private fun setActiveBadge(count: Int) {
        if (count > 0) {
            binding.bottomNav.setCount(R.id.active, count.toString())
            binding.activeCount.text = count.toString()

        } else {
            binding.bottomNav.clearCount(R.id.active)

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

    private fun createBottomNavigationMenu() {
        binding.bottomNav.apply {
            add(MeowBottomNavigation.Model(R.id.active, R.drawable.ic_baseline_group_24))
            add(MeowBottomNavigation.Model(R.id.inactives_section, R.drawable.ic_inactive))
            add(MeowBottomNavigation.Model(R.id.payments_section,
                R.drawable.ic_baseline_monetization_on_24))
            show(R.id.active)
        }
    }

    private fun onBottomNavItemSelected() {
        binding.bottomNav.setOnClickMenuListener {
            when (it.id) {
                R.id.inactives_section -> {
                    navController.navigate(R.id.inactiveFragment)

                }
                R.id.active -> {
                    navController.navigate(R.id.activeFragment)
                }
                R.id.payments_section -> {
                    navController.navigate(R.id.incomeFragment)
                }
            }
        }
    }

    private fun doOnStateChanged() {
        viewModel.state.onEach { state ->
            state.activesDetails?.let { activeDetails ->
                setActiveBadge(activeDetails.count)
            }
            state.inactiveDetails?.let { inactiveDetails ->
                setInactiveBadge(inactiveDetails.count)
            }

        }.launchIn(lifecycleScope)
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (destination.id) {
            R.id.detailsFragment -> {
                binding.apply {
                    bottomNav.visibility = View.GONE
                    newMemberFab.visibility = View.GONE
                    statsLayout.visibility = View.INVISIBLE
                }

            }
            R.id.incomeFragment -> {
                binding.newMemberFab.visibility = View.INVISIBLE
                binding.statsLayout.visibility = View.INVISIBLE
            }
            else -> {
                binding.bottomNav.visibility = View.VISIBLE
                binding.newMemberFab.visibility = View.VISIBLE
                binding.statsLayout.visibility = View.VISIBLE
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
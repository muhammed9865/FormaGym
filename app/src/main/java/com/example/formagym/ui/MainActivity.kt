package com.example.formagym.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.formagym.R
import com.example.formagym.databinding.ActivityMainBinding
import com.example.formagym.ui.mainviewmodel.MainViewModel
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


        createBottomNavigationMenu()

        viewModel.getActiveMembersLength()
        viewModel.getInactiveMembersLength()

        binding.bottomNav.setOnShowListener {
            Log.d(TAG, "onCreate: ${it.count}")
        }

        setInactiveBadge()
        setActiveBadge()
        onFragmentChanged()
        onBottomNavItemSelected()
        onAddFabClicked()


    }


    private fun setInactiveBadge() {

        viewModel.inactiveSubsCount.observe(this) { count ->
            if (count > 0) {
                binding.bottomNav.setCount(R.id.inactives_section, count.toString())

            } else {
                binding.bottomNav.clearCount(R.id.inactives_section)

            }
        }
    }

    private fun setActiveBadge() {

        viewModel.activeSubsCount.observe(this) { count ->
            if (count > 0) {
                binding.bottomNav.setCount(R.id.active, count.toString())

            } else {
                binding.bottomNav.clearCount(R.id.active)

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

    private fun createBottomNavigationMenu() {
        binding.bottomNav.apply {
            add(MeowBottomNavigation.Model(R.id.active, R.drawable.ic_baseline_group_24))
            add(MeowBottomNavigation.Model(R.id.inactives_section, R.drawable.ic_inactive))
            add(MeowBottomNavigation.Model(R.id.payments_section, R.drawable.ic_baseline_monetization_on_24))
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
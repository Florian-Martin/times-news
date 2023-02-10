package fr.martinflorian.timesnews.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.ActivityMainBinding
import fr.martinflorian.timesnews.utils.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    /**************************************
     * PROPERTIES
     *************************************/
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    companion object {
        const val ANIMATION_DURATION = 1000L
    }


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val connectivityObserver = NetworkConnectivityObserver(this)
        connectivityObserver.observe().onEach { networkStatus ->
            when (networkStatus) {
                ConnectivityObserver.Status.Available -> {
                    binding.apply {
                        networkStatusText.apply {
                            text = getString(R.string.connection_back)
                            setDrawableLeft(R.drawable.ic_connection_back)
                        }
                        networkStatusContainer.apply {
                            setBackgroundColor(getColorRes(R.color.connection_back))
                            animate()
                                .alpha(1f)
                                .setStartDelay(ANIMATION_DURATION)
                                .setDuration(ANIMATION_DURATION)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        showView(networkStatusContainer, false)
                                    }
                                })
                        }
                    }
                }
                ConnectivityObserver.Status.Lost -> {
                    binding.apply {
                        networkStatusText.apply {
                            text = getString(R.string.no_connection)
                            setDrawableLeft(R.drawable.ic_no_connection)
                        }
                        networkStatusContainer.apply {
                            showView(this, true)
                            setBackgroundColor(getColorRes(R.color.no_connection))
                        }
                    }
                }
            }
        }.launchIn(lifecycleScope)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main_activity) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        navView.setupWithNavController(navController)
    }


    /**************************************
     * FUNCTIONS
     *************************************/
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
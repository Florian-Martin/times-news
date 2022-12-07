package fr.martinflorian.timesnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    /**************************************
     * PROPERTIES
     *************************************/
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
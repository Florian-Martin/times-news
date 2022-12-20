package fr.martinflorian.timesnews.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by lazy {
        ViewModelProvider(
            this,
            SplashViewModel.Factory(application)
        )[SplashViewModel::class.java]
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash);

        viewModel.launchActivityEvent.observe(this) { activityName ->
            when (activityName) {
                MainActivity::class.simpleName -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> throw IllegalArgumentException("Undefined activity id $activityName")
            }
        }
    }
}
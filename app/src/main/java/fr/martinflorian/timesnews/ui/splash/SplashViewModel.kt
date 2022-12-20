package fr.martinflorian.timesnews.ui.splash

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import fr.martinflorian.timesnews.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart

class SplashViewModel(application: Application) : ViewModel() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    val launchActivityEvent = flowOf(MainActivity::class.simpleName)
        .onStart {
            delay(SPLASH_DURATION)
        }
        .asLiveData()

    companion object {
        const val SPLASH_DURATION = 2_000L
    }

    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}
package fr.martinflorian.timesnews.ui.newswire

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.martinflorian.timesnews.BuildConfig
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.data.AppDatabase
import fr.martinflorian.timesnews.data.Resource.Companion.toState
import fr.martinflorian.timesnews.data.State
import fr.martinflorian.timesnews.data.repository.ArticlesRepository
import fr.martinflorian.timesnews.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class NewswireViewModel(application: Application) : ViewModel() {

    /**************************************
     * PROPERTIES
     *************************************/
    private val repository =
        ArticlesRepository(application, AppDatabase.getAppDatabase(application))

    private val articleType = application.resources.getString(R.string.title_newswire)

    // HTTP params for NYT API
    private val params = mapOf(
        "api-key" to BuildConfig.API_KEY
    )

    private val _articles: MutableStateFlow<State<List<Article>>> =
        MutableStateFlow(State.Loading())
    val articles: StateFlow<State<List<Article>>> get() = _articles

    /**************************************
     * FUNCTIONS
     *************************************/

    /**
     * Get articles from [ArticlesRepository]
     */
    fun getArticles() {
        viewModelScope.launch {
            repository.getArticlesFromType(params, articleType)
                .map { it.toState() }
                .collect { _articles.value = it }
        }
    }


    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewswireViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewswireViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}
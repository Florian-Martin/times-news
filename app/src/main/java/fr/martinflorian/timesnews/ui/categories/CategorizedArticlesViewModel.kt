package fr.martinflorian.timesnews.ui.categories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.martinflorian.timesnews.BuildConfig
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.data.local.AppDatabase
import fr.martinflorian.timesnews.data.Resource.Companion.toState
import fr.martinflorian.timesnews.data.State
import fr.martinflorian.timesnews.data.repository.ArticlesRepository
import fr.martinflorian.timesnews.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategorizedArticlesViewModel(application: Application) : ViewModel() {

    /**************************************
     * PROPERTIES
     *************************************/
    private val repository =
        ArticlesRepository(application, AppDatabase.getAppDatabase(application))

    private val articleType = application.resources.getString(R.string.title_categories)

    private var currentCategory = "Technology"

    private val _articles: MutableStateFlow<State<List<Article>>> =
        MutableStateFlow(State.Loading())
    val articles: StateFlow<State<List<Article>>> get() = _articles


    /**************************************
     * FUNCTIONS
     *************************************/
    fun getArticles() {
        viewModelScope.launch {

            // HTTP params for NYT API
            val params = mutableMapOf(
                "api-key" to BuildConfig.API_KEY,
                "fq" to "(section_name:(\"$currentCategory\") AND document_type:(\"article\"))",
                "sort" to "newest"
            )
            repository.getArticlesFromType(params, articleType)
                .map { it.toState() }
                .collect { _articles.value = it }
        }
    }

    fun switchCategory(name: String) {
        currentCategory = name
        refreshArticles()
    }

    /**
     * Refresh articles after a category has been clicked
     */
    private fun refreshArticles() {
        _articles.value = State.Loading()
        viewModelScope.launch {
            getArticles()
        }
    }


    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CategorizedArticlesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CategorizedArticlesViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}
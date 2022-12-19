package fr.martinflorian.timesnews.ui.detail

import android.app.Application
import androidx.lifecycle.*
import fr.martinflorian.timesnews.data.AppDatabase
import fr.martinflorian.timesnews.data.repository.ArticlesRepository
import fr.martinflorian.timesnews.model.Article
import kotlinx.coroutines.launch

class ArticleDetailViewModel(private val application: Application) : ViewModel() {

    /**************************************
     * PROPERTIES
     *************************************/
    private val _article: MutableLiveData<Article> = MutableLiveData()
    val article: LiveData<Article>
        get() = _article
    private val database = AppDatabase.getAppDatabase(application)
    private val repository = ArticlesRepository(application, database)


    /**************************************
     * FUNCTIONS
     *************************************/
    fun getArticleById(id: Int) {
        viewModelScope.launch {
            _article.value = repository.getArticleById(id)
        }
    }


    class Factory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArticleDetailViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}

package fr.martinflorian.timesnews.ui.detail

import android.app.Application
import androidx.lifecycle.*
import fr.martinflorian.timesnews.data.AppDatabase
import fr.martinflorian.timesnews.data.repository.ArticlesRepository
import fr.martinflorian.timesnews.model.Article
import kotlinx.coroutines.launch

class ArticleDetailViewModel(application: Application) : ViewModel() {

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

    /**
     * Updates Entity isBookmarked status depending on isBookmarked parameter value
     * 1 = [Article] bookmarked
     * 0 = [Article] unbookmarked
     *
     * @param isBookmarked: Whether the [Article] has to be added to or removed from bookmarks
     */
    fun updateBookmarkStatus(isBookmarked: Boolean) {
        if (isBookmarked) addToBookmarks() else removeFromBookmarks()
    }

    private fun addToBookmarks() {
        viewModelScope.launch {
            article.value?.let { repository.updateArticle(it, true) }
        }
    }

    private fun removeFromBookmarks() {
        viewModelScope.launch {
            article.value?.let { repository.updateArticle(it, false) }
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

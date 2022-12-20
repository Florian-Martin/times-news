package fr.martinflorian.timesnews.ui.bookmarks

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import fr.martinflorian.timesnews.data.AppDatabase
import fr.martinflorian.timesnews.data.Resource
import fr.martinflorian.timesnews.data.Resource.Companion.toState
import fr.martinflorian.timesnews.data.State
import fr.martinflorian.timesnews.data.repository.ArticlesRepository
import fr.martinflorian.timesnews.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BookmarksViewModel(private val application: Application) : ViewModel() {
    /**************************************
     * PROPERTIES
     *************************************/
    private val database = AppDatabase.getAppDatabase(application)

    private val repository = ArticlesRepository(application, database)

    private val _bookmarks: MutableStateFlow<State<List<Article>>> =
        MutableStateFlow(State.Loading())

    val bookmarks: Flow<State<List<Article>>> get() = _bookmarks

    /**************************************
     * FUNCTIONS
     *************************************/
    fun getBookmarks() {
        viewModelScope.launch {
            repository.getBookmarks().collect {
                _bookmarks.value = Resource.Success(it).toState()
            }
        }
    }


    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookmarksViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BookmarksViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown viewModel class.")
        }
    }
}
package fr.martinflorian.timesnews.data.repository

import android.app.Application
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.data.AppDatabase
import fr.martinflorian.timesnews.data.Resource
import fr.martinflorian.timesnews.data.remote.NYTimesApi
import fr.martinflorian.timesnews.model.Article
import fr.martinflorian.timesnews.utils.logDebug
import fr.martinflorian.timesnews.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class ArticlesRepository(
    private val application: Application,
    database: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**************************************
     * PROPERTIES
     *************************************/
    private val dao = database.getArticlesDao()


    /**************************************
     * FUNCTIONS
     *************************************/

    /**
     * For a better user experience data is cached in device's database every time HTTP calls to
     * New York Times REST API are successful.
     *
     * Depending on the kind of data requested by the user, database's partially refreshed with
     * new data if the HTTP call was successful.
     *
     * Data is only provided to UI from device's database.
     */
    fun getArticlesFromType(
        requestParams: Map<String, String>,
        articleType: String
    ): Flow<Resource<List<Article>>> {

        return flow {

            // Emit database data first
            emit(Resource.Success(fetchFromLocal(articleType).first()))

            // Fetch fresh data from New York Times API
            val apiResponse = when (articleType) {
                application.resources.getString(R.string.title_newswire) -> NYTimesApi.retrofitService.getNewsWire(
                    requestParams
                )
                application.resources.getString(R.string.title_categories) -> NYTimesApi.retrofitService.getCategorizedArticles(
                    requestParams
                )
                application.resources.getString(R.string.title_trending) -> NYTimesApi.retrofitService.getTrending(
                    requestParams
                )
                else -> null
            }

            val remoteArticles = apiResponse?.body()

            // If remote data fetch is successful, refresh the relevant articles from database,
            // deleting cached data, replacing it by recently remotely fetched articles.
            apiResponse?.let {
                if (apiResponse.isSuccessful && remoteArticles != null) {
                    deleteArticlesFromType(articleType)
                    insertRemoteDataInDatabase(remoteArticles.asDatabaseModel())
                    logDebug("Network request result: ${apiResponse.code()}: ${apiResponse.message()}")
                } else {
                    emit(Resource.Failed(apiResponse.message()))
                    logError("Network request error: ${apiResponse.code()}: ${apiResponse.message()}")
                }
            }

            // Emit database content again
            emitAll(
                fetchFromLocal(articleType).map { Resource.Success(it) }
            )
        }
            .catch {
                it.printStackTrace()
                emit(Resource.Failed("Network error! Latest posts cannot get loaded."))
            }
    }

    private suspend fun insertRemoteDataInDatabase(data: List<Article>) {
        dao.insert(data)
    }

    /**
     * Fetches [Articles]s from database depending on their type
     *
     * @param type: [Article] type (e.g. "Newwswire")
     */
    private fun fetchFromLocal(type: String): Flow<List<Article>> {
        return dao.getArticlesByType(type)
    }

    /**
     * Deletes [Article]s depending on their type
     *
     * @param type: [Article] type (e.g. "Newwswire")
     */
    private suspend fun deleteArticlesFromType(type: String) {
        dao.deleteArticlesByType(type)
    }

    suspend fun getArticleById(id: Int): Article {
        return withContext(dispatcher) {
            dao.getArticleById(id)
        }
    }

    /**
     * Updates a given [Article]
     *
     * @param article: an [Article] to update
     * @param isBookmarked: Whether the [Article] isBookmarked field has to be 1 or 0
     */
    suspend fun updateArticle(article: Article, isBookmarked: Boolean) {
        val articleUpdated = article.copy(isBookmarked = isBookmarked)
        dao.update(articleUpdated)
    }
}
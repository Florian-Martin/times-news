package fr.martinflorian.timesnews.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.martinflorian.timesnews.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list: List<Article.Article>)

    @Update
    suspend fun update(article: Article)

    @Query("SELECT * FROM Article WHERE articleType = :type ORDER BY publicationDate DESC")
    fun getArticlesByType(type: String): Flow<List<Article>>

    @Query("SELECT * FROM Article WHERE id = :id")
    fun getArticleById(id: Int): LiveData<Article>

    @Query("SELECT * FROM Article WHERE isBookmarked = 1 ORDER BY publicationDate DESC")
    fun getBookmarks(): Flow<List<Article>>

    @Query("DELETE FROM Article WHERE articleType = :type AND isBookmarked = 0")
    suspend fun deleteArticlesByType(type: String)
}
package fr.martinflorian.timesnews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.martinflorian.timesnews.data.AppDatabase
import fr.martinflorian.timesnews.data.local.ArticlesDao
import fr.martinflorian.timesnews.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTests {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var dao: ArticlesDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.getArticlesDao()
    }

    @After
    fun tearDown() = database.close()


    /**
     * Checks if insertion of [Article] list succeeds
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertArticlesTest() = runTest {
        val articleType = "Newswire"
        val article1 = Article(
            1,
            "test title1",
            "test section1",
            "test paragraph1",
            "test url1",
            "test imgUrl1",
            "test snippet1",
            "test pubDate1",
            "test authors1",
            false,
            articleType
        )
        val article2 = article1.copy(id = 2)
        val articlesList = listOf(article1, article2)

        dao.insert(articlesList)

        val allArticles = dao.getArticlesByType(articleType).asLiveData().getOrAwaitValue()

        Assert.assertEquals(true, allArticles.contains(article1))
    }

    /**
     * Checks if deletion of a [List] of [Article] objects succeeds
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteTodoTest() = runTest {
        val articleType = "Newswire"
        val article1 = Article(
            1,
            "test title1",
            "test section1",
            "test paragraph1",
            "test url1",
            "test imgUrl1",
            "test snippet1",
            "test pubDate1",
            "test authors1",
            false,
            articleType
        )
        val article2 = article1.copy(id = 2)
        val articlesList = listOf(article1, article2)

        dao.insert(articlesList)
        dao.deleteArticlesByType(articleType)

        val allArticles = dao.getArticlesByType(articleType).asLiveData().getOrAwaitValue()

        Assert.assertEquals(false, allArticles.contains(article2))
    }


    /**
     * Checks if the update of [Article] bookmark's status succeeds
     */
    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun updateTodoTest() = runTest {
        val articleType = "Newswire"
        val article1 = Article(
            1,
            "test title1",
            "test section1",
            "test paragraph1",
            "test url1",
            "test imgUrl1",
            "test snippet1",
            "test pubDate1",
            "test authors1",
            false,
            articleType
        )
        val article2 = article1.copy(id = 2)
        val articlesList = listOf(article1, article2)

        dao.insert(articlesList)

        val updatedArticle = article1.copy(isBookmarked = true)
        dao.update(updatedArticle)

        val updatedArticleBookmarkStatus = dao.getArticleById(1).getOrAwaitValue().isBookmarked

        Assert.assertEquals(true, updatedArticleBookmarkStatus)
    }
}
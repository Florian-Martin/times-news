package fr.martinflorian.timesnews.data.remote

import com.squareup.moshi.Json
import fr.martinflorian.timesnews.model.Article
import fr.martinflorian.timesnews.utils.capitalizeAllFirstChars
import fr.martinflorian.timesnews.utils.capitalizeFirstChar
import fr.martinflorian.timesnews.utils.parseDate

data class TopStoriesArticlesApiData(val results: List<TopStories>) : BaseArticle {

    override fun asDatabaseModel(): List<Article> {
        return results.filter {
            it.imagesList?.isNotEmpty() ?: false
                    && it.title?.isNotBlank() ?: false
                    && it.snippet?.isNotBlank() ?: false
                    && it.webUrl?.isNotBlank() ?: false
        }.map {
            Article(
                title = it.title?.capitalizeFirstChar(),
                section = it.section?.capitalizeFirstChar(),
                leadParagraph = null,
                webUrl = it.webUrl,
                imageUrl = it.imagesList?.get(1)?.url,
                snippet = it.snippet?.capitalizeFirstChar(),
                publicationDate = it.publicationDate?.parseDate(),
                authors = it.authors?.capitalizeAllFirstChars(),
                isBookmarked = false,
                articleType = "Trending"
            )
        }
    }
}

data class TopStories(
    val section: String?,
    val title: String?,
    @Json(name = "url") val webUrl: String?,
    @Json(name = "published_date") val publicationDate: String?,
    @Json(name = "abstract") val snippet: String?,
    @Json(name = "byline") val authors: String?,
    @Json(name = "multimedia") val imagesList: List<ArticleImage>?
)
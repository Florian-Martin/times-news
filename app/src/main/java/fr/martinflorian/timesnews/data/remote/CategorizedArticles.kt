package fr.martinflorian.timesnews.data.remote

import com.squareup.moshi.Json
import fr.martinflorian.timesnews.model.Article
import fr.martinflorian.timesnews.utils.IMAGE_URL_PREFIX
import fr.martinflorian.timesnews.utils.capitalizeFirstChar
import fr.martinflorian.timesnews.utils.parseDate

data class CategorizedArticlesApiData(val response: ArticleResponse) : BaseArticle {

    override fun asDatabaseModel(): List<Article> {
        return response.docs.filter {
            it.imagesList?.isNotEmpty() ?: false
                    && it.title?.main?.isNotBlank() ?: false
                    && it.webUrl?.isNotBlank() ?: false
        }.map {
            var authorsConcat = "By "
            it.byline?.authors?.forEach { author ->
                authorsConcat += "${author.firstName} ${author.lastName}, "
            }

            val imageUrl = IMAGE_URL_PREFIX.plus(it.imagesList?.get(0)?.url)

            Article(
                title = it.title?.main?.capitalizeFirstChar(),
                section = it.section?.capitalizeFirstChar(),
                leadParagraph = it.leadParagraph,
                webUrl = it.webUrl,
                imageUrl = imageUrl,
                snippet = it.snippet?.capitalizeFirstChar(),
                publicationDate = it.publicationDate?.parseDate(),
                authors = authorsConcat,
                isBookmarked = false,
                articleType = "Categories"
            )
        }
    }
}

data class ArticleResponse(
    val docs: List<Doc>
)

data class Doc(
    @Json(name = "section_name") val section: String?,
    @Json(name = "web_url") val webUrl: String?,
    @Json(name = "lead_paragraph") val leadParagraph: String?,
    @Json(name = "multimedia") val imagesList: List<ArticleImage>?,
    @Json(name = "pub_date") val publicationDate: String?,
    @Json(name = "headline") val title: Headline?,
    val byline: Byline?,
    val snippet: String?
)

data class ArticleImage(
    val url: String?
)

data class Headline(
    val main: String?
)

data class Byline(
    @Json(name = "person") val authors: List<ArticleAuthor>?
)

data class ArticleAuthor(
    @Json(name = "firstname") val firstName: String?,
    @Json(name = "lastname") val lastName: String?
)



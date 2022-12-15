package fr.martinflorian.timesnews.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["title", "webUrl"], unique = true)])
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val section: String?,
    val leadParagraph: String?,
    val webUrl: String?,
    val imageUrl: String?,
    val snippet: String?,
    val publicationDate: String?,
    val authors: String?,
    var isBookmarked: Boolean = false,
    val articleType: String
)

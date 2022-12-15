package fr.martinflorian.timesnews.data.remote

import fr.martinflorian.timesnews.model.Article

interface BaseArticle {

    /**
     * Parse data from New York Times API to database model
     */
    fun asDatabaseModel(): List<Article>
}
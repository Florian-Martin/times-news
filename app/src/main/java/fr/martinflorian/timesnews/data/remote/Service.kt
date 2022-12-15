package fr.martinflorian.timesnews.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.martinflorian.timesnews.utils.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface NYTimesApiService {

    /**
     * Endpoint to get articles by category
     */
    @GET("search/v2/articlesearch.json")
    suspend fun getCategorizedArticles(
        @QueryMap(encoded = true) params: Map<String, String>
    ): Response<CategorizedArticlesApiData>

    /**
     * Endpoint to get Trending articles
     */
    @GET("topstories/v2/home.json")
    suspend fun getTopStories(
        @QueryMap(encoded = true) params: Map<String, String>
    ): Response<TopStoriesArticlesApiData>

    /**
     * Endpoint to get Newswire articles
     */
    @GET("news/v3/content/all/all.json?")
    suspend fun getNewsWire(
        @QueryMap(encoded = true) params: Map<String, String>
    ): Response<NewsWireArticlesApiData>
}


object NYTimesApi {
    val retrofitService: NYTimesApiService by lazy {
        retrofit.create(NYTimesApiService::class.java)
    }
}
package etf.ri.rma.newsfeedapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query

data class NewsResponse(
    val data: List<NewsArticle>
)

data class NewsArticle(
    val uuid: String,
    val title: String,
    val snippet: String,
    val image_url: String?,
    val categories: List<String>,
    val source: String?,
    val published_at: String
)

interface TheNewsAPIService {
    @GET("news/top")
    suspend fun getTopNews(
        @Query("api_token") apiToken: String,
        @Query("categories") category: String,
        @Query("locale") locale: String = "us",
        @Query("limit") limit: Int = 5
    ): NewsResponse
    @GET("news/similar")
    suspend fun getSimilarNews(
        @Query("api_token") token: String,
        @Query("uuid") uuid: String
    ): NewsResponse

}

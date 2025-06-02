package etf.ri.rma.newsfeedapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
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
    @GET("news/top")//pravi zahtjeve prema tom endpointu, koji vraća najnovije vijesti.
    suspend fun getTopNews(
        @Query("api_token") apiToken: String,
        @Query("categories") category: String,
        @Query("locale") locale: String = "us",
        @Query("limit") limit: Int = 5
    ): NewsResponse
    // tačan endpoint iz TheNewsAPI dokumentacije, koji omogućava da dobiješ slične vijesti na osnovu UUID-a određene vijesti.
    @GET("news/similar/{uuid}")
    suspend fun getSimilarNews(
        @Path("uuid") uuid: String,
        @Query("api_token") token: String
    ): NewsResponse


}

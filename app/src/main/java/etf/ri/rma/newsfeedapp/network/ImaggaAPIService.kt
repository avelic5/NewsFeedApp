package etf.ri.rma.newsfeedapp.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class ImaggaTagResponse(val result: ImaggaResult)
data class ImaggaResult(val tags: List<TagWrapper>)
data class TagWrapper(val tag: Tag)
data class Tag(val en: String)

interface ImaggaAPIService {
    @GET("tags")
    suspend fun getImageTags(
        @Header("Authorization") auth: String,
        @Query("image_url") imageUrl: String
    ): ImaggaTagResponse

}

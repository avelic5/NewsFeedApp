package etf.ri.rma.newsfeedapp.data.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class ImagaTagResponse(val result: ImagaResult)
data class ImagaResult(val tags: List<TagWrapper>)
data class TagWrapper(val tag: Tag)
data class Tag(val en: String)

interface ImagaAPIService {
    @GET("v2/tags")
    suspend fun getImageTags(
        @Header("Authorization") auth: String,//kroz ovo se salje api key
        @Query("image_url") imageUrl: String
    ): ImagaTagResponse

}

package etf.ri.rma.newsfeedapp.data.network

import android.util.Base64
import etf.ri.rma.newsfeedapp.data.network.api.ImagaAPIService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class ImagaDAO(private val service: ImagaAPIService) {
    private val cache = mutableMapOf<String, List<String>>()
    private val apiKey = "acc_fafccabce025c33"
    private val apiSecret = "c218046d590352f6a7942c1ad264bb52"

    private val authHeader: String by lazy {
        val credentials = "$apiKey:$apiSecret"
        "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun getTags(imageURL: String): List<String> = withContext(Dispatchers.IO) {
        try {
            URL(imageURL) // Validate URL
        } catch (e: Exception) {
            throw InvalidImageURLException("Neispravan URL slike: $imageURL")
        }

        if (cache.containsKey(imageURL)) {
            return@withContext cache[imageURL]!!
        }

        try {
            val response = service.getImageTags(authHeader, imageURL)
            val tags = response.result.tags.map { it.tag.en }.take(10)
            cache[imageURL] = tags
            return@withContext tags
        } catch (e: Exception) {
            e.printStackTrace()
            throw InvalidImageURLException("Neuspjelo dohvaćanje tagova za URL: $imageURL")
        }
    }
}
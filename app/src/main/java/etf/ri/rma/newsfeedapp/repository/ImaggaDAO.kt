package etf.ri.rma.newsfeedapp.repository

import android.util.Base64

import etf.ri.rma.newsfeedapp.exception.InvalidImageURLException
import etf.ri.rma.newsfeedapp.network.ImaggaAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object ImaggaDAO {
    private const val apiKey = "acc_fafccabce025c33"
    private const val apiSecret = "c218046d590352f6a7942c1ad264bb52"
    private const val baseUrl = "https://api.imagga.com/v2/"

    private val cache = mutableMapOf<String, List<String>>()

    private val authHeader: String by lazy {
        val credentials = "$apiKey:$apiSecret"
        "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    }

    private val service: ImaggaAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImaggaAPIService::class.java)
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

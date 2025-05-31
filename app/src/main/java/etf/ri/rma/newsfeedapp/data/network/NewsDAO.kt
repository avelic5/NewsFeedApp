package etf.ri.rma.newsfeedapp.data.network

import etf.ri.rma.newsfeedapp.data.network.api.TheNewsAPIService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.model.NewsItem
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

class NewsDAO(private val service: TheNewsAPIService) {

    private val newsCache = mutableListOf<NewsItem>()
    private val lastFetchTime = mutableMapOf<String, Long>()
    private val similarNewsCache = mutableMapOf<String, List<NewsItem>>()
    private var initialLoaded = false
    private val initMutex = Mutex()

    private val apiToken = "wGikBCnKzNcykeizwmW9Ihc4IALhG6uEt8QZSUVU"

    suspend fun getAllStories(): List<NewsItem> {
        if (!initialLoaded) {
            initMutex.withLock {
                if (!initialLoaded) {
                    val initialNews = listOf(
                        NewsItem(
                            uuid = "a8ed26a2-12de-47a8-818a-dacb0757bc9e",
                            title = "No. 1 Aryna Sabalenka cruises into second round of French Open",
                            snippet = "Open Extended Reactions...",
                            imageUrl = "https://a3.espncdn.com/combiner/i?img=/photo/2025/0525/r1497989_1296x729_16-9.jpg",
                            category = "sports",
                            isFeatured = false,
                            source = "espn.com",
                            publishedDate = "2025-05-25"
                        )
                        // Dodaj još ako želiš
                    )
                    newsCache.addAll(initialNews)
                    initialLoaded = true
                }
            }
        }
        return newsCache
    }

    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        if (!uuid.matches(Regex("[a-fA-F0-9\\-]{36}"))) {
            throw InvalidUUIDException("UUID nije u ispravnom formatu")
        }

        if (similarNewsCache.containsKey(uuid)) {
            return similarNewsCache[uuid]!!
        }

        return try {
            val response = service.getSimilarNews(apiToken, uuid)
            val similar = response.data.map { article ->
                NewsItem(
                    uuid = article.uuid,
                    title = article.title,
                    snippet = article.snippet,
                    imageUrl = article.image_url ?: "",
                    category = article.categories.firstOrNull() ?: "general",
                    isFeatured = false,
                    source = article.source ?: "",
                    publishedDate = article.published_at.substring(0, 10),
                    imageTags = arrayListOf()
                )
            }

            similarNewsCache[uuid] = similar
            similar
        } catch (e: Exception) {
            println("Greška prilikom dohvaćanja sličnih vijesti: ${e.message}")
            emptyList()
        }
    }


    suspend fun getTopStoriesByCategory(category: String): List<NewsItem> {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastFetchTime[category] ?: 0L

        val cached = newsCache.filter { it.category == category }

        if (currentTime - lastTime < 30_000) {
            return cached
        }

        return try {
            val response = service.getTopNews(apiToken, category)
            val newItems = response.data.take(3).map { article ->
                NewsItem(
                    uuid = article.uuid,
                    title = article.title,
                    snippet = article.snippet,
                    imageUrl = article.image_url,
                    category = article.categories.firstOrNull() ?: category,
                    isFeatured = true,
                    source = article.source,
                    publishedDate = article.published_at.substring(0, 10),
                    imageTags = arrayListOf()
                )
            }

            newItems.forEach { item ->
                val existing = newsCache.find { it.uuid == item.uuid }
                if (existing == null) {
                    newsCache.add(item.copy(category = "All"))
                    newsCache.add(item)
                } else {
                    existing.isFeatured = true
                }
            }

            newsCache.filter { it.category == category && newItems.none { n -> n.uuid == it.uuid } }
                .forEach { it.isFeatured = false }

            lastFetchTime[category] = currentTime

            val oldNews = newsCache.filter {
                it.category == category && newItems.none { n -> n.uuid == it.uuid }
            }

            return newItems + oldNews
        } catch (e: Exception) {
            e.printStackTrace()
            return cached
        }
    }

    private fun findSimilarNews(newsItem: NewsItem): List<NewsItem> {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = try {
            formatter.parse(newsItem.publishedDate)
        } catch (e: Exception) {
            null
        }

        return newsCache.filter {
            it.category == newsItem.category && it.uuid != newsItem.uuid
        }.sortedBy {
            val date = try {
                formatter.parse(it.publishedDate)
            } catch (e: Exception) {
                null
            }

            if (date != null && currentDate != null) {
                abs(date.time - currentDate.time)
            } else {
                Long.MAX_VALUE
            }
        }.take(2)
    }
}

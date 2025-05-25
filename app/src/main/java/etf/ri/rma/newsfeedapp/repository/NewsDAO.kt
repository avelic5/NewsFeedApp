package etf.ri.rma.newsfeedapp.repository

import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.network.TheNewsAPIService
import etf.ri.rma.newsfeedapp.network.NewsArticle
import etf.ri.rma.newsfeedapp.network.NewsResponse

import etf.ri.rma.newsfeedapp.exception.InvalidUUIDException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object NewsDAO {
    private val apiToken = "wGikBCnKzNcykeizwmW9Ihc4IALhG6uEt8QZSUVU" // 🔁 zamijeni s pravim tokenom
    private val baseUrl = "https://api.thenewsapi.com/v1/"

    private val newsCache = mutableListOf<NewsItem>()
    private val lastFetchTime = mutableMapOf<String, Long>()
    private val similarNewsCache = mutableMapOf<String, List<NewsItem>>()

    private val service: TheNewsAPIService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TheNewsAPIService::class.java)

    private var initialLoaded = false

    suspend fun getAllStories(): List<NewsItem> {
        if (!initialLoaded) {
            newsCache.addAll(
                listOf(
                    NewsItem(
                        uuid = "a8ed26a2-12de-47a8-818a-dacb0757bc9e",
                        title = "No. 1 Aryna Sabalenka cruises into second round of French Open",
                        snippet = "Open Extended Reactions\n\nPARIS -- World No. 1 Aryna Sabalenka sent an early warning to her French Open rivals, producing a dominant display in her first-round v...",
                        imageUrl = "https://a3.espncdn.com/combiner/i?img=/photo/2025/0525/r1497989_1296x729_16-9.jpg",
                        category = "sports",
                        isFeatured = false,
                        source = "espn.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "a07b7684-3bce-449e-8854-97f2c2e46bbe",
                        title = "DAVID MARCUS: Jake Tapper chose lying Biden sources over his own eyes",
                        snippet = "NEW You can now listen to Fox News articles!\n\nA great deal of mirth and ribbing has been directed at CNN’s Jake Tapper...",
                        imageUrl = "https://static.foxnews.com/foxnews.com/content/uploads/2025/05/jaketapper.jpg",
                        category = "politics",
                        isFeatured = false,
                        source = "foxnews.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "6e88da60-f3ab-48e0-92bb-23d2ec4001aa",
                        title = "'Original Sin' co-author exposes 'frantic efforts' to hide Biden's cognitive decline",
                        snippet = "Members of former President Joe Biden's inner circle \"weren't telling the truth\" when describing him...",
                        imageUrl = "https://static.foxnews.com/foxnews.com/content/uploads/2025/05/gettyimages-2158994243-scaled.jpg",
                        category = "politics",
                        isFeatured = false,
                        source = "foxnews.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "097fe734-b660-4853-9d60-3e408879422a",
                        title = "Ukrainian attack destroys Russian church dome (VIDEO)",
                        snippet = "The main spire of St. Nicholas Cathedral in Tula Region was completely burned...",
                        imageUrl = "https://mf.b37mrtl.ru/files/2025.05/article/6833237385f5403e6e235ff8.png",
                        category = "general",
                        isFeatured = false,
                        source = "rt.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "2c154544-5148-4a78-a6e0-ec1d4806df30",
                        title = "Falcons' Kirk Cousins pokes fun at Travis Kelce's hairy back",
                        snippet = "Atlanta Falcons quarterback Kirk Cousins poked fun at Travis Kelce’s hairy back...",
                        imageUrl = "https://static.foxnews.com/foxnews.com/content/uploads/2025/05/travis-kelce-and-kirk-cousins.jpg",
                        category = "sports",
                        isFeatured = false,
                        source = "foxnews.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "105f3593-091d-4f25-b6c1-b651abc4b92c",
                        title = "Kate Middleton's Brother James Opens Up About Their Bond",
                        snippet = "Princess Kate Middleton’s brother James Middleton says he’s lucky to have grown up with two incredible sisters.",
                        imageUrl = "https://www.usmagazine.com/wp-content/uploads/2025/05/GettyImages-1059191722-Kate-Middletons-Brother-James-Shares-Rare-Insight-Into-Their-Relationship.jpg?w=1200&h=630&crop=1&quality=86&strip=all",
                        category = "entertainment",
                        isFeatured = false,
                        source = "usmagazine.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "802cfd12-118d-474d-ba5d-e5bd082a09bc",
                        title = "Netflix Greenlights New Argentine Films As Ricardo Darín & Juan José Campanella Movies Begin Production",
                        snippet = "EXCLUSIVE: With The Eternaut still nestled high in Netflix’s viewing charts, the streamer is adding to its line-up of content from Argentina...",
                        imageUrl = "https://deadline.com/wp-content/uploads/2025/05/Lo-Dejamos-Aca2.jpg?w=1024",
                        category = "entertainment",
                        isFeatured = false,
                        source = "deadline.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "9b6019d8-c3ae-4e9a-81a7-a1bf4ffd7d5f",
                        title = "Full interview: World Food Programme Executive Director Cindy McCain",
                        snippet = "Full interview with the UN's World Food Programme Executive Director Cindy McCain",
                        imageUrl = "https://assets2.cbsnewsstatic.com/hub/i/r/2025/05/25/6399ef53-ae84-4d91-87a0-70eecfc8c820/thumbnail/1200x630/10ba32d240613c3ca7d8a738849e7911/0525-ftn-mccain-full.jpg?v=48f078ae8a74a1412614b0db6f935e48",
                        category = "general",
                        isFeatured = false,
                        source = "cbsnews.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "26cd5cec-9430-4d7d-8fe1-964008985ab4",
                        title = "Christie Brinkley Supports Ex Billy Joel Amid Brain Diagnosis",
                        snippet = "Christie Brinkley is sending lots of well-wishes and prayers to ex-husband Billy Joel as he navigates a battle with normal pressure hydrocephalus (NPH).",
                        imageUrl = "https://www.usmagazine.com/wp-content/uploads/2025/05/Christie-Brinkley-Billy-Joel-GettyImages-123563827.jpg?crop=0px,63px,1420px,746px&resize=1200,630&quality=86&strip=all",
                        category = "entertainment",
                        isFeatured = false,
                        source = "usmagazine.com",
                        publishedDate = "2025-05-25"
                    ),
                    NewsItem(
                        uuid = "100142c7-c266-4ce3-a5ce-e84db2bb83a0",
                        title = "Lando Norris wins iconic F1 Monaco Grand Prix",
                        snippet = "McLaren's Lando Norris won Formula 1's iconic Monaco Grand Prix from pole position on Sunday...",
                        imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/rockcms/2025-05/250525-Lando-Norris-ch-1101-e33573.jpg",
                        category = "sports",
                        isFeatured = false,
                        source = "nbcnews.com",
                        publishedDate = "2025-05-25"
                    )
                )
            )

            initialLoaded = true
        }
        return newsCache
    }
    suspend fun getTopStoriesByCategory(category: String): List<NewsItem> {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastFetchTime[category] ?: 0L

        val cached = newsCache.filter { it.category == category }

        // Ako nije prošlo 30 sekundi → koristi samo keš
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

            // Dodaj nove u cache ako nisu već tu, inače postavi ih na featured
            newItems.forEach { item ->
                val existing = newsCache.find { it.uuid == item.uuid }
                if (existing == null) {
                    newsCache.add(item)
                } else {
                    existing.isFeatured = true
                }
            }

            // Sve ostale iz te kategorije postavi na isFeatured = false
            newsCache.filter { it.category == category && newItems.none { n -> n.uuid == it.uuid } }
                .forEach { it.isFeatured = false }

            lastFetchTime[category] = currentTime

            // Vrati kombinaciju novih (featured) + starih (standard)
            val oldNews = newsCache.filter {
                it.category == category && newItems.none { n -> n.uuid == it.uuid }
            }

            return newItems + oldNews
        } catch (e: Exception) {
            e.printStackTrace()
            return cached
        }
    }



    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        if (!uuid.matches(Regex("[a-fA-F0-9\\-]{36}")))
            throw InvalidUUIDException() as Throwable

        if (similarNewsCache.containsKey(uuid))
            return similarNewsCache[uuid]!!

        val currentNews = newsCache.find { it.uuid == uuid } ?: throw InvalidUUIDException("Vijest nije pronađena.")
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = formatter.parse(currentNews.publishedDate)

        val similar = newsCache.filter {
            it.category == currentNews.category && it.uuid != uuid
        }.sortedBy {
            val date = formatter.parse(it.publishedDate)
            if (date != null&& currentDate!=null) kotlin.math.abs(date.time - currentDate.time)
            else Long.MAX_VALUE // ako je parsiranje neuspješno, stavi na kraj
        }.take(2)

        similarNewsCache[uuid] = similar
        return similar
    }
}

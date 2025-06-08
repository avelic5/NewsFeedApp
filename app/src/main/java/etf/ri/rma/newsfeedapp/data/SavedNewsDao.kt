// SavedNewsDAO.kt
package etf.ri.rma.newsfeedapp.data

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.room.*
import etf.ri.rma.newsfeedapp.model.*
import etf.ri.rma.newsfeedapp.util.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedNewsDAO {

    @Transaction
    suspend fun saveNews(news: NewsItem): Boolean {
        val existing = getNewsByUUID(news.uuid)
        if (existing != null) return false
        val newsId = insertNews(news.toEntity())
        return newsId > 0//broj unesenih redova
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)//isti pk, ali to se nece desiti
    suspend fun insertNews(news: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewsTagCrossRef(crossRef: NewsTagCrossRef)

    @Transaction
    @Query("SELECT * FROM news")
    suspend fun getAllNewsWithTags(): List<NewsWithTags>//moja modelska klasa

    @Transaction
    suspend fun allNews(): List<NewsItem> {
        return getAllNewsWithTags().map { it.toNewsItem() }
    }

    @Transaction //vraca modelsku klasu NewsWithTags koju moram mapirati u NewsItem u sljedecoj
    @Query("SELECT * FROM news WHERE category = :category")
    suspend fun getNewsByCategoryRaw(category: String): List<NewsWithTags>

    @Transaction
    suspend fun getNewsWithCategory(category: String): List<NewsItem> {
        return getNewsByCategoryRaw(category).map { it.toNewsItem() }
    }

    @Query("SELECT * FROM news WHERE uuid = :uuid")
    suspend fun getNewsByUUID(uuid: String): NewsEntity?
                                        //dvotacka je da se mapira sa parametrom
    @Query("SELECT * FROM tags WHERE value = :value")
    suspend fun getTagByValue(value: String): TagEntity?//uzima samo prvi

    @Transaction
    suspend fun addTags(tags: List<String>, newsId: Int): Int {
        var added = 0
        for (tag in tags) {
            val existingTag = getTagByValue(tag)
            val tagId = if (existingTag != null) {
                existingTag.id
            } else {
                val newId = insertTag(TagEntity(value = tag))
                if (newId > 0) added++
                newId
            }
            insertNewsTagCrossRef(NewsTagCrossRef(newsId.toLong(), tagId))
        }
        return added
    }

    @Transaction
    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsWithTagsById(newsId: Int): NewsWithTags

    suspend fun getTags(newsId: Int): List<String> {
        return getNewsWithTagsById(newsId).tags.map { it.value }
    }//map vraca novu listu tako da uzme value od svakog taga

    @Transaction
    @Query("SELECT * FROM news")
    suspend fun getAllNewsWithAllTags(): List<NewsWithTags>

    suspend fun getSimilarNews(tags: List<String>): List<NewsItem> {
        val allNews = getAllNewsWithAllTags()
        return allNews.filter { news ->
            news.tags.any { it.value in tags }
        }.sortedByDescending { it.news.publishedDate }.map { it.toNewsItem() }
    }
}

// Helper function to check internet connection
@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
} 

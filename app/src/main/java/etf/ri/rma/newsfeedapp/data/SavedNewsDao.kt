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
        return newsId > 0
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewsTagCrossRef(crossRef: NewsTagCrossRef)

    @Transaction
    @Query("SELECT * FROM news")
    suspend fun getAllNewsWithTags(): List<NewsWithTags>

    @Transaction
    suspend fun allNews(): List<NewsItem> {
        return getAllNewsWithTags().map { it.toNewsItem() }
    }

    @Transaction
    @Query("SELECT * FROM news WHERE category = :category")
    suspend fun getNewsByCategoryRaw(category: String): List<NewsWithTags>

    @Transaction
    suspend fun getNewsWithCategory(category: String): List<NewsItem> {
        return getNewsByCategoryRaw(category).map { it.toNewsItem() }
    }

    @Query("SELECT * FROM news WHERE uuid = :uuid")
    suspend fun getNewsByUUID(uuid: String): NewsEntity?

    @Query("SELECT * FROM tags WHERE value = :value")
    suspend fun getTagByValue(value: String): TagEntity?

    @Transaction
    suspend fun addTags(tags: List<String>, newsId: Int): Int {
        var added = 0
        for (tag in tags) {
            val existingTag = getTagByValue(tag)
            val tagId = if (existingTag != null) {
                existingTag.tagId
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
    }

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

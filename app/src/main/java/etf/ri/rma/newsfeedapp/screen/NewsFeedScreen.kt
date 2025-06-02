package etf.ri.rma.newsfeedapp.screen

import FilterChips
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import kotlinx.coroutines.launch

import java.util.Locale
@Composable
fun NewsFeedScreen(
    navController: NavController,
    filters: Triple<String, String, List<String>> = Triple("all", "Svi datumi", emptyList()),
    newsItems: List<NewsItem> = emptyList(),
    onCategorySelected: (String) -> Unit,
    newsDAO: NewsDAO
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedCategory by remember { mutableStateOf(filters.first) }
    val selectedDateRange = filters.second
    val unwantedWords = filters.third

    var newsItemsInternal by remember { mutableStateOf(newsItems) }

    LaunchedEffect(Unit) {
        if (newsItemsInternal.isEmpty()) {
            newsItemsInternal = newsDAO.getAllStories()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FilterChips(
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onCategorySelected(category)

                    coroutineScope.launch {
                        newsItemsInternal = if (category.lowercase() == "all") {
                            newsDAO.getAllStories()
                        } else {
                            newsDAO.getTopStoriesByCategory(category)
                        }
                    }
                }
            )

            AssistChip(
                onClick = { navController.navigate("/filters") },
                modifier = Modifier.testTag("filter_chip_more"),
                label = { Text("Više filtera ...") }
            )

            val filteredNewsItems = newsItemsInternal.filter { newsItem ->
                val categoryMatches = when (selectedCategory.lowercase()) {
                    "all" -> true
                    "science", "tech" -> newsItem.category.equals("science", true)
                            || newsItem.category.equals("tech", true)
                    else -> newsItem.category.equals(selectedCategory, true)
                }

                categoryMatches &&
                        isWithinDateRange(newsItem.publishedDate, selectedDateRange) &&
                        unwantedWords.none { word ->
                            newsItem.title.contains(word, true) ||
                                    (newsItem.snippet?.contains(word, true) == true)
                        }
            }

            val uniqueFilteredNews = filteredNewsItems.distinctBy { it.uuid }
            val sortedNews = uniqueFilteredNews.sortedByDescending { it.isFeatured }

            NewsList(
                newsItems = sortedNews,
                selectedCategory = selectedCategory,
                onNewsClick = { newsId -> navController.navigate("/details/$newsId") }
            )
        }
    }
}


fun isWithinDateRange(publishedDate: String, dateRange: String): Boolean {
    if (dateRange.isEmpty() || dateRange == "Svi datumi") return true

    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        val (startDateString, endDateString) = dateRange.split(";")
        val startDate = formatter.parse(startDateString.trim())
        val endDate = formatter.parse(endDateString.trim())
        val itemDate = formatter.parse(publishedDate.trim())
        itemDate != null && startDate != null && endDate != null &&
                !itemDate.before(startDate) && !itemDate.after(endDate)
    } catch (e: Exception) {
        false
    }
}


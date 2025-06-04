package etf.ri.rma.newsfeedapp.screen

import FilterChips
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
                val categoryMatches = selectedCategory == "all" ||
                        newsItem.category?.lowercase() == selectedCategory.lowercase()

                categoryMatches &&
                        isWithinDateRange(newsItem.publishedDate, selectedDateRange) &&
                        unwantedWords.none { word ->
                            newsItem.title.contains(word, ignoreCase = true) ||
                                    (newsItem.snippet?.contains(word, ignoreCase = true) == true)
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


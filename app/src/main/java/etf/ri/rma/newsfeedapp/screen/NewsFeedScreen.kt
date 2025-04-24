package etf.ri.rma.newsfeedapp.screen

import FilterChips
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem


import java.util.Locale
import kotlin.text.category

@Composable
fun NewsFeedScreen(
    navController: NavController,
    filters: Triple<String, String, List<String>>, // (Category, DateRange, UnwantedWords)
    newsItems: List<NewsItem>
) {
    var selectedCategory by remember { mutableStateOf(filters.first) }
    val selectedDateRange = filters.second
    val unwantedWords = filters.third

    // Filter news items based on category, date range, and unwanted words
    val filteredNewsItems = newsItems.filter { newsItem ->
        // Filter by category
        (newsItem.category == selectedCategory || selectedCategory == "All") &&
                // Filter by date range
                isWithinDateRange(newsItem.publishedDate, selectedDateRange) &&
                // Filter by unwanted words
                unwantedWords.none { unwantedWord ->
                    newsItem.title.contains(unwantedWord, ignoreCase = true) ||
                            (newsItem.snippet?.contains(unwantedWord, ignoreCase = true) == true)
                }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Filter Chips
            FilterChips(
                selectedCategory = selectedCategory,
                onCategorySelected = { category -> selectedCategory = category }
            )
            AssistChip(
                onClick = { navController.navigate("/filters") },
                modifier = Modifier.testTag("filter_chip_more"),
                label = { Text("Više filtera ...") }
            )
            // News List
            NewsList(
                newsItems = filteredNewsItems,
                selectedCategory = selectedCategory,
                onNewsClick = { newsId -> navController.navigate("/details/$newsId") }
            )
        }
    }
}

fun isWithinDateRange(publishedDate: String, dateRange: String): Boolean {
    if (dateRange.isEmpty() || !dateRange.contains(" - ")) return true

    return try {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        dateFormat.isLenient = false // Prevents incorrect parsing

        val (startStr, endStr) = dateRange.split(" - ")
        val startDate = dateFormat.parse(startStr)
        val endDate = dateFormat.parse(endStr)
        val pubDate = dateFormat.parse(publishedDate)

        pubDate != null && startDate != null && endDate != null &&
                !pubDate.before(startDate) && !pubDate.after(endDate)
    } catch (e: Exception) {
        false // Return false if parsing fails
    }
}

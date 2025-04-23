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
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.data.NewsData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsFeedScreen(
    navController: NavController,
    filters: Triple<String, String, List<String>>
) {
    val (category, dateRange, unwantedWords) = filters
    var selectedCategory by remember { mutableStateOf("All") }

    val newsItems = NewsData.getAllNews().filter { newsItem ->
        // Apply category filter
        (newsItem.category == selectedCategory || selectedCategory == "All") &&
                // Apply date range filter
                (dateRange.isEmpty() || isWithinDateRange(newsItem.publishedDate, dateRange)) &&
                // Apply unwanted words filter
                unwantedWords.none { word -> newsItem.title.contains(word, ignoreCase = true) }
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
                newsItems = newsItems,
                selectedCategory = selectedCategory, // Pass the selectedCategory here
                onNewsClick = { newsId -> navController.navigate("/details/$newsId") }
            )
        }
    }
}

// Helper function to check if a date is within the selected range
fun isWithinDateRange(publishedDate: String, dateRange: String): Boolean {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val (startDate, endDate) = dateRange.split(" - ").map { dateFormat.parse(it) }
    val newsDate = dateFormat.parse(publishedDate)
    return newsDate in startDate..endDate
}
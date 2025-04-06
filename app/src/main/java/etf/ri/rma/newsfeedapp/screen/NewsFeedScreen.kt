package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsFeedScreen(newsItems: List<NewsItem>) {
    val selectedCategory = remember { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxSize()) {
        FilterChips(
            selectedCategory = selectedCategory.value,
            onCategorySelected = { category -> selectedCategory.value = category }
        )
        NewsList(
            newsItems = newsItems.filter {
                it.category == selectedCategory.value || selectedCategory.value == "All"
            },
            selectedCategory = selectedCategory.value
        )
    }
}
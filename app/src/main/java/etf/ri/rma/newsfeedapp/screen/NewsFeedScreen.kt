package etf.ri.rma.newsfeedapp.screen
import FilterChips
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.screen.NewsList

@Composable
fun NewsFeedScreen() {
    var selectedCategory by remember { mutableStateOf("All") }

    val newsItems = NewsData.getAllNews()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FilterChips(
                selectedCategory = selectedCategory,
                onCategorySelected = { category -> selectedCategory = category }
            )
            NewsList(
                newsItems = newsItems.filter {
                    it.category == selectedCategory || selectedCategory == "All"
                },
                selectedCategory = selectedCategory
            )
        }
    }
}



package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsList(
    newsItems: List<NewsItem>,
    selectedCategory: String,
    onNewsClick: (String) -> Unit
) {
    if (newsItems.isEmpty()) {
        MessageCard("Nema pronađenih vijesti u kategoriji ${selectedCategory.replaceFirstChar { it.uppercase() }}")
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().testTag("news_list")) {
            items(newsItems) { newsItem ->
                if (newsItem.isFeatured) {
                    FeaturedNewsCard(
                        newsItem = newsItem,
                        onClick = { onNewsClick(newsItem.uuid) }
                    )
                } else {
                    StandardNewsCard(
                        newsItem = newsItem,
                        onClick = { onNewsClick(newsItem.uuid) }
                    )
                }
            }
        }
    }
}
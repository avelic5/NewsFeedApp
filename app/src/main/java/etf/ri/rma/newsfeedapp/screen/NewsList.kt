package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        val labels = mapOf(
            "all" to "Sve",
            "politics" to "Politika",
            "sports" to "Sport",
            "science" to "Nauka/tehnologija",
            "tech" to "Nauka/tehnologija",
            "music" to "Muzika"
        )
        MessageCard("Nema pronađenih vijesti u kategoriji ${labels[selectedCategory.lowercase()]}")
    } else {
        val featuredNews = newsItems.filter { it.isFeatured }.distinctBy { it.uuid }
        val standardNews = newsItems.filter { !it.isFeatured }.distinctBy { it.uuid }
        val allNews = featuredNews + standardNews

        LazyColumn(modifier = Modifier.fillMaxSize().testTag("news_list")) {
            items(allNews) { newsItem ->
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

package etf.ri.rma.newsfeedapp.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.R
import etf.ri.rma.newsfeedapp.data.SavedNewsDAO
import etf.ri.rma.newsfeedapp.data.hasInternetConnection
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
@Composable
fun NewsDetailsScreen(navController: NavController, newsId: String, newsDAO: NewsDAO, imaggaDAO: ImagaDAO, savedNewsDAO: SavedNewsDAO, context: Context) {
    val coroutineScope = rememberCoroutineScope()

    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
    var relatedNews by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(newsId) {
        val allNews = newsDAO.getAllStories().toMutableList()
        val item = allNews.find { it.uuid == newsId }
        newsItem = item

        item?.let { currentItem ->
            val saved = savedNewsDAO.saveNews(currentItem)
            val savedEntity = savedNewsDAO.getNewsByUUID(currentItem.uuid)
            val savedId = savedEntity?.id?.toInt()

            if (savedId != null) {
                val localTags = savedNewsDAO.getTags(savedId)
                if (localTags.isNotEmpty()) {
                    tags = localTags
                } else if (!currentItem.imageUrl.isNullOrBlank()) {
                    try {
                        val newTags = imaggaDAO.getTags(currentItem.imageUrl!!)
                        tags = newTags
                        savedNewsDAO.addTags(newTags, savedId)
                    } catch (e: Exception) {
                        tags = listOf("Greška pri dohvaćanju tagova")
                    }
                }

                relatedNews = if (!hasInternetConnection(context)) {
                    savedNewsDAO.getSimilarNews(tags.take(2))
                } else {
                    newsDAO.getSimilarStories(currentItem.uuid)
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (newsItem == null) {
            MessageCard("Vijest nije pronađena.")
            return@Surface
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(newsItem!!.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.testTag("details_title"))
            Spacer(modifier = Modifier.height(8.dp))

            newsItem!!.imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    error = painterResource(id = R.drawable.vijesti),
                    contentDescription = "Slika vijesti",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(bottom = 12.dp)
                )
            }

            Text(newsItem!!.snippet, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.testTag("details_snippet"))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Kategorija: ${newsItem!!.category ?: "Nepoznato"}", modifier = Modifier.testTag("details_category"))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Izvor: ${newsItem!!.source ?: "Nepoznato"}", modifier = Modifier.testTag("details_source"))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Datum: ${newsItem!!.publishedDate ?: "Nepoznat"}", modifier = Modifier.testTag("details_date"))

            Spacer(modifier = Modifier.height(16.dp))

            if (tags.isNotEmpty()) {
                Text("Tagovi slike:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                tags.forEach { tag ->
                    Text("- $tag", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (relatedNews.isNotEmpty()) {
                Text("Slične vijesti", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                relatedNews.forEachIndexed { index, relatedItem ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .clickable {
                                navController.popBackStack()
                                navController.navigate("/details/${relatedItem.uuid}")
                            }
                            .testTag("related_news_title_${index + 1}")
                    ) {
                        Text(
                            text = relatedItem.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("/home?refresh=true")
                },
                modifier = Modifier.testTag("details_close_button")
            ) {
                Text("Zatvori detalje")
            }
        }
    }
}


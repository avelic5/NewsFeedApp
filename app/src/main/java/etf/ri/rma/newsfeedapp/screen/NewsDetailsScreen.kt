package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.repository.NewsDAO
import etf.ri.rma.newsfeedapp.repository.ImaggaDAO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
@Composable
fun NewsDetailsScreen(navController: NavController, newsId: String) {
    val coroutineScope = rememberCoroutineScope()

    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
    var relatedNews by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(newsId) {
        val allNews = NewsDAO.getAllStories()
        val item = allNews.find { it.uuid == newsId }
        newsItem = item

        item?.let {
            // ✅ Ako već ima imageTags, ne zovemo API
            if (it.imageTags.isNotEmpty()) {
                tags = it.imageTags
            } else if (!it.imageUrl.isNullOrBlank()) {
                try {
                    val newTags = ImaggaDAO.getTags(it.imageUrl!!)
                    tags = newTags
                    it.imageTags.addAll(newTags) // sačuvaj u samom objektu
                } catch (e: Exception) {
                    tags = listOf("Greška pri dohvaćanju tagova")
                }
            }

            // ✅ Pronađi slične vijesti samo ako nisu već dohvaćene
            if (relatedNews.isEmpty()) {
                try {
                    relatedNews = NewsDAO.getSimilarStories(it.uuid)
                } catch (e: Exception) {
                    relatedNews = emptyList()
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

            // ✅ Prikaz slike iz imageUrl
            newsItem!!.imageUrl?.let { url ->
                AsyncImage(
                    model = url,
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
                Text("Povezane vijesti iz iste kategorije", style = MaterialTheme.typography.titleMedium)
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
                onClick = { navController.navigate("/home") },
                modifier = Modifier.testTag("details_close_button")
            ) {
                Text("Zatvori detalje")
            }
        }
    }
}

